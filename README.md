# Tarefa 4 — Gerador de Código (.cmm)

Este projeto estende o exemplo de geração de código visto em aula para incluir novas construções da linguagem e bônus.

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

Extras (2.0 pts):
- Structs (básico):
  - Definição de tipo: `struct Nome { int campo1; int campo2; };`
  - Declaração de variável: `struct Nome var;`
  - Acesso a campos: `var.campo`, com leitura, escrita e `+=` suportados.

Pendente (pode ser implementado depois):
- Arrays de structs e structs com array como campo (bônus adicional).

## Arquivos principais
- `exemploGC.flex`: Léxico — tokens estendidos (`INC`, `DEC`, `PLUSEQ`, `DO`, `FOR`, `BREAK`, `CONTINUE`, `STRUCT`, além de `?` e `:`).
- `exemploGC.y`: Sintático + ações semânticas — geração de código x86 (AT&T) para as novas construções.
- `TabSimb.java`: Geração da área de dados, com alocação para arrays e structs; registro de tipos `struct` e offsets de campos.
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

Este projeto usa:
- JFlex (Java) para gerar `Yylex.java`.
- ByACC/J para gerar `Parser.java` a partir de `exemploGC.y`.
- `as`/`ld` (binutils) para montar e ligar o assembly x86 de 32 bits gerado.

Como a ferramenta `yacc.linux` fornecida é um binário Linux, no Windows recomenda-se usar WSL (Ubuntu) ou um ambiente Linux.

### Passos em Linux ou WSL
1. Garanta que tem `java`, `make`, `as`, `ld` e permissões de execução:
   - `sudo apt update && sudo apt install default-jre make binutils` (em WSL/Ubuntu)
2. No diretório do projeto, gere o léxico e o parser e compile:
   - `make` (gera `Yylex.java`, `Parser.java` e compila `Parser.class`).
3. Execute um teste e gere assembly:
   - `java Parser teste_05_incr.cmm > teste_05_incr.s`
   - Para montar/ligar (x86 32 bits):
     - `as --32 -o teste_05_incr.o teste_05_incr.s`
     - `ld -m elf_i386 -s -o teste_05_incr teste_05_incr.o`
     - `./teste_05_incr`

Como atalho, o script `run.x` (bash) automatiza a compilação para `.s` e binário 32 bits (requer Linux/WSL).

### Passos no Windows puro (sem WSL)
- Já que `yacc.linux` não roda no Windows, use uma das opções:
  1. Instale o ByACC/J nativo para Windows ou pegue o `byaccj.jar` e rode:
     - `java -jar byaccj.jar -J -tv exemploGC.y > Parser.java`
  2. Use WSL (recomendado) e siga os passos acima.
- O `Makefile` pressupõe Linux/WSL. Em Windows puro, execute manualmente:
  - `java -jar JFlex.jar exemploGC.flex` (gera `Yylex.java`)
  - Gere `Parser.java` com ByACC/J
  - `javac Parser.java`

## Notas de implementação
- Atribuição como expressão empilha o valor atribuído.
- `++x/--x` retornam o valor pós-atualização; `x++/x--` retornam o valor anterior.
- `break` salta para o rótulo de fim do laço corrente; `continue` para o rótulo de incremento (em `for`) ou condição (em `while`/`do-while`).
- Arrays usam endereçamento `LEA _id(,%EAX,4)` para obter o endereço de `id[EAX]` e instruções de carga/armazenamento sobre `(%reg)`.

## Limitações atuais
- Sem verificação de tipos avançada (ex.: bool vs int) além do que já havia no exemplo.
- `struct` ainda não suportado; pode ser adicionado com novas produções e offsets na tabela de símbolos.

## Créditos
Baseado no exemplo de geração de código em aula. Extensões por [Seu Nome/Grupo].
