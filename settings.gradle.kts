pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ArcLogbook"

include(":app")
include(":core:common")
include(":core:database")
include(":core:network")
include(":feature:logbook")
include(":feature:intel")
include(":feature:vuln")
include(":feature:darkweb")
include(":feature:osint")
include(":feature:metadata")
include(":feature:ai")
include(":feature:connectors")
include(":feature:widgets")
include(":feature:export")
include(":feature:settings")