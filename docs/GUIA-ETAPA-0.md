# GZ AutoReplay — Guia da Etapa 0 (pelo celular)

Faça tudo no navegador do celular, logado no GitHub.

## Parte A — Criar a chave (uma vez)
1. No repositório, toque em **Actions**.
2. Toque em **Gerar chave de assinatura (rodar UMA vez)** > **Run workflow** > **Run workflow**.
3. Aguarde ficar verde (~1 min). Abra a execução e, no fim da página, baixe **gz-chave-assinatura-SECRETO** (expira em 1 dia).
4. Extraia o zip (app Arquivos do Xiaomi). **Faça o backup agora** (veja `ASSINATURA.md`).
5. Em **Settings > Secrets and variables > Actions > New repository secret**, crie os 4 Secrets, copiando o texto de cada arquivo (abra o `.txt`, selecione tudo, copie e cole no campo "Secret").

## Parte B — Gerar o APK
1. **Actions** > **Build GZ AutoReplay** > **Run workflow**.
2. Aguarde os dois blocos ficarem verdes: **Testes** e **Gerar APK**.
3. Se algum ficar vermelho, abra, toque no passo com ❌ e me envie print do erro.

## Parte C — Instalar no Redmi Note 13
1. No repositório, toque em **Releases** e abra o mais recente (`GZ AutoReplay - build N`).
2. Toque no arquivo `.apk` para baixar.
3. Ao abrir o APK, o Android pedirá para permitir instalar apps desse navegador: **Permitir**.
4. Se aparecer aviso do Play Protect ou da Xiaomi sobre app desconhecido: **Instalar mesmo assim**.
5. Abra o **GZ AutoReplay**. Você deve ver o ícone (balão com raio), o nome e "Etapa 0 concluída ✔" com a versão no rodapé.

## Como saber que deu certo
- Testes verdes no GitHub.
- APK instala e abre.
- Rodar o Build de novo e instalar por cima: o app atualiza sem pedir para desinstalar (só funciona com a chave oficial cadastrada).
