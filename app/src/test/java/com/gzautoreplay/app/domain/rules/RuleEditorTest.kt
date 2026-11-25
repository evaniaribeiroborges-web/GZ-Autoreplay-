package com.gzautoreplay.app.domain.rules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleEditorTest {

    /** Gerador previsível: id-1, id-2, ... */
    private fun editorComIdsPrevisiveis(): RuleEditor {
        var contador = 0
        return RuleEditor { "id-${++contador}" }
    }

    private fun draft(
        keyword: String = "oi",
        response: String = "olá, tudo bem?",
        type: MatchType = MatchType.CONTAINS,
        ignoreCase: Boolean = true,
    ) = RuleDraft(keyword = keyword, response = response, matchType = type, ignoreCase = ignoreCase)

    private fun regra(id: String, enabled: Boolean = true) = Rule(
        id = id,
        keyword = "palavra-$id",
        response = "resposta-$id",
        matchType = MatchType.EQUALS,
        ignoreCase = false,
        enabled = enabled,
    )

    // ---------- add ----------

    @Test
    fun add_criaRegraAtivaComIdGerado() {
        val editor = editorComIdsPrevisiveis()

        val resultado = editor.add(emptyList(), draft())

        assertEquals(1, resultado.size)
        assertEquals("id-1", resultado[0].id)
        assertTrue(resultado[0].enabled)
    }

    @Test
    fun add_copiaTodosOsCamposDoRascunho() {
        val editor = editorComIdsPrevisiveis()

        val criada = editor.add(
            emptyList(),
            draft(keyword = "preço", response = "R\$ 10", type = MatchType.STARTS_WITH, ignoreCase = false),
        ).single()

        assertEquals("preço", criada.keyword)
        assertEquals("R\$ 10", criada.response)
        assertEquals(MatchType.STARTS_WITH, criada.matchType)
        assertFalse(criada.ignoreCase)
        assertTrue(criada.enabled)
    }

    @Test
    fun add_removeEspacosDasBordasDaPalavraEDaResposta() {
        val editor = editorComIdsPrevisiveis()

        val criada = editor.add(emptyList(), draft(keyword = "  oi  ", response = "\n olá \n")).single()

        assertEquals("oi", criada.keyword)
        assertEquals("olá", criada.response)
    }

    @Test
    fun add_colocaNovaRegraNoFimSemAlterarAsOutras() {
        val editor = editorComIdsPrevisiveis()
        val existentes = listOf(regra("a"), regra("b", enabled = false))

        val resultado = editor.add(existentes, draft())

        assertEquals(listOf("a", "b", "id-1"), resultado.map { it.id })
        assertEquals(existentes[0], resultado[0])
        assertEquals(existentes[1], resultado[1])
    }

    @Test
    fun add_comRascunhoInvalidoLancaErroEMantemListaOriginal() {
        val editor = editorComIdsPrevisiveis()
        val existentes = listOf(regra("a"))

        assertThrows(IllegalArgumentException::class.java) {
            editor.add(existentes, draft(keyword = "   "))
        }
        assertEquals(listOf("a"), existentes.map { it.id })
    }

    @Test
    fun add_comIdJaExistenteLancaErro() {
        val editor = RuleEditor { "repetido" }

        assertThrows(IllegalArgumentException::class.java) {
            editor.add(listOf(regra("repetido")), draft())
        }
    }

    @Test
    fun add_usaIgnoreCaseVerdadeiroPorPadrao() {
        val editor = editorComIdsPrevisiveis()

        val criada = editor.add(emptyList(), RuleDraft(keyword = "oi", response = "olá", matchType = MatchType.EQUALS)).single()

        assertTrue(criada.ignoreCase)
    }

    // ---------- toggle ----------

    @Test
    fun toggle_desativaRegraAtiva() {
        val resultado = RuleEditor().toggle(listOf(regra("a", enabled = true)), "a")

        assertFalse(resultado.single().enabled)
    }

    @Test
    fun toggle_ativaRegraDesativada() {
        val resultado = RuleEditor().toggle(listOf(regra("a", enabled = false)), "a")

        assertTrue(resultado.single().enabled)
    }

    @Test
    fun toggle_alteraSomenteARegraSelecionada() {
        val entrada = listOf(regra("a"), regra("b"), regra("c"))

        val resultado = RuleEditor().toggle(entrada, "b")

        assertTrue(resultado[0].enabled)
        assertFalse(resultado[1].enabled)
        assertTrue(resultado[2].enabled)
    }

    @Test
    fun toggle_comIdDesconhecidoNaoAlteraNada() {
        val entrada = listOf(regra("a"))

        assertEquals(entrada, RuleEditor().toggle(entrada, "inexistente"))
    }

    @Test
    fun toggle_naoAlteraAListaOriginal() {
        val entrada = listOf(regra("a", enabled = true))

        RuleEditor().toggle(entrada, "a")

        assertTrue(entrada.single().enabled)
    }

    // ---------- delete ----------

    @Test
    fun delete_removeSomenteARegraSelecionada() {
        val resultado = RuleEditor().delete(listOf(regra("a"), regra("b"), regra("c")), "b")

        assertEquals(listOf("a", "c"), resultado.map { it.id })
    }

    @Test
    fun delete_comIdDesconhecidoNaoAlteraNada() {
        val entrada = listOf(regra("a"))

        assertEquals(entrada, RuleEditor().delete(entrada, "inexistente"))
    }

    @Test
    fun delete_deListaVaziaContinuaVazia() {
        assertTrue(RuleEditor().delete(emptyList(), "a").isEmpty())
    }

    @Test
    fun delete_naoAlteraAListaOriginal() {
        val entrada = listOf(regra("a"), regra("b"))

        RuleEditor().delete(entrada, "a")

        assertEquals(listOf("a", "b"), entrada.map { it.id })
    }
}

class RuleSummaryTest {

    @Test
    fun listaVaziaTemZeroRegrasEZeroAtivas() {
        assertEquals(RuleSummary(total = 0, active = 0), RuleSummary.of(emptyList()))
    }

    @Test
    fun contaTotalEAtivasSeparadamente() {
        val regras = listOf(
            Rule("1", "a", "r", MatchType.CONTAINS, true, enabled = true),
            Rule("2", "b", "r", MatchType.CONTAINS, true, enabled = false),
            Rule("3", "c", "r", MatchType.EQUALS, false, enabled = true),
        )

        assertEquals(RuleSummary(total = 3, active = 2), RuleSummary.of(regras))
    }
}
