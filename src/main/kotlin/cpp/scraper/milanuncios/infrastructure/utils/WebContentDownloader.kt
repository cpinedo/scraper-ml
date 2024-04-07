package cpp.scraper.milanuncios.infrastructure.utils

import okhttp3.*
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream

@Component
class WebContentDownloader() {
    fun retrieveHtmlContent(url: String, userAgent: String, headersSet: Map<String, String>): String {
        val cookieJar: CookieJar = object : CookieJar {
            private val cookieStore: HashMap<String, List<Cookie>> = HashMap<String, List<Cookie>>()
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies: List<Cookie>? = cookieStore[url.host]
                return cookies ?: ArrayList<Cookie>()
            }
        }

//        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("157.245.95.247", 443))

        val client = OkHttpClient().newBuilder()
            .cookieJar(cookieJar)
//            .proxy(proxy)
            .build()

        val requestBuilder = Request.Builder()
            .url(url)
        headersSet.forEach {
            requestBuilder.addHeader(it.key, it.value)
        }
        requestBuilder.addHeader("User-Agent", userAgent)

        val request = requestBuilder.build()

        val response = client.newCall(request).execute()

        val cookies = Cookie.parseAll(request.url, response.headers)
        for (cookie in cookies) {
            println(cookie.value)
        }

        return String(decompressIfNeeded(response.body?.bytes()), Charset.forName("UTF-8"))
    }

    private fun decompressIfNeeded(input: ByteArray?): ByteArray {
        return GZIPInputStream(input!!.inputStream()).readBytes()
    }
}