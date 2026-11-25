package com.gzautoreplay.app.data.storage

import android.content.Context

/**
 * Implementação de [KeyValueStore] com SharedPreferences do Android.
 *
 * Por que SharedPreferences nesta etapa: é nativo do Android (zero dependências), guarda
 * poucos dados (algumas dezenas de regras) e os dados ficam só no aparelho, em área privada
 * do app. Room entra na etapa 2, quando houver histórico de eventos.
 */
class SharedPreferencesKeyValueStore(
    context: Context,
    fileName: String,
) : KeyValueStore {

    private val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun getString(key: String): String? = prefs.getString(key, null)

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
