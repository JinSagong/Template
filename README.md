# Template

## build.gradle (project)
```gradle
buildscript {
    ext {
        // App Info
        applicationId = "[applicationId]"
        versionName = '1.0.0' // X.Y.Z; X = Major, Y = minor, Z = Patch level
        versionCode = 1_00_00 // X YY ZZ

        // SDK and tools
        compileSdkVersion = 30
        buildToolsVersion = "29.0.3"
        minSdkVersion = 21
        targetSdkVersion = 30

        // Project dependencies
        androidGradlePluginVersion = '4.0.2'
        kotlinPluginVersion = '1.4.21'
        googleServiceVersion = '4.3.5'
        ossLicensesPluginVersion = '0.10.4'

        // Test
        junitVersion = '4.12'
        junitTestVersion = '1.1.3'
        espressoVersion = '3.4.0'

        // AndroidX
        appCompatVersion = '1.3.1'
        coreKtxVersion = '1.6.0'
        materialVersion = '1.4.0'
        constraintLayoutVersion = '2.1.2'
        lifecycleVersion = '2.2.0'

        // Kotlin and Splitties
        kotlinVersion = '1.5.30'
        splittiesVersion = '2.1.1'

        // Dagger
        daggerVersion = '2.35.1'
    }

    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        classpath "com.android.tools.build:gradle:$androidGradlePluginVersion"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinPluginVersion"
        classpath "com.google.gms:google-services:$googleServiceVersion"
        classpath "com.google.android.gms:oss-licenses-plugin:$ossLicensesPluginVersion"
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
