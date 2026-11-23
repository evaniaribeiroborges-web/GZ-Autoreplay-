// GZ AutoReplay - configuração raiz do Gradle.
// Os plugins são apenas declarados aqui; quem os aplica é o módulo :app.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
