# y	= 127x - 65z + 1
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
lw	$s1	, 0 	($t1)		# $s1	= MEM[0x10010000]	= x		= 5
lw	$s2	, 4 	($t1)		# $s2	= MEM[0x10010004]	= z		= 7

# Calcula 127x
sll	$t2	, $s1	, 7		# $t2	= x << 7		= 128x
sub	$t2	, $t2	, $s1		# $t2	= 128x - x		= 127x

# Calcula 65z
sll	$t3	, $s2	, 6		# $t3	= z << 6		= 64z
add	$t3	, $t3	, $s2		# $t3	= 64z + z		= 65z

# Calcula 127x - 65z + 1
sub	$t2	, $t2	, $t3		# $t2	= 127x - 65z
addi	$s3	, $t2	, 1		# $s3	= 127x - 65z + 1

# Guarda o resultado na variável "y" na memória
sw	$s3	, 8	($t1)		# MEM[0x10010010] = MEM[y]	= 127x - 65z + 1

.data
x: .word 5
z: .word 7
y: .word 0
