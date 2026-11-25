package com.gzautoreplay.app.data.storage

/**
 * Armazenamento chave-valor de texto. Existe para que a lógica de salvar regras possa
 * ser testada no computador, sem precisar do Android.
 */
interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}
