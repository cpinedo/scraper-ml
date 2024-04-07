package cpp.scraper.milanuncios.infrastructure.milanuncios

import cpp.scraper.milanuncios.application.port.MilaninciosScraperService
import cpp.scraper.milanuncios.domain.MilanunciosListingRelevantData
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.script
import org.json.JSONObject
import org.springframework.stereotype.Service

@Service
class MilaninciosScraperServiceImpl : MilaninciosScraperService {
    override fun extractRelevantData(body: String): MilanunciosListingRelevantData {
        val json = extractContentJson(body)

        val jsonObject = JSONObject(json)

        val ads = jsonObject
            .getJSONObject("adListPagination")
            .getJSONObject("adList")
            .getJSONArray("ads")

        val urls = mutableListOf<String>()

        for (i in 0 until ads.length()) {
            urls.add(
                ads.getJSONObject(i)
                    .getString("url")
            )
        }

        val nextToken = jsonObject
            .getJSONObject("adListPagination")
            .getJSONObject("pagination")
            .getString("nextToken")

        val totalAds = jsonObject
            .getJSONObject("adListPagination")
            .getJSONObject("pagination")
            .getInt("totalAds")

        return MilanunciosListingRelevantData(urls = urls.toList(), nextToken = nextToken, totalAds = totalAds)
    }

    override fun extractContentJson(body: String): String {
        val hrefList = htmlDocument(body) {
            script {
                findAll {
                    this
                }
            }
        }

        return hrefList.filter {
            it.html.contains("INITIAL_PROPS")
        }.map {
            it.html
        }.map {
            it.substring(39, it.length - 3)
        }.map {
            it.replace("\\\\\\\"", "!/*9856*/!--->")
        }.map {
            it.replace("\\\"", "\"")
        }.map {
            it.replace("!/*9856*/!--->", "\\\"")
        }.first()
    }

    override fun isBotDetectedBody(body: String): Boolean {
        return body.contains("<title>Pardon Our Interruption</title>")
    }
}