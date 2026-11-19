package com.elsauron.aitube.data

enum class ContentCategory { AI_TECH, SCIENCE }
enum class ProductionStage {
    IDLE, RESEARCH, SCRIPT, SCRIPT_REVIEW, VOICE, VISUALS, EDITING,
    FINAL_REVIEW, READY_TO_UPLOAD, UPLOADED, BLOCKED
}

data class ShortProject(
    val category: ContentCategory,
    val stage: ProductionStage = ProductionStage.IDLE,
    val scenarioApproved: Boolean = false,
    val finalVideoApproved: Boolean = false,
    val visibility: String = "public",
    val durationSeconds: Int = 50,
    val aspectRatio: String = "9:16"
) {
    fun canUpload(): Boolean = scenarioApproved && finalVideoApproved &&
        stage == ProductionStage.READY_TO_UPLOAD
}

sealed interface QuotaDecision {
    data object Allowed : QuotaDecision
    data class Stop(val reason: String) : QuotaDecision
}

/** Provider adapters must call this before paid/limited API operations. */
class QuotaGate(private val freeQuotaAvailable: () -> Boolean) {
    fun beforeRequest(): QuotaDecision =
        if (freeQuotaAvailable()) QuotaDecision.Allowed
        else QuotaDecision.Stop("Ücretsiz kota doldu. İşlem durduruldu; ücretli kullanıma geçilmedi.")
}
