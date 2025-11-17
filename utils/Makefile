#!/usr/bin/make -f
# Build config for reorganized layout:
#   utils/   -> tools (this Makefile, JFlex.jar, yacc.exe|yacc.linux)
#   parser/  -> sources (exemploGC.flex, exemploGC.y, *.java)
#   testes/  -> .cmm samples

SRCDIR   := ../parser
TESTDIR  := ../testes

JFLEX    := java -jar ../utils/JFlex.jar
BYACCJ   := ../utils/yacc.linux -tv -J
PARSER_EXE ?= ../utils/yacc.exe
BYACCJ_JAR ?= byaccj.jar
JAVAC    := javac
JAVA     := java

.PHONY: all build clean run \
        lexer parser parser-jar parser-exe \
        windows windows-exe

all: $(SRCDIR)/Parser.class

run: $(SRCDIR)/Parser.class
	cd $(SRCDIR)/.. && $(JAVA) parser.Parser

build: clean $(SRCDIR)/Parser.class

clean:
	cd $(SRCDIR) && rm -f *~ *.class *.o *.s Yylex.java Parser.java y.output

# Compile Java
$(SRCDIR)/Parser.class: $(SRCDIR)/TS_entry.java $(SRCDIR)/TabSimb.java $(SRCDIR)/Yylex.java $(SRCDIR)/Parser.java
	cd $(SRCDIR) && $(JAVAC) Parser.java

# Generate lexer
$(SRCDIR)/Yylex.java: $(SRCDIR)/exemploGC.flex
	cd $(SRCDIR) && $(JFLEX) exemploGC.flex

# Generate parser (Linux/WSL)
$(SRCDIR)/Parser.java: $(SRCDIR)/exemploGC.y
	cd $(SRCDIR) && $(BYACCJ) exemploGC.y

# Alternative generator using ByACC/J jar on Windows: set BYACCJ_JAR if filename differs
parser-jar:
	cd $(SRCDIR) && java -jar ../utils/$(BYACCJ_JAR) -J -tv exemploGC.y > Parser.java

# Convenience target for Windows without WSL: generate lexer, parser via jar, then compile
windows: lexer parser-jar $(SRCDIR)/Parser.class

lexer: $(SRCDIR)/Yylex.java

parser: $(SRCDIR)/Parser.java

# Alternative generator using Windows byacc/j executable (yacc.exe)
parser-exe:
	cd $(SRCDIR) && $(PARSER_EXE) -J -tv exemploGC.y

windows-exe: lexer parser-exe $(SRCDIR)/Parser.class
