# Tarefa 4 — Gerador de Código (.cmm)

Este projeto estende o exemplo visto em aula com novas construções da linguagem e organização por pastas.

Estrutura do repositório:
- `parser/` — fontes (léxico, gramática e Java: `exemploGC.flex`, `exemploGC.y`, `Parser*.java`, `Yylex.java`, `TabSimb.java`, `TS_entry.java`). As classes Java usam o pacote `parser`.
- `testes/` — programas `.cmm` pequenos que exercitam cada recurso.
- `utils/` — ferramentas e build (`Makefile`, `JFlex.jar`, `yacc.exe`/`yacc.linux`, `run.x`).

## Funcionalidades implementadas

Obrigatórias (9.0 pts):
- Atribuição como expressão: `x = y + 1` agora produz valor (pode ser usado em outras expressões).
- Pré/pós incremento e decremento: `++x`, `x++`, `--x`, `x--`.
- Operador composto: `+=` para inteiros (ex.: `x += 5`).
- Operador condicional: `cond ? expr1 : expr2`.
- Laço `do { ... } while (cond);`.
- Laço `for (init; cond; incr) { ... }` com expressões opcionais (podem ser vazias).
- Comandos `break` e `continue` com suporte a laços aninhados.

Bônus (parcial):
- Arrays de inteiros:
  - Declaração: `int a[10];`
  - Acesso rvalue: `a[i]`
  - Atribuição: `a[i] = expr;`
  - Operador `+=` em elementos: `a[i] += expr;`
- Alocação correta no segmento `.data` pelo tamanho do array.

Structs (incluindo composições):
- Definição de tipo: `struct Nome { int campo1; int campo2; };` e suporte a campos array `int campo[N];`.
- Declaração de variável: `struct Nome var;` e arrays de structs `struct Nome a[N];`.
- Acesso/manipulação de campos:
  - Escalar: `var.campo`, com leitura, escrita e `+=`.
  - Campo array: `var.campo[i]`, com leitura, escrita e `+=`.
  - Array de structs: `a[i].campo`, com leitura, escrita e `+=`.

## Arquivos principais (em `parser/`)
- `exemploGC.flex`: Léxico — tokens estendidos (`INC`, `DEC`, `PLUSEQ`, `DO`, `FOR`, `BREAK`, `CONTINUE`, `STRUCT`, além de `?` e `:`). Ignora comentários `//` e `/* ... */`.
- `exemploGC.y`: Sintático + ações semânticas — geração de código x86 (AT&T) para as novas construções.
- `TabSimb.java`: Geração da área de dados; arrays (tamanho em bytes) e structs (tamanho total e offsets de campos).
- `TS_entry.java` e `ParserVal.java`: Tabela de símbolos e valores semânticos.

## Exemplos de teste
Arquivos `.cmm` adicionados (cada um exercita uma feature):
- `teste_05_incr.cmm` — `++`, `--`, `+=`.
- `teste_06_cond.cmm` — operador `?:`.
- `teste_07_do_while.cmm` — laço `do-while`.
- `teste_08_for.cmm` — laço `for` com expressões vazias + `break/continue`.
- `teste_09_break_continue.cmm` — `break`/`continue` em `while`.
- `teste_10_assign_expr.cmm` — atribuição como expressão.
- `teste_11_arrays.cmm` — declaração e uso de arrays de inteiros.
- `teste_12_structs.cmm` — definição de tipo `struct`, variável, acesso a campos e `+=` em campo.

## Como construir e executar

Pré?requisitos: Java (JRE/JDK). Para montar/rodar binários `.s`, use Linux/WSL com `as`/`ld` (binutils 32?bit).

Use sempre os alvos do `Makefile` em `utils/` (eles já apontam para as pastas corretas):

### Linux/WSL
No diretório `utils/`:
- `make` — Gera `parser/Yylex.java`, `parser/Parser.java` (com `yacc.linux`) e compila as classes do pacote `parser`.
- `make run` — Executa `parser.Parser` no modo console.
- Para produzir binário a partir de um `.cmm`, use o script:
  - `./run.x ../testes/teste_10_assign_expr.cmm`

### Windows (sem WSL)
No diretório `utils/` escolha uma opção:
- `make windows-exe` — Usa `yacc.exe` (byacc/j para Windows) para gerar o `Parser.java` e compilar.
- `make windows BYACCJ_JAR=byaccj-XYZ.jar` — Usa o JAR do byacc/j informado.

Para gerar `.s` a partir de um teste (Windows):
- `powershell`: `(java -cp .. parser.Parser ..\testes\teste_10_assign_expr.cmm) | Out-File -Encoding ASCII ..\parser\teste_10_assign_expr.s`

Para montar e rodar o binário, prefira WSL/Linux (ver seção Linux/WSL acima).

### Windows Quickstart (sem make)
No diretório do repositório, use os scripts PowerShell em `utils/`:

- Build (gera léxico e parser, ajusta pacote e compila):
  ```powershell
  powershell -ExecutionPolicy Bypass -File .\utils\build-windows.ps1
  ```

- Gerar `.s` para todos os testes em `testes/`:
  ```powershell
  powershell -ExecutionPolicy Bypass -File .\utils\gen-tests.ps1
  ```

- Gerar `.s` de um arquivo específico manualmente:
  ```powershell
  (java -cp . parser.Parser .\testes\teste_10_assign_expr.cmm) | Out-File -Encoding ASCII .\parser\teste_10_assign_expr.s
  ```

- Limpar artefatos gerados (classes, `.s`, `y.output`):
  ```powershell
  powershell -ExecutionPolicy Bypass -File .\utils\clean-windows.ps1
  ```

## Notas de implementação
- Atribuição como expressão empilha o valor atribuído.
- `++x/--x` retornam o valor pós-atualização; `x++/x--` retornam o valor anterior.
- `break` salta para o rótulo de fim do laço corrente; `continue` para o rótulo de incremento (em `for`) ou condição (em `while`/`do-while`).
- Arrays usam endereçamento `LEA _id(,%EAX,4)` para obter o endereço de `id[EAX]` e instruções de carga/armazenamento sobre `(%reg)`.
 - Structs: offsets de campos via `TabSimb`; acesso `var.campo` e `var.campo += expr` geram load/store na posição correta.

## Limitações atuais
- Sem verificação de tipos avançada (ex.: bool vs int) além do que já havia no exemplo.
- Não há (ainda) `++/--` diretamente em `a[i]`/`var.campo` (mas `+=` nesses lvalues está implementado).

## Créditos
Baseado no exemplo de geração de código em aula. Extensões por [Seu Nome/Grupo].
