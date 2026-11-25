package com.gzautoreplay.app.domain.rules

/**
 * Contrato de armazenamento das regras. O domínio não sabe como os dados são guardados.
 * Hoje a implementação usa SharedPreferences; a etapa 2 pode trocá-la por Room sem
 * mexer no motor de regras nem na tela.
 */
interface RuleRepository {

    /** Carrega todas as regras salvas. Retorna lista vazia se não houver nenhuma. */
    fun loadRules(): List<Rule>

    /** Substitui todas as regras salvas por [rules]. */
    fun saveRules(rules: List<Rule>)
}
