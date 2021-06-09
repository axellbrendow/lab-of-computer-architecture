#// programa 7
# Considere a seguinte instrução iniciando um programa:
# ori $8, $0, 0x01
# Usando apenas instruções reg-reg lógicas e instruções de deslocamento (sll,
# srl e sra), continuar o programa de forma que ao final, tenhamos o seguinte
# conteúdo no registrador $8:
# $8 = 0xFFFFFFFF

.text
.globl main
main:
ori $8, $0, 0x01	# $8	= $0 | 0x00000001	= 00000000000000000000000000000001
sll $t1, $8, 1		# $t1	= $8 << 1		= 00000000000000000000000000000010
or $t1, $t1, $8		# $t1	= $t1 | $8		= 00000000000000000000000000000011
sll $t2, $t1, 2		# $t2	= $t1 << 2		= 00000000000000000000000000001100
or $t1, $t2, $t1	# $t1	= $t2 | $t1		= 00000000000000000000000000001111
sll $t2, $t1, 4		# $t2	= $t1 << 4		= 00000000000000000000000011110000
or $t1, $t2, $t1	# $t1	= $t2 | $t1		= 00000000000000000000000011111111
sll $t2, $t1, 8		# $t2	= $t1 << 8		= 00000000000000001111111100000000
or $t1, $t2, $t1	# $t1	= $t2 | $t1		= 00000000000000001111111111111111
sll $t2, $t1, 16	# $t2	= $t1 << 16		= 11111111111111110000000000000000
or $8, $t2, $t1		# $8	= $t2 | $t1		= 11111111111111111111111111111111
