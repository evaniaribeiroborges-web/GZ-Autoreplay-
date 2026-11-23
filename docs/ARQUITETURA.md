# GZ AutoReplay — Arquitetura

## Camadas (pacote `com.gzautoreplay.app`)
- `domain/` — Kotlin puro: modelos, motor de regras, interfaces de repositório. Sem Android.
- `data/` — Room, DataStore, implementações dos repositórios (a partir da etapa 2).
- `service/` — leitor de notificações e envio da resposta (a partir da etapa 3).
- `ui/` — telas Jetpack Compose e ViewModels.

Regra de dependência: `ui` e `service` e `data` dependem de `domain`; `domain` não depende de ninguém.

## Pastas
```
GZ-Autoreplay/
├─ .github/workflows/   build.yml, gerar-chave.yml
├─ docs/
└─ app/src/
   ├─ main/java/com/gzautoreplay/app/  domain, ui (e depois data, service)
   ├─ main/res/
   └─ test/
```

## Limitações do Android (não vamos contorná-las)
- A resposta usa a ação "Responder" da notificação. Sem notificação ativa, não há resposta.
- Distinguir grupo de conversa individual é heurístico e pode variar entre versões do WhatsApp.
- O acesso a notificações é permissão especial, concedida manualmente. No Android 15, apps instalados fora da Play Store exigem antes "Permitir configurações restritas" nas informações do app.
- Xiaomi/HyperOS pode encerrar o serviço em segundo plano; será tratado na etapa 7.

## Etapas
0. Base, CI e APK instalável **(atual)**
1. Motor de regras + testes
2. Banco de dados e telas de regras
3. Permissão de notificações (onboarding)
4. Resposta automática, chave geral, intervalo
5. Filtros individual/grupo
6. Histórico e estatísticas
7. Robustez (bateria, fabricantes) e privacidade
8. Release
9. Painel online e sincronização
10. Assinaturas Mercado Pago

Cada etapa só começa depois da revisão da anterior.
