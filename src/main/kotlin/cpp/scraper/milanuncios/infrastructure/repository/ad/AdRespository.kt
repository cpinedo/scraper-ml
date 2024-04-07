package cpp.scraper.milanuncios.infrastructure.repository.ad

import org.springframework.data.mongodb.repository.MongoRepository

interface AdRespository : MongoRepository<AdEntity, Long> {
}