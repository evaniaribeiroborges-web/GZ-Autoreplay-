package com.gzautoreplay.app.data.rules

import com.gzautoreplay.app.domain.rules.MatchType
import com.gzautoreplay.app.domain.rules.Rule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleJsonCodecTest {

    @Test
    fun roundTrip_preservaTodosOsCamposComCaracteresEspeciais() {
        val regras = listOf(
            Rule(
                id = "abc-123",
                keyword = "preço \"especial\"",
                response = "Linha 1\nLinha 2 \\ barra 🙂 ação",
                matchType = MatchType.STARTS_WITH,
                ignoreCase = false,
                enabled = false,
            ),
            Rule(
                id = "def-456",
                keyword = "oi",
                response = "olá",
                matchType = MatchType.EQUALS,
                ignoreCase = true,
                enabled = true,
            ),
        )

        val texto = RuleJsonCodec.encode(regras)

        assertEquals(regras, RuleJsonCodec.decode(texto))
    }

    @Test
    fun roundTrip_listaVazia() {
        assertEquals(emptyList<Rule>(), RuleJsonCodec.decode(RuleJsonCodec.encode(emptyList())))
    }

    @Test
    fun encode_incluiVersaoAtual() {
        val texto = RuleJsonCodec.encode(emptyList())

        assertTrue(texto.contains("\"version\":${RuleJsonCodec.CURRENT_VERSION}"))
    }

    @Test
    fun decode_textoQueNaoEJsonLancaRuleStorageException() {
        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode("isto não é json")
        }
    }

    @Test
    fun decode_semCampoVersaoLancaRuleStorageException() {
        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode("""{"rules":[]}""")
        }
    }

    @Test
    fun decode_versaoDesconhecidaLancaRuleStorageException() {
        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode("""{"version":999,"rules":[]}""")
        }
    }

    @Test
    fun decode_campoFaltandoLancaRuleStorageException() {
        val semKeyword = """
            {"version":1,"rules":[{"id":"1","response":"r","matchType":"CONTAINS","ignoreCase":true,"enabled":true}]}
        """.trimIndent()

        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode(semKeyword)
        }
    }

    @Test
    fun decode_tipoDeCondicaoDesconhecidoLancaRuleStorageException() {
        val tipoInvalido = """
            {"version":1,"rules":[{"id":"1","keyword":"k","response":"r","matchType":"REGEX","ignoreCase":true,"enabled":true}]}
        """.trimIndent()

        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode(tipoInvalido)
        }
    }

    @Test
    fun decode_idVazioLancaRuleStorageException() {
        val idVazio = """
            {"version":1,"rules":[{"id":"","keyword":"k","response":"r","matchType":"CONTAINS","ignoreCase":true,"enabled":true}]}
        """.trimIndent()

        assertThrows(RuleStorageException::class.java) {
            RuleJsonCodec.decode(idVazio)
        }
    }
}
