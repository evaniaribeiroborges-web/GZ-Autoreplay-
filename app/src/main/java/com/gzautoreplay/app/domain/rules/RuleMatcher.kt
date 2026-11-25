package com.gzautoreplay.app.domain.rules

/**
 * Motor que decide quais regras correspondem a uma mensagem recebida.
 *
 * Regras de comparação (decididas em DECISOES.md, D14):
 * - Espaços no início e no fim da mensagem e da palavra-chave são ignorados.
 * - Espaços no meio do texto contam (ex.: "bom dia" não é igual a "bom  dia").
 * - A palavra-chave é texto literal, nunca expressão regular.
 * - Acentos contam: "preço" não corresponde a "preco".
 * - Regras desativadas nunca correspondem.
 * - Palavra-chave vazia ou só com espaços nunca corresponde.
 * - Mensagem vazia ou só com espaços não corresponde a nenhuma regra.
 *
 * É um `object` sem estado, então pode ser chamado de qualquer lugar e testado sem Android.
 */
object RuleMatcher {

    /**
     * Retorna as regras que correspondem à [message], na mesma ordem em que estão em [rules].
     * Pode retornar mais de uma regra.
     */
    fun findMatches(message: String, rules: List<Rule>): List<Rule> {
        val text = message.trim()
        if (text.isEmpty()) return emptyList()
        return rules.filter { it.enabled && matches(text, it) }
    }

    private fun matches(text: String, rule: Rule): Boolean {
        val keyword = rule.keyword.trim()
        if (keyword.isEmpty()) return false

        return when (rule.matchType) {
            MatchType.CONTAINS -> text.contains(keyword, ignoreCase = rule.ignoreCase)
            MatchType.EQUALS -> text.equals(keyword, ignoreCase = rule.ignoreCase)
            MatchType.STARTS_WITH -> text.startsWith(keyword, ignoreCase = rule.ignoreCase)
        }
    }
}
