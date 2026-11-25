package com.gzautoreplay.app.domain.rules

/** Motivos pelos quais um [RuleDraft] não pode virar uma regra. */
enum class RuleValidationError {
    EMPTY_KEYWORD,
    KEYWORD_TOO_LONG,
    EMPTY_RESPONSE,
    RESPONSE_TOO_LONG,
}

/**
 * Regras de validação dos dados digitados. Espaços nas bordas não contam como conteúdo.
 */
object RuleValidator {

    const val MAX_KEYWORD_LENGTH = 100
    const val MAX_RESPONSE_LENGTH = 1000

    /** Retorna todos os erros encontrados. Lista vazia significa que o rascunho é válido. */
    fun validate(draft: RuleDraft): List<RuleValidationError> = buildList {
        val keyword = draft.keyword.trim()
        val response = draft.response.trim()

        if (keyword.isEmpty()) {
            add(RuleValidationError.EMPTY_KEYWORD)
        } else if (keyword.length > MAX_KEYWORD_LENGTH) {
            add(RuleValidationError.KEYWORD_TOO_LONG)
        }

        if (response.isEmpty()) {
            add(RuleValidationError.EMPTY_RESPONSE)
        } else if (response.length > MAX_RESPONSE_LENGTH) {
            add(RuleValidationError.RESPONSE_TOO_LONG)
        }
    }
}
