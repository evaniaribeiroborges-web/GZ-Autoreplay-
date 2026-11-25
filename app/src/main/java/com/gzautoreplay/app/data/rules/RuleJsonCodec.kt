package com.gzautoreplay.app.data.rules

import com.gzautoreplay.app.domain.rules.MatchType
import com.gzautoreplay.app.domain.rules.Rule
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/** Dados salvos não puderam ser lidos (formato inválido, versão desconhecida ou campo faltando). */
class RuleStorageException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Converte regras em texto JSON e de volta.
 *
 * Formato (versão 1):
 * ```
 * {"version":1,"rules":[{"id":"...","keyword":"...","response":"...",
 *   "matchType":"CONTAINS","ignoreCase":true,"enabled":true}]}
 * ```
 * O campo `version` existe para que, em atualizações futuras, o app saiba como ler dados antigos
 * sem perdê-los. Qualquer mudança de formato deve aumentar a versão e ter uma migração testada.
 */
object RuleJsonCodec {

    const val CURRENT_VERSION = 1

    fun encode(rules: List<Rule>): String {
        val array = JSONArray()
        rules.forEach { rule ->
            array.put(
                JSONObject()
                    .put("id", rule.id)
                    .put("keyword", rule.keyword)
                    .put("response", rule.response)
                    .put("matchType", rule.matchType.name)
                    .put("ignoreCase", rule.ignoreCase)
                    .put("enabled", rule.enabled),
            )
        }
        return JSONObject()
            .put("version", CURRENT_VERSION)
            .put("rules", array)
            .toString()
    }

    /**
     * @throws RuleStorageException se o texto não for um formato de regras reconhecido.
     */
    fun decode(text: String): List<Rule> {
        try {
            val root = JSONObject(text)
            val version = root.getInt("version")
            if (version != CURRENT_VERSION) {
                throw RuleStorageException("Versão de dados desconhecida: $version")
            }

            val array = root.getJSONArray("rules")
            return List(array.length()) { index ->
                val item = array.getJSONObject(index)
                Rule(
                    id = item.getString("id"),
                    keyword = item.getString("keyword"),
                    response = item.getString("response"),
                    matchType = MatchType.valueOf(item.getString("matchType")),
                    ignoreCase = item.getBoolean("ignoreCase"),
                    enabled = item.getBoolean("enabled"),
                )
            }
        } catch (e: JSONException) {
            throw RuleStorageException("Dados de regras inválidos.", e)
        } catch (e: IllegalArgumentException) {
            // MatchType.valueOf com nome desconhecido, ou Rule com id vazio.
            throw RuleStorageException("Dados de regras inválidos.", e)
        }
    }
}
