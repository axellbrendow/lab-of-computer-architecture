# programa 14
# base	= $s0
# temp	= $s1

.text
.globl main

main:
lui	$s0	, 0x1001		# calcula o endereco $s0

lw	$s1	, 0	($s0)		# carrega o primeiro valor na memoria

slti	$t0	, $s1	, 30		# $t0 = ( $s1 < 30 ? 1 : 0 )
bne	$t0	, $zero	, flag0		# vai para flag0 se $t0 != 0, ou seja, se $t0 == 1, ou seja, $s1 < 30
slti	$t0	, $s1	, 51		# $t0 = ( $s1 < 51 ? 1 : 0 )
bne	$t0	, $zero	, flag1		# vai para flag1 se $t0 != 0, ou seja, se $t0 == 1, ou seja, $s1 < 51

flag0:
add	$t0	, $zero	, $zero
j store_flag

flag1:
addi	$t0	, $zero	, 1

store_flag:
sw	$t0	, 4	($s0)

.data
s1:	.word	30