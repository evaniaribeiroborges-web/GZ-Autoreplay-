package com.gzautoreplay.app.data.rules

import com.gzautoreplay.app.data.storage.KeyValueStore
import com.gzautoreplay.app.domain.rules.Rule
import com.gzautoreplay.app.domain.rules.RuleRepository

/**
 * Salva as regras em [KeyValueStore] usando [RuleJsonCodec].
 *
 * Se os dados salvos estiverem corrompidos, o texto original é copiado para [BACKUP_KEY]
 * (para recuperação manual) e o app começa com a lista vazia. Assim o app não quebra e
 * nada é apagado sem deixar rastro.
 */
class RuleRepositoryImpl(
    private val store: KeyValueStore,
) : RuleRepository {

    override fun loadRules(): List<Rule> {
        val raw = store.getString(STORAGE_KEY) ?: return emptyList()
        return try {
            RuleJsonCodec.decode(raw)
        } catch (e: RuleStorageException) {
            store.putString(BACKUP_KEY, raw)
            store.remove(STORAGE_KEY)
            emptyList()
        }
    }

    override fun saveRules(rules: List<Rule>) {
        store.putString(STORAGE_KEY, RuleJsonCodec.encode(rules))
    }

    companion object {
        const val STORAGE_KEY = "rules_v1"
        const val BACKUP_KEY = "rules_v1_corrupted_backup"
    }
}
