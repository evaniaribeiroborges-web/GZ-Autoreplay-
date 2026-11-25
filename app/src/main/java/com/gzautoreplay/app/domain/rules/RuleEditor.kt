package com.gzautoreplay.app.domain.rules

import java.util.UUID

/**
 * Operações de criar, ativar/desativar e excluir regras.
 *
 * Todas as funções recebem a lista atual e devolvem uma NOVA lista; a lista original
 * nunca é alterada. Isso facilita testes e evita estados inconsistentes na tela.
 *
 * @param newId gerador de ids. Em produção usa UUID; nos testes, um gerador previsível.
 */
class RuleEditor(
    private val newId: () -> String = { UUID.randomUUID().toString() },
) {

    /**
     * Cria uma regra ativa a partir do [draft] e a adiciona no fim da lista.
     *
     * @throws IllegalArgumentException se o rascunho for inválido ([RuleValidator]) ou
     * se o id gerado já existir.
     */
    fun add(rules: List<Rule>, draft: RuleDraft): List<Rule> {
        val errors = RuleValidator.validate(draft)
        require(errors.isEmpty()) { "Regra inválida: $errors" }

        val id = newId()
        require(rules.none { it.id == id }) { "Já existe uma regra com o id $id." }

        val rule = Rule(
            id = id,
            keyword = draft.keyword.trim(),
            response = draft.response.trim(),
            matchType = draft.matchType,
            ignoreCase = draft.ignoreCase,
            enabled = true,
        )
        return rules + rule
    }

    /** Inverte o estado ativo/inativo da regra com [id]. Id desconhecido não altera nada. */
    fun toggle(rules: List<Rule>, id: String): List<Rule> =
        rules.map { if (it.id == id) it.copy(enabled = !it.enabled) else it }

    /** Remove a regra com [id]. Id desconhecido não altera nada. */
    fun delete(rules: List<Rule>, id: String): List<Rule> =
        rules.filterNot { it.id == id }
}
