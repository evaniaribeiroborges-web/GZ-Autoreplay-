package com.gzautoreplay.app.ui.rules

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gzautoreplay.app.data.rules.RuleRepositoryImpl
import com.gzautoreplay.app.data.storage.SharedPreferencesKeyValueStore
import com.gzautoreplay.app.domain.rules.Rule
import com.gzautoreplay.app.domain.rules.RuleDraft
import com.gzautoreplay.app.domain.rules.RuleEditor
import com.gzautoreplay.app.domain.rules.RuleRepository
import com.gzautoreplay.app.domain.rules.RuleValidationError
import com.gzautoreplay.app.domain.rules.RuleValidator

/**
 * Mantém a lista de regras da tela. Cada alteração é salva antes de ser exibida.
 * A lógica de regras fica no domínio; aqui só há coordenação entre tela e armazenamento.
 *
 * Usa o estado observável do Compose (em vez de Flow) para não depender de
 * bibliotecas de coroutines que não são declaradas diretamente no projeto.
 */
class RulesViewModel(
    private val repository: RuleRepository,
    private val editor: RuleEditor = RuleEditor(),
) : ViewModel() {

    private var currentRules by mutableStateOf(repository.loadRules())

    /** Regras atuais. Ler esta propriedade dentro de uma tela faz a tela atualizar sozinha. */
    val rules: List<Rule>
        get() = currentRules

    /**
     * Tenta criar a regra. Se houver erros de validação, devolve a lista de erros
     * e não salva nada. Lista vazia significa sucesso.
     */
    fun addRule(draft: RuleDraft): List<RuleValidationError> {
        val errors = RuleValidator.validate(draft)
        if (errors.isNotEmpty()) return errors
        commit(editor.add(currentRules, draft))
        return emptyList()
    }

    fun toggleRule(id: String) {
        commit(editor.toggle(currentRules, id))
    }

    fun deleteRule(id: String) {
        commit(editor.delete(currentRules, id))
    }

    private fun commit(updated: List<Rule>) {
        repository.saveRules(updated)
        currentRules = updated
    }

    companion object {
        private const val PREFERENCES_FILE = "gz_autoreplay_rules"

        /** Fábrica usada pela tela, com o armazenamento real do Android. */
        fun factory(context: Context) = viewModelFactory {
            initializer {
                RulesViewModel(
                    RuleRepositoryImpl(
                        SharedPreferencesKeyValueStore(context.applicationContext, PREFERENCES_FILE),
                    ),
                )
            }
        }
    }
}
