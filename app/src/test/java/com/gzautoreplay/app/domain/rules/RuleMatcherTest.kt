package com.gzautoreplay.app.domain.rules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleMatcherTest {

    private fun rule(
        keyword: String,
        type: MatchType = MatchType.CONTAINS,
        ignoreCase: Boolean = true,
        enabled: Boolean = true,
        id: String = "id-$keyword-$type",
    ) = Rule(
        id = id,
        keyword = keyword,
        response = "resposta",
        matchType = type,
        ignoreCase = ignoreCase,
        enabled = enabled,
    )

    private fun corresponde(message: String, rule: Rule): Boolean =
        RuleMatcher.findMatches(message, listOf(rule)).isNotEmpty()

    // ---------- CONTAINS ----------

    @Test
    fun contains_encontraPalavraNoMeioDaMensagem() {
        assertTrue(corresponde("Qual é o preço do produto?", rule("preço")))
    }

    @Test
    fun contains_encontraPalavraNoInicioENoFim() {
        assertTrue(corresponde("preço alto", rule("preço")))
        assertTrue(corresponde("o preço", rule("preço")))
    }

    @Test
    fun contains_naoCorrespondeQuandoPalavraAusente() {
        assertFalse(corresponde("Bom dia", rule("preço")))
    }

    @Test
    fun contains_ignorandoCaixa_encontraMaiusculas() {
        assertTrue(corresponde("QUAL O PREÇO", rule("preço", ignoreCase = true)))
    }

    @Test
    fun contains_respeitandoCaixa_naoEncontraCaixaDiferente() {
        assertFalse(corresponde("QUAL O PREÇO", rule("preço", ignoreCase = false)))
        assertTrue(corresponde("qual o preço", rule("preço", ignoreCase = false)))
    }

    // ---------- EQUALS ----------

    @Test
    fun equals_encontraTextoExatamenteIgual() {
        assertTrue(corresponde("oi", rule("oi", MatchType.EQUALS)))
    }

    @Test
    fun equals_naoAceitaTextoExtra() {
        assertFalse(corresponde("oi tudo bem", rule("oi", MatchType.EQUALS)))
        assertFalse(corresponde("oiii", rule("oi", MatchType.EQUALS)))
    }

    @Test
    fun equals_ignoraEspacosNasBordasDaMensagem() {
        assertTrue(corresponde("  oi  \n", rule("oi", MatchType.EQUALS)))
    }

    @Test
    fun equals_preservaEspacosInternos() {
        assertFalse(corresponde("bom  dia", rule("bom dia", MatchType.EQUALS)))
        assertTrue(corresponde("bom dia", rule("bom dia", MatchType.EQUALS)))
    }

    @Test
    fun equals_ignorandoCaixa() {
        assertTrue(corresponde("OI", rule("Oi", MatchType.EQUALS, ignoreCase = true)))
    }

    @Test
    fun equals_respeitandoCaixa() {
        assertFalse(corresponde("oi", rule("Oi", MatchType.EQUALS, ignoreCase = false)))
        assertTrue(corresponde("Oi", rule("Oi", MatchType.EQUALS, ignoreCase = false)))
    }

    // ---------- STARTS_WITH ----------

    @Test
    fun startsWith_encontraMensagemQueComeca() {
        assertTrue(corresponde("oi, tudo bem?", rule("oi", MatchType.STARTS_WITH)))
    }

    @Test
    fun startsWith_naoCorrespondeSePalavraEstaNoMeio() {
        assertFalse(corresponde("oi tudo", rule("tudo", MatchType.STARTS_WITH)))
    }

    @Test
    fun startsWith_ignoraEspacosIniciaisDaMensagem() {
        assertTrue(corresponde("   oi tudo", rule("oi", MatchType.STARTS_WITH)))
    }

    @Test
    fun startsWith_ignorandoCaixa() {
        assertTrue(corresponde("OLÁ pessoal", rule("olá", MatchType.STARTS_WITH, ignoreCase = true)))
    }

    @Test
    fun startsWith_respeitandoCaixa() {
        assertFalse(corresponde("oi tudo", rule("Oi", MatchType.STARTS_WITH, ignoreCase = false)))
    }

    // ---------- Regras desativadas ----------

    @Test
    fun regraDesativadaNuncaCorrespondeEmNenhumTipo() {
        for (type in MatchType.entries) {
            val desativada = rule("oi", type, enabled = false)
            assertFalse("Regra desativada não pode corresponder ($type)", corresponde("oi tudo", desativada))
            assertFalse(corresponde("oi", desativada))
        }
    }

    // ---------- Palavra-chave e mensagem vazias ----------

    @Test
    fun palavraChaveVaziaOuSoComEspacosNuncaCorresponde() {
        for (type in MatchType.entries) {
            assertTrue(RuleMatcher.findMatches("qualquer coisa", listOf(rule("", type))).isEmpty())
            assertTrue(RuleMatcher.findMatches("qualquer coisa", listOf(rule("   ", type))).isEmpty())
            assertTrue(RuleMatcher.findMatches("", listOf(rule("", type))).isEmpty())
        }
    }

    @Test
    fun mensagemVaziaOuSoComEspacosNaoCorrespondeANenhumaRegra() {
        for (type in MatchType.entries) {
            val regra = rule("a", type)
            assertTrue(RuleMatcher.findMatches("", listOf(regra)).isEmpty())
            assertTrue(RuleMatcher.findMatches("   \n\t ", listOf(regra)).isEmpty())
        }
    }

    // ---------- Casos extremos de texto ----------

    @Test
    fun palavraChaveComEspacosNasBordasEhTratadaSemELes() {
        assertTrue(corresponde("qual o preço?", rule("  preço  ")))
    }

    @Test
    fun palavraChaveEhTextoLiteralNaoExpressaoRegular() {
        assertTrue(corresponde("use a.c* agora", rule("a.c*")))
        assertFalse(corresponde("abc", rule("a.c*")))
    }

    @Test
    fun acentosContam() {
        assertFalse(corresponde("qual o preco?", rule("preço")))
    }

    @Test
    fun caixaComAcentosEhIgnoradaQuandoOptarPorIsso() {
        assertTrue(corresponde("uma ação rápida", rule("AÇÃO", ignoreCase = true)))
    }

    @Test
    fun mensagemComQuebraDeLinhaEncontraPalavraEmQualquerLinha() {
        assertTrue(corresponde("linha um\nlinha dois", rule("linha dois")))
    }

    @Test
    fun emojiFazParteDaPalavraChave() {
        assertTrue(corresponde("oi 🙂 tudo bem", rule("🙂")))
    }

    // ---------- Resultado com várias regras ----------

    @Test
    fun retornaTodasAsRegrasCorrespondentesNaOrdemOriginal() {
        val regras = listOf(
            rule("oi", id = "1"),
            rule("bom dia", id = "2"),
            rule("oi tudo", MatchType.STARTS_WITH, id = "3"),
            rule("preço", id = "4"),
        )

        val resultado = RuleMatcher.findMatches("oi tudo bem?", regras)

        assertEquals(listOf("1", "3"), resultado.map { it.id })
    }

    @Test
    fun ignoraRegrasDesativadasEmListaMista() {
        val regras = listOf(
            rule("oi", id = "ativa", enabled = true),
            rule("oi", id = "desativada", enabled = false),
        )

        val resultado = RuleMatcher.findMatches("oi", regras)

        assertEquals(listOf("ativa"), resultado.map { it.id })
    }

    @Test
    fun listaVaziaDeRegrasRetornaListaVazia() {
        assertTrue(RuleMatcher.findMatches("oi", emptyList()).isEmpty())
    }

    @Test
    fun semCorrespondenciaRetornaListaVazia() {
        assertTrue(RuleMatcher.findMatches("tudo bem", listOf(rule("preço"))).isEmpty())
    }
}
