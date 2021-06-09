# programa 15
# $s0	= contador
# $s1	= end. base
# $s2	= copia end. base
# $s3	= soma
# $s4	= (const) 100
# $t0	= end. base[contador]

.text
.globl main

main:
lui	$s1	, 0x1001		# calcula o endereco base
add	$s2	, $s1	, $zero		# copia end. base = end. base
addi	$s0	, $zero	, 0		# contador = 0
add	$s3	, $zero	, $zero		# soma = 0
addi	$s4	, $zero , 100		# $s4 = (const) 100

dowhile:
add	$t0	, $s0	, $s0		# $t0 = 2 * contador
addi	$t0	, $t0	, 1		# $t0 = 2 * contador + 1
sw	$t0	, 0	($s2)		# *copia end. base = 2 * contador + 1
lw	$t0	, 0	($s2)		# $t0 = contador = *copia end. base
addi	$s2	, $s2	, 4		# copia end. base = copia end. base + 4
addi	$s0	, $s0	, 1		# contador = contador + 1
add	$s3	, $s3	, $t0		# soma = soma + 2 * contador + 1
bne	$s0	, $s4	, dowhile	# jump to do: if contador != 0

sw	$s3	, 0	($s2)		# *copia end. base = soma