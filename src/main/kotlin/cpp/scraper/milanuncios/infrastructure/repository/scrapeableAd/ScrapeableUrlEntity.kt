package cpp.scraper.milanuncios.infrastructure.repository.scrapeableAd

import cpp.scraper.milanuncios.domain.AdvertiseUrl
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "scrapeable_url")
data class ScrapeableUrlEntity(
    val id: UUID,
    val url: String,
    val lastScrapedDate: LocalDateTime?,
    val scrapeAttempts: Int,
) {
    fun toEntity(): AdvertiseUrl =
        AdvertiseUrl(
            id = this.id,
            url = this.url,
            lastScrapedDate = this.lastScrapedDate,
            scrapeAttempts = this.scrapeAttempts,
        )

    companion object {
        fun fromEntity(entity: AdvertiseUrl): ScrapeableUrlEntity {
            return ScrapeableUrlEntity(
                id = entity.id,
                url = entity.url,
                lastScrapedDate = entity.lastScrapedDate,
                scrapeAttempts = entity.scrapeAttempts,
            )
        }
    }

}