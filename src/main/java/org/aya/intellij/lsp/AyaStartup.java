package org.aya.intellij.lsp;

import com.google.gson.JsonParseException;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import org.aya.generic.Constants;
import org.aya.intellij.AyaBundle;
import org.aya.intellij.notification.AyaNotification;
import org.aya.intellij.service.AyaSettingService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AyaStartup implements StartupActivity {
  @Override public void runActivity(@NotNull Project project) {
    if (!AyaSettingService.getInstance().useAyaLsp) return;
    var ayaJson = findAyaJson(project);
    if (ayaJson != null) {
      if (!JB.fileSupported(ayaJson)) return;

      try {
        AyaLsp.start(ayaJson, project);
      } catch (JsonParseException e) {
        // Catch exception from service.registerLibrary -> lsp.registerLibrary

        // Show error
        AyaNotification.showError(project,
          AyaBundle.INSTANCE.message("aya.notice.error.lsp.title"),
          AyaBundle.INSTANCE.message("aya.notice.error.lsp", e.getMessage()));
      }
    }
  }

  private @Nullable VirtualFile findAyaJson(@NotNull Project project) {
    var mods = ModuleManager.getInstance(project).getModules();
    if (mods.length != 1) return null;
    var mod = mods[0];
    var contentRoots = ModuleRootManager.getInstance(mod).getContentRoots();
    if (contentRoots.length != 1) return null;
    var root = contentRoots[0];
    return root.findChild(Constants.AYA_JSON);
  }
}
