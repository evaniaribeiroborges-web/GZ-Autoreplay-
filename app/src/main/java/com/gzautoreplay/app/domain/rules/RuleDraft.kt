package com.gzautoreplay.app.domain.rules

/**
 * Dados digitados pelo usuário para criar uma regra, antes de validar e gerar o id.
 *
 * @property ignoreCase por padrão `true`, porque a maioria das pessoas não quer
 * diferenciar "Oi" de "oi".
 */
data class RuleDraft(
    val keyword: String,
    val response: String,
    val matchType: MatchType,
    val ignoreCase: Boolean = true,
)
