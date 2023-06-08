package org.aya.intellij.actions.lsp

import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFile
import org.aya.generic.Constants
import org.aya.intellij.service.AyaSettingService

class AyaStartup : ProjectActivity {
  override suspend fun execute(project: Project) {
    if (AyaSettingService.getInstance().ayaLspState == AyaSettingService.AyaState.Disable) return
    if (AyaLsp.isActive(project)) return
    val ayaJson = findAyaJson(project)
    if (ayaJson != null) {
      if (!JB.fileSupported(ayaJson)) return
      AyaLsp.start(ayaJson, project)
    }
  }

  private fun findAyaJson(project: Project): VirtualFile? {
    return project.getBaseDirectories()
      .takeIf { it.size == 1 }
      ?.firstOrNull()
      ?.findChild(Constants.AYA_JSON)
  }
}
