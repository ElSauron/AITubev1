package com.elsauron.aitube.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/** Minimal Gemini REST client. Model availability and quotas must be checked against current Google documentation. */
class GeminiClient(private val apiKey: String) {
    private val http = OkHttpClient.Builder().callTimeout(90, TimeUnit.SECONDS).build()

    suspend fun generateText(prompt: String, model: String = "gemini-2.0-flash"): String = withContext(Dispatchers.IO) {
        require(apiKey.isNotBlank()) { "Gemini API anahtarı girilmedi." }
        val body = JSONObject().put("contents", org.json.JSONArray().put(
            JSONObject().put("parts", org.json.JSONArray().put(JSONObject().put("text", prompt)))
        )).toString()
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
            .post(body.toRequestBody("application/json".toMediaType())).build()
        http.newCall(request).execute().use { response ->
            val raw = response.body?.string().orEmpty()
            if (!response.isSuccessful) error("Gemini API ${response.code}: $raw")
            val root = JSONObject(raw)
            root.optJSONArray("candidates")?.optJSONObject(0)?.optJSONObject("content")
                ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")
                ?.takeIf { it.isNotBlank() } ?: error("Gemini yanıtında metin bulunamadı.")
        }
    }

    /** Prompt is constrained to preserve every fact and instruction; output still requires human review. */
    suspend fun refineScript(original: String): String = generateText(
        "Aşağıdaki Türkçe metni yalnızca daha akıcı ve anlaşılır hale getir. Yeni bilgi, örnek, iddia, kişi, olay veya ayrıntı EKLEME. " +
            "Hiçbir bilgi, koşul, anlam veya talimatı ÇIKARMA ya da DEĞİŞTİRME. Belirsizliği kendin çözme. " +
            "Sadece anlatım bozukluklarını düzelt ve mevcut anlamı koru. Yalnızca düzenlenmiş metni döndür.\n\nMETİN:\n$original"
    )
}
