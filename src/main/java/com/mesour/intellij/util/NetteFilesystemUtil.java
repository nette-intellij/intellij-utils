package com.mesour.intellij.util;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class NetteFilesystemUtil {
    @Nullable
    public static Path saveFileToProjectTemp(
            final @NotNull Project project,
            final @NotNull String setupFile,
            final @NotNull String pluginDir
    ) {
        try {
            Path tempDir = getTempPath(project, pluginDir);
            if (!Files.isDirectory(tempDir)) {
                Files.createDirectory(tempDir);
            }

            Path setupScriptPath = Paths.get(tempDir.toString(), setupFile);
            Path parent = setupScriptPath.getParent();
            if (!Files.isDirectory(parent)) {
                Files.createDirectory(parent);
            }
            InputStream setupResourceStream = NetteFilesystemUtil.class.getClassLoader().getResourceAsStream(setupFile);
            if (setupResourceStream == null) {
                return null;
            }
            Files.copy(setupResourceStream, setupScriptPath, StandardCopyOption.REPLACE_EXISTING);
            setupResourceStream.close();

            return setupScriptPath;

        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static Path getPathToProjectTemp(
            final @NotNull Project project,
            final @NotNull String setupFile,
            final @NotNull String pluginDir
    ) {
        try {
            Path tempDir = getTempPath(project, pluginDir);
            if (!Files.isDirectory(tempDir)) {
                Files.createDirectory(tempDir);
            }

            return Paths.get(tempDir.toString(), setupFile);

        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static XmlFile getXmlFileForPath(final @NotNull Project project, final @NotNull Path path) {
        PsiFile psiFile = getPsiFileForPath(project, path);
        return psiFile instanceof XmlFile ? (XmlFile) psiFile : null;
    }

    @Nullable
    public static PsiFile getPsiFileForPath(final @NotNull Project project, final @NotNull Path path) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(path.toString());
        if (virtualFile == null) {
            return null;
        }
        return PsiManager.getInstance(project).findFile(virtualFile);
    }

    @NotNull
    private static Path getTempPath(final @NotNull Project project, final @NotNull String pluginDir) {
        if (project.getBasePath() != null) {
            return Paths.get(project.getBasePath(), ".idea", pluginDir);
        } else {
            return Paths.get(PathManager.getPluginsPath(), pluginDir);
        }
    }

}
