import java.nio.file.Files.*
import java.nio.file.Path

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}

rootProject.name = "environment-switcher"

fun Path.relativeTo(base: Path): List<String> {
    return base.relativize(this).run {
        (0 until nameCount).map { getName(it) }.mapNotNull { it.fileName?.toString()?.ifBlank { null } }
    }
}

val rootPath: Path = rootDir.toPath()

walk(rootPath).filter { isDirectory(it) && it.fileName.toString() != "buildSrc" && exists(it.resolve("build.gradle.kts")) }
    .forEach { path ->
        val projectPath = path.relativeTo(rootPath)
        if (projectPath.isNotEmpty()) {
            include(":${projectPath.joinToString(":")}")
            val project = project(path.toFile())
            if (!project.name.startsWith("environment-switcher")) {
                val newName = "environment-switcher-${project.name}"
                project.name = newName
            }
        }
    }
