package com.elsauron.aitube.data

enum class Topic(val label: String) { AI("AI / Teknoloji"), SCIENCE("Bilim") }
enum class Stage(val label: String) {
    IDEA("Hazır"), RESEARCH("Araştırma bekliyor"), SCRIPT("Senaryo hazırlanıyor"), SCRIPT_REVIEW("Senaryo onayı bekliyor"),
    VOICE("Ses üretimi bekliyor"), VISUALS("Görseller bekliyor"), EDIT("Montaj bekliyor"), FINAL_REVIEW("Son video onayı bekliyor"),
    READY("Yüklemeye hazır"), UPLOADED("Yüklendi"), ERROR("Hata")
}

data class ShortProject(
    val id: String = java.util.UUID.randomUUID().toString(),
    val topic: Topic = Topic.AI,
    val title: String = "Yeni Shorts",
    val sourceNotes: String = "",
    val script: String = "",
    val stage: Stage = Stage.IDEA,
    val scriptApproved: Boolean = false,
    val finalVideoApproved: Boolean = false,
    val finalVideoPath: String? = null,
    val uploadVisibility: String = "public"
) {
    fun canUpload(): Boolean = scriptApproved && finalVideoApproved && stage == Stage.READY && !finalVideoPath.isNullOrBlank()
}
