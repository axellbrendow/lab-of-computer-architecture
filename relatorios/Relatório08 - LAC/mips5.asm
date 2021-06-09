#// programa 5
#{
#	x = 100000; #0x000186a0
#	y = 200000; #0x00030D40
#	z = x + y;
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
ori $s4, $zero, 0x186a	# x	= 0x0000186a
sll $s4, $s4, 4		# x	= 0x000186a0
ori $s5, $zero, 0x30D4	# y	= 0x000030D4
sll $s5, $s5, 4		# y	= 0x00030D40

# operações aritméticas para z
add $s6, $s4, $s5	# z	= x + y		= 0x000493e0
