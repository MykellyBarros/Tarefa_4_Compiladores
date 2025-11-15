.text

#	 nome COMPLETO e matricula dos componentes do grupo...
#

.GLOBL _start


_start:
	PUSHL $0
	POPL %EDX
	MOVL %EDX, _i
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
rot_01:
	PUSHL _i
	PUSHL $5
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETL  %AL
	PUSHL %EAX
	POPL %EAX   # desvia se falso...
	CMPL $0, %EAX
	JE rot_02
	PUSHL _i
	PUSHL _i
	PUSHL $10
	POPL %EBX
	POPL %EAX
	IMULL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX	# valor
	POPL %EAX	# indice
	LEA _a(,%EAX,4), %ECX
	MOVL %EDX, (%ECX)
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL _i
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _i
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
		# terminou o bloco...
	JMP rot_01   # terminou cmd na linha de cima
rot_02:
	PUSHL $0
	POPL %EDX
	MOVL %EDX, _i
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL $0
	POPL %EDX
	MOVL %EDX, _v
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
rot_03:
	PUSHL _i
	PUSHL $5
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETL  %AL
	PUSHL %EAX
	POPL %EAX   # desvia se falso...
	CMPL $0, %EAX
	JE rot_04
	PUSHL _i
	POPL %EAX
	LEA _a(,%EAX,4), %EDX
	MOVL (%EDX), %EAX
	PUSHL %EAX
	POPL %EBX
	MOVL _v, %EAX
	ADDL %EBX, %EAX
	MOVL %EAX, _v
	PUSHL %EAX
	POPL %EAX	# descarta resultado da expressao
	PUSHL _i
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _i
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
		# terminou o bloco...
	JMP rot_03   # terminou cmd na linha de cima
rot_04:
	MOVL $_str_0Len, %EDX
	MOVL $_str_0, %ECX
	CALL _writeLit
	PUSHL _v
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
_a:	.zero 20
_i:	.zero 4
_v:	.zero 4

#
# area de literais
#
__msg:
	.zero 30
__fim_msg:
	.byte 0


_str_0:
	 .ascii "sum= "
_str_0Len = . - _str_0
