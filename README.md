# GZ AutoReplay

Aplicativo Android de respostas automáticas para WhatsApp, feito em Kotlin + Jetpack Compose.

**Estado atual: Etapa 0** — app mínimo com identidade visual inicial, build e testes automáticos no GitHub Actions e APK instalável. Ainda não há regras nem automação.

## Como funciona (resumo)
O app lê as notificações do WhatsApp (permissão de acesso a notificações) e responde pelo botão "Responder" da própria notificação. Não acessa nada além disso. Limitações do Android estão em `docs/ARQUITETURA.md`.

## Documentação
- `docs/GUIA-ETAPA-0.md` — passo a passo para compilar e instalar pelo celular
- `docs/ASSINATURA.md` — chave de assinatura, segredos e backup
- `docs/ARQUITETURA.md` — arquitetura, pastas e etapas
- `docs/DECISOES.md` — decisões técnicas e motivos

## Privacidade
O conteúdo das mensagens não será armazenado sem necessidade. Detalhes serão documentados conforme o recurso for construído.
