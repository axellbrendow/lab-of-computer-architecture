#// programa 8
# Inicialmente escreva um programa que fa�a:
# $8 = 0x12345678.
# A partir do registrador $8 acima, usando apenas instru��es l�gicas (or, ori,
# and, andi, xor, xori) e instru��es de deslocamento (sll, srl e sra), voc�
# dever� obter os seguintes valores nos respectivos registradores:
# $9 = 0x12
# $10 = 0x34
# $11 = 0x56
# $12 = 0x78

.text
.globl main
main:
# inicializa��o das vari�veis
ori $8, $zero, 0x1234	# $8	= $0 | 0x1234	= 0x00001234
sll $8, $8, 16		# $8	= $8 << 16	= 0x12340000
ori $8, $8, 0x5678	# $8	= $8 | 0x5678	= 0x12345678

# extra��o para o registrador $9
srl $t5, $8, 24		# $t5	= $8 >> 24	= 0x00000012
andi $9, $t5, 0x00FF	# $9	= $t5 | 0x00FF	= 0x00000012

# extra��o para o registrador $10
srl $t5, $8, 16		# $t5	= $8 >> 16	= 0x00001234
andi $10, $t5, 0x00FF	# $10	= $t5 | 0x00FF	= 0x00000034

# extra��o para o registrador $11
srl $t5, $8, 8		# $t5	= $8 >> 8	= 0x00123456
andi $11, $t5, 0x00FF	# $11	= $t5 | 0x00FF	= 0x00000056

# extra��o para o registrador $12
srl $t5, $8, 0		# $t5	= $8 >> 0	= 0x12345678
andi $12, $t5, 0x00FF	# $12	= $t5 | 0x00FF	= 0x00000078
