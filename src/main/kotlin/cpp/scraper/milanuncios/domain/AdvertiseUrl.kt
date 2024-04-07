package cpp.scraper.milanuncios.domain

import java.time.LocalDateTime
import java.util.*

data class AdvertiseUrl(
    val id: UUID,
    val url: String,
    val lastScrapedDate: LocalDateTime?= null,
    val scrapeAttempts: Int,
)