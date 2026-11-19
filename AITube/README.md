# AITube

AITube, Android üzerinde Türkçe YouTube Shorts üretim sürecini yönetmek için hazırlanmış Kotlin + Jetpack Compose başlangıç projesidir.

## Bu sürümde bulunanlar
- Jetpack Compose ana ekranı ve üretim adımları taslağı
- Senaryo onayı ve son video onayı için ayrı kapılar
- API anahtarlarını Android Keystore ile AES-GCM kullanarak şifreli saklayan yerel kasa
- API Yönetimi ekranı (Gemini metin, Gemini TTS, görsel üretimi, Pexels ve YouTube alanları)
- Anahtarları göster/gizle, kaydet ve sil
- Ücretsiz kota sınırı ilkesi için merkezi kota kapısı arayüzü
- Android manifest ve temel proje yapılandırması

## Henüz tamamlanmamış entegrasyonlar
Bu depo ilk kaynak kod iskeletidir; gerçek servis çağrıları, Google OAuth akışı, Gemini içerik/TTS adaptörleri, görsel sağlayıcı adaptörü, Media3 video birleştirme ve YouTube `videos.insert` yüklemesi henüz bağlanmamıştır. Arayüzdeki üretim adımları bu nedenle gerçek üretim yapıyormuş gibi gösterilmez. Bu adaptörler sırayla eklenip cihaz/API testleriyle doğrulanmalıdır.

## Geliştirme ortamı
- Android Studio (güncel kararlı sürüm)
- JDK 17
- Android SDK 35
- Gradle 8.13 veya Android Gradle Plugin'in gerektirdiği sürüm

Projeyi Android Studio ile açıp Gradle Sync yapın. `local.properties` dosyası makinenizde Android SDK yolunu içermelidir; bu dosya Git'e gönderilmez.

## API güvenliği
API anahtarları uygulama içindeki Android Keystore anahtarıyla AES-GCM kullanılarak şifrelenir. Bu, dosyada düz metin saklamayı önler; ancak cihazda çalışan uygulamanın kullanmak zorunda olduğu anahtarların/root edilmiş veya ele geçirilmiş cihazlarda tamamen gizli kalacağını garanti etmez. Üretim ortamında mümkünse kendi backend'iniz üzerinden anahtar proxy'leme tercih edilmelidir. Hiçbir anahtar kaynak koda, `gradle.properties` dosyasına veya GitHub'a eklenmemelidir.

## Ücretsiz kota politikası
Ücretsiz kota veya sağlayıcı limiti tespit edildiğinde işlem durdurulmalı; otomatik ücretli plana geçilmemelidir. `QuotaGate` bu politikanın merkezi kontrol noktası olarak tanımlanmıştır. Gerçek sağlayıcı adaptörleri eklenirken her isteğin öncesinde ve sonrasında kullanılmalıdır.

## Yayın onayı
`scenarioApproved` ve `finalVideoApproved` birbirinden bağımsızdır. YouTube yükleme adaptörü yalnızca ikisi de true olduğunda çağrılmalıdır. Varsayılan görünürlük `public` olsa da bu durum kullanıcı onayının yerine geçmez.
