#// programa 6
#{
#	x = o maior inteiro poss�vel;
#	y = 300000;
#	z = x - 4y
#}
#
# associa��es:
# s4 = x
# s5 = y
# s6 = z

.text
.globl main
main:
# inicializa��o das vari�veis
# vari�vel x
ori $s4, $zero, 0x0111	# x	= 0x00000111
sll $s4, $s4, 16	# x	= 0x01110000
ori $s4, $s4, 0x1111	# x	= 0x01111111

# vari�vel y
ori $s5, $zero, 0x493e	# y	= 0x0000493e
sll $s5, $s5, 4		# y	= 0x000493e0

# opera��es aritm�ticas para 4y
sll $t0, $s5, 2		# t0	= y * 4		= 0x00124F80

# opera��es aritm�ticas para z
sub $s6, $s4, $t0	# z	= x - 4y	= 0x7FEDB07F
