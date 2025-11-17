.text

#	 nome COMPLETO e matricula dos componentes do grupo...
#

.GLOBL _start


_start:
rot_01:
	PUSHL $1
	POPL %EAX   # desvia se falso...
	CMPL $0, %EAX
	JE rot_02
	MOVL $_str_0Len, %EDX
	MOVL $_str_0, %ECX
	CALL _writeLit
	CALL _writeln
	MOVL $_str_1Len, %EDX
	MOVL $_str_1, %ECX
	CALL _writeLit
	CALL _writeln
	PUSHL $_num
	CALL _read
	POPL %EDX
	MOVL %EAX, (%EDX)
	PUSHL _num
	POPL %EDX
	MOVL %EDX, _fim
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL $1
	POPL %EDX
	MOVL %EDX, _atual
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
rot_03:
	PUSHL _fim
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	PUSHL _atual
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EAX   # desvia se falso...
	CMPL $0, %EAX
	JE rot_04
	PUSHL _atual
	POPL %EDX
	MOVL %EDX, _num
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL $1
	POPL %EDX
	MOVL %EDX, _cont
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL $0
	POPL %EDX
	MOVL %EDX, _result
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
rot_05:
	PUSHL _num
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	PUSHL _cont
	POPL %EAX
	POPL %EDX
	CMPL %EAX, %EDX
	MOVL $0, %EAX
	SETG  %AL
	PUSHL %EAX
	POPL %EAX   # desvia se falso...
	CMPL $0, %EAX
	JE rot_06
	PUSHL _result
	PUSHL _cont
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _result
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
	PUSHL _cont
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _cont
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
		# terminou o bloco...
	JMP rot_05   # terminou cmd na linha de cima
rot_06:
	MOVL $_str_2Len, %EDX
	MOVL $_str_2, %ECX
	CALL _writeLit
	PUSHL _num
	POPL %EAX
	CALL _write
	CALL _writeln
	MOVL $_str_3Len, %EDX
	MOVL $_str_3, %ECX
	CALL _writeLit
	PUSHL _result
	POPL %EAX
	CALL _write
	CALL _writeln
	PUSHL _atual
	PUSHL $1
	POPL %EBX
	POPL %EAX
	ADDL %EBX, %EAX
	PUSHL %EAX
	POPL %EDX
	MOVL %EDX, _atual
	PUSHL %EDX
	POPL %EAX	# descarta resultado da expressao
		# terminou o bloco...
	JMP rot_03   # terminou cmd na linha de cima
rot_04:
		# terminou o bloco...
	JMP rot_01   # terminou cmd na linha de cima
rot_02:



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
_num:	.zero 4
_cont:	.zero 4
_result:	.zero 4
_atual:	.zero 4
_fim:	.zero 4

#
# area de literais
#
__msg:
	.zero 30
__fim_msg:
	.byte 0


_str_0:
	 .ascii " - - - - - - - - "
_str_0Len = . - _str_0
_str_1:
	 .ascii "Informe um numero: "
_str_1Len = . - _str_1
_str_2:
	 .ascii "Valor do somatorio de 1 a "
_str_2Len = . - _str_2
_str_3:
	 .ascii "    --> resultado: "
_str_3Len = . - _str_3
