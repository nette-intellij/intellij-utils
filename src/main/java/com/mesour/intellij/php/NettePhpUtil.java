package com.mesour.intellij.php;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NettePhpUtil {
    public static boolean isReferenceFor(@NotNull PhpClass originalClass, @NotNull PhpClass targetClass) {
        return isReferenceFor(originalClass.getFQN(), targetClass);
    }

    public static boolean isReferenceFor(@NotNull String originalClass, @NotNull PhpClass targetClass) {
        return isReferenceFor(new String[]{originalClass}, targetClass);
    }

    public static boolean isReferenceFor(@NotNull String[] originalClasses, @NotNull PhpClass targetClass) {
        List<String> normalized = new ArrayList<>();
        for (String originalClass : originalClasses) {
            originalClass = normalizeClassName(originalClass);
            normalized.add(originalClass);
            if (originalClass.equals(targetClass.getFQN())) {
                return true;
            }
        }

        ExtendsList extendsList = targetClass.getExtendsList();
        for (ClassReference reference : extendsList.getReferenceElements()) {
            String fqn = reference.getFQN();
            if (fqn == null) {
                continue;
            }

            for (String originalClass : normalized) {
                if (fqn.equals(originalClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public static String normalizeClassName(@Nullable String className) {
        if (className != null && className.startsWith("#S")) {
            // since 2019.2.3 phpstorm started to prefix _static_ class type with "#S"
            className = className.substring(2);
        }

        String normalized = className == null ? "" : (className.startsWith("\\") ? className : ("\\" + className));
        if (normalized.contains("|null")) {
            normalized = normalized.replace("|null", "");
        }
        if (normalized.contains("|NULL")) {
            normalized = normalized.replace("|NULL", "");
        }
        return normalized;
    }

    @NotNull
    public static String normalizePhpVariable(final @NotNull String name) {
        return name.startsWith("$") ? name.substring(1) : name;
    }

    @NotNull
    public static String normalizeMethodName(final @NotNull String name) {
        return name.trim().toLowerCase();
    }

    public static boolean isNullable(final @NotNull PhpType type) {
        return type.isNullable();
    }

}