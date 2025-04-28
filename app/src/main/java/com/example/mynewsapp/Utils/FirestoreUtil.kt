import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object FirestoreUtil {

    fun encodeUrlToId(url: String): String {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    }

    fun decodeIdToUrl(id: String): String {
        return URLDecoder.decode(id, StandardCharsets.UTF_8.toString())
    }
}
