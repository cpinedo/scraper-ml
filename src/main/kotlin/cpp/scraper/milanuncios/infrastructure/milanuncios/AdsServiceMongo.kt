package cpp.scraper.milanuncios.infrastructure.milanuncios

import cpp.scraper.milanuncios.application.port.AdsService
import cpp.scraper.milanuncios.domain.AdvertiseUrl
import cpp.scraper.milanuncios.infrastructure.repository.scrapeableAd.ScrapeableUrlEntity
import cpp.scraper.milanuncios.infrastructure.repository.scrapeableAd.ScrapeableUrlRepository
import org.bson.Document
import org.json.JSONObject
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class AdsServiceMongo(
    private val mongoTemplate: MongoTemplate,
    private val scrapeableUrlRepository: ScrapeableUrlRepository,
) : AdsService {
    override fun saveAd(data: String): String {
        return mongoTemplate.getCollection("ads")
            .insertOne(Document.parse(JSONObject(data).toString())).insertedId.toString()
    }

    override fun saveUrlToScrape(url: AdvertiseUrl) {
        scrapeableUrlRepository.save(ScrapeableUrlEntity.fromEntity(url))
    }

    override fun getNotScrapedUrlsToScrape(): List<AdvertiseUrl> =
        scrapeableUrlRepository
            .findByLastScrapedDateIsNull()
            .toList()
            .map {
                it.toEntity()
            }

    override fun updateUrlScrapedStatus(url: AdvertiseUrl) {
        scrapeableUrlRepository.save(ScrapeableUrlEntity.fromEntity(url))
    }

}