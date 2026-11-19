package com.elsauron.aitube.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.elsauron.aitube.data.*

private val repo = WorkflowRepository()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AITubeApp() {
    var settings by remember { mutableStateOf(false) }
    MaterialTheme(colorScheme = lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF6750A4))) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("AITube") }, actions = { IconButton(onClick = { settings = !settings }) { Icon(Icons.Default.Settings, "Ayarlar") } })
        }) { padding ->
            if (settings) SettingsScreen(Modifier.padding(padding)) else HomeScreen(Modifier.padding(padding), onSettings = { settings = true })
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier, onSettings: () -> Unit) {
    val projects by repo.projects.collectAsState()
    var topic by remember { mutableStateOf(Topic.AI) }
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Bugünün Shorts üretimi", style = MaterialTheme.typography.headlineSmall)
        Text("44–55 saniye • 9:16 • Türkçe • Enerjik anlatım")
        Text("Kategori", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Topic.values().forEach { item -> FilterChip(selected = topic == item, onClick = { topic = item }, label = { Text(item.label) }) }
        }
        Button(onClick = { repo.add(topic) }, modifier = Modifier.fillMaxWidth()) { Text("Yeni video iş akışı oluştur") }
        Divider()
        Text("Projeler", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(projects, key = { it.id }) { project -> ProjectCard(project) }
        }
        TextButton(onClick = onSettings) { Text("API ve kanal ayarlarını aç") }
    }
}

@Composable
private fun ProjectCard(p: ShortProject) {
    var scriptApproved by remember(p.id, p.scriptApproved) { mutableStateOf(p.scriptApproved) }
    var finalApproved by remember(p.id, p.finalVideoApproved) { mutableStateOf(p.finalVideoApproved) }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(p.title, style = MaterialTheme.typography.titleMedium)
            Text("${p.topic.label} • ${p.stage.label}")
            OutlinedTextField(value = p.script, onValueChange = { repo.update(p.copy(script = it, stage = Stage.SCRIPT_REVIEW)) }, label = { Text("Senaryo taslağı") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = scriptApproved, onCheckedChange = { scriptApproved = it; repo.approveScript(p.id, it) })
                Text("Senaryoyu onaylıyorum")
            }
            Text("Son MP4 dosyası: ${p.finalVideoPath ?: "henüz eklenmedi"}")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = finalApproved, onCheckedChange = { finalApproved = it; repo.approveFinal(p.id, it) }, enabled = !p.finalVideoPath.isNullOrBlank())
                Text("Son videoyu onaylıyorum")
            }
            Text("Yükleme kilidi: ${if (p.canUpload()) "Koşullar sağlandı" else "İki onay + hazır MP4 gerekli"}", color = MaterialTheme.colorScheme.secondary)
            Text("Not: Bu sürüm YouTube'a otomatik yükleme yapmaz.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val store = remember { SecureSettings(context) }
    var gemini by remember { mutableStateOf(store.get("gemini_text")) }
    var tts by remember { mutableStateOf(store.get("gemini_tts")) }
    var image by remember { mutableStateOf(store.get("image_api")) }
    var pexels by remember { mutableStateOf(store.get("pexels")) }
    var youtubeId by remember { mutableStateOf(store.get("youtube_client_id")) }
    var saved by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("API ve kanal ayarları", style = MaterialTheme.typography.headlineSmall)
        Text("Anahtarlar cihazda şifreli saklanır. Root edilmiş/ele geçirilmiş cihazlarda istemci anahtarları tamamen gizli kabul edilemez.", style = MaterialTheme.typography.bodySmall)
        SecretField("Gemini metin API anahtarı", gemini) { gemini = it }
        SecretField("Gemini TTS API anahtarı", tts) { tts = it }
        SecretField("Görsel API anahtarı", image) { image = it }
        SecretField("Pexels API anahtarı (isteğe bağlı)", pexels) { pexels = it }
        OutlinedTextField(youtubeId, { youtubeId = it }, label = { Text("YouTube OAuth Client ID") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            store.put("gemini_text", gemini); store.put("gemini_tts", tts); store.put("image_api", image)
            store.put("pexels", pexels); store.put("youtube_client_id", youtubeId); saved = true
        }, modifier = Modifier.fillMaxWidth()) { Text("Ayarları güvenli kaydet") }
        if (saved) Text("Ayarlar kaydedildi.")
        Text("Ücretsiz kota dolduğunda otomatik ücretli kullanıma geçiş bu uygulamada uygulanmaz. Servis kotaları ayrıca sağlayıcı konsolundan kontrol edilmelidir.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun SecretField(label: String, value: String, onValue: (String) -> Unit) {
    OutlinedTextField(value, onValue, label = { Text(label) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
}
