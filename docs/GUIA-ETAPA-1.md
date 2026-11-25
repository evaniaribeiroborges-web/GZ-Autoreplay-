# GZ AutoReplay — Guia da Etapa 1 (pelo celular)

Faça tudo no navegador do celular, logado no GitHub.

## Parte A — Gerar o APK novo
1. No repositório, toque em **Actions**.
2. Toque em **Build GZ AutoReplay** > **Run workflow** > **Run workflow**.
3. Aguarde. Dois blocos devem ficar verdes: **Testes** e **Gerar APK**.
4. Se algum ficar vermelho, abra o bloco, toque no passo com ❌ e me envie um print.

**Antes de instalar:** no fim da execução, confira o texto da release. Ela deve dizer **"assinado com a chave oficial"**. Se disser **DEBUG**, a chave de assinatura não está configurada e o APK **não atualizará** a versão que você já tem (veja `ASSINATURA.md`).

## Parte B — Instalar por cima da versão atual
1. Toque em **Releases** e abra a mais recente (`GZ AutoReplay - build N`).
2. Baixe o `.apk` e instale. Se o Android pedir permissão para este navegador, toque em **Permitir**.
3. Se aparecer aviso do Play Protect ou da Xiaomi, toque em **Instalar mesmo assim**.
4. Abra o app. No rodapé, deve aparecer **Versão 0.2.0**.

Instalar por cima mantém os dados. Se o Android recusar a instalação, provavelmente a assinatura é diferente. Não desinstale nada antes de me avisar.

## Parte C — Testar a tela de regras
Faça estes testes em ordem e anote o que acontecer:

1. **Tela vazia:** aparece "Nenhuma regra ainda" e o resumo com **Regras 0** e **Ativas 0**.
2. **Criar regra:** toque em **Nova regra**, digite `oi`, uma resposta (ex.: `Olá! Em breve respondo.`), deixe **Contém** e salve. O resumo deve mostrar **Regras 1** e **Ativas 1**.
3. **Validação:** abra **Nova regra** e toque em **Salvar** sem preencher nada. Devem aparecer dois avisos em vermelho e a regra não é criada.
4. **Condições:** crie uma regra **Igual a** e outra **Começa com**. Confira se os textos da lista mostram a condição certa.
5. **Desativar:** toque no interruptor de uma regra. O número de **Ativas** deve diminuir. Ligue de novo e confira.
6. **Excluir:** toque em **Excluir**. Deve aparecer a pergunta de confirmação. Toque em **Cancelar** e depois em **Excluir** para ver a regra sumir.
7. **Permanência:** feche o app completamente (pelos apps recentes) e abra de novo. As regras devem continuar lá.
8. **Girar o celular:** gire a tela com o formulário aberto. Os dados digitados devem permanecer.

## O que este app ainda NÃO faz
- Não lê mensagens do WhatsApp.
- Não envia respostas automáticas.
Isso começa na etapa 3. Por enquanto, a tela só cria e organiza as regras.

## Se algo der errado
Me envie: o número da versão no rodapé, o passo que falhou e um print da tela.
