package com.elsauron.aitube.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WorkflowRepository {
    private val _projects = MutableStateFlow(listOf(ShortProject()))
    val projects: StateFlow<List<ShortProject>> = _projects
    fun add(topic: Topic) { _projects.update { listOf(ShortProject(topic = topic)) + it } }
    fun update(project: ShortProject) { _projects.update { list -> list.map { if (it.id == project.id) project else it } } }
    fun approveScript(id: String, approved: Boolean) = mutate(id) { it.copy(scriptApproved = approved, stage = if (approved) Stage.VOICE else Stage.SCRIPT_REVIEW) }
    fun attachFinalVideo(id: String, path: String) = mutate(id) { it.copy(finalVideoPath = path, stage = Stage.FINAL_REVIEW, finalVideoApproved = false) }
    fun approveFinal(id: String, approved: Boolean) = mutate(id) { it.copy(finalVideoApproved = approved, stage = if (approved) Stage.READY else Stage.FINAL_REVIEW) }
    fun uploadAllowed(id: String): Boolean = _projects.value.firstOrNull { it.id == id }?.canUpload() == true
    private fun mutate(id: String, transform: (ShortProject) -> ShortProject) = _projects.update { list -> list.map { if (it.id == id) transform(it) else it } }
}
