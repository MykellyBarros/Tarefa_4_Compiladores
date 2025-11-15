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
  import java.io.*;
  import java.util.ArrayList;
  import java.util.Stack;
//#line 21 "Parser.java"




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
    9,   12,   10,   11,   11,    1,    1,    1,    7,    7,
   13,   13,   13,   15,   13,   13,   16,   17,   13,   18,
   13,   22,   23,   13,   13,   13,   24,   13,   26,   25,
   25,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   27,
   28,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,    2,   19,   19,   20,   20,
   21,   21,
};
final static short yylen[] = {                            2,
    0,    3,    0,    0,    9,    2,    2,    0,    3,    6,
    4,    0,    7,    4,    0,    1,    1,    1,    2,    0,
    2,    3,    5,    0,    8,    5,    0,    0,    7,    0,
    8,    0,    0,   11,    2,    2,    0,    7,    0,    3,
    0,    1,    1,    1,    1,    3,    2,    3,    3,    2,
    2,    2,    2,    3,    5,    5,    4,    6,    6,    0,
    0,    7,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    1,    1,    0,    1,    0,
    1,    0,
};
final static short yydefred[] = {                         1,
    0,    0,   16,   17,   18,    0,    0,    0,    0,    0,
    0,    0,    0,    2,    6,    7,    0,   12,    9,    0,
    0,   11,   15,    0,    0,    0,    0,    3,    0,    0,
   10,    0,    0,   13,   20,   14,    0,    0,   42,    0,
    0,    0,   27,   43,   44,   30,    0,    0,    0,    0,
    0,    0,    0,   20,    0,    0,   19,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,   36,   76,   50,
   51,   47,    0,    0,   52,   53,    0,    0,    5,    0,
    0,    0,    0,    0,    0,   60,    0,    0,    0,    0,
    0,    0,    0,   21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   46,   22,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   65,   66,
   67,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,   23,    0,    0,
   28,    0,    0,    0,   61,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   39,   38,   29,    0,    0,    0,
    0,   25,    0,   31,   32,   40,    0,   33,   34,
};
final static short yydgoto[] = {                          1,
    7,   55,    8,    2,   14,   32,   37,   56,    9,   10,
   26,   23,   57,   58,  127,   64,  150,   65,  103,  144,
  160,  167,  169,  128,  156,  163,  114,  153,
};
final static short yysindex[] = {                         0,
    0, -209,    0,    0,    0, -247, -242, -245, -209, -209,
 -118,  -48, -239,    0,    0,    0,  -32,    0,    0, -232,
   -7,    0,    0,  -56,   11, -124,  -15,    0, -211,   -6,
    0,  -67,    1,    0,    0,    0,    5,  -37,    0,   19,
   21,   23,    0,    0,    0,    0,   31,    6,   16, -177,
 -177,   24,   24,    0,  -53,  -41,    0,   99,   24, -171,
 -169, -173,   24,   53,    5,   24,    0,    0,    0,    0,
    0,    0,  126,  -33,    0,    0,   24,   24,    0,   24,
   24,   24,   24,   24,   24,    0,   24,   24,   24,   24,
   24,   24,   24,    0,  133,  -59,   62,   75,  410,   24,
 -145,  410,   71,    0,    0,  410,  410,   70,   70,   70,
   70,  417,   25,   24,   70,   70,  118,  118,    0,    0,
    0,  -57,   24,   24,   79,   81,  101,  102,  157,  107,
   24,  287,   24,   24,  410,  410,    0,    0,   24,    5,
    0,   24,  410,   92,    0,  410,  410,  379, -111,    5,
  403,   24,   24,  113,    0,    0,    0,  115,  410,  125,
  410,    0,    5,    0,    0,    0,    5,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -86,    0,    0,    0,    0,    0,    0,  -86,  -86,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   54,   36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  128,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   63,    0,  140,  144,    0,
    0,  131,    0,    0,    0,  -38,  -24,  151,  294,  332,
  439,  -35,  -27,    0,  446,  453,  426,  433,    0,    0,
    0,   90,    0,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0,  -19,  -11,    0,    0,    0,    0,
    0,    0,  138,    0,    0,   61,  123,    0,  -14,    0,
    0,  160,    0,    0,    0,    0,    0,    0,  162,    0,
  232,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -9,    3,    0,    0,    0,  152,    0,    0,    0,
    0,    0,  -49,  683,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=836;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
   30,  124,   49,  134,   18,   75,   53,   78,   60,   11,
   19,   15,   16,   74,   12,  101,   48,   13,   41,   49,
   49,   56,   75,   75,   21,   41,   22,   75,   24,   55,
   74,   74,   25,   48,   48,   74,   27,   52,   56,   56,
   70,   71,   20,   31,   53,   33,   55,   55,    3,    4,
    5,   28,   34,   59,   49,   35,   52,   75,   61,   36,
   62,   93,   63,   53,   67,   74,   91,   89,   48,   90,
   66,   92,   45,   56,   68,    6,   45,   45,   45,   69,
   45,   55,   45,   79,   88,   96,   87,   97,   98,   54,
  149,  105,  100,   45,   45,   45,   76,   45,   45,   54,
  157,   59,  125,   54,   54,   54,   93,   54,   41,   54,
   41,   91,   89,  166,   90,  126,   92,  168,   59,   59,
   54,   54,   54,  130,   54,   54,   57,   54,   45,  131,
   57,   57,   57,   29,   57,   93,   57,  137,   17,  138,
   91,   89,  140,   90,  139,   92,  142,   57,   57,   57,
  152,   57,   57,   59,   93,   54,  155,   94,   88,   91,
   87,   86,   93,   58,   92,  165,  104,   91,   89,   93,
   90,  162,   92,  164,   91,   89,    8,   90,    4,   92,
   58,   58,   57,   24,   37,   88,   78,   87,   86,   77,
   80,   70,   88,   93,   87,   86,   79,  141,   91,   89,
   82,   90,   81,   92,    0,   74,    0,    0,   70,   70,
   70,    0,   70,   70,    0,   58,   88,    0,   87,   86,
    0,    0,    0,   38,  123,  122,  133,   39,   75,   76,
   77,   40,   41,   42,    0,   43,   44,   45,   46,   47,
   48,   49,   41,   70,   75,   75,   41,    0,   50,   51,
   41,   41,   41,   74,   41,   41,   41,   41,   41,   41,
   41,   38,    0,    0,    0,   39,    0,   41,   41,   40,
   41,   42,   62,   43,   44,   45,   46,   47,   48,   49,
   38,    0,    0,    0,   39,    0,   50,   51,    0,   62,
   62,    0,    0,   44,   45,    0,    0,    0,    0,    0,
   80,   81,   82,   83,   84,   50,   51,    0,    0,    0,
    0,   45,   45,   45,   45,   45,   45,   76,   76,   76,
    0,    0,    0,   93,   62,    0,    0,    0,   91,   89,
    0,   90,    0,   92,   71,    0,    0,    0,   54,   54,
   54,   54,   54,   54,  145,    0,   88,    0,   87,   86,
    0,   71,   71,   71,    0,   71,   71,    0,    0,    0,
    0,    0,    0,    0,    0,   57,   57,   57,   57,   57,
   57,    0,   72,    0,   80,   81,   82,   83,   84,   85,
    0,    0,    0,    0,    0,    0,   71,    0,    0,   72,
   72,   72,    0,   72,   72,    0,    0,    0,    0,    0,
    0,   80,   81,   82,   83,   84,   85,    0,   80,   81,
   82,   83,   84,   85,    0,   93,    0,    0,    0,  154,
   91,   89,    0,   90,   72,   92,   70,   70,   70,   70,
   70,   70,   80,   81,   82,   83,   84,   85,   88,   93,
   87,   86,    0,  158,   91,   89,   93,   90,    0,   92,
    0,   91,   89,   93,   90,    0,   92,    0,   91,   89,
    0,   90,   88,   92,   87,   86,   63,    0,   63,   88,
   63,   87,   86,   64,    0,   64,   88,   64,   87,   73,
    0,    0,    0,   63,   63,   63,   68,   63,   63,    0,
   64,   64,   64,   69,   64,   64,   73,   73,   73,    0,
   73,   73,    0,   68,   68,   68,    0,   68,   68,    0,
   69,   69,   69,    0,   69,   69,    0,    0,   63,    0,
    0,    0,    0,    0,    0,   64,    0,    0,    0,    0,
    0,   73,    0,    0,    0,    0,    0,    0,   68,    0,
    0,    0,    0,    0,    0,   69,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   80,   81,   82,   83,   84,   85,    0,   71,
   71,   71,   71,   71,   71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   72,   72,   72,
   72,   72,   72,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   80,   81,   82,   83,   84,   85,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   80,   81,
   82,   83,   84,   85,    0,   80,   81,   82,   83,   84,
   85,    0,   80,   81,   82,   83,    0,    0,    0,    0,
    0,   63,   63,   63,   63,   63,   63,    0,   64,   64,
   64,   64,   64,   64,   73,   73,   73,   73,   73,   73,
    0,   68,   68,   68,   68,   68,   68,    0,   69,   69,
   69,   69,   69,   69,   72,   73,    0,    0,    0,    0,
    0,   95,    0,    0,    0,   99,    0,    0,  102,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  106,
  107,    0,  108,  109,  110,  111,  112,  113,    0,  115,
  116,  117,  118,  119,  120,  121,    0,    0,    0,    0,
    0,    0,  129,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  132,    0,    0,    0,
    0,    0,    0,    0,    0,  135,  136,    0,    0,    0,
    0,    0,    0,  143,    0,  146,  147,    0,    0,    0,
    0,  148,    0,    0,  151,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  159,  161,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
  125,   61,   41,   61,  123,   41,   40,   61,   46,  257,
   59,    9,   10,   41,  257,   65,   41,  263,   33,   58,
   59,   41,   58,   59,  264,   40,   59,   63,  261,   41,
   58,   59,   40,   58,   59,   63,   93,   33,   58,   59,
   50,   51,   91,   59,   40,  257,   58,   59,  258,  259,
  260,   41,   59,   91,   93,  123,   33,   93,   40,   59,
   40,   37,   40,   40,   59,   93,   42,   43,   93,   45,
   40,   47,   37,   93,   59,  285,   41,   42,   43,  257,
   45,   93,   47,  125,   60,  257,   62,  257,  262,  123,
  140,  125,   40,   58,   59,   60,   61,   62,   63,   37,
  150,   41,   41,   41,   42,   43,   37,   45,  123,   47,
  125,   42,   43,  163,   45,   41,   47,  167,   58,   59,
   58,   59,   60,  269,   62,   63,   37,  123,   93,   59,
   41,   42,   43,  258,   45,   37,   47,   59,  257,   59,
   42,   43,   41,   45,   44,   47,   40,   58,   59,   60,
   59,   62,   63,   93,   37,   93,  268,   59,   60,   42,
   62,   63,   37,   41,   47,   41,   41,   42,   43,   37,
   45,   59,   47,   59,   42,   43,  263,   45,  125,   47,
   58,   59,   93,   44,   41,   60,   59,   62,   63,   59,
   59,   41,   60,   37,   62,   63,   59,   41,   42,   43,
   41,   45,   41,   47,   -1,   54,   -1,   -1,   58,   59,
   60,   -1,   62,   63,   -1,   93,   60,   -1,   62,   63,
   -1,   -1,   -1,  257,  284,   93,  284,  261,  282,  283,
  284,  265,  266,  267,   -1,  269,  270,  271,  272,  273,
  274,  275,  257,   93,  280,  281,  261,   -1,  282,  283,
  265,  266,  267,  281,  269,  270,  271,  272,  273,  274,
  275,  257,   -1,   -1,   -1,  261,   -1,  282,  283,  265,
  266,  267,   41,  269,  270,  271,  272,  273,  274,  275,
  257,   -1,   -1,   -1,  261,   -1,  282,  283,   -1,   58,
   59,   -1,   -1,  270,  271,   -1,   -1,   -1,   -1,   -1,
  276,  277,  278,  279,  280,  282,  283,   -1,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,  282,  283,  284,
   -1,   -1,   -1,   37,   93,   -1,   -1,   -1,   42,   43,
   -1,   45,   -1,   47,   41,   -1,   -1,   -1,  276,  277,
  278,  279,  280,  281,   58,   -1,   60,   -1,   62,   63,
   -1,   58,   59,   60,   -1,   62,   63,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  281,   -1,   41,   -1,  276,  277,  278,  279,  280,  281,
   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,   58,
   59,   60,   -1,   62,   63,   -1,   -1,   -1,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,   -1,  276,  277,
  278,  279,  280,  281,   -1,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   93,   47,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   60,   37,
   62,   63,   -1,   41,   42,   43,   37,   45,   -1,   47,
   -1,   42,   43,   37,   45,   -1,   47,   -1,   42,   43,
   -1,   45,   60,   47,   62,   63,   41,   -1,   43,   60,
   45,   62,   63,   41,   -1,   43,   60,   45,   62,   41,
   -1,   -1,   -1,   58,   59,   60,   41,   62,   63,   -1,
   58,   59,   60,   41,   62,   63,   58,   59,   60,   -1,
   62,   63,   -1,   58,   59,   60,   -1,   62,   63,   -1,
   58,   59,   60,   -1,   62,   63,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  276,  277,  278,  279,  280,  281,   -1,  276,
  277,  278,  279,  280,  281,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,  278,
  279,  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,  281,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,  280,  281,   -1,  276,  277,  278,  279,  280,
  281,   -1,  276,  277,  278,  279,   -1,   -1,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,   -1,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
   -1,  276,  277,  278,  279,  280,  281,   -1,  276,  277,
  278,  279,  280,  281,   52,   53,   -1,   -1,   -1,   -1,
   -1,   59,   -1,   -1,   -1,   63,   -1,   -1,   66,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   77,
   78,   -1,   80,   81,   82,   83,   84,   85,   -1,   87,
   88,   89,   90,   91,   92,   93,   -1,   -1,   -1,   -1,
   -1,   -1,  100,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  114,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  123,  124,   -1,   -1,   -1,
   -1,   -1,   -1,  131,   -1,  133,  134,   -1,   -1,   -1,
   -1,  139,   -1,   -1,  142,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  152,  153,
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
"$$4 :",
"sDecl : STRUCT ID '{' $$4 sFields '}' ';'",
"sFields : sFields INT ID ';'",
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

//#line 345 "exemploGC.y"

  private Yylex lexer;

  private TabSimb ts = new TabSimb();

  private int strCount = 0;
  private ArrayList<String> strTab = new ArrayList<String>();
	private ArrayList<String> tmpStructFields; // usado na definicao de tipos struct

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
   
//#line 755 "Parser.java"
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
//#line 33 "exemploGC.y"
{ geraInicio(); }
break;
case 2:
//#line 33 "exemploGC.y"
{ geraAreaDados(); geraAreaLiterais(); }
break;
case 3:
//#line 35 "exemploGC.y"
{ System.out.println("_start:"); }
break;
case 4:
//#line 36 "exemploGC.y"
{ geraFinal(); }
break;
case 9:
//#line 41 "exemploGC.y"
{  TS_entry nodo = ts.pesquisa(val_peek(1).sval);
		                if (nodo != null) 
							yyerror("(sem) variavel >" + val_peek(1).sval + "< jah declarada");
						else ts.insert(new TS_entry(val_peek(1).sval, val_peek(2).ival)); }
break;
case 10:
//#line 45 "exemploGC.y"
{ TS_entry nodo = ts.pesquisa(val_peek(4).sval);
						if (nodo != null)
							yyerror("(sem) variavel >" + val_peek(4).sval + "< jah declarada");
						else ts.insert(new TS_entry(val_peek(4).sval, ARRAY, Integer.parseInt(val_peek(2).sval), val_peek(5).ival)); }
break;
case 11:
//#line 49 "exemploGC.y"
{ if (!ts.hasStructType(val_peek(2).sval)) yyerror("(sem) struct tipo >"+val_peek(2).sval+"< nao definido");
						   else { TS_entry nodo = ts.pesquisa(val_peek(1).sval);
								  if (nodo != null) yyerror("(sem) variavel >"+val_peek(1).sval+"< jah declarada");
								  else ts.insert(new TS_entry(val_peek(1).sval, val_peek(2).sval)); } }
break;
case 12:
//#line 56 "exemploGC.y"
{ tmpStructFields = new ArrayList<String>(); }
break;
case 13:
//#line 56 "exemploGC.y"
{ ts.registerStructType(val_peek(5).sval, tmpStructFields); }
break;
case 14:
//#line 57 "exemploGC.y"
{ tmpStructFields.add(val_peek(1).sval); }
break;
case 16:
//#line 61 "exemploGC.y"
{ yyval.ival = INT; }
break;
case 17:
//#line 62 "exemploGC.y"
{ yyval.ival = FLOAT; }
break;
case 18:
//#line 63 "exemploGC.y"
{ yyval.ival = BOOL; }
break;
case 21:
//#line 70 "exemploGC.y"
{  System.out.println("\tPOPL %EAX\t# descarta resultado da expressao"); }
break;
case 22:
//#line 71 "exemploGC.y"
{ System.out.println("\t\t# terminou o bloco..."); }
break;
case 23:
//#line 74 "exemploGC.y"
{ strTab.add(val_peek(2).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				System.out.println("\tCALL _writeln"); 
                                strCount++;
				}
break;
case 24:
//#line 83 "exemploGC.y"
{ strTab.add(val_peek(0).sval);
                                System.out.println("\tMOVL $_str_"+strCount+"Len, %EDX"); 
				System.out.println("\tMOVL $_str_"+strCount+", %ECX"); 
                                System.out.println("\tCALL _writeLit"); 
				strCount++;
				}
break;
case 25:
//#line 91 "exemploGC.y"
{ 
			 System.out.println("\tPOPL %EAX"); 
			 System.out.println("\tCALL _write");	
			 System.out.println("\tCALL _writeln"); 
                        }
break;
case 26:
//#line 98 "exemploGC.y"
{
									System.out.println("\tPUSHL $_"+val_peek(2).sval);
									System.out.println("\tCALL _read");
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EAX, (%EDX)");
									
								}
break;
case 27:
//#line 106 "exemploGC.y"
{
					pRot.push(proxRot);  proxRot += 2;
					System.out.printf("rot_%02d:\n",pRot.peek());
					/* preparar break/continue*/
					contStack.push(pRot.peek());
					breakStack.push(pRot.peek()+1);
				  }
break;
case 28:
//#line 113 "exemploGC.y"
{
			 							System.out.println("\tPOPL %EAX   # desvia se falso...");
											System.out.println("\tCMPL $0, %EAX");
											System.out.printf("\tJE rot_%02d\n", (int)pRot.peek()+1);
										}
break;
case 29:
//#line 118 "exemploGC.y"
{
				  		System.out.printf("\tJMP rot_%02d   # terminou cmd na linha de cima\n", pRot.peek());
							System.out.printf("rot_%02d:\n",(int)pRot.peek()+1);
						pRot.pop();
						breakStack.pop();
						contStack.pop();
							}
break;
case 30:
//#line 126 "exemploGC.y"
{
										/* do-while: inicio do corpo*/
										pRot.push(proxRot); proxRot += 2;
										System.out.printf("rot_%02d:\n", pRot.peek());
										/* continue deve pular para checagem*/
										contStack.push(pRot.peek()+1);
										breakStack.push(pRot.peek()+1);
									}
break;
case 31:
//#line 134 "exemploGC.y"
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
case 32:
//#line 145 "exemploGC.y"
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
case 33:
//#line 157 "exemploGC.y"
{
										/* pular para incremento ao final do corpo*/
										System.out.printf("\tJMP rot_%02d\n", pRot.peek()+2);
										System.out.printf("rot_%02d:\n", pRot.peek()+2);
									}
break;
case 34:
//#line 162 "exemploGC.y"
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
case 35:
//#line 172 "exemploGC.y"
{ System.out.printf("\tJMP rot_%02d\n", breakStack.peek()); }
break;
case 36:
//#line 173 "exemploGC.y"
{ System.out.printf("\tJMP rot_%02d\n", contStack.peek()); }
break;
case 37:
//#line 175 "exemploGC.y"
{	
											pRot.push(proxRot);  proxRot += 2;
															
											System.out.println("\tPOPL %EAX");
											System.out.println("\tCMPL $0, %EAX");
											System.out.printf("\tJE rot_%02d\n", pRot.peek());
										}
break;
case 38:
//#line 184 "exemploGC.y"
{
											System.out.printf("rot_%02d:\n",pRot.peek()+1);
											pRot.pop();
										}
break;
case 39:
//#line 191 "exemploGC.y"
{
											System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
											System.out.printf("rot_%02d:\n",pRot.peek());
								
										}
break;
case 41:
//#line 199 "exemploGC.y"
{
		    System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
				System.out.printf("rot_%02d:\n",pRot.peek());
				}
break;
case 42:
//#line 206 "exemploGC.y"
{ System.out.println("\tPUSHL $"+val_peek(0).sval); }
break;
case 43:
//#line 207 "exemploGC.y"
{ System.out.println("\tPUSHL $1"); }
break;
case 44:
//#line 208 "exemploGC.y"
{ System.out.println("\tPUSHL $0"); }
break;
case 45:
//#line 209 "exemploGC.y"
{ System.out.println("\tPUSHL _"+val_peek(0).sval); }
break;
case 47:
//#line 211 "exemploGC.y"
{ gcExpNot(); }
break;
case 48:
//#line 212 "exemploGC.y"
{ 
									System.out.println("\tPOPL %EDX");
									System.out.println("\tMOVL %EDX, _"+val_peek(2).sval);
									System.out.println("\tPUSHL %EDX");
						 }
break;
case 49:
//#line 217 "exemploGC.y"
{
									System.out.println("\tPOPL %EBX");
									System.out.println("\tMOVL _"+val_peek(2).sval+", %EAX");
									System.out.println("\tADDL %EBX, %EAX");
									System.out.println("\tMOVL %EAX, _"+val_peek(2).sval);
									System.out.println("\tPUSHL %EAX");
								}
break;
case 50:
//#line 224 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
								System.out.println("\tINCL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
								System.out.println("\tPUSHL %EAX");
							}
break;
case 51:
//#line 230 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(0).sval+", %EAX");
								System.out.println("\tDECL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(0).sval);
								System.out.println("\tPUSHL %EAX");
							}
break;
case 52:
//#line 236 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
								System.out.println("\tPUSHL %EAX");
								System.out.println("\tINCL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
							}
break;
case 53:
//#line 242 "exemploGC.y"
{ 
								System.out.println("\tMOVL _"+val_peek(1).sval+", %EAX");
								System.out.println("\tPUSHL %EAX");
								System.out.println("\tDECL %EAX");
								System.out.println("\tMOVL %EAX, _"+val_peek(1).sval);
							}
break;
case 54:
//#line 248 "exemploGC.y"
{ 
								int off = ts.getFieldOffsetForVar(val_peek(2).sval, val_peek(0).sval);
								System.out.println("\tLEA _"+val_peek(2).sval+"+"+off+", %EDX");
								System.out.println("\tMOVL (%EDX), %EAX");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 55:
//#line 254 "exemploGC.y"
{
								int off = ts.getFieldOffsetForVar(val_peek(4).sval, val_peek(2).sval);
								System.out.println("\tPOPL %EDX\t# valor");
								System.out.println("\tLEA _"+val_peek(4).sval+"+"+off+", %ECX");
								System.out.println("\tMOVL %EDX, (%ECX)");
								System.out.println("\tPUSHL %EDX");
							}
break;
case 56:
//#line 261 "exemploGC.y"
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
case 57:
//#line 270 "exemploGC.y"
{
								/* rvalue acesso a[exp]*/
								System.out.println("\tPOPL %EAX");
								System.out.println("\tLEA _"+val_peek(3).sval+"(,%EAX,4), %EDX");
								System.out.println("\tMOVL (%EDX), %EAX");
								System.out.println("\tPUSHL %EAX");
							}
break;
case 58:
//#line 277 "exemploGC.y"
{
								/* atribuicao a[i] = exp*/
								System.out.println("\tPOPL %EDX\t# valor");
								System.out.println("\tPOPL %EAX\t# indice");
								System.out.println("\tLEA _"+val_peek(5).sval+"(,%EAX,4), %ECX");
								System.out.println("\tMOVL %EDX, (%ECX)");
								System.out.println("\tPUSHL %EDX");
							}
break;
case 59:
//#line 285 "exemploGC.y"
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
case 60:
//#line 295 "exemploGC.y"
{
								int base = proxRot; proxRot += 2;
								System.out.println("\tPOPL %EAX");
								System.out.println("\tCMPL $0, %EAX");
								System.out.printf("\tJE rot_%02d\n", base);
								pRot.push(base);
							}
break;
case 61:
//#line 302 "exemploGC.y"
{
								System.out.printf("\tJMP rot_%02d\n", pRot.peek()+1);
								System.out.printf("rot_%02d:\n", pRot.peek());
							}
break;
case 62:
//#line 306 "exemploGC.y"
{
										System.out.printf("rot_%02d:\n", pRot.peek()+1);
										pRot.pop();
									}
break;
case 63:
//#line 311 "exemploGC.y"
{ gcExpArit('+'); }
break;
case 64:
//#line 312 "exemploGC.y"
{ gcExpArit('-'); }
break;
case 65:
//#line 313 "exemploGC.y"
{ gcExpArit('*'); }
break;
case 66:
//#line 314 "exemploGC.y"
{ gcExpArit('/'); }
break;
case 67:
//#line 315 "exemploGC.y"
{ gcExpArit('%'); }
break;
case 68:
//#line 317 "exemploGC.y"
{ gcExpRel('>'); }
break;
case 69:
//#line 318 "exemploGC.y"
{ gcExpRel('<'); }
break;
case 70:
//#line 319 "exemploGC.y"
{ gcExpRel(EQ); }
break;
case 71:
//#line 320 "exemploGC.y"
{ gcExpRel(LEQ); }
break;
case 72:
//#line 321 "exemploGC.y"
{ gcExpRel(GEQ); }
break;
case 73:
//#line 322 "exemploGC.y"
{ gcExpRel(NEQ); }
break;
case 74:
//#line 324 "exemploGC.y"
{ gcExpLog(OR); }
break;
case 75:
//#line 325 "exemploGC.y"
{ gcExpLog(AND); }
break;
case 76:
//#line 329 "exemploGC.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 331 "exemploGC.y"
{ System.out.println("\tPOPL %EAX\t# descarta init do for"); }
break;
case 78:
//#line 332 "exemploGC.y"
{ /* nada */ }
break;
case 79:
//#line 335 "exemploGC.y"
{ /* valor da condicao no topo */ }
break;
case 80:
//#line 336 "exemploGC.y"
{ System.out.println("\tPUSHL $1\t# condicao vazia => true"); }
break;
case 81:
//#line 339 "exemploGC.y"
{ System.out.println("\tPOPL %EAX\t# descarta incremento do for"); }
break;
case 82:
//#line 340 "exemploGC.y"
{ /* nada */ }
break;
//#line 1380 "Parser.java"
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
