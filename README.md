# GZ AutoReplay

Aplicativo Android de respostas automáticas para WhatsApp, feito em Kotlin + Jetpack Compose.

**Estado atual: Etapa 1 (motor de regras e tela de regras), aguardando validação.** O app permite criar, ativar, desativar e excluir regras, salvas no aparelho. **Ainda não lê notificações nem responde no WhatsApp.** Isso começa na etapa 3.

## Como vai funcionar (resumo)
O app lê as notificações do WhatsApp (permissão de acesso a notificações) e responde pelo botão "Responder" da própria notificação. Não acessa nada além disso. Limitações do Android estão em `docs/ARQUITETURA.md`.

## Documentação
- `docs/PROGRESSO.md` — o que já foi feito, o que foi testado e o que falta
- `docs/GUIA-ETAPA-1.md` — passo a passo para testar a etapa 1 pelo celular
- `docs/GUIA-ETAPA-0.md` — passo a passo da instalação inicial
- `docs/ASSINATURA.md` — chave de assinatura, segredos e backup
- `docs/ARQUITETURA.md` — arquitetura, pastas e etapas
- `docs/DECISOES.md` — decisões técnicas e motivos

## Privacidade
Nesta etapa, as regras (palavra-chave e resposta) ficam só no aparelho, em área privada do app. Nenhuma mensagem recebida é lida ou guardada. Detalhes serão documentados conforme cada recurso for construído.
