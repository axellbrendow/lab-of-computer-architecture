#// programa 3 (add, addi, sub, l�gicas)
#{
#	x = 3;
#	y = 4 ;
#	z = ( 15*x + 67*y)*4
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
ori $s4, $zero, 3	# x	= 3
ori $s5, $zero, 4	# y	= 4

# opera��es aritm�ticas para 15x
add $t0, $s4, $s4	# t0	= x + x		= 2x
add $t0, $t0, $t0	# t0	= t0 + t0	= 4x
add $t0, $t0, $t0	# t0	= t0 + t0	= 8x
add $t0, $t0, $t0	# t0	= t0 + t0	= 16x
sub $t0, $t0, $s4	# t0	= t0 - x	= 15x

# opera��es aritm�ticas para 67y
add $t1, $s5, $s5	# t1	= y + y		= 2y
add $t2, $t1, $s5	# t2	= t1 + y	= 3y
add $t1, $t1, $t1	# t1	= t1 + t1	= 4y
add $t1, $t1, $t1	# t1	= t1 + t1	= 8y
add $t1, $t1, $t1	# t1	= t1 + t1	= 16y
add $t1, $t1, $t1	# t1	= t1 + t1	= 32y
add $t1, $t1, $t1	# t1	= t1 + t1	= 64y
add $t1, $t1, $t2	# t1	= t1 + t2	= 67y

# opera��es aritm�ticas para z
add $t0, $t0, $t1	# t0	= t0 + t1	= 15x + 67y
add $t0, $t0, $t0	# t0	= t0 + t0	= (15x + 67y) * 2
add $s6, $t0, $t0	# s6	= t0 + t0	= (15x + 67y) * 4
