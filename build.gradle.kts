val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val mysql_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("com.google.cloud.tools.jib") version "3.1.4"
    id("com.palantir.git-version") version "0.12.3"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("java")
}

group = "com.nnao45"

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()
version = details.gitHashFull

application {
    mainClass.set("com.nnao45.ApplicationKt")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    // log
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // rdb
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("mysql:mysql-connector-java:$mysql_version")
    // test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

task("runMain", JavaExec::class) {
    main = "com.nnao45.ApplicationKt"
    classpath = sourceSets["main"].runtimeClasspath
}

jib {
    from {
        image = "docker://nnao45/query-lab-api-kt:base"
    }
    to {
        image = "docker.io/nnao45/query-lab-api-kt"
        tags = setOf(details.gitHashFull, "latest")
    }
    container {
        jvmFlags = listOf("-Xms512m", "-Xmx4096m", "-Xdebug")
        ports = listOf("8081/tcp")
        format = com.google.cloud.tools.jib.api.buildplan.ImageFormat.OCI
    }
}
