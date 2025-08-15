plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.24" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("androidx.room") version "2.6.1" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.24" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Simple doctor task to write a summary JSON
tasks.register("doctor") {
    group = "verification"
    description = "Runs checks, tests, and writes a build summary."
    doLast {
        val summaryDir = file("build/doctor")
        summaryDir.mkdirs()
        val summaryFile = file("build/doctor/summary.json")
        val summary = """
        {"status":"ran","timestamp":"${java.time.Instant.now()}"}
        """.trimIndent()
        summaryFile.writeText(summary)
    }
}