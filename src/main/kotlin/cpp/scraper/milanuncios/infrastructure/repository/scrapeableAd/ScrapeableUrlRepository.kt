package cpp.scraper.milanuncios.infrastructure.repository.scrapeableAd

import org.springframework.data.mongodb.repository.MongoRepository

interface ScrapeableUrlRepository : MongoRepository<ScrapeableUrlEntity, Long>{
    fun findByLastScrapedDateIsNull(): List<ScrapeableUrlEntity>
}