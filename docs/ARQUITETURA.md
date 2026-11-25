# GZ AutoReplay — Arquitetura

## Camadas (pacote `com.gzautoreplay.app`)
- `domain/` — Kotlin puro: modelos, motor de regras, validação, edição e interfaces de repositório. Sem Android.
- `data/` — armazenamento e serialização. Hoje: SharedPreferences + JSON. A partir da etapa 2: Room.
- `service/` — leitor de notificações e envio da resposta (a partir da etapa 3).
- `ui/` — telas Jetpack Compose e ViewModels.

Regra de dependência: `ui`, `service` e `data` dependem de `domain`; `domain` não depende de ninguém.

## Módulo de regras (etapa 1)
```
domain/rules/
├─ MatchType.kt        CONTAINS, EQUALS, STARTS_WITH
├─ Rule.kt             modelo da regra (id, palavra-chave, resposta, condição, ignoreCase, enabled)
├─ RuleDraft.kt        dados digitados pelo usuário, antes de virar regra
├─ RuleValidator.kt    erros de validação (vazio, muito longo)
├─ RuleMatcher.kt      motor: quais regras correspondem a uma mensagem
├─ RuleEditor.kt       criar, ativar/desativar e excluir (sempre devolve nova lista)
├─ RuleSummary.kt      contagem total e ativas
└─ RuleRepository.kt   contrato de armazenamento

data/storage/
├─ KeyValueStore.kt                  interface chave-valor (testável)
└─ SharedPreferencesKeyValueStore.kt implementação Android

data/rules/
├─ RuleJsonCodec.kt        regras <-> JSON com campo "version"
└─ RuleRepositoryImpl.kt   salva/carrega; faz backup de dados corrompidos

ui/rules/
├─ RulesScreen.kt       lista, resumo, ativar/desativar, excluir
├─ AddRuleDialog.kt     formulário de nova regra
└─ RulesViewModel.kt    coordena tela e armazenamento
```

Fluxo: a tela chama o ViewModel → o `RuleEditor` (domínio) calcula a nova lista → o `RuleRepositoryImpl` salva → a tela atualiza.

## Pastas
```
GZ-Autoreplay/
├─ .github/workflows/   build.yml, gerar-chave.yml
├─ docs/                ARQUITETURA, DECISOES, PROGRESSO, ASSINATURA, GUIAS
└─ app/src/
   ├─ main/java/com/gzautoreplay/app/  domain, data, ui (service na etapa 3)
   ├─ main/res/
   └─ test/            testes unitários de domain, data e AppIdentity
```

## Limitações do Android (não vamos contorná-las)
- A resposta usa a ação "Responder" da notificação. Sem notificação ativa, não há resposta.
- Distinguir grupo de conversa individual é heurístico e pode variar entre versões do WhatsApp.
- O acesso a notificações é permissão especial, concedida manualmente. No Android 15, apps instalados fora da Play Store exigem antes "Permitir configurações restritas" nas informações do app.
- Xiaomi/HyperOS pode encerrar o serviço em segundo plano; será tratado na etapa 7.

## Etapas
0. Base, CI e APK instalável **(concluída)**
1. Motor de regras + testes + tela de regras **(atual, aguardando validação)**
2. Banco Room e migração dos dados da etapa 1
3. Permissão de notificações (onboarding)
4. Resposta automática, chave geral, intervalo
5. Filtros individual/grupo
6. Histórico e estatísticas
7. Robustez (bateria, fabricantes) e privacidade
8. Release
9. Painel online e sincronização
10. Assinaturas Mercado Pago

Cada etapa só começa depois da revisão da anterior.
