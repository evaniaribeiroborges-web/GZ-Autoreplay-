package com.gzautoreplay.app.ui.rules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.gzautoreplay.app.R
import com.gzautoreplay.app.domain.rules.MatchType
import com.gzautoreplay.app.domain.rules.RuleDraft
import com.gzautoreplay.app.domain.rules.RuleValidationError

/**
 * Diálogo para criar uma regra. [onSave] devolve os erros de validação;
 * lista vazia significa que a regra foi salva e o diálogo pode fechar.
 */
@Composable
fun AddRuleDialog(
    onDismiss: () -> Unit,
    onSave: (RuleDraft) -> List<RuleValidationError>,
) {
    var keyword by rememberSaveable { mutableStateOf("") }
    var response by rememberSaveable { mutableStateOf("") }
    var matchType by remember { mutableStateOf(MatchType.CONTAINS) }
    var ignoreCase by rememberSaveable { mutableStateOf(true) }
    var errors by remember { mutableStateOf<List<RuleValidationError>>(emptyList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.rules_add_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = keyword,
                    onValueChange = { keyword = it },
                    label = { Text(stringResource(R.string.rules_field_keyword)) },
                    singleLine = true,
                    isError = errors.any { it == RuleValidationError.EMPTY_KEYWORD || it == RuleValidationError.KEYWORD_TOO_LONG },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = response,
                    onValueChange = { response = it },
                    label = { Text(stringResource(R.string.rules_field_response)) },
                    minLines = 3,
                    maxLines = 6,
                    isError = errors.any { it == RuleValidationError.EMPTY_RESPONSE || it == RuleValidationError.RESPONSE_TOO_LONG },
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = stringResource(R.string.rules_field_condition),
                    style = MaterialTheme.typography.titleSmall,
                )
                MatchTypeOption(
                    label = stringResource(R.string.match_contains),
                    selected = matchType == MatchType.CONTAINS,
                    onSelect = { matchType = MatchType.CONTAINS },
                )
                MatchTypeOption(
                    label = stringResource(R.string.match_equals),
                    selected = matchType == MatchType.EQUALS,
                    onSelect = { matchType = MatchType.EQUALS },
                )
                MatchTypeOption(
                    label = stringResource(R.string.match_starts_with),
                    selected = matchType == MatchType.STARTS_WITH,
                    onSelect = { matchType = MatchType.STARTS_WITH },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = ignoreCase,
                            role = Role.Switch,
                            onValueChange = { ignoreCase = it },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.rules_ignore_case),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = stringResource(R.string.rules_ignore_case_hint),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = ignoreCase, onCheckedChange = null)
                }

                errors.forEach { error ->
                    Text(
                        text = errorText(error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    errors = onSave(
                        RuleDraft(
                            keyword = keyword,
                            response = response,
                            matchType = matchType,
                            ignoreCase = ignoreCase,
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun MatchTypeOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun errorText(error: RuleValidationError): String = when (error) {
    RuleValidationError.EMPTY_KEYWORD -> stringResource(R.string.error_empty_keyword)
    RuleValidationError.KEYWORD_TOO_LONG -> stringResource(R.string.error_keyword_too_long)
    RuleValidationError.EMPTY_RESPONSE -> stringResource(R.string.error_empty_response)
    RuleValidationError.RESPONSE_TOO_LONG -> stringResource(R.string.error_response_too_long)
}
