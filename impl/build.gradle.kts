plugins {
    kotlin("kapt")
}

dependencies {
    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")
    api(project(":${rootProject.name}-core"))
}