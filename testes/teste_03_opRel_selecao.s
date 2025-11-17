.text

#	 nome COMPLETO e matricula dos componentes do grupo...
#

.GLOBL _start


_start:
	MOVL $_str_0Len, %EDX
	MOVL $_str_0, %ECX
	CALL _writeLit
	CALL _writeln
	PUSHL $_a
	CALL _read
	POPL %EDX
	MOVL %EAX, (%EDX)
	PUSHL $_b
	CALL _read
	POPL %EDX
	MOVL %EAX, (%EDX)
	PUSHL $_c
	CALL _read
	POPL %EDX
	MOVL %EAX, (%EDX)
	MOVL $_str_1Len, %EDX
	MOVL $_str_1, %ECX
	CALL _writeLit
	PUSHL _a
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_2Len, %EDX
	MOVL $_str_2, %ECX
	CALL _writeLit
	PUSHL _b
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_3Len, %EDX
	MOVL $_str_3, %ECX
	CALL _writeLit
	PUSHL _c
	POPL %EAX
	CALL _write
	CALL _writeln
	PUSHL _a
	PUSHL _b
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _p1
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL _a
	PUSHL _c
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _p2
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL _p1
	PUSHL _p2
	POPL %EDX
	POPL %EAX
	CMPL $0, %EAX
	MOVL $0, %EAX
	SETNE %AL
	CMPL $0, %EDX
	MOVL $0, %EDX
	SETNE %DL
	ANDL  %EDX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _ok
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	MOVL $_str_4Len, %EDX
	MOVL $_str_4, %ECX
	CALL _writeLit
	PUSHL _p1
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_5Len, %EDX
	MOVL $_str_5, %ECX
	CALL _writeLit
	PUSHL _p2
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_6Len, %EDX
	MOVL $_str_6, %ECX
	CALL _writeLit
	PUSHL _ok
	POPL %EAX
	CALL _write
	CALL _writeln
	PUSHL _ok
	POPL %EAX
	CMPL $0, %EAX
	JE rot_01
	MOVL $_str_7Len, %EDX
	MOVL $_str_7, %ECX
	CALL _writeLit
	PUSHL _ok
	POPL %EAX
	CALL _write
	CALL _writeln
	JMP rot_02
rot_01:
	MOVL $_str_8Len, %EDX
	MOVL $_str_8, %ECX
	CALL _writeLit
	PUSHL _ok
	POPL %EAX
	CALL _write
	CALL _writeln
rot_02:
	PUSHL _a
	PUSHL $0
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EAX
	CMPL $0, %EAX
	JE rot_03
	MOVL $_str_9Len, %EDX
	MOVL $_str_9, %ECX
	CALL _writeLit
	CALL _writeln
	JMP rot_04
rot_03:
	MOVL $_str_10Len, %EDX
	MOVL $_str_10, %ECX
	CALL _writeLit
	CALL _writeln
rot_04:
	PUSHL _ok
	POPL %EAX
	CMPL $0, %EAX
	JE rot_05
	PUSHL _a
	POPL %EDX
	MOVL %EDX, _maior
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	JMP rot_06
rot_05:
	PUSHL _b
	PUSHL _c
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EAX
	CMPL $0, %EAX
	JE rot_07
	PUSHL _b
	POPL %EDX
	MOVL %EDX, _maior
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	JMP rot_08
rot_07:
	PUSHL _c
	POPL %EDX
	MOVL %EDX, _maior
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
rot_08:
rot_06:
	MOVL $_str_11Len, %EDX
	MOVL $_str_11, %ECX
	CALL _writeLit
	PUSHL _maior
	POPL %EAX
	CALL _write
	CALL _writeln



#
# devolve o controle para o SO (final da main)
#
	mov $0, %ebx
	mov $1, %eax
	int $0x80


#
# Funcoes da biblioteca (IO)
#


_writeln:
	MOVL $__fim_msg, %ECX
	DECL %ECX
	MOVB $10, (%ECX)
	MOVL $1, %EDX
	JMP _writeLit
_write:
	MOVL $__fim_msg, %ECX
	MOVL $0, %EBX
	CMPL $0, %EAX
	JGE _write3
	NEGL %EAX
	MOVL $1, %EBX
_write3:
	PUSHL %EBX
	MOVL $10, %EBX
_divide:
	MOVL $0, %EDX
	IDIVL %EBX
	DECL %ECX
	ADD $48, %DL
	MOVB %DL, (%ECX)
	CMPL $0, %EAX
	JNE _divide
	POPL %EBX
	CMPL $0, %EBX
	JE _print
	DECL %ECX
	MOVB $'-', (%ECX)
_print:
	MOVL $__fim_msg, %EDX
	SUBL %ECX, %EDX
_writeLit:
	MOVL $1, %EBX
	MOVL $4, %EAX
	int $0x80
	RET
_read:
	MOVL $15, %EDX
	MOVL $__msg, %ECX
	MOVL $0, %EBX
	MOVL $3, %EAX
	int $0x80
	MOVL $0, %EAX
	MOVL $0, %EBX
	MOVL $0, %EDX
	MOVL $__msg, %ECX
	CMPB $'-', (%ECX)
	JNE _reading
	INCL %ECX
	INC %BL
_reading:
	MOVB (%ECX), %DL
	CMP $10, %DL
	JE _fimread
	SUB $48, %DL
	IMULL $10, %EAX
	ADDL %EDX, %EAX
	INCL %ECX
	JMP _reading
_fimread:
	CMPB $1, %BL
	JNE _fimread2
	NEGL %EAX
_fimread2:
	RET



#
# area de dados
#
.data
#
# variaveis globais
#
_a:	.zero 4
_b:	.zero 4
_c:	.zero 4
_maior:	.zero 4
_ok:	.zero 4
_p1:	.zero 4
_p2:	.zero 4

#
# area de literais
#
__msg:
	.zero 30
__fim_msg:
	.byte 0


_str_0:
	 .ascii "Informe 3 numeros: "
_str_0Len = . - _str_0
_str_1:
	 .ascii " a =  "
_str_1Len = . - _str_1
_str_2:
	 .ascii " b =  "
_str_2Len = . - _str_2
_str_3:
	 .ascii " c =  "
_str_3Len = . - _str_3
_str_4:
	 .ascii " p1 =  "
_str_4Len = . - _str_4
_str_5:
	 .ascii " p2 =  "
_str_5Len = . - _str_5
_str_6:
	 .ascii " ok =  "
_str_6Len = . - _str_6
_str_7:
	 .ascii " ok =  "
_str_7Len = . - _str_7
_str_8:
	 .ascii " nao ok =  "
_str_8Len = . - _str_8
_str_9:
	 .ascii "a eh um numero positivo"
_str_9Len = . - _str_9
_str_10:
	 .ascii "a NAO eh um numero positivo"
_str_10Len = . - _str_10
_str_11:
	 .ascii "maior dos tres valores: "
_str_11Len = . - _str_11
