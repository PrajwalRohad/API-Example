pluginManagement {
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
        maven {
            url = uri("https://www.maven.crackle.co.in/releases")
            credentials {
                username = "admin"
                password = "ycqcKDmwGHQvfqw0pWmzCUu32Q8xHRx/2Ea5OivM82eOv2FmQJjCdAOhMquZv+UH"
            }
        }
    }

}

rootProject.name = "API-Example"
include(":app")
 