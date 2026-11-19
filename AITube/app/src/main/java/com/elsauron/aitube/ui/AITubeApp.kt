package com.elsauron.aitube.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elsauron.aitube.data.SecureKeyStore

private enum class Screen { HOME, SETTINGS }

@Composable
fun AITubeApp(keyStore: SecureKeyStore) {
    var screen by remember { mutableStateOf(Screen.HOME) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = screen == Screen.HOME,
                    onClick = { screen = Screen.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Ana sayfa") },
                    label = { Text("Üretim") }
                )
                NavigationBarItem(
                    selected = screen == Screen.SETTINGS,
                    onClick = { screen = Screen.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Ayarlar") },
                    label = { Text("API Yönetimi") }
                )
            }
        }
    ) { padding ->
        when (screen) {
            Screen.HOME -> HomeScreen(Modifier.padding(padding), onSettings = { screen = Screen.SETTINGS })
            Screen.SETTINGS -> ApiSettingsScreen(keyStore, Modifier.padding(padding))
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, onSettings: () -> Unit) {
    var category by remember { mutableStateOf("AI / Teknoloji") }
    var scriptApproved by remember { mutableStateOf(false) }
    var finalApproved by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Henüz üretim başlatılmadı.") }
    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color(0xFFF8F7FB)).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("AITube", style = MaterialTheme.typography.headlineLarge)
            Text("Günlük YouTube Shorts stüdyon", color = Color.Gray)
        }
        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Bugünün içeriği", style = MaterialTheme.typography.titleLarge)
                    Text("Hedef: 44–55 saniye · Dikey 9:16 · 1080 × 1920")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = category == "AI / Teknoloji", onClick = { category = "AI / Teknoloji" }, label = { Text("AI / Teknoloji") })
                        FilterChip(selected = category == "Bilim", onClick = { category = "Bilim" }, label = { Text("Bilim") })
                    }
                    Button(onClick = {
                        scriptApproved = false
                        finalApproved = false
                        message = "$category için araştırma ve senaryo akışı hazır. Servis adaptörleri henüz bağlanmadı."
                    }, modifier = Modifier.fillMaxWidth()) { Text("Üretim akışını hazırla") }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Üretim adımları", style = MaterialTheme.typography.titleLarge)
                    listOf("1. Son 7 günün gelişmelerini araştır", "2. Kaynakları doğrula ve senaryoyu yaz",
                        "3. Gemini TTS ile seslendir", "4. Görsel ve stok görüntüleri hazırla",
                        "5. Altyazı ve montaj", "6. Son video onayı", "7. YouTube'a yükle").forEach {
                        Text("○  $it", style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(message, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Zorunlu onay kapıları", style = MaterialTheme.typography.titleLarge)
                    Text("Senaryo onayı ve son MP4 onayı birbirinden bağımsızdır. İkisi de verilmeden yükleme yapılamaz.")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = scriptApproved, onCheckedChange = { scriptApproved = it })
                        Text("Senaryoyu onayladım")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = finalApproved, onCheckedChange = { finalApproved = it })
                        Text("Son MP4 videosunu onayladım")
                    }
                    Text(
                        if (scriptApproved && finalApproved) "Onaylar verildi; gerçek yükleme adaptörü henüz eklenmedi."
                        else "Yükleme kilitli.",
                        color = if (scriptApproved && finalApproved) Color(0xFF26734D) else Color(0xFF9A5B00)
                    )
                }
            }
        }
        item {
            OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) {
                Text("API anahtarlarını ve kurulum rehberlerini aç")
            }
        }
    }
}
