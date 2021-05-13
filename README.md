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

        // App dependencies
        androidGradlePluginVersion = '4.0.2'
        appCompatVersion = '1.2.0'
        constraintLayoutVersion = '2.0.4'
        daggerVersion = '2.24'
        firebaseAnalyticsVersion = '18.0.3'
        firebaseKtxVersion = '19.7.0'
        googleServicesVersion = '4.3.5'
        kotlinVersion = '1.4.21'
        ktxVersion = '1.3.2'
        materialVersion = '1.4.0-beta01'
        ossLicensesPluginVersion = '0.10.4'
        splittiesVersion = '2.1.1'
    }

    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        classpath "com.android.tools.build:gradle:$androidGradlePluginVersion"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        classpath "com.google.gms:google-services:$googleServicesVersion"
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
