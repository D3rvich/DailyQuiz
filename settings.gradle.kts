pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "DailyQuiz"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:navigation")
include(":core:network")
include(":core:database")
include(":feature:quiz:api")
include(":feature:quiz:impl")
include(":feature:history:api")
include(":feature:history:impl")
include(":feature:result:api")
include(":feature:result:impl")