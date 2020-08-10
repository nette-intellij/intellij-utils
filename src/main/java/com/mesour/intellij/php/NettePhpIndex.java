package com.mesour.intellij.php;

import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class NettePhpIndex {
    @NotNull
    public static Collection<PhpNamedElement> getAllClassNamesAndInterfaces(
            final @NotNull Project project,
            final @NotNull Collection<String> classNames
    ) {
        Collection<PhpNamedElement> variants = new THashSet<>();
        PhpIndex phpIndex = getPhpIndex(project);

        for (String name : classNames) {
            variants.addAll(filterClasses(phpIndex.getClassesByName(name), null));
            variants.addAll(filterClasses(phpIndex.getInterfacesByName(name), null));
        }
        return variants;
    }

    @NotNull
    public static Collection<Function> getAllFunctions(
            final @NotNull Project project,
            final @NotNull Collection<String> functionNames
    ) {
        Collection<Function> variants = new THashSet<>();
        PhpIndex phpIndex = getPhpIndex(project);

        for (String name : functionNames) {
            variants.addAll(phpIndex.getFunctionsByName(name));
        }
        return variants;
    }

    @NotNull
    private static Collection<PhpClass> filterClasses(
            final @NotNull Collection<PhpClass> classes,
            final @Nullable String namespace
    ) {
        if (namespace == null) {
            return classes;
        }
        String normalized = "\\" + namespace + "\\";
        Collection<PhpClass> result = new ArrayList<>();
        for (PhpClass cls : classes) {
            String classNs = cls.getNamespaceName();
            if (classNs.equals(normalized) || classNs.startsWith(normalized)) {
                result.add(cls);
            }
        }
        return result;
    }

    @NotNull
    public static Collection<PhpClass> getClassesByFQN(final @NotNull Project project, final @NotNull String className) {
        return getPhpIndex(project).getAnyByFQN(className);
    }

    @NotNull
    public static Collection<Function> getFunctionByName(
            final @NotNull Project project,
            final @NotNull String functionName
    ) {
        return getPhpIndex(project).getFunctionsByName(functionName);
    }

    @NotNull
    public static Collection<PhpNamespace> getNamespacesByName(
            final @NotNull Project project,
            final @NotNull String className
    ) {
        return getPhpIndex(project).getNamespacesByName(className);
    }

    @NotNull
    public static Collection<String> getAllExistingFunctionNames(
            final @NotNull Project project,
            final @NotNull PrefixMatcher prefixMatcher
    ) {
        return getPhpIndex(project).getAllFunctionNames(prefixMatcher);
    }

    @NotNull
    public static Collection<String> getAllExistingClassNames(
            final @NotNull Project project,
            final @NotNull PrefixMatcher prefixMatcher
    ) {
        return getPhpIndex(project).getAllClassNames(prefixMatcher);
    }

    @NotNull
    private static PhpIndex getPhpIndex(final @NotNull Project project) {
        return PhpIndex.getInstance(project);
    }

}