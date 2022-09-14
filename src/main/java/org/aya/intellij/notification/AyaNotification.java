package org.aya.intellij.notification;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class AyaNotification {
  private static NotificationGroup GROUP;

  public static @NotNull NotificationGroup getNotificationGroup() {
    // Duplicate construction doesn't matter.
    if (GROUP == null) {
      GROUP = NotificationGroupManager.getInstance()
        // registered in plugin.xml
        .getNotificationGroup("org.aya.intellij.notification.AyaNotification");
    }

    return GROUP;
  }

  /**
   * Display a top-level balloon notification
   * @param project the project that the notification displays in
   * @param title the title
   * @param content the content
   */
  public static void showError(Project project, String title, String content) {
    getNotificationGroup()
      .createNotification(title, content, NotificationType.ERROR)
      .notify(project);
  }
}
