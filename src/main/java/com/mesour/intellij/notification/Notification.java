package com.mesour.intellij.notification;

import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Notification extends com.intellij.notification.Notification {
    public Notification(
            @NotNull @NonNls String group,
            @NotNull @Nls(capitalization = Nls.Capitalization.Title) String title,
            @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String content,
            @NotNull NotificationType type
    ) {
        super(group, title, content, type);
    }

    public static Notification create(
            @NotNull @NonNls String group,
            @NotNull @Nls(capitalization = Nls.Capitalization.Title) String title,
            @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String content,
            @NotNull NotificationType type
    ) {
        return new Notification(group, title, content, type);
    }

    @Override
    public void notify(@Nullable Project project) {
        if (project != null && !project.isDisposed() && !project.isDefault()) {
            super.notify(project);
        } else {
            Application app = ApplicationManager.getApplication();
            if (!app.isDisposed()) {
                app.getMessageBus().syncPublisher(Notifications.TOPIC).notify(this);
            }
        }
    }
}