package com.gzautoreplay.app.domain.rules

/** Contagens exibidas na tela de regras. */
data class RuleSummary(
    val total: Int,
    val active: Int,
) {
    companion object {
        fun of(rules: List<Rule>): RuleSummary =
            RuleSummary(total = rules.size, active = rules.count { it.enabled })
    }
}
