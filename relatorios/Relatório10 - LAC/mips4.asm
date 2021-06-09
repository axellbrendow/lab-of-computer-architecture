# programa 16
# Algoritmo de ordenação da bolha
# 
# $s1	= &A[0]

.text
.globl main # rótulo onde o programa começa
j main

bubblesort: #($a0 = end. base, $a1 = inicio, $a2 = numero de elementos a serem ordenados)
# $t9	= numero de elementos ordenados
# $t8	= endereco da bolha
# $t7	= indice final da ordenacao
# $t6	= indice da bolha
# $t5	= registrador para set on less than
# $t4	= elemento da bolha
# $t3	= elemento 'a esquerda da bolha (IMAGINANDO UM ARRANJO HORIZONTAL)
add	$t9	, $zero	, $zero		# numero de elementos ordenados
add	$t7	, $a1	, $a2		# indice final
addi	$t7	, $t7	, -1		# indice final

while:
	beq	$t9	, $a2	, end		# vai para end se ( numero de elementos ordenados == quantidade desejada (2º param) )
	add	$t6	, $zero	, $t7		# indice bolha = indice final de ordenacao
	
	for:
		slt	$t5	, $t9	, $t6		# $t5 = ( numero de elementos ordenados < indice bolha ) ? 1 : 0
		beq	$t5	, $zero	, endfor	# vai para endfor se ( numero de elementos ordenados >= bolha )
		sll	$t8	, $t6	, 2		# multiplica o indice por 4
		addi	$t6	, $t6	, -1		# indice bolha = indice bolha - 1
		add	$t8	, $a0	, $t8		# endereco bolha = endereco base + deslocamento
		lw	$t4	, 0	($t8)		# elemento da bolha = end. base[indice bolha]
		lw	$t3	, -4	($t8)		# elemento 'a esquerda da bolha = end. base[indice bolha - 1]
		slt	$t5	, $t4	, $t3		# $t5 = ( elem_bolha < elem_a_esq_bolha ) ? 1 : 0
		beq	$t5	, $zero	, for		# vai para for se ( elem_bolha >= elem_a_esq_bolha )
		sw	$t4	, -4	($t8)		# swap
		sw	$t3	, 0	($t8)		# swap
		
		j for
	endfor:
	
	addi	$t9	, $t9	, 1		# numero de elementos ordenados++
	j while
end:
jr $ra

main:

lui	$s1	, 0x1001		# calcula o endereco base
add	$s2	, $s1	, $zero		# copia end. base = end. base
addi	$s0	, $zero	, 0		# contador = 0
add	$s3	, $zero	, $zero		# soma = 0
addi	$s4	, $zero , 100		# $s4 = (const) 100

dowhile:
add	$t0	, $s0	, $s0		# $t0 = 2 * contador
sub	$t0	, $zero	, $t0		# $t0 = -2 * contador
addi	$t0	, $t0	, 1		# $t0 = -2 * contador + 1
sw	$t0	, 0	($s2)		# *copia end. base = 2 * contador + 1
lw	$t0	, 0	($s2)		# $t0 = contador = *copia end. base
addi	$s2	, $s2	, 4		# copia end. base = copia end. base + 4
addi	$s0	, $s0	, 1		# contador = contador + 1
add	$s3	, $s3	, $t0		# soma = soma + 2 * contador + 1
bne	$s0	, $s4	, dowhile	# jump to do: if contador != 0

lui	$a0	, 0x1001		# calcula o endereco base
addi	$a1	, $zero	,	0	# indice inicial
addi	$a2	, $zero	,	100	# numero de elementos a serem ordenados
jal bubblesort					# ordena-os

lw	$t1	, 4	($a0)		# pega o elemento do meio
sw	$t1	, 12	($a0)		# guarda-o apos o vetor ordenado
