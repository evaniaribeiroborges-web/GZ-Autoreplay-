# GZ AutoReplay — Decisões técnicas

Cada decisão tem um motivo e pode ser revisada. Formato: **decisão → motivo**.

## D1. Módulo único `:app`, organizado por pacotes
`domain` (Kotlin puro), `data`, `service`, `ui`. → Simples para iniciante e já separa o que é testável. Se crescer, `domain` vira módulo próprio sem reescrever.

## D2. Sem Hilt por enquanto
Injeção de dependência manual (um `AppContainer` simples) quando surgir a necessidade, na etapa 2. → Menos dependências, menos magia e build mais rápido. Reavaliar se o número de classes justificar.

## D3. Dependências mínimas na etapa 0
Somente core-ktx, activity-compose, Compose (ui + material3) e JUnit. Cada dependência nova precisa de um motivo registrado aqui.

## D4. Build sem `gradlew` no repositório
O GitHub Actions instala o Gradle 8.9 diretamente (`gradle/actions/setup-gradle`). → O wrapper exige um arquivo binário que não conseguimos gerar pelo celular. Pode ser adicionado depois sem impacto.

## D5. Versões estáveis e conhecidas
AGP 8.7.3, Kotlin 2.0.21, Compose BOM 2024.12.01, compileSdk/targetSdk 35 (Android 15), minSdk 26, Java 17. → Combinação consistente entre si. Atualização de versões será uma tarefa própria, nunca misturada com funcionalidade.

## D6. Preservar dados dos usuários em atualizações
- `applicationId` fixo (`com.gzautoreplay.app`), protegido por teste.
- Chave de assinatura única e estável (ver `ASSINATURA.md`).
- `versionCode` sempre crescente (número da execução do GitHub Actions).
- Banco Room (etapa 2): `exportSchema = true`, migrações escritas à mão e testadas; **proibido** `fallbackToDestructiveMigration`.

## D7. `allowBackup=false` por enquanto
O backup automático do Android poderia copiar histórico para a nuvem do Google sem controle. Reavaliar na etapa 6 com regras de exclusão explícitas.

## D8. Preparado para painel online e Mercado Pago, sem construir agora
- Repositórios definidos como interfaces em `domain`; hoje com dados locais, amanhã com sincronização.
- Assinatura: o app nunca guarda credenciais do Mercado Pago. Tudo passa por um servidor próprio (etapa 10).
- Distribuição pela Play Store exigirá avaliar a política de cobrança do Google antes da etapa 10.

## D9. Privacidade como regra
Texto das mensagens é processado em memória e descartado. O histórico guarda apenas regra acionada, horário e resultado.

## D10. Sem funcionalidades que o Android/WhatsApp não permitem
Resposta só é possível enquanto a notificação com ação "Responder" existir. Isso será mostrado ao usuário de forma honesta.
