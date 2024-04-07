package cpp.scraper.milanuncios.application.port

import cpp.scraper.milanuncios.domain.AdvertiseUrl

interface AdsService {
    fun saveAd(data: String): String
    fun saveUrlToScrape(url: AdvertiseUrl)
    fun getNotScrapedUrlsToScrape(): List<AdvertiseUrl>
    fun updateUrlScrapedStatus(url: AdvertiseUrl)
}