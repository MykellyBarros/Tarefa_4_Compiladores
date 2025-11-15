.text

#	 nome COMPLETO e matricula dos componentes do grupo...
#

.GLOBL _start


_start:
	PUSHL $10
	POPL %EDX	# valor
	LEA _p+0, %ECX
	MOVL %EDX, (%ECX)
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL $20
	POPL %EDX	# valor
	LEA _p+4, %ECX
	MOVL %EDX, (%ECX)
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	MOVL $_str_0Len, %EDX
	MOVL $_str_0, %ECX
	CALL _writeLit
	LEA _p+0, %EDX
	MOVL (%EDX), %EAX
	PUSHL %EAX
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_1Len, %EDX
	MOVL $_str_1, %ECX
	CALL _writeLit
	LEA _p+4, %EDX
	MOVL (%EDX), %EAX
	PUSHL %EAX
	POPL %EAX
	CALL _write
	CALL _writeln
	LEA _p+4, %EDX
	MOVL (%EDX), %EAX
	PUSHL %EAX
	POPL %EBX	# valor
	LEA _p+0, %ECX
	MOVL (%ECX), %EAX
	ADDL %EBX, %EAX
	MOVL %EAX, (%ECX)
	PUSHL %EAX
	POPL %EAX	# descarta resultado da expressao
	MOVL $_str_2Len, %EDX
	MOVL $_str_2, %ECX
	CALL _writeLit
	LEA _p+0, %EDX
	MOVL (%EDX), %EAX
	PUSHL %EAX
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
_p:	.zero 8

#
# area de literais
#
__msg:
	.zero 30
__fim_msg:
	.byte 0


_str_0:
	 .ascii "px= "
_str_0Len = . - _str_0
_str_1:
	 .ascii "py= "
_str_1Len = . - _str_1
_str_2:
	 .ascii "px= "
_str_2Len = . - _str_2
