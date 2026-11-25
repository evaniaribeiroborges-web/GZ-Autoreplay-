# GZ AutoReplay — Decisões técnicas

Cada decisão tem um motivo e pode ser revisada. Formato: **decisão → motivo**.

## D1. Módulo único `:app`, organizado por pacotes
`domain` (Kotlin puro), `data`, `service`, `ui`. → Simples para iniciante e já separa o que é testável. Se crescer, `domain` vira módulo próprio sem reescrever.

## D2. Sem Hilt por enquanto
Injeção de dependência manual: a fábrica do ViewModel cria o repositório (`RulesViewModel.factory`). → Menos dependências, menos magia e build mais rápido. Reavaliar se o número de classes justificar.

## D3. Dependências mínimas
Etapa 0: core-ktx, activity-compose, Compose (ui + material3) e JUnit.
Etapa 1 acrescentou:
- `androidx.lifecycle:lifecycle-viewmodel-compose` (2.8.7): ViewModel para a tela, para que a lista não se perca ao girar o celular.
- `org.json:json` (20240303), **somente em testes**: o `org.json` do Android é um "stub" em testes unitários no computador, então o codec JSON precisa da implementação real para ser testado.

Cada dependência nova precisa de um motivo registrado aqui.

## D4. Build sem `gradlew` no repositório
O GitHub Actions instala o Gradle 8.9 diretamente (`gradle/actions/setup-gradle`). → O wrapper exige um arquivo binário que não conseguimos gerar pelo celular. Pode ser adicionado depois sem impacto.

## D5. Versões estáveis e conhecidas
AGP 8.7.3, Kotlin 2.0.21, Compose BOM 2024.12.01, compileSdk/targetSdk 35 (Android 15), minSdk 26, Java 17. → Combinação consistente entre si. Atualização de versões será uma tarefa própria, nunca misturada com funcionalidade.

## D6. Preservar dados dos usuários em atualizações
- `applicationId` fixo (`com.gzautoreplay.app`), protegido por teste que lê o valor do `build.gradle.kts`.
- Chave de assinatura única e estável (ver `ASSINATURA.md`).
- `versionCode` sempre crescente (número da execução do GitHub Actions).
- Regras salvas em JSON com campo `version`. Qualquer mudança de formato aumenta a versão e exige migração testada.
- Room (etapa 2): `exportSchema = true`, migrações escritas à mão e testadas; **proibido** `fallbackToDestructiveMigration`.

## D7. `allowBackup=false` por enquanto
O backup automático do Android poderia copiar dados para a nuvem do Google sem controle. Consequência: se o app for desinstalado, as regras somem. Reavaliar na etapa 6 com regras de exclusão explícitas.

## D8. Preparado para painel online e Mercado Pago, sem construir agora
- Repositórios definidos como interfaces em `domain` (`RuleRepository`); hoje com dados locais, amanhã com sincronização.
- Assinatura: o app nunca guarda credenciais do Mercado Pago. Tudo passa por um servidor próprio (etapa 10).
- Distribuição pela Play Store exigirá avaliar a política de cobrança do Google antes da etapa 10.

## D9. Privacidade como regra
Texto das mensagens é processado em memória e descartado. O histórico guarda apenas regra acionada, horário e resultado.

## D10. Sem funcionalidades que o Android/WhatsApp não permitem
Resposta só é possível enquanto a notificação com ação "Responder" existir. Isso será mostrado ao usuário de forma honesta.

## D11. Armazenamento da etapa 1: SharedPreferences + JSON (mudança de plano)
A etapa 2 previa Room. A tela de regras pedida para a etapa 1 precisa de persistência, então usamos:
- **SharedPreferences** (nativo, zero dependências) guardando **um JSON** com as regras.
- Volume pequeno (dezenas de regras), sem consultas complexas.
- Atrás da interface `RuleRepository`: trocar por Room na etapa 2 não muda o motor nem a tela.
- Motivo para Room depois: histórico e estatísticas exigem consultas e volume maiores.

## D12. Dados corrompidos não são apagados em silêncio
Se o JSON salvo não puder ser lido, o texto original é copiado para a chave `rules_v1_corrupted_backup` e o app começa vazio. → O app não quebra e nada desaparece sem rastro.

## D13. Motor de regras é Kotlin puro e sem estado
`RuleMatcher` é um `object` sem Android. → Pode ser testado no computador e no GitHub Actions, sem emulador.

## D14. Regras de comparação de texto
- Espaços nas bordas da mensagem e da palavra-chave são ignorados. Espaços no meio contam.
- A palavra-chave é texto literal, nunca expressão regular.
- Acentos contam: "preço" não corresponde a "preco". Usar comparação sem acento é possível no futuro, mas exige decisão própria.
- Maiúsculas e minúsculas: o usuário escolhe por regra (padrão: ignorar). A comparação segue as regras do Kotlin, que também tratam letras acentuadas (ex.: "ação" = "AÇÃO").
- Palavra-chave vazia ou só com espaços nunca corresponde. Mensagem vazia não corresponde a nenhuma regra.
- Regras desativadas nunca correspondem.
- Se mais de uma regra corresponder, todas são devolvidas, na ordem da lista. Escolher uma só (a primeira, por exemplo) será decidido na etapa 4, junto com a resposta real.

## D15. Limites de tamanho
Palavra-chave: até 100 caracteres. Resposta: até 1000 caracteres. → Evita dados grandes demais no armazenamento e textos que o WhatsApp não exibiria bem. Valores podem ser ajustados com revisão.

## D16. Duplicidade de palavras-chave é permitida
Duas regras com a mesma palavra-chave são aceitas. → Decisão do usuário; a etapa 4 definirá a prioridade ao responder.

## D17. Exclusão pede confirmação; ativar/desativar não
Excluir remove a regra de forma definitiva, então o app pergunta antes. → Proteção contra toque acidental.
