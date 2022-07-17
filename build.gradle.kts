import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10" apply false
}

allprojects {
    group = "com.kairlec"
    version = "1.0.0"

    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            jvmTarget = "11"
            allWarningsAsErrors = true
        }
    }
}