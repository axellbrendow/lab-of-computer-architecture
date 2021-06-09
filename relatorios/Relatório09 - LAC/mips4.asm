# $t1 = endereco base. Posteriormente será = k. Depois 2k.
# $t2 = ponteiros

.text
.globl teste
teste:

# Coloca o endereco base da memória no registrador $t1.
ori	$t1	, $zero	, 0x1001	# $t1		= $zero | 0x1001	= 0x00001001
sll	$t1	, $t1	, 16		# $t1		= $t1 << 16		= 0x10010000

lw	$t2	, 12	($t1)		# $t2		= MEM[&ppp_k]		= ppp_k		= &pp_k
lw	$t2	, 0	($t2)		# $t2		= MEM[&pp_k]		= pp_k		= &p_k
lw	$t2	, 0	($t2)		# $t2		= MEM[&p_k]		= p_k		= &k
lw	$t1	, 0	($t2)		# $t1		= MEM[$k]		= k		= 4

sll	$t1	, $t1	, 1		# $t1		= $t1 << 1		= 2k		= 8

sw	$t1	, 0	($t2)		# MEM[$k]	= $t1			= 2k		= 8

.data
k:	.word	4			# endereco = 0x10010000
p_k:	.word	0x10010000		# endereco = 0x10010004
pp_k:	.word	0x10010004		# endereco = 0x10010008
ppp_k:	.word	0x10010008		# endereco = 0x1001000C