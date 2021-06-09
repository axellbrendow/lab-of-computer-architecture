#Implementar os seguintes programas (pode-se usar sll,
#srl e sra)
#// programa 4
#{
#	x = 3;
#	y = 4 ;
#	z = ( 15*x + 67*y)*4
#}
#
# associações:
# s4 = x
# s5 = y
# s6 = z

.text
.globl main
main:
# inicialização das variáveis
ori $s4, $zero, 3	# x	= 3
ori $s5, $zero, 4	# y	= 4

# operações aritméticas para 15x
add $t0, $s4, $s4	# t0	= x + x		= 2x
sll $t0, $t0, 3
sub $t0, $t0, $s4	# t0	= t0 - x	= 15x

# operações aritméticas para 67y
add $t1, $s5, $s5	# t1	= y + y		= 2y
add $t2, $t1, $s5	# t2	= t1 + y	= 3y
sll $t1, $t1, 5
add $t1, $t1, $t2	# t1	= t1 + t2	= 67y

# operações aritméticas para z
add $t0, $t0, $t1	# t0	= t0 + t1	= 15x + 67y
sll $s6, $t0, 2
