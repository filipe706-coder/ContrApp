# NIF na Caixa

App Android que guarda o teu número de contribuinte e o mostra como código de barras,
para o operador da caixa fazer scan em vez de o escrever.

## O que faz
- Valida o NIF (9 dígitos, prefixo e dígito de controlo) antes de guardar.
- Mostra o código de barras (Code 128) e os números em grande.
- Põe o brilho no máximo e não deixa o ecrã apagar enquanto o código está visível.
- Guarda tudo só no telemóvel. Não usa internet nem pede permissões.

## Ficheiros principais
| Ficheiro | Para que serve |
|---|---|
| `Nif.kt` | Valida e formata o NIF |
| `Code128.kt` | Transforma o número na sequência de barras e espaços |
| `BarcodeView.kt` | Desenha essas barras no ecrã |
| `MainActivity.kt` | O ecrã: guardar, mostrar, alterar |
| `res/layout/activity_main.xml` | Aspeto do ecrã |

## Como obter o APK

**Opção A, Android Studio:** abre esta pasta (File → Open), espera que sincronize e carrega em ▶ Run
com o telemóvel ligado por USB, ou Build → Build APK(s).

**Opção B, GitHub:** cria um repositório, faz push desta pasta e vai a *Actions*.
Ao fim de uns minutos aparece o APK em *Artifacts* para descarregares e instalares.
