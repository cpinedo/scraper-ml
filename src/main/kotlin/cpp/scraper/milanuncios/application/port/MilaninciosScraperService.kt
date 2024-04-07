package cpp.scraper.milanuncios.application.port

import cpp.scraper.milanuncios.domain.MilanunciosListingRelevantData

interface MilaninciosScraperService {
    fun extractRelevantData(body: String): MilanunciosListingRelevantData
    fun extractContentJson(body: String): String
    fun isBotDetectedBody(body: String): Boolean
}
