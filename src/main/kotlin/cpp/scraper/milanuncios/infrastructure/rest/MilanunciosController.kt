package cpp.scraper.milanuncios.infrastructure.rest

import cpp.scraper.milanuncios.application.MilanunciosScraperHandler
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/milanuncios")
class MilanunciosController(private val milanunciosScraperHandler: MilanunciosScraperHandler) {

    @PostMapping("/scan")
    fun triggerScan(
        @RequestBody url: UrlRequest
    ) {
        milanunciosScraperHandler.handle(url.url)
    }

}

data class UrlRequest(val url: String)
