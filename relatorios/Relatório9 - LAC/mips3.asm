# $s1	= x
# $s2	= z
# $s3	= y
# $t1	= end. base memória

.text
.globl teste
teste:
# Coloca o endereco base da memória no registrador $t1.
ori	$t1	, $zero	, 0x1001	# $t1	= $zero | 0x1001	= 0x00001001
sll	$t1	, $t1	, 16		# $t1	= $t1 << 16		= 0x10010000

# Carrega os registrados $s1, $s2 com os valores.
lw	$s1	, 0 	($t1)		# $s1	= MEM[0x10010000]	= x		= 100000
lw	$s2	, 4 	($t1)		# $s2	= MEM[0x10010004]	= z		= 200000

# Gera o valor 300000 = 0x000493E0
ori	$t2	, $zero	, 0x0004	# $t2	= 0x00000004
sll	$t2	, $t2	, 16		# $t2	= $t2 << 16		= 0x00040000
ori	$t2	, $t2	, 0x93E0	# $t2	= $t2 | 0x93E0		= 0x000493E0

add	$t2	, $t2	, $s1		# $t2	= $t2 + x		= 300000 + 100000	= 400000
sub	$s3	, $t2	, $s2		# $t2	= $t2 - z		= 400000 - 200000	= 200000

sw	$s3	, 8	($t1)		# MEM[0x10010008]		= y			= 200000


.data
x: .word 100000
z: .word 200000
y: .word 0
