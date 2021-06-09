#//programa 1 (add, addi, sub, l�gicas)
#{
#	a =2;
#	b =3;
#	c =4;
#	d =5;
#	x = (a+b) - (c+d);
#	y = a � b + x;
#	b = x � y;
#}
#
# associa��es:
# s0 = a
# s1 = b
# s2 = c
# s3 = d
# s4 = x
# s5 = y

.text
.globl main
main:
# inicializa��o das vari�veis
ori $s0, $zero, 2 # a	= 2
ori $s1, $zero, 3 # b	= 3
ori $s2, $zero, 4 # c	= 4
ori $s3, $zero, 5 # d	= 5

# opera��es aritm�ticas para x
add $t0, $s0, $s1 # t0	= a + b
sub $t0, $t0, $s2 # t0	= t0 - c
sub $s4, $t0, $s3 # x	= t0 - d

# opera��es aritm�ticas para y
sub $t0, $s0, $s1 # t0	= a - b
add $s5, $t0, $s4 # y	= t0 + x

# opera��es aritm�ticas para b
sub $s1, $s4, $s5 # b	= x - y