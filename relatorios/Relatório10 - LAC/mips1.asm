# // programa 13:
# Escreva um programa que leia um valor A da memória, identifique se o número é negativo ou não e encontre o seu módulo.
# O valor deverá ser reescrito sobre A.
#
# $s0 = A

.text
.globl main

main:
lui	$t1	, 0x1001		# Coloca o endereco base da memória no registrador $t1.

lw	$s0	, 0	($t1)		# $s0		= MEM[0x10010000]	= A

slt 	$t2	, $s0	, $zero		# $t2		= A < 0 ? 1 : 0
beq	$t2	, $zero	, save_A	# A >= 0 ?	jump save_A
sub	$s0	, $zero	, $s0		# $t2		= 0 - A = -A

save_A:
sw	$s0	, 0	($t1)		# MEM[0x10010000] = |A|

.data
A:	.word	-2			# endereco = 0x10010000
