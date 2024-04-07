package cpp.scraper.milanuncios.infrastructure.scheduled

import cpp.scraper.milanuncios.application.port.AdsService
import cpp.scraper.milanuncios.application.port.MilaninciosScraperService
import cpp.scraper.milanuncios.domain.BotActivityDetectedException
import cpp.scraper.milanuncios.infrastructure.utils.HeadersSetProvider
import cpp.scraper.milanuncios.infrastructure.utils.UserAgentCreator
import cpp.scraper.milanuncios.infrastructure.utils.WebContentDownloader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class ScrapeAnAdScheduler(
    private val userAgentCreator: UserAgentCreator,
    private val webContentDownloader: WebContentDownloader,
    private val milaninciosScraperService: MilaninciosScraperService,
    private val adsService: AdsService,
    private val headersSetProvider: HeadersSetProvider,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private var stopExecutionUntil: LocalDateTime = LocalDateTime.now()

    @Async
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    fun run() {
        if (stopExecutionUntil.isAfter(LocalDateTime.now()))
            return

        log.debug("Running an advertise scrape")
        runBlocking {

            val urls = adsService.getNotScrapedUrlsToScrape()
            if (urls.isEmpty())
                return@runBlocking

            (3000..40000).random().toLong().let {
                log.info("Waiting for $it millis")
                delay(it)
            }

            if (stopExecutionUntil.isAfter(LocalDateTime.now()))
                return@runBlocking

            val urlToScrape = urls[urls.indices.random()]
            adsService.updateUrlScrapedStatus(urlToScrape.copy(scrapeAttempts = urlToScrape.scrapeAttempts + 1))

            runCatching {
                scrapAndStoreAd(
                    url = urlToScrape.url,
                    userAgent = userAgentCreator.getLastUserAgent(),
                    userAgentCreator = userAgentCreator,
                )
            }.onFailure {
                userAgentCreator.getRandomUserAgent()
                stopExecutionUntil = LocalDateTime.now().plusMinutes(30)
                log.info("Bot activity detected, pausing execution for 30 minutes. Will continue at $stopExecutionUntil")
            }.onSuccess {
                log.info("Ad scraped successfully!")
                adsService.updateUrlScrapedStatus(urlToScrape.copy(lastScrapedDate = LocalDateTime.now()))
            }
        }
    }

    private fun scrapAndStoreAd(url: String, userAgent: String?, userAgentCreator: UserAgentCreator) {
        log.debug("Scraping Ad: $url")
        val extractedBody = webContentDownloader.retrieveHtmlContent(
            url = url,
            userAgent = userAgent ?: userAgentCreator.getRandomUserAgent(),
            headersSet = headersSetProvider.advertiseHeaders()
        )
        if (milaninciosScraperService.isBotDetectedBody(extractedBody))
            throw BotActivityDetectedException()

        val data = milaninciosScraperService.extractContentJson(extractedBody)
        val result = adsService.saveAd(data)
        log.debug("Inserted document ID: $result")
    }
}