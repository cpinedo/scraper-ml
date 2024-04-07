package cpp.scraper.milanuncios.infrastructure.utils

import org.springframework.stereotype.Component

@Component
class HeadersSetProvider {
    fun listingHeaders(): Map<String, String> {
        return mapOf(
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding" to "gzip, deflate, br, zstd",
            "Accept-Language" to "es-ES,es;q=0.9",
            "Cache-Control" to "max-age=0",
            "Sec-Ch-Ua" to "\"Brave\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"",
            "Sec-Ch-Ua-Mobile" to "?0",
            "Sec-Ch-Ua-Platform" to "\"Windows\"",
            "Sec-Fetch-Dest" to "document",
            "Sec-Fetch-Mode" to "navigate",
            "Sec-Fetch-Site" to "same-origin",
            "Sec-Fetch-User" to "?1",
            "Sec-Gpc" to "1",
            "Upgrade-Insecure-Requests" to "1",
        )
    }

    fun advertiseHeaders(): Map<String, String> {
        return mapOf(
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding" to "gzip, deflate, br, zstd",
            "Accept-Language" to "es-ES,es;q=0.8",
            "Cache-Control" to "no-cache",
            "Pragma" to "no-cache",
            "Sec-Ch-Ua" to "\"Brave\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"",
            "Sec-Ch-Ua-Mobile" to "?0",
            "Sec-Ch-Ua-Platform" to "Windows",
            "Sec-Fetch-Dest" to "document",
            "Sec-Fetch-Mode" to "navigate",
            "Sec-Fetch-Site" to "none",
            "Sec-Fetch-User" to "?1",
            "Sec-Gpc" to "1",
            "Upgrade-Insecure-Requests" to "1",
        )
    }
}