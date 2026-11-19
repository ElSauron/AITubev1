# AITube — Android başlangıç projesi

Bu arşiv, AITube için Kotlin + Jetpack Compose tabanlı Android proje iskeletidir. Ana ekran, proje kartları, senaryo düzenleme alanı, iki aşamalı onay mantığı, şifreli API ayar deposu, temel Gemini metin REST istemcisi ve GitHub Actions debug APK workflow'u içerir.

## Gereksinimler
- Android Studio güncel kararlı sürüm
- JDK 17
- Android SDK 35
- İnternet bağlantısı

## Açma ve derleme
1. ZIP'i çıkarın.
2. Android Studio'da klasörü açın.
3. Gradle senkronizasyonunu tamamlayın.
4. `Build > Build Bundle(s) / APK(s) > Build APK(s)` seçin.

Komut satırında: `gradle assembleDebug`

GitHub Actions, `main` veya `master` dalına push/PR olduğunda debug APK üretip artifact olarak saklar.

## Bu sürümde gerçekten bulunanlar
- Compose ana ekranı ve AI/Bilim kategori seçimi
- Yerel bellek içi proje listesi ve senaryo düzenleme alanı
- Senaryo onayı ve son MP4 onayı için ayrı kontroller
- Upload kilidi için `canUpload()` koşulu: iki onay, READY aşaması ve MP4 dosya yolu gerekir
- AndroidX Security Crypto ile cihazda şifreli ayar deposu
- Gemini `generateContent` için temel REST metin istemcisi
- GitHub Actions debug APK workflow'u

## Henüz tamamlanmayan / gerçek hesapla doğrulanmayan parçalar
- Son 7 gün haber/araştırma keşfi, kaynak doğrulama ve kaynakça üretimi
- Gemini TTS gerçek ses üretimi ve ses dosyası yönetimi
- Görsel üretim/stock medya sağlayıcı adaptörleri
- Video montajı, altyazı, 1080×1920 MP4 dışa aktarma ve oynatma
- Google OAuth akışı, kanal kimliği doğrulaması ve YouTube Data API upload
- WorkManager ile dayanıklı arka plan iş akışı, Room ile kalıcı proje verisi
- Kota sorgulama ve sağlayıcı bazlı ücret engeli (şu an yalnızca tasarım notu)

Dolayısıyla bu paket tamamlanmış, otomatik video üretip YouTube'a yükleyen ürün değildir. Bunlar için servis entegrasyonları, OAuth doğrulaması, gerçek cihaz testleri ve API kota testleri gerekir.

## Güvenlik
- Gerçek API anahtarlarını Git'e veya kaynak koda yazmayın.
- Bu uygulama anahtarları cihazda şifreli saklar; mobil istemcide kullanılan anahtarlar ele geçirilmiş/root cihazda kesin gizli değildir. Üretim ortamında sunucu tarafı proxy daha güvenlidir.
- Daha önce sohbet içinde paylaşılmış GitHub tokenlarını iptal edin ve yenilerini sohbet içinde paylaşmayın.

## Gemini
`GeminiClient` temel `generateContent` çağrısını gösterir. Model kimliği, TTS modeli, erişilebilirlik ve ücretsiz kota limitleri zamanla değişebileceği için Google'ın güncel resmi dokümantasyonundan doğrulanmalıdır. `refineScript` promptu yeni bilgi eklememe/çıkarma kısıtını içerir; model çıktısı yine de insan tarafından kontrol edilmelidir.
