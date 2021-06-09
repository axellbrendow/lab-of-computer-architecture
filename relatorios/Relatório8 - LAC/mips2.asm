#//programa 2 (add, addi, sub, lógicas)
#{
#	x = 1;
#	y = 5*x + 15;
#}
#
# associações:
# s4 = x
# s5 = y

.text
.globl main
main:
# inicialização das variáveis
ori $s4, $zero, 1	# x	= 1

# operações aritméticas para y
add $t0, $s4, $s4	# t0	= x + x
add $t0, $t0, $t0	# t0	= t0 + t0
add $t0, $t0, $s4	# t0	= t0 + x
addi $s5, $t0, 15	# y	= t0 + 15
