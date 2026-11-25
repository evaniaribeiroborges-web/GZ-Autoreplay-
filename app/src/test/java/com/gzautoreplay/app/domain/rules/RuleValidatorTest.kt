package com.gzautoreplay.app.domain.rules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleValidatorTest {

    private fun draft(keyword: String = "oi", response: String = "olá!") =
        RuleDraft(keyword = keyword, response = response, matchType = MatchType.CONTAINS)

    @Test
    fun rascunhoValidoNaoTemErros() {
        assertTrue(RuleValidator.validate(draft()).isEmpty())
    }

    @Test
    fun palavraChaveVaziaEhErro() {
        assertEquals(listOf(RuleValidationError.EMPTY_KEYWORD), RuleValidator.validate(draft(keyword = "")))
    }

    @Test
    fun palavraChaveSoComEspacosEhErro() {
        assertEquals(
            listOf(RuleValidationError.EMPTY_KEYWORD),
            RuleValidator.validate(draft(keyword = "   \t ")),
        )
    }

    @Test
    fun respostaVaziaEhErro() {
        assertEquals(listOf(RuleValidationError.EMPTY_RESPONSE), RuleValidator.validate(draft(response = "")))
    }

    @Test
    fun respostaSoComEspacosEhErro() {
        assertEquals(listOf(RuleValidationError.EMPTY_RESPONSE), RuleValidator.validate(draft(response = "  \n ")))
    }

    @Test
    fun palavraChaveMuitoLongaEhErro() {
        val longa = "a".repeat(RuleValidator.MAX_KEYWORD_LENGTH + 1)
        assertEquals(listOf(RuleValidationError.KEYWORD_TOO_LONG), RuleValidator.validate(draft(keyword = longa)))
    }

    @Test
    fun palavraChaveNoLimiteExatoEhValida() {
        val noLimite = "a".repeat(RuleValidator.MAX_KEYWORD_LENGTH)
        assertTrue(RuleValidator.validate(draft(keyword = noLimite)).isEmpty())
    }

    @Test
    fun respostaMuitoLongaEhErro() {
        val longa = "a".repeat(RuleValidator.MAX_RESPONSE_LENGTH + 1)
        assertEquals(listOf(RuleValidationError.RESPONSE_TOO_LONG), RuleValidator.validate(draft(response = longa)))
    }

    @Test
    fun respostaNoLimiteExatoEhValida() {
        val noLimite = "a".repeat(RuleValidator.MAX_RESPONSE_LENGTH)
        assertTrue(RuleValidator.validate(draft(response = noLimite)).isEmpty())
    }

    @Test
    fun espacosNasBordasNaoContamParaOLimite() {
        val keyword = " " + "a".repeat(RuleValidator.MAX_KEYWORD_LENGTH) + " "
        assertTrue(RuleValidator.validate(draft(keyword = keyword)).isEmpty())
    }

    @Test
    fun todosOsErrosSaoInformadosDeUmaVez() {
        assertEquals(
            listOf(RuleValidationError.EMPTY_KEYWORD, RuleValidationError.EMPTY_RESPONSE),
            RuleValidator.validate(draft(keyword = "", response = "")),
        )
    }
}
