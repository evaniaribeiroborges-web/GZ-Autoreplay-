package com.gzautoreplay.app.domain.rules

/**
 * Uma regra de resposta automática.
 *
 * @property id identificador único da regra. Gerado na criação e nunca alterado.
 * @property keyword palavra-chave que a mensagem recebida deve atender pela [matchType].
 * @property response texto da resposta automática.
 * @property matchType condição de correspondência.
 * @property ignoreCase se `true`, maiúsculas e minúsculas são tratadas como iguais.
 * @property enabled regras desativadas nunca correspondem a nenhuma mensagem.
 */
data class Rule(
    val id: String,
    val keyword: String,
    val response: String,
    val matchType: MatchType,
    val ignoreCase: Boolean,
    val enabled: Boolean,
) {
    init {
        require(id.isNotBlank()) { "O id da regra não pode ser vazio." }
    }
}
