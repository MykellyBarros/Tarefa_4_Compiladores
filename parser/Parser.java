//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 "exemploGC.y"
	package parser;
	/*
	 Resumo: Gramática e geração de código (byacc/j) para .cmm.
	 Observações:
	 - Declara tokens, precedências e produções; ações semânticas emitem assembly x86/AT&T.
	 - Suporta atribuição como expressão, ++/--, +=, ?:, do-while, for, break/continue,
	   arrays de ints, tipos/variáveis struct, arrays de structs e campos de struct que são arrays.
	 - Usa `TabSimb` para gerenciamento de símbolos e pilhas de rótulos para controle de fluxo.
	*/
	import java.io.*;
	import java.util.ArrayList;
	import java.util.Stack;
//#line 30 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short INT=258;
public final static short FLOAT=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short LIT=262;
public final static short VOID=263;
public final static short MAIN=264;
public final static short READ=265;
public final static short WRITE=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short WHILE=269;
public final static short TRUE=270;
public final static short FALSE=271;
public final static short DO=272;
public final static short FOR=273;
public final static short BREAK=274;
public final static short CONTINUE=275;
public final static short EQ=276;
public final static short LEQ=277;
public final static short GEQ=278;
public final static short NEQ=279;
public final static short AND=280;
public final static short OR=281;
public final static short INC=282;
public final static short DEC=283;
public final static short PLUSEQ=284;
public final static short STRUCT=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    4,    0,    6,    8,    5,    3,    3,    3,    9,    9,
    9,    9,   12,   10,   11,   11,   11,    1,    1,    1,
    7,    7,   13,   13,   13,   15,   13,   13,   16,   17,
   13,   18,   13,   22,   23,   13,   13,   13,   24,   13,
   26,   25,   25,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   27,   28,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,    2,   19,   19,   20,   20,   21,   21,
};
final static short yylen[] = {                            2,
    0,    3,    0,    0,    9,    2,    2,    0,    3,    6,
    4,    7,    0,    7,    4,    7,    0,    1,    1,    1,
    2,    0,    2,    3,    5,    0,    8,    5,    0,    0,
    7,    0,    8,    0,    0,   11,    2,    2,    0,    7,
    0,    3,    0,    1,    1,    1,    1,    3,    2,    3,
    3,    2,    2,    2,    2,    3,    5,    5,    4,    6,
    6,    6,    8,    8,    6,    8,    8,    0,    0,    7,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    1,    1,    0,    1,    0,    1,    0,
};
final static short yydefred[] = {                         1,
    0,    0,   18,   19,   20,    0,    0,    0,    0,    0,
    0,    0,    0,    2,    6,    7,    0,   13,    9,    0,
    0,   11,    0,   17,    0,    0,    0,    0,    0,    3,
    0,    0,    0,   10,    0,   12,    0,   14,   22,   15,
    0,    0,    0,    0,   44,    0,    0,    0,   29,   45,
   46,   32,    0,    0,    0,    0,    0,    0,    0,   22,
    0,    0,   21,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   37,   38,   84,   52,   53,   49,    0,
    0,   54,   55,    0,    0,    5,    0,    0,    0,    0,
    0,    0,   68,    0,    0,    0,    0,    0,    0,    0,
   23,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   48,   24,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,   74,   75,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   28,   25,    0,
    0,   30,    0,    0,    0,   69,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   41,   40,   31,    0,    0,    0,    0,    0,    0,
    0,    0,   27,    0,   33,   34,   42,    0,   35,   36,
};
final static short yydgoto[] = {                          1,
    7,   61,    8,    2,   14,   35,   42,   62,    9,   10,
   28,   24,   63,   64,  136,   71,  163,   72,  111,  155,
  177,  188,  190,  137,  173,  184,  122,  166,
};
final static short yysindex[] = {                         0,
    0, -209,    0,    0,    0, -255, -244, -247, -209, -209,
 -118,  -56, -237,    0,    0,    0,  -50,    0,    0, -241,
   -7,    0, -232,    0,  -51,   15,  -47, -124,  -15,    0,
    2, -198,    4,    0,  -58,    0,  -48,    0,    0,    0,
 -192,    5,  -22,  -31,    0,   40,   44,   46,    0,    0,
    0,    0,   48,   32,   34, -156, -156,   24,   24,    0,
  -53,  -23,    0,  135,   57,   24, -154, -139, -142,   24,
   98,    5,   24,    0,    0,    0,    0,    0,    0,  163,
  -33,    0,    0,   24,   24,    0,   24,   24,   24,   24,
   24,   24,    0,   24,   24,   24,   24,   24,   24,   24,
    0,    0,  288,  -57,   89,  102,  457,   24, -114,  457,
   86,    0,    0,  457,  457,   70,   70,   70,   70,  312,
   25,   24,   70,   70,   77,   77,    0,    0,    0,  -36,
   24,   24,   24,   92,  101,  130,  129,  385,  136,   24,
  409,   24,   24,  -82,  457,  457,  416,    0,    0,   24,
    5,    0,   24,  457,  120,    0,  457,  457,  -49,  -40,
  423,  -87,    5,  450,   24,   24,   24,   24,   24,   24,
  131,    0,    0,    0,  134,  457,  155,  457,  457,  457,
  457,  457,    0,    5,    0,    0,    0,    5,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -62,    0,    0,    0,    0,    0,    0,  -62,  -62,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   78,    0,   36,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  143,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   63,    0,  165,  166,    0,    0,  152,
    0,    0,    0,  -19,  -11,  324,  332,  492,  500,  -35,
  -27,    0,  536,  556,  480,  486,    0,    0,    0,   90,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  153,
    0,    0,    0,    0,   -4,  106,    0,    0,    0,    0,
    0,    0,    0,  154,    0,    0,  241,  252,   99,  126,
    0,  -14,    0,    0,  173,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  174,    0,  268,  305,  477,
  565,  576,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  -39,   43,    0,    0,    0,  156,    0,    0,    0,
    0,    0,    3,  772,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=942;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   33,   11,   19,  132,   18,   83,   59,   85,   22,  144,
   40,  168,   12,   82,   67,   13,   77,   78,   43,   25,
  170,   51,   83,   83,  143,   43,   21,   83,   27,   50,
   82,   82,   26,  133,   20,   82,   58,   58,   51,   51,
   23,   29,   41,   34,   59,   31,   50,   50,    3,    4,
    5,   15,   16,   58,   58,   30,   58,   83,   37,   66,
   36,  100,   38,   59,   39,   82,   98,   96,   43,   97,
   65,   99,   47,   51,  109,    6,   47,   47,   47,   68,
   47,   50,   47,   69,   95,   70,   94,   73,   58,   60,
   74,  113,   75,   47,   47,   47,   84,   47,   47,   56,
   76,   86,  104,   56,   56,   56,  100,   56,   43,   56,
   43,   98,   96,  100,   97,  102,   99,  105,   98,  106,
   56,   56,   56,   99,   56,   56,   59,   60,   47,  134,
   59,   59,   59,   32,   59,   62,   59,  108,   17,   62,
   62,   62,  135,   62,  140,   62,   57,   59,   59,   59,
  148,   59,   59,  162,  139,   56,   62,   62,   62,  149,
   62,   62,   65,   57,   57,  174,   65,   65,   65,  151,
   65,  100,   65,  150,  159,  153,   98,   96,  165,   97,
  172,   99,   59,   65,   65,   65,  187,   65,   65,  183,
  189,   62,  185,  101,   95,  186,   94,   93,   57,  100,
    8,   86,    4,  112,   98,   96,   39,   97,   26,   99,
   85,   88,   87,   90,   89,   81,    0,    0,   65,    0,
    0,    0,   95,   44,   94,   93,  131,   45,   82,   83,
   84,   46,   47,   48,  167,   49,   50,   51,   52,   53,
   54,   55,   43,  169,   83,   83,   43,  142,   56,   57,
   43,   43,   43,   82,   43,   43,   43,   43,   43,   43,
   43,   44,    0,    0,    0,   45,    0,   43,   43,   46,
   47,   48,    0,   49,   50,   51,   52,   53,   54,   55,
   44,   61,    0,    0,   45,    0,   56,   57,    0,    0,
    0,    0,   60,   50,   51,    0,    0,    0,   61,   61,
   87,   88,   89,   90,   91,   56,   57,    0,   70,   60,
   60,   47,   47,   47,   47,   47,   47,   84,   84,   84,
    0,    0,    0,    0,  100,   70,   70,    0,    0,   98,
   96,    0,   97,   61,   99,    0,    0,    0,   56,   56,
   56,   56,   56,   56,   60,   64,    0,   95,  100,   94,
   93,    0,    0,   98,   96,    0,   97,    0,   99,    0,
   70,    0,   64,   64,   78,   59,   59,   59,   59,   59,
   59,   95,   79,   94,   62,   62,   62,   62,   62,   62,
  130,   78,   78,   78,    0,   78,   78,    0,    0,   79,
   79,   79,    0,   79,   79,    0,    0,   64,    0,    0,
    0,   65,   65,   65,   65,   65,   65,    0,    0,    0,
   87,   88,   89,   90,   91,   92,   78,    0,    0,    0,
    0,  100,    0,    0,   79,  152,   98,   96,    0,   97,
    0,   99,    0,    0,    0,    0,    0,    0,   87,   88,
   89,   90,   91,   92,   95,  100,   94,   93,    0,    0,
   98,   96,  100,   97,    0,   99,    0,   98,   96,  100,
   97,    0,   99,  171,   98,   96,  156,   97,   95,   99,
   94,   93,    0,    0,    0,   95,    0,   94,   93,    0,
    0,    0,   95,    0,   94,   93,  100,    0,    0,    0,
  175,   98,   96,  100,   97,    0,   99,    0,   98,   96,
    0,   97,    0,   99,    0,    0,    0,    0,  160,   95,
    0,   94,   93,    0,    0,    0,   95,   63,   94,   93,
   71,    0,   71,    0,   71,    0,   72,    0,   72,    0,
   72,    0,   80,    0,   63,   63,    0,   71,   71,   71,
   81,   71,   71,   72,   72,   72,    0,   72,   72,   80,
   80,   80,    0,   80,   80,    0,    0,   81,   81,   81,
    0,   81,   81,   87,   88,   89,   90,   91,   92,   63,
    0,    0,   71,    0,    0,    0,   76,    0,   72,    0,
    0,    0,    0,    0,   80,    0,    0,   87,   88,   89,
   90,    0,   81,   76,   76,   76,   77,   76,   76,   78,
   78,   78,   78,   78,   78,   67,    0,   79,   79,   79,
   79,   79,   79,   77,   77,   77,   66,   77,   77,    0,
    0,    0,   67,   67,    0,    0,    0,    0,   76,    0,
    0,    0,    0,   66,   66,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   77,    0,
    0,    0,    0,    0,    0,    0,    0,   67,    0,    0,
   87,   88,   89,   90,   91,   92,    0,    0,   66,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   87,   88,   89,   90,   91,   92,
    0,   87,   88,   89,   90,   91,   92,    0,   87,   88,
   89,   90,   91,   92,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   87,   88,   89,   90,   91,
   92,    0,   87,   88,   89,   90,   91,   92,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   71,   71,   71,   71,   71,
   71,   72,   72,   72,   72,   72,   72,   80,   80,   80,
   80,   80,   80,    0,    0,   81,   81,   81,   81,   81,
   81,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   76,   76,   76,   76,   76,   76,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   79,
   80,   77,   77,   77,   77,   77,   77,  103,    0,    0,
    0,  107,    0,    0,  110,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  114,  115,    0,  116,  117,
  118,  119,  120,  121,    0,  123,  124,  125,  126,  127,
  128,  129,    0,    0,    0,    0,    0,    0,    0,  138,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  141,    0,    0,    0,    0,    0,    0,
    0,    0,  145,  146,  147,    0,    0,    0,    0,    0,
    0,  154,    0,  157,  158,    0,    0,    0,    0,    0,
    0,  161,    0,    0,  164,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  176,  178,  179,  180,
  181,  182,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
  125,  257,   59,   61,  123,   41,   40,   61,   59,   46,
   59,   61,  257,   41,   46,  263,   56,   57,   33,  261,
   61,   41,   58,   59,   61,   40,  264,   63,  261,   41,
   58,   59,   40,   91,   91,   63,   41,   33,   58,   59,
   91,   93,   91,   59,   40,   93,   58,   59,  258,  259,
  260,    9,   10,   58,   59,   41,   33,   93,  257,   91,
   59,   37,   59,   40,  123,   93,   42,   43,  261,   45,
   93,   47,   37,   93,   72,  285,   41,   42,   43,   40,
   45,   93,   47,   40,   60,   40,   62,   40,   93,  123,
   59,  125,   59,   58,   59,   60,   61,   62,   63,   37,
  257,  125,  257,   41,   42,   43,   37,   45,  123,   47,
  125,   42,   43,   37,   45,   59,   47,  257,   42,  262,
   58,   59,   60,   47,   62,   63,   37,  123,   93,   41,
   41,   42,   43,  258,   45,   37,   47,   40,  257,   41,
   42,   43,   41,   45,   59,   47,   41,   58,   59,   60,
   59,   62,   63,  151,  269,   93,   58,   59,   60,   59,
   62,   63,   37,   58,   59,  163,   41,   42,   43,   41,
   45,   37,   47,   44,  257,   40,   42,   43,   59,   45,
  268,   47,   93,   58,   59,   60,  184,   62,   63,   59,
  188,   93,   59,   59,   60,   41,   62,   63,   93,   37,
  263,   59,  125,   41,   42,   43,   41,   45,   44,   47,
   59,   59,   59,   41,   41,   60,   -1,   -1,   93,   -1,
   -1,   -1,   60,  257,   62,   63,  284,  261,  282,  283,
  284,  265,  266,  267,  284,  269,  270,  271,  272,  273,
  274,  275,  257,  284,  280,  281,  261,  284,  282,  283,
  265,  266,  267,  281,  269,  270,  271,  272,  273,  274,
  275,  257,   -1,   -1,   -1,  261,   -1,  282,  283,  265,
  266,  267,   -1,  269,  270,  271,  272,  273,  274,  275,
  257,   41,   -1,   -1,  261,   -1,  282,  283,   -1,   -1,
   -1,   -1,   41,  270,  271,   -1,   -1,   -1,   58,   59,
  276,  277,  278,  279,  280,  282,  283,   -1,   41,   58,
   59,  276,  277,  278,  279,  280,  281,  282,  283,  284,
   -1,   -1,   -1,   -1,   37,   58,   59,   -1,   -1,   42,
   43,   -1,   45,   93,   47,   -1,   -1,   -1,  276,  277,
  278,  279,  280,  281,   93,   41,   -1,   60,   37,   62,
   63,   -1,   -1,   42,   43,   -1,   45,   -1,   47,   -1,
   93,   -1,   58,   59,   41,  276,  277,  278,  279,  280,
  281,   60,   41,   62,  276,  277,  278,  279,  280,  281,
   93,   58,   59,   60,   -1,   62,   63,   -1,   -1,   58,
   59,   60,   -1,   62,   63,   -1,   -1,   93,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,   -1,   -1,   -1,
  276,  277,  278,  279,  280,  281,   93,   -1,   -1,   -1,
   -1,   37,   -1,   -1,   93,   41,   42,   43,   -1,   45,
   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,  280,  281,   60,   37,   62,   63,   -1,   -1,
   42,   43,   37,   45,   -1,   47,   -1,   42,   43,   37,
   45,   -1,   47,   41,   42,   43,   58,   45,   60,   47,
   62,   63,   -1,   -1,   -1,   60,   -1,   62,   63,   -1,
   -1,   -1,   60,   -1,   62,   63,   37,   -1,   -1,   -1,
   41,   42,   43,   37,   45,   -1,   47,   -1,   42,   43,
   -1,   45,   -1,   47,   -1,   -1,   -1,   -1,   93,   60,
   -1,   62,   63,   -1,   -1,   -1,   60,   41,   62,   63,
   41,   -1,   43,   -1,   45,   -1,   41,   -1,   43,   -1,
   45,   -1,   41,   -1,   58,   59,   -1,   58,   59,   60,
   41,   62,   63,   58,   59,   60,   -1,   62,   63,   58,
   59,   60,   -1,   62,   63,   -1,   -1,   58,   59,   60,
   -1,   62,   63,  276,  277,  278,  279,  280,  281,   93,
   -1,   -1,   93,   -1,   -1,   -1,   41,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   93,   -1,   -1,  276,  277,  278,
  279,   -1,   93,   58,   59,   60,   41,   62,   63,  276,
  277,  278,  279,  280,  281,   41,   -1,  276,  277,  278,
  279,  280,  281,   58,   59,   60,   41,   62,   63,   -1,
   -1,   -1,   58,   59,   -1,   -1,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   58,   59,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,
  276,  277,  278,  279,  280,  281,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,  281,
   -1,  276,  277,  278,  279,  280,  281,   -1,  276,  277,
  278,  279,  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  281,   -1,  276,  277,  278,  279,  280,  281,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  281,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,   -1,   -1,  276,  277,  278,  279,  280,
  281,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   58,
   59,  276,  277,  278,  279,  280,  281,   66,   -1,   -1,
   -1,   70,   -1,   -1,   73,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   84,   85,   -1,   87,   88,
   89,   90,   91,   92,   -1,   94,   95,   96,   97,   98,
   99,  100,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  108,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  122,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  131,  132,  133,   -1,   -1,   -1,   -1,   -1,
   -1,  140,   -1,  142,  143,   -1,   -1,   -1,   -1,   -1,
   -1,  150,   -1,   -1,  153,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  165,  166,  167,  168,
  169,  170,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=285;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'","'?'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"ID","INT","FLOAT","BOOL","NUM",
"LIT","VOID","MAIN","READ","WRITE","IF","ELSE","WHILE","TRUE","FALSE","DO",
"FOR","BREAK","CONTINUE","EQ","LEQ","GEQ","NEQ","AND","OR","INC","DEC","PLUSEQ",
"STRUCT",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"prog : $$1 dList mainF",
"$$2 :",
"$$3 :",
"mainF : VOID MAIN '(' ')' $$2 '{' lcmd $$3 '}'",
"dList : decl dList",
"dList : sDecl dList",
"dList :",
"decl : type ID ';'",
"decl : type ID '[' NUM ']' ';'",
"decl : STRUCT ID ID ';'",
"decl : STRUCT ID ID '[' NUM ']' ';'",
"$$4 :",
"sDecl : STRUCT ID '{' $$4 sFields '}' ';'",
"sFields : sFields INT ID ';'",
"sFields : sFields INT ID '[' NUM ']' ';'",
"sFields :",
"type : INT",
"type : FLOAT",
"type : BOOL",
"lcmd : lcmd cmd",
"lcmd :",
"cmd : exp ';'",
"cmd : '{' lcmd '}'",
"cmd : WRITE '(' LIT ')' ';'",
"$$5 :",
"cmd : WRITE '(' LIT $$5 ',' exp ')' ';'",
"cmd : READ '(' ID ')' ';'",
"$$6 :",
"$$7 :",
"cmd : WHILE $$6 '(' exp ')' $$7 cmd",
"$$8 :",
"cmd : DO $$8 cmd WHILE '(' exp ')' ';'",
"$$9 :",
"$$10 :",
"cmd : FOR '(' optInit ';' optCond ';' optIncr ')' $$9 cmd $$10",
"cmd : BREAK ';'",
"cmd : CONTINUE ';'",
"$$11 :",
"cmd : IF '(' exp $$11 ')' cmd restoIf",
"$$12 :",
"restoIf : ELSE $$12 cmd",
"restoIf :",
"exp : NUM",
"exp : TRUE",
"exp : FALSE",
"exp : ID",
"exp : '(' exp ')'",
"exp : '!' exp",
"exp : lval '=' exp",
"exp : lval PLUSEQ exp",
"exp : INC lval",
"exp : DEC lval",
"exp : lval INC",
"exp : lval DEC",
"exp : ID '.' ID",
"exp : ID '.' ID '=' exp",
"exp : ID '.' ID PLUSEQ exp",
"exp : ID '[' exp ']'",
"exp : ID '[' exp ']' '=' exp",
"exp : ID '[' exp ']' PLUSEQ exp",
"exp : ID '[' exp ']' '.' ID",
"exp : ID '[' exp ']' '.' ID '=' exp",
"exp : ID '[' exp ']' '.' ID PLUSEQ exp",
"exp : ID '.' ID '[' exp ']'",
"exp : ID '.' ID '[' exp ']' '=' exp",
"exp : ID '.' ID '[' exp ']' PLUSEQ exp",
"$$13 :",
"$$14 :",
"exp : exp '?' $$13 exp ':' $$14 exp",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '%' exp",
"exp : exp '>' exp",
"exp : exp '<' exp",
"exp : exp EQ exp",
"exp : exp LEQ exp",
"exp : exp GEQ exp",
"exp : exp NEQ exp",
"exp : exp OR exp",
"exp : exp AND exp",
"lval : ID",
"optInit : exp",
"optInit :",
"optCond : exp",
"optCond :",
"optIncr : exp",
"optIncr :",
};

//#line 426 "exemploGC.y"

  private Yylex lexer;

  private TabSimb ts = new TabSimb();

  private int strCount = 0;
  private ArrayList<String> strTab = new ArrayList<String>();
	private ArrayList<TabSimb.StructField> tmpStructFields; // usado na definicao de tipos struct

  private Stack<Integer> pRot = new Stack<Integer>();
  private int proxRot = 1;

	private Stack<Integer> breakStack = new Stack<Integer>();
	private Stack<Integer> contStack = new Stack<Integer>();


  public static int ARRAY = 100;


  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error + "  linha: " + lexer.getLine());
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }  

  public void setDebug(boolean debug) {
    yydebug = debug;
  }

  public void listarTS() { ts.listar();}

  public static void main(String args[]) throws IOException {

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
      yyparser.yyparse();
      // yyparser.listarTS();

    }
    else {
      // interactive mode
      System.out.println("\n\tFormato: java Parser entrada.cmm >entrada.s\n");
    }

  }

							
		void gcExpArit(int oparit) {
 				System.out.println("\tPOPL %EBX");
   			System.out.println("\tPOPL %EAX");

   		switch (oparit) {
     		case '+' : System.out.println("\tADDL %EBX, %EAX" ); break;
     		case '-' : System.out.println("\tSUBL %EBX, %EAX" ); break;
     		case '*' : System.out.println("\tIMULL %EBX, %EAX" ); break;

    		case '/': 
           		     System.out.println("\tMOVL $0, %EDX");
           		     System.out.println("\tIDIVL %EBX");
           		     break;
     		case '%': 
           		     System.out.println("\tMOVL $0, %EDX");
           		     System.out.println("\tIDIVL %EBX");
           		     System.out.println("\tMOVL %EDX, %EAX");
           		     break;
    		}
   		System.out.println("\tPUSHL %EAX");
		}

	public void gcExpRel(int oprel) {

    System.out.println("\tPOPL %EAX");
    System.out.println("\tPOPL %EDX");
    System.out.println("\tCMPL %EAX, %EDX");
    System.out.println("\tMOVL $0, %EAX");
    
    switch (oprel) {
       case '<':  			System.out.println("\tSETL  %AL"); break;
       case '>':  			System.out.println("\tSETG  %AL"); break;
       case Parser.EQ:  System.out.println("\tSETE  %AL"); break;
       case Parser.GEQ: System.out.println("\tSETGE %AL"); break;
       case Parser.LEQ: System.out.println("\tSETLE %AL"); break;
       case Parser.NEQ: System.out.println("\tSETNE %AL"); break;
       }
    
    System.out.println("\tPUSHL %EAX");

	}


	public void gcExpLog(int oplog) {

	   	System.out.println("\tPOPL %EDX");
 		 	System.out.println("\tPOPL %EAX");

  	 	System.out.println("\tCMPL $0, %EAX");
 		  System.out.println("\tMOVL $0, %EAX");
   		System.out.println("\tSETNE %AL");
   		System.out.println("\tCMPL $0, %EDX");
   		System.out.println("\tMOVL $0, %EDX");
   		System.out.println("\tSETNE %DL");

   		switch (oplog) {
    			case Parser.OR:  System.out.println("\tORL  %EDX, %EAX");  break;
    			case Parser.AND: System.out.println("\tANDL  %EDX, %EAX"); break;
       }

    	System.out.println("\tPUSHL %EAX");
	}

	public void gcExpNot(){

  	 System.out.println("\tPOPL %EAX" );
 	   System.out.println("	\tNEGL %EAX" );
  	 System.out.println("	\tPUSHL %EAX");
	}

   private void geraInicio() {
			System.out.println(".text\n\n#\t nome COMPLETO e matricula dos componentes do grupo...\n#\n"); 
			System.out.println(".GLOBL _start\n\n");  
   }

   private void geraFinal(){
	
			System.out.println("\n\n");
			System.out.println("#");
			System.out.println("# devolve o controle para o SO (final da main)");
			System.out.println("#");
			System.out.println("\tmov $0, %ebx");
			System.out.println("\tmov $1, %eax");
			System.out.println("\tint $0x80");
	
			System.out.println("\n");
			System.out.println("#");
			System.out.println("# Funcoes da biblioteca (IO)");
			System.out.println("#");
			System.out.println("\n");
			System.out.println("_writeln:");
			System.out.println("\tMOVL $__fim_msg, %ECX");
			System.out.println("\tDECL %ECX");
			System.out.println("\tMOVB $10, (%ECX)");
			System.out.println("\tMOVL $1, %EDX");
			System.out.println("\tJMP _writeLit");
			System.out.println("_write:");
			System.out.println("\tMOVL $__fim_msg, %ECX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tCMPL $0, %EAX");
			System.out.println("\tJGE _write3");
			System.out.println("\tNEGL %EAX");
			System.out.println("\tMOVL $1, %EBX");
			System.out.println("_write3:");
			System.out.println("\tPUSHL %EBX");
			System.out.println("\tMOVL $10, %EBX");
			System.out.println("_divide:");
			System.out.println("\tMOVL $0, %EDX");
			System.out.println("\tIDIVL %EBX");
			System.out.println("\tDECL %ECX");
			System.out.println("\tADD $48, %DL");
			System.out.println("\tMOVB %DL, (%ECX)");
			System.out.println("\tCMPL $0, %EAX");
			System.out.println("\tJNE _divide");
			System.out.println("\tPOPL %EBX");
			System.out.println("\tCMPL $0, %EBX");
			System.out.println("\tJE _print");
			System.out.println("\tDECL %ECX");
			System.out.println("\tMOVB $'-', (%ECX)");
			System.out.println("_print:");
			System.out.println("\tMOVL $__fim_msg, %EDX");
			System.out.println("\tSUBL %ECX, %EDX");
			System.out.println("_writeLit:");
			System.out.println("\tMOVL $1, %EBX");
			System.out.println("\tMOVL $4, %EAX");
			System.out.println("\tint $0x80");
			System.out.println("\tRET");
			System.out.println("_read:");
			System.out.println("\tMOVL $15, %EDX");
			System.out.println("\tMOVL $__msg, %ECX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tMOVL $3, %EAX");
			System.out.println("\tint $0x80");
			System.out.println("\tMOVL $0, %EAX");
			System.out.println("\tMOVL $0, %EBX");
			System.out.println("\tMOVL $0, %EDX");
			System.out.println("\tMOVL $__msg, %ECX");
			System.out.println("\tCMPB $'-', (%ECX)");
			System.out.println("\tJNE _reading");
			System.out.println("\tINCL %ECX");
			System.out.println("\tINC %BL");
			System.out.println("_reading:");
			System.out.println("\tMOVB (%ECX), %DL");
			System.out.println("\tCMP $10, %DL");
			System.out.println("\tJE _fimread");
			System.out.println("\tSUB $48, %DL");
			System.out.println("\tIMULL $10, %EAX");
			System.out.println("\tADDL %EDX, %EAX");
			System.out.println("\tINCL %ECX");
			System.out.println("\tJMP _reading");
			System.out.println("_fimread:");
			System.out.println("\tCMPB $1, %BL");
			System.out.println("\tJNE _fimread2");
			System.out.println("\tNEGL %EAX");
			System.out.println("_fimread2:");
			System.out.println("\tRET");
			System.out.println("\n");
     }

     private void geraAreaDados(){
			System.out.println("");		
			System.out.println("#");
			System.out.println("# area de dados");
			System.out.println("#");
			System.out.println(".data");
			System.out.println("#");
			System.out.println("# variaveis globais");
			System.out.println("#");
			ts.geraGlobais();	
			System.out.println("");
	
    }

     private void geraAreaLiterais() { 

         System.out.println("#\n# area de literais\n#");
         System.out.println("__msg:");
	       System.out.println("\t.zero 30");
	       System.out.println("__fim_msg:");
	       System.out.println("\t.byte 0");
	       System.out.println("\n");

         for (int i = 0; i<strTab.size(); i++ ) {
             System.out.println("_str_"+i+":");
             System.out.println("\t .ascii \""+strTab.get(i)+"\""); 
	           System.out.println("_str_"+i+"Len = . - _str_"+i);  
	      }		
   }
   
//#line 800 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 42 "exemploGC.y"
{ geraInicio(); }
break;
case 2:
//#line 42 "exemploGC.y"
{ geraAreaDados(); geraAreaLiterais(); }
break;
case 3:
//#line 44 "exemploGC.y"
{ System.out.println("_start:"); }
break;
case 4:
//#line 45 "exemploGC.y"
{ geraFinal(); }
break;
case 9:
//#line 50 "exemploGC.y"
{  TS_entry nodo = ts.pesquisa(val_peek(1).sval);
		                if (nodo != null) 
							yyerror("(sem) variavel >" + val_peek(1).sval + "< jah declarada");
						else ts.insert(new TS_entry(val_peek(1).sval, val_peek(2).ival)); }
break;
case 10:
//#line 54 "exemploGC.y"
{ TS_entry nodo = ts.pesquisa(val_peek(4).sval);
						if (nodo != null)
							yyerror("(sem) variavel >" + val_peek(4).sval + "< jah declarada");
						else ts.insert(new TS_entry(val_peek(4).sval, ARRAY, Integer.parseInt(val_peek(2).sval), val_peek(5).ival)); }
break;
case 11:
//#line 58 "exemploGC.y"
{ if (!ts.hasStructType(val_peek(2).sval)) yyerror("(sem) struct tipo >"+val_peek(2).sval+"< nao definido");
						   else { TS_entry nodo = ts.pesquisa(val_peek(1).sval);
								  if (nodo != null) yyerror("(sem) variavel >"+val_peek(1).sval+"< jah declarada");
								  else ts.insert(new TS_entry(val_peek(1).sval, val_peek(2).sval)); } }
break;
case 12:
//#line 62 "exemploGC.y"
{ if (!ts.hasStructType(val_peek(5).sval)) yyerror("(sem) struct tipo >"+val_peek(5).sval+"< nao definido");
						   else { TS_entry nodo = ts.pesquisa(val_peek(4).sval);
								  if (nodo != null) yyerror("(sem) variavel >"+val_peek(4).sval+"< jah declarada");
								  else ts.insert(new TS_entry(val_peek(4).sval, val_peek(5).sval, Integer.parseInt(val_peek(2).sval))); } }
break;
case 13:
//#line 69 "exemploGC.y"
{ tmpStructFields = new ArrayList<TabSimb.StructField>(); }
break;
case 14:
//#line 69 "exemploGC.y"
{ ts.registerStructType(val_peek(5).sval, tmpStructFields); }
break;
case 15:
//#line 70 "exemploGC.y"
{ tmpStructFields.add(new TabSimb.StructField(val_peek(1).sval, 1)); }
break;
case 16:
//#line 71 "exemploGC.y"
{ tmpStructFields.add(new TabSimb.StructField(val_peek(4).sval, Integer.parseInt(val_peek(2).sval))); }
break;
case 18:
//#line 75 "exemploGC.y"
{ yyval.ival = INT; }
break;
case 19:
//#line 76 "exemploGC.y"
{ yyval.ival = FLOAT; }
break;
case 20:
//#line 77 "exemploGC.y"
{ yyval.ival = BOOL; }
break;
case 23:
//#line 84 "exemploGC.y"
{  System.out.println("\tPOPL %EAX\t# descarta resultado da expressao"); }
break;
case 24:
//#line 85 "exemploGC.y"
{ System.out.println("\t\t# terminou o bloco..."); }
break;
case 25:
//#line 88 "exemploGC.y"
{ strTab.add(val_peek(2).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				System.out.println("\tCALL _writeln"); 
                                strCount++;
				}
break;
case 26:
//#line 97 "exemploGC.y"
{ strTab.add(val_peek(0).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				strCount++;
				}
break;
case 27:
//#line 105 "exemploGC.y"
{ 
			 System.out.println("\tPOPL %EAX"); 
			 System.out.println("\tCALL _write");	
			 System.out.println("\tCALL _writeln"); 
                        }
break;
case 28:
//#line 112 "exemploGC.y"
{
									System.out.println("\tPUSHL $_"+val_peek(2).sval);
									System.out.println("\tCALL _read");
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EAX, (%EDX)");
									
								}
break;
case 29:
//#line 120 "exemploGC.y"
{
					pRot.push(proxRot);  proxRot += 2;
					System.out.printf("rot_%02d:\n",pRot.peek());
					/* preparar break/continue*/
					contStack.push(pRot.peek());
					breakStack.push(pRot.peek()+1);
				  }
break;
case 30:
//#line 127 "exemploGC.y"
{
			 							System.out.println("\tPOPL %EAX   # desvia se falso...");
											System.out.println("\tCMPL $0, %EAX");
											System.out.printf("\tJE rot_%02d\n", (int)pRot.peek()+1);
										}
break;
case 31:
//#line 132 "exemploGC.y"
{
				  		System.out.printf("\tJMP rot_%02d   # terminou cmd na linha de cima\n", pRot.peek());
							System.out.printf("rot_%02d:\n",(int)pRot.peek()+1);
						pRot.pop();
						breakStack.pop();
						contStack.pop();
							}
break;
case 32:
//#line 140 "exemploGC.y"
{
										/* do-while: inicio do corpo*/
										pRot.push(proxRot); proxRot += 2;
										System.out.printf("rot_%02d:\n", pRot.peek());
										/* continue deve pular para checagem*/
										contStack.push(pRot.peek()+1);
										breakStack.push(pRot.peek()+1);
									}
break;
case 33:
//#line 148 "exemploGC.y"
{
										/* checagem da condicao*/
										System.out.printf("rot_%02d:\n", pRot.peek()+1);
										System.out.println("\tPOPL %EAX   # desvia se falso...");
										System.out.println("\tCMPL $0, %EAX");
										System.out.printf("\tJNE rot_%02d\n", pRot.peek());
										pRot.pop();
										breakStack.pop();
										contStack.pop();
									}
break;
case 34:
//#line 159 "exemploGC.y"
{
										/* for: base labels: cond=base, end=base+1, inc=base+2*/
										int base = proxRot; proxRot += 3;
										System.out.printf("rot_%02d:\n", base);
										/* cond result esta no topo (optExp 2)*/
										System.out.println("\tPOPL %EAX   # cond for");
										System.out.println("\tCMPL $0, %EAX");
										System.out.printf("\tJE rot_%02d\n", base+1);
										pRot.push(base);
										breakStack.push(base+1);
										contStack.push(base+2);
									}
break;
case 35:
//#line 171 "exemploGC.y"
{
										/* pular para incremento ao final do corpo*/
										System.out.printf("\tJMP rot_%02d\n", pRot.peek()+2);
										System.out.printf("rot_%02d:\n", pRot.peek()+2);
									}
break;
case 36:
//#line 176 "exemploGC.y"
{
										/* fim do incremento; voltar para condicao*/
										System.out.println("\t# fim incremento for");
										System.out.printf("\tJMP rot_%02d\n", pRot.peek());
										System.out.printf("rot_%02d:\n", pRot.peek()+1);
										breakStack.pop();
										contStack.pop();
										pRot.pop();
									}
break;
case 37:
//#line 186 "exemploGC.y"
{ System.out.printf("\tJMP rot_%02d\n", breakStack.peek()); }
break;
case 38:
//#line 187 "exemploGC.y"
{ System.out.printf("\tJMP rot_%02d\n", contStack.peek()); }
break;
case 39:
//#line 189 "exemploGC.y"
{	
											pRot.push(proxRot);  proxRot += 2;
															
											System.out.println("\tPOPL %EAX");
											System.out.println("\tCMPL $0, %EAX");
											System.out.printf("\tJE rot_%02d\n", pRot.peek());
										}
break;
case 40:
//#line 198 "exemploGC.y"
{
											System.out.printf("rot_%02d:\n",pRot.peek()+1);
											pRot.pop();
										}
break;
case 41:
//#line 205 "exemploGC.y"
{
											System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
											System.out.printf("rot_%02d:\n",pRot.peek());
								
										}
break;
case 43:
//#line 213 "exemploGC.y"
{
		    System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
				System.out.printf("rot_%02d:\n",pRot.peek());
				}
break;
case 44:
//#line 220 "exemploGC.y"
{ System.out.println("\tPUSHL $"+val_peek(0).sval); }
break;
case 45:
//#line 221 "exemploGC.y"
{ System.out.println("\tPUSHL $1"); }
break;
case 46:
//#line 222 "exemploGC.y"
{ System.out.println("\tPUSHL $0"); }
break;
case 47:
//#line 223 "exemploGC.y"
{ System.out.println("\tPUSHL _"+val_peek(0).sval); }
break;
case 49:
//#line 225 "exemploGC.y"
{ gcExpNot(); }
break;
case 50:
//#line 226 "exemploGC.y"
{ 
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EDX, _"+val_peek(2).sval);
									System.out.println("\tPUSHL %EDX");
						 }
break;
case 51:
//#line 231 "exemploGC.y"
{
									System.out.println("\tPOPL %EBX");
									System.out.println("\tMOVL _"+val_peek(2).sval+", %EAX");
									System.out.println("\tADDL %EBX, %EAX");
									System.out.println("\tMOVL %EAX, _"+val_peek(2).sval);
									System.out.println("\tPUSHL %EAX");
								}
break;
case 52:
//#line 238 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
								System.out.println("\tINCL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
								System.out.println("\tPUSHL %EAX");
							}
break;
case 53:
//#line 244 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
								System.out.println("\tDECL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
								System.out.println("\tPUSHL %EAX");
							}
break;
case 54:
//#line 250 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
								System.out.println("\tPUSHL %EAX");
								System.out.println("\tINCL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
							}
break;
case 55:
//#line 256 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
								System.out.println("\tPUSHL %EAX");
								System.out.println("\tDECL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
							}
break;
case 56:
//#line 262 "exemploGC.y"
{ 
								int off = ts.getFieldOffsetForVar(val_peek(2).sval, val_peek(0).sval);
								System.out.println("\tLEA _"+val_peek(2).sval+"+"+off+", %EDX");
								System.out.println("\tMOVL (%EDX), %EAX");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 57:
//#line 268 "exemploGC.y"
{
								int off = ts.getFieldOffsetForVar(val_peek(4).sval, val_peek(2).sval);
								System.out.println("\tPOPL %EDX\t# valor");
								System.out.println("\tLEA _"+val_peek(4).sval+"+"+off+", %ECX");
								System.out.println("\tMOVL %EDX, (%ECX)");
								System.out.println("\tPUSHL %EDX");
							}
break;
case 58:
//#line 275 "exemploGC.y"
{
								int off = ts.getFieldOffsetForVar(val_peek(4).sval, val_peek(2).sval);
								System.out.println("\tPOPL %EBX\t# valor");
								System.out.println("\tLEA _"+val_peek(4).sval+"+"+off+", %ECX");
								System.out.println("\tMOVL (%ECX), %EAX");
								System.out.println("\tADDL %EBX, %EAX");
								System.out.println("\tMOVL %EAX, (%ECX)");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 59:
//#line 284 "exemploGC.y"
{
								/* rvalue acesso a[exp]*/
								System.out.println("\tPOPL %EAX");
								System.out.println("\tLEA _"+val_peek(3).sval+"(,%EAX,4), %EDX");
								System.out.println("\tMOVL (%EDX), %EAX");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 60:
//#line 291 "exemploGC.y"
{
								/* atribuicao a[i] = exp*/
								System.out.println("\tPOPL %EDX\t# valor");
								System.out.println("\tPOPL %EAX\t# indice");
								System.out.println("\tLEA _"+val_peek(5).sval+"(,%EAX,4), %ECX");
								System.out.println("\tMOVL %EDX, (%ECX)");
								System.out.println("\tPUSHL %EDX");
							}
break;
case 61:
//#line 299 "exemploGC.y"
{
								/* a[i] += exp*/
								System.out.println("\tPOPL %EBX\t# valor");
								System.out.println("\tPOPL %EAX\t# indice");
								System.out.println("\tLEA _"+val_peek(5).sval+"(,%EAX,4), %ECX");
								System.out.println("\tMOVL (%ECX), %EAX");
								System.out.println("\tADDL %EBX, %EAX");
								System.out.println("\tMOVL %EAX, (%ECX)");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 62:
//#line 309 "exemploGC.y"
{
						/* rvalue a[i].campo para array de structs*/
						TS_entry v = ts.pesquisa(val_peek(5).sval);
						String st = (v==null)?null:v.getStructName();
						int sz = (st==null)?4:ts.getStructSize(st);
						int off = (st==null)?0:ts.getFieldOffsetForType(st, val_peek(0).sval);
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tMOVL $"+sz+", %EBX");
						System.out.println("\tIMULL %EBX, %EAX");
						System.out.println("\tLEA _"+val_peek(5).sval+"+"+off+"(%EAX), %ECX");
						System.out.println("\tMOVL (%ECX), %EAX");
						System.out.println("\tPUSHL %EAX");
					}
break;
case 63:
//#line 322 "exemploGC.y"
{
						TS_entry v = ts.pesquisa(val_peek(7).sval);
						String st = (v==null)?null:v.getStructName();
						int sz = (st==null)?4:ts.getStructSize(st);
						int off = (st==null)?0:ts.getFieldOffsetForType(st, val_peek(2).sval);
						System.out.println("\tPOPL %EDX\t# valor");
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tMOVL $"+sz+", %EBX");
						System.out.println("\tIMULL %EBX, %EAX");
						System.out.println("\tLEA _"+val_peek(7).sval+"+"+off+"(%EAX), %ECX");
						System.out.println("\tMOVL %EDX, (%ECX)");
						System.out.println("\tPUSHL %EDX");
					}
break;
case 64:
//#line 335 "exemploGC.y"
{
						TS_entry v = ts.pesquisa(val_peek(7).sval);
						String st = (v==null)?null:v.getStructName();
						int sz = (st==null)?4:ts.getStructSize(st);
						int off = (st==null)?0:ts.getFieldOffsetForType(st, val_peek(2).sval);
						System.out.println("\tPOPL %EBX\t# valor");
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tMOVL $"+sz+", %EDX");
						System.out.println("\tIMULL %EDX, %EAX");
						System.out.println("\tLEA _"+val_peek(7).sval+"+"+off+"(%EAX), %ECX");
						System.out.println("\tMOVL (%ECX), %EAX");
						System.out.println("\tADDL %EBX, %EAX");
						System.out.println("\tMOVL %EAX, (%ECX)");
						System.out.println("\tPUSHL %EAX");
					}
break;
case 65:
//#line 350 "exemploGC.y"
{
						/* rvalue var.campo[i] para array dentro da struct*/
						int off = ts.getFieldOffsetForVar(val_peek(5).sval, val_peek(3).sval);
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tLEA _"+val_peek(5).sval+"+"+off+"(,%EAX,4), %ECX");
						System.out.println("\tMOVL (%ECX), %EAX");
						System.out.println("\tPUSHL %EAX");
					}
break;
case 66:
//#line 358 "exemploGC.y"
{
						int off = ts.getFieldOffsetForVar(val_peek(7).sval, val_peek(5).sval);
						System.out.println("\tPOPL %EDX\t# valor");
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tLEA _"+val_peek(7).sval+"+"+off+"(,%EAX,4), %ECX");
						System.out.println("\tMOVL %EDX, (%ECX)");
						System.out.println("\tPUSHL %EDX");
					}
break;
case 67:
//#line 366 "exemploGC.y"
{
						int off = ts.getFieldOffsetForVar(val_peek(7).sval, val_peek(5).sval);
						System.out.println("\tPOPL %EBX\t# valor");
						System.out.println("\tPOPL %EAX\t# indice");
						System.out.println("\tLEA _"+val_peek(7).sval+"+"+off+"(,%EAX,4), %ECX");
						System.out.println("\tMOVL (%ECX), %EAX");
						System.out.println("\tADDL %EBX, %EAX");
						System.out.println("\tMOVL %EAX, (%ECX)");
						System.out.println("\tPUSHL %EAX");
					}
break;
case 68:
//#line 376 "exemploGC.y"
{
								int base = proxRot; proxRot += 2;
								System.out.println("\tPOPL %EAX");
								System.out.println("\tCMPL $0, %EAX");
								System.out.printf("\tJE rot_%02d\n", base);
								pRot.push(base);
							}
break;
case 69:
//#line 383 "exemploGC.y"
{
								System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
								System.out.printf("rot_%02d:\n", pRot.peek());
							}
break;
case 70:
//#line 387 "exemploGC.y"
{
										System.out.printf("rot_%02d:\n", pRot.peek()+1);
										pRot.pop();
									}
break;
case 71:
//#line 392 "exemploGC.y"
{ gcExpArit('+'); }
break;
case 72:
//#line 393 "exemploGC.y"
{ gcExpArit('-'); }
break;
case 73:
//#line 394 "exemploGC.y"
{ gcExpArit('*'); }
break;
case 74:
//#line 395 "exemploGC.y"
{ gcExpArit('/'); }
break;
case 75:
//#line 396 "exemploGC.y"
{ gcExpArit('%'); }
break;
case 76:
//#line 398 "exemploGC.y"
{ gcExpRel('>'); }
break;
case 77:
//#line 399 "exemploGC.y"
{ gcExpRel('<'); }
break;
case 78:
//#line 400 "exemploGC.y"
{ gcExpRel(EQ); }
break;
case 79:
//#line 401 "exemploGC.y"
{ gcExpRel(LEQ); }
break;
case 80:
//#line 402 "exemploGC.y"
{ gcExpRel(GEQ); }
break;
case 81:
//#line 403 "exemploGC.y"
{ gcExpRel(NEQ); }
break;
case 82:
//#line 405 "exemploGC.y"
{ gcExpLog(OR); }
break;
case 83:
//#line 406 "exemploGC.y"
{ gcExpLog(AND); }
break;
case 84:
//#line 410 "exemploGC.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 85:
//#line 412 "exemploGC.y"
{ System.out.println("\tPOPL %EAX\t# descarta init do for"); }
break;
case 86:
//#line 413 "exemploGC.y"
{ /* nada */ }
break;
case 87:
//#line 416 "exemploGC.y"
{ /* valor da condicao no topo */ }
break;
case 88:
//#line 417 "exemploGC.y"
{ System.out.println("\tPUSHL $1\t# condicao vazia => true"); }
break;
case 89:
//#line 420 "exemploGC.y"
{ System.out.println("\tPOPL %EAX\t# descarta incremento do for"); }
break;
case 90:
//#line 421 "exemploGC.y"
{ /* nada */ }
break;
//#line 1521 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
