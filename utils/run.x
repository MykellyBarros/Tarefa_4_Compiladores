#!/bin/bash

set -euo pipefail

# Resolve project directories relative to this script (utils/)
UTILS_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJ_DIR="$(cd "$UTILS_DIR/.." && pwd)"
PARSER_DIR="$PROJ_DIR/parser"

if [ $# -lt 1 ]; then
	echo "Uso: $0 caminho/para/arquivo.cmm" >&2
	exit 1
fi

SRC_PATH="$1"
ARQ="$(basename "$SRC_PATH" | sed 's/\.cmm$//')"

# Gera assembly .s (executa a classe empacotada parser.Parser a partir da raiz)
cd "$PROJ_DIR"
java parser.Parser "$SRC_PATH" >"$PARSER_DIR/$ARQ.s"

# Monta e liga (x86 32-bits) dentro de parser/
cd "$PARSER_DIR"
as --32 -o "$ARQ.o" "$ARQ.s"
ld -m elf_i386 -s -o "$ARQ" "$ARQ.o"
