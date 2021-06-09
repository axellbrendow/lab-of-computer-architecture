# $s1	= x1
# $s2	= x2
# $s3	= x3
# $s4	= x4
# $t1	= end. base memória

.text
.globl teste
teste:
# Coloca o endereco base da memória no registrador $t1.
ori	$t1	, $zero	, 0x1001	# $t1	= $zero | 0x1001	= 0x00001001
sll	$t1	, $t1	, 16		# $t1	= $t1 << 16		= 0x10010000

# Carrega os registrados $s1, $s2, $s3 e $s4 com os quatro primeiros valores.
lw	$s1	, 0 	($t1)		# $s1	= MEM[0x10010000]	= 15
lw	$s2	, 4 	($t1)		# $s2	= MEM[0x10010004]	= 25
lw	$s3	, 8 	($t1)		# $s3	= MEM[0x10010008]	= 13
lw	$s4	, 12 	($t1)		# $s4	= MEM[0x1001000C]	= 17

# Soma os valores
add	$s5	, $s1	, $s2		# $s5	= 15 + 25		= 40
add	$s5	, $s5	, $s3		# $s5	= 40 + 13		= 53
add	$s5	, $s5	, $s4		# $s5	= 53 + 17		= 70

# Guarda o resultado na "variável" soma na memória
sw	$s5	, 16	($t1)		# MEM[0x10010010] = 70

.data
x1: .word 15
x2: .word 25
x3: .word 13
x4: .word 17
soma: .word -1