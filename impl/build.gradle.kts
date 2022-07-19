plugins {
    kotlin("kapt")
}

dependencies {
    implementation("net.java.dev.jna:jna:5.11.0")
    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")
    api(project(":${rootProject.name}-core"))
}