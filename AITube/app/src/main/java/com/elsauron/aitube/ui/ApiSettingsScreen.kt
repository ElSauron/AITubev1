package com.elsauron.aitube.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.elsauron.aitube.data.SecureKeyStore

private data class ApiField(val key: String, val title: String, val help: String)

private val apiFields = listOf(
    ApiField("gemini_text", "Gemini metin API anahtarı", "Google AI Studio'dan API anahtarı oluşturun. Senaryo ve araştırma destek adaptöründe kullanılacak."),
    ApiField("gemini_tts", "Gemini TTS API anahtarı", "Gemini TTS için kullanılacak. Seçilen ses modeli ve kota, Google AI Studio/Cloud projesinden doğrulanmalıdır."),
    ApiField("image_api", "Görsel üretim API anahtarı", "Kullanacağınız görsel sağlayıcının anahtarını girin. Sağlayıcı entegrasyonu henüz bağlanmadı."),
    ApiField("pexels_api", "Pexels API anahtarı", "Pexels geliştirici panelinden alınır. Ücretsiz stok video araması için planlanan alan."),
    ApiField("youtube_client_id", "YouTube OAuth Client ID", "Google Cloud Console'da Android/OAuth istemci yapılandırması gerekir. Bu alan şimdilik yapılandırma kaydıdır.")
)

@Composable
fun ApiSettingsScreen(store: SecureKeyStore, modifier: Modifier = Modifier) {
    val values = remember { mutableStateMapOf<String, String>() }
    val visible = remember { mutableStateMapOf<String, Boolean>() }
    var notice by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { apiFields.forEach { values[it.key] = store.get(it.key).orEmpty() } }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("API Yönetimi", style = MaterialTheme.typography.headlineMedium)
        Text("Anahtarlar bu cihazda Android Keystore destekli AES-GCM ile şifrelenerek saklanır. GitHub'a gönderilmez.")
        apiFields.forEach { field ->
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(field.title, style = MaterialTheme.typography.titleMedium)
                    Text(field.help, style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value = values[field.key].orEmpty(),
                        onValueChange = { values[field.key] = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("API anahtarı / kimlik") },
                        visualTransformation = if (visible[field.key] == true) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { visible[field.key] = visible[field.key] != true }) {
                            Text(if (visible[field.key] == true) "Gizle" else "Göster")
                        }
                        TextButton(onClick = {
                            store.put(field.key, values[field.key].orEmpty())
                            notice = "${field.title} kaydedildi."
                        }) { Text("Kaydet") }
                        TextButton(onClick = {
                            store.delete(field.key)
                            values[field.key] = ""
                            notice = "${field.title} silindi."
                        }) { Text("Sil") }
                    }
                }
            }
        }
        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Güvenlik ve kota kuralları", style = MaterialTheme.typography.titleMedium)
                Text("• API anahtarlarını kaynak koda veya GitHub'a koymayın.")
                Text("• Cihazda saklama mutlak gizlilik sağlamaz; üretimde backend proxy tercih edin.")
                Text("• Ücretsiz kota dolduğunda işlem durur; otomatik ücretli kullanım yoktur.")
                Text("• YouTube varsayılan görünürlüğü public olarak planlanmıştır; yükleme öncesi son onay zorunludur.")
            }
        }
        if (notice.isNotBlank()) Text(notice, color = MaterialTheme.colorScheme.primary)
    }
}
