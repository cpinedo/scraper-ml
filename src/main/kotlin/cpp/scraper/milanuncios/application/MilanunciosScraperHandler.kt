package cpp.scraper.milanuncios.application

import cpp.scraper.milanuncios.application.port.AdsService
import cpp.scraper.milanuncios.application.port.MilaninciosScraperService
import cpp.scraper.milanuncios.domain.AdvertiseUrl
import cpp.scraper.milanuncios.infrastructure.utils.HeadersSetProvider
import cpp.scraper.milanuncios.infrastructure.utils.UserAgentCreator
import cpp.scraper.milanuncios.infrastructure.utils.WebContentDownloader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class MilanunciosScraperHandler(
    private val userAgentCreator: UserAgentCreator,
    private val webContentDownloader: WebContentDownloader,
    private val milaninciosScraperService: MilaninciosScraperService,
    private val adsService: AdsService,
    private val headersSetProvider: HeadersSetProvider,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun handle(url: String) {
        val advertisesUrls = mutableListOf<String>()
        val userAgent = userAgentCreator.getRandomUserAgent()

        processPage(
            url = url,
            isFirstPage = true,
            urls = advertisesUrls,
            page = 1,
            total = 100,
            nextToken = null,
            userAgent = userAgent,
        )

        advertisesUrls.toList().map {
            AdvertiseUrl(
                id = UUID.randomUUID(),
                url = it,
                scrapeAttempts = 0,
            )
        }.forEach {
            adsService.saveUrlToScrape(it)
        }

        log.info("Obtained ${advertisesUrls.size} to scrape")
    }


    private fun processPage(
        url: String,
        isFirstPage: Boolean,
        urls: MutableList<String>,
        page: Int,
        total: Int?,
        nextToken: String?,
        userAgent: String
    ) {
        runBlocking {
            if (total == null || total <= urls.size)
                return@runBlocking

            if (!isFirstPage) {
                (6000..20000).random().toLong().let {
                    log.info("Waiting for $it millis")
                    delay(it)
                }
            }

            val fullUrl = if (isFirstPage) url else url.plus("&nextToken=${nextToken}&pagina=$page")

            log.info("Start Scraping url: $fullUrl")
            val extractedBody = webContentDownloader.retrieveHtmlContent(
                url = fullUrl,
                userAgent = userAgent,
                headersSet = headersSetProvider.listingHeaders()
            )
            log.debug("Extracted Body: $extractedBody")
            val data = milaninciosScraperService.extractRelevantData(extractedBody)
            data.urls.forEach { link ->
                urls.add("https://www.milanuncios.com$link")
            }

            log.info("Current url extracted ${urls.size}")

            processPage(
                url = url,
                isFirstPage = false,
                urls = urls,
                page = page + 1,
                total = data.totalAds,
//                total = null,
                nextToken = data.nextToken,
                userAgent = userAgent,
            )
        }
    }


}
