package com.gzautoreplay.app.data.rules

import com.gzautoreplay.app.data.storage.FakeKeyValueStore
import com.gzautoreplay.app.domain.rules.MatchType
import com.gzautoreplay.app.domain.rules.Rule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RuleRepositoryImplTest {

    private val regra = Rule(
        id = "1",
        keyword = "oi",
        response = "olá",
        matchType = MatchType.CONTAINS,
        ignoreCase = true,
        enabled = true,
    )

    @Test
    fun semDadosSalvosRetornaListaVazia() {
        val repositorio = RuleRepositoryImpl(FakeKeyValueStore())

        assertEquals(emptyList<Rule>(), repositorio.loadRules())
    }

    @Test
    fun salvarECarregarMantemAsRegras() {
        val store = FakeKeyValueStore()
        val repositorio = RuleRepositoryImpl(store)

        repositorio.saveRules(listOf(regra))

        assertEquals(listOf(regra), RuleRepositoryImpl(store).loadRules())
    }

    @Test
    fun dadosCorrompidosSaoCopiadosParaBackupESemApagarTudo() {
        val store = FakeKeyValueStore()
        val textoQuebrado = "{quebrado"
        store.putString(RuleRepositoryImpl.STORAGE_KEY, textoQuebrado)

        val carregadas = RuleRepositoryImpl(store).loadRules()

        assertEquals(emptyList<Rule>(), carregadas)
        assertEquals(textoQuebrado, store.getString(RuleRepositoryImpl.BACKUP_KEY))
        assertNull(store.getString(RuleRepositoryImpl.STORAGE_KEY))
    }

    @Test
    fun depoisDeDadosCorrompidosOAppVoltaAFuncionarNormalmente() {
        val store = FakeKeyValueStore()
        store.putString(RuleRepositoryImpl.STORAGE_KEY, "{quebrado")
        val repositorio = RuleRepositoryImpl(store)

        repositorio.loadRules()
        repositorio.saveRules(listOf(regra))

        assertEquals(listOf(regra), repositorio.loadRules())
    }

    @Test
    fun saveRulesSubstituiTodasAsRegrasAnteriores() {
        val store = FakeKeyValueStore()
        val repositorio = RuleRepositoryImpl(store)
        repositorio.saveRules(listOf(regra, regra.copy(id = "2")))

        repositorio.saveRules(listOf(regra.copy(id = "3")))

        assertEquals(listOf("3"), repositorio.loadRules().map { it.id })
    }
}
