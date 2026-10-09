plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.filipe.nifcaixa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.filipe.nifcaixa"
        minSdk = 26          // Android 8.0 ou mais recente
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

// Sem dependências externas: o código de barras é desenhado pelo nosso próprio código.
dependencies {
}
