pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        /*google()
        jcenter()
        gradlePluginPortal()*/
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        /*maven { url = uri("https://jitpack.io") } // Use uri() to specify the repository URL
        google()
        jcenter()*/
    }
}

rootProject.name = "SkyWatch"
include(":app")
 