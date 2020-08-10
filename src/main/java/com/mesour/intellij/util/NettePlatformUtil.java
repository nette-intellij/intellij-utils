package com.mesour.intellij.util;

import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.openapi.application.*;
import com.intellij.openapi.application.ex.ApplicationEx;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NettePlatformUtil {
    public static boolean holdsReadLock() {
        Application app = ApplicationManager.getApplication();
        try {
            return ((ApplicationEx) app).holdsReadLock();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static void openUrl(final @NotNull String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URI uri = new URI(url);
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException ignored) {
                }
            }
        }
    }

    public static void navigateToPsiElement(final @NotNull PsiElement psiElement) {
        final Navigatable descriptor = PsiNavigationSupport.getInstance().getDescriptor(psiElement);
        if (descriptor != null) {
            descriptor.navigate(true);
        }
    }

}
