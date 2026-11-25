# GZ AutoReplay — Progresso

Este arquivo registra apenas o que foi **realmente feito** e **realmente executado**. Quando algo não foi executado, está marcado como tal.

## Versão atual: 0.2.0 (etapa 1)

### Etapa 0 — Base, CI e APK instalável
- **Status:** concluída. Validada pelo responsável: compilou pelo GitHub Actions, foi instalada e aberta no Redmi Note 13, e os 3 testes passaram (relatado pelo responsável).

### Etapa 1 — Motor de regras e tela de regras
- **Status:** código escrito e revisado por leitura. **Ainda não compilado nem testado pelo GitHub Actions.** Aguarda a primeira execução.

**Implementado**
- Modelo `Rule`: id único, palavra-chave, resposta, condição, "ignorar maiúsculas" e ativa/inativa.
- Condições: `CONTAINS` (contém), `EQUALS` (igual a), `STARTS_WITH` (começa com).
- `RuleMatcher`: devolve as regras que correspondem a uma mensagem. Regras desativadas e palavras-chave vazias nunca correspondem.
- `RuleValidator`: palavra-chave e resposta obrigatórias, com limites de 100 e 1000 caracteres.
- `RuleEditor`: criar, ativar/desativar e excluir, sempre devolvendo nova lista.
- Armazenamento local: SharedPreferences com JSON versionado. Dados corrompidos vão para backup.
- Tela: resumo (total e ativas), lista de regras, botão "Nova regra", interruptor para ativar/desativar, exclusão com confirmação.
- Versão 0.2.0; `versionCode` automático pela execução do GitHub Actions.

**Testes escritos (não executados até o momento)**
| Arquivo | Cobertura |
|---|---|
| `RuleMatcherTest` | 3 condições, caixa alta/baixa, espaços, acentos, emojis, quebra de linha, regex literal, regras desativadas, palavra vazia, mensagem vazia, ordem dos resultados |
| `RuleValidatorTest` | vazio, só espaços, limites exatos e um acima, acumulação de erros |
| `RuleEditorTest` | criar, copiar campos, limpar espaços, ordem, rascunho inválido, id repetido, desativar/ativar, excluir, imutabilidade; `RuleSummaryTest` |
| `RuleJsonCodecTest` | ida e volta com caracteres especiais, versão, JSON inválido, versão desconhecida, campo faltando, condição inválida, id vazio |
| `RuleRepositoryImplTest` | salvar/carregar, dados corrompidos com backup, recuperação depois de corrupção, substituição |
| `AppIdentityTest` | nome do produto, `applicationId` lido do `build.gradle.kts` e valor oficial |

**Total de testes do projeto:** 75 (3 da etapa 0 + 72 novos da etapa 1). Contagem feita por leitura dos arquivos (`@Test`); o número oficial é o que o GitHub Actions informar ao executar.

**Limitações conhecidas**
- Nenhum teste de interface (Compose) nem do ViewModel. A lógica está no domínio, testada; a tela precisa de teste manual no celular.
- O app ainda não lê notificações nem responde no WhatsApp.
- Dados ficam só no aparelho. Desinstalar o app apaga as regras (D7).
- Comparação sem acento não existe ainda (D14).
- Vários tipos de regra podem casar com a mesma mensagem; a escolha da resposta será feita na etapa 4 (D14).

**Próximo passo:** validação do responsável (build no GitHub Actions + teste no celular, com `docs/GUIA-ETAPA-1.md`). Só depois disso começa a etapa 2.

## Histórico de versões
| Versão | Etapa | Observação |
|---|---|---|
| 0.1.0 | 0 | Base e APK instalável |
| 0.2.0 | 1 | Motor de regras e tela de regras (não validado ainda) |
