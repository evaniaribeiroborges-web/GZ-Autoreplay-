# GZ AutoReplay — Assinatura do APK, segredos e backup

## Por que isso importa
O Android só atualiza um app "por cima" (mantendo os dados) se o novo APK for assinado com **a mesma chave** do instalado. Perdeu a chave = os usuários precisam desinstalar e perdem os dados.

## Como funciona aqui
- A chave é criada uma única vez pelo workflow **Gerar chave de assinatura**, dentro do GitHub.
- Ela fica guardada em 4 **Secrets** do repositório (criptografados pelo GitHub, nunca no código).
- Todo build usa essa chave. Sem os Secrets, o build gera um APK **debug** de teste (aviso amarelo na execução).

## Os 4 Secrets
| Nome | Conteúdo |
|---|---|
| `GZ_KEYSTORE_BASE64` | arquivo `1-GZ_KEYSTORE_BASE64.txt` |
| `GZ_KEY_ALIAS` | arquivo `2-GZ_KEY_ALIAS.txt` (`gzautoreplay`) |
| `GZ_KEYSTORE_PASSWORD` | arquivo `3-GZ_KEYSTORE_PASSWORD.txt` |
| `GZ_KEY_PASSWORD` | arquivo `4-GZ_KEY_PASSWORD.txt` |

## Backup (obrigatório)
Guarde `gz-release.jks` **e** a senha em pelo menos **dois lugares separados, fora do GitHub**. Exemplos: Google Drive pessoal e outra conta/dispositivo. Os Secrets do GitHub não podem ser lidos de volta, então eles **não são um backup**.

## Regras
- Nunca colocar a chave ou as senhas no repositório (o `.gitignore` bloqueia `*.jks`).
- Nunca rodar "Gerar chave" de novo depois de cadastrar os Secrets: criaria outra chave.
- Na futura publicação na Play Store, será usado o Play App Signing; a chave atual vira a "chave de upload".
