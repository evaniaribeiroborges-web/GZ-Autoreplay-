package com.gzautoreplay.app.domain.rules

/**
 * Condição usada para comparar a mensagem recebida com a palavra-chave da regra.
 *
 * - [CONTAINS]: a mensagem contém a palavra-chave em qualquer posição.
 * - [EQUALS]: a mensagem é exatamente igual à palavra-chave.
 * - [STARTS_WITH]: a mensagem começa com a palavra-chave.
 */
enum class MatchType {
    CONTAINS,
    EQUALS,
    STARTS_WITH,
}
