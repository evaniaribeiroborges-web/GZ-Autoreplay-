plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// O GitHub Actions envia o número da execução (-PgzVersionCode=N).
// Assim, cada APK novo tem versionCode MAIOR que o anterior e o Android
// aceita instalar por cima, mantendo os dados do app.
// Fallback para builds locais. No GitHub Actions o número sempre vem de -PgzVersionCode.
val gzVersionCode = (findProperty("gzVersionCode") as String?)?.toIntOrNull() ?: 2
val gzVersionName = "0.2.0"

// A chave de assinatura chega por variáveis de ambiente (GitHub Secrets).
// Ela nunca fica gravada no repositório.
val gzKeystorePath: String? = System.getenv("GZ_KEYSTORE_PATH")?.takeIf { it.isNotBlank() }

android {
    namespace = "com.gzautoreplay.app"
    compileSdk = 35

    defaultConfig {
        // ATENÇÃO: o applicationId NUNCA deve mudar depois da primeira instalação real.
        applicationId = "com.gzautoreplay.app"
        minSdk = 26
        targetSdk = 35
        versionCode = gzVersionCode
        versionName = gzVersionName
    }

    signingConfigs {
        if (gzKeystorePath != null) {
            create("gzRelease") {
                storeFile = file(gzKeystorePath)
                storePassword = System.getenv("GZ_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("GZ_KEY_ALIAS")
                keyPassword = System.getenv("GZ_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (gzKeystorePath != null) {
                signingConfig = signingConfigs.getByName("gzRelease")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    // ViewModel para a tela de regras (estado sobrevive à rotação de tela).
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    // Somente para testes: o org.json do Android é um "stub" em testes unitários no computador.
    testImplementation(libs.org.json)
}
