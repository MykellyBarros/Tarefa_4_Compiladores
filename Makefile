# only works with the Java extension of yacc: 
# byacc/j from http://troi.lincom-asg.com/~rjamison/byacc/

JFLEX  = java -jar JFlex.jar 
BYACCJ = ./yacc.linux -tv -J
JAVAC  = javac
BYACCJ_JAR ?= byaccj.jar
PARSER_EXE ?= yacc.exe

# targets:

all: Parser.class

run: Parser.class
	java Parser

build: clean Parser.class

clean:
	rm -f *~ *.class *.o *.s Yylex.java Parser.java y.output

Parser.class: TS_entry.java TabSimb.java Yylex.java Parser.java
	$(JAVAC) Parser.java

Yylex.java: exemploGC.flex
	$(JFLEX) exemploGC.flex

Parser.java: exemploGC.y
	$(BYACCJ) exemploGC.y

# Alternative generator using ByACC/J jar on Windows: set BYACCJ_JAR if filename differs
parser-jar:
	java -jar $(BYACCJ_JAR) -J -tv exemploGC.y > Parser.java

# Convenience target for Windows without WSL: generate lexer, parser via jar, then compile
windows: Yylex.java parser-jar Parser.class

# Alternative generator using Windows byacc/j executable (yacc.exe)
parser-exe:
	$(PARSER_EXE) -J -tv exemploGC.y

windows-exe: Yylex.java parser-exe Parser.class
