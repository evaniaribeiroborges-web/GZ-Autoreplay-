package com.gzautoreplay.app.domain

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppIdentityTest {

    @Test
    fun nomeDoProdutoEhGZAutoReplay() {
        assertEquals("GZ AutoReplay", AppIdentity.NAME)
    }

    @Test
    fun applicationIdDoGradleEhIgualAoDoCodigo() {
        // Mudar o applicationId faz o Android tratar o app como outro app
        // e os dados dos usuários ficariam para trás. Este teste lê o valor
        // REAL de app/build.gradle.kts e falha se ele for diferente do esperado.
        val gradle = localizarBuildGradle()
        val texto = gradle.readText()

        // Procura somente atribuições do tipo: applicationId = "..."
        // (o comentário de aviso no arquivo não tem "=" logo depois da palavra).
        val valores = Regex("""applicationId\s*=\s*"([^"]+)"""")
            .findAll(texto)
            .map { it.groupValues[1] }
            .toList()

        assertEquals(
            "Esperado exatamente um applicationId em ${gradle.path}, encontrado: $valores",
            1,
            valores.size,
        )
        assertEquals(
            "applicationId do Gradle foi alterado. Isso quebra as atualizações dos usuários.",
            AppIdentity.APPLICATION_ID,
            valores.single(),
        )
    }

    @Test
    fun applicationIdEstaFixoComoEsperado() {
        // Valor oficial. Se for mudar de propósito, mude aqui e em app/build.gradle.kts,
        // e registre a decisão em docs/DECISOES.md.
        assertEquals("com.gzautoreplay.app", AppIdentity.APPLICATION_ID)
    }

    private fun localizarBuildGradle(): File {
        // Gradle roda os testes com o diretório de trabalho na pasta do módulo (app/),
        // mas aceitamos também a raiz do projeto para não depender disso.
        val candidatos = listOf(
            File("build.gradle.kts"),
            File("app/build.gradle.kts"),
        )
        // Escolhe o primeiro arquivo que existe E contém applicationId.
        // O build.gradle.kts da raiz não tem applicationId, então é ignorado.
        val encontrado = candidatos.firstOrNull { it.isFile && it.readText().contains("applicationId") }
        assertTrue(
            "Não encontrei app/build.gradle.kts com applicationId. Diretório atual: ${File(".").absolutePath}",
            encontrado != null,
        )
        return encontrado!!
    }
}
