;************** AULA 01 - PROGRAMA PARA PISCAR LED *****************
LIST   P=PIC16F628A
RADIX DEC
#INCLUDE <P16F628A.INC>
	__CONFIG H'3F10'

	CBLOCK	0x20	;ENDEREÇO INICIAL DA MEMÓRIA DE
					;USUÁRIO
	CONTADOR1
	CONTADOR2
	CONTADOR3
	ENDC			;FIM DO BLOCO DE MEMÓRIA
	
	ORG	0x00		;ENDEREÇO INICIAL DE PROCESSAMENTO
	GOTO	INICIO
	
INICIO
	CLRF	PORTA		;LIMPA O PORTA
	CLRF	PORTB		;LIMPA O PORTB
	BSF STATUS, RP0
	CLRF TRISB
	BCF STATUS, RP0

REPETE
	BSF PORTB, 1
	CALL ATRASO1_HEADER
	BCF PORTB, 1
	CALL ATRASO1_HEADER
	GOTO REPETE


ATRASO1_HEADER ; header of for1
	MOVLW	220			; Move literal to accumulator
	MOVWF	CONTADOR1	; Move accumulator value to file
	
ATRASO1_BODY ; body of for1

	ATRASO2_HEADER ; header of for2
		MOVLW	208			; Move literal to accumulator
		MOVWF	CONTADOR2	; Move accumulator value to file
		
	ATRASO2_BODY ; body of for2
	
		ATRASO3_HEADER ; header of for3
			MOVLW	76			; Move literal to accumulator
			MOVWF	CONTADOR3	; Move accumulator value to file
			
			
		ATRASO3_BODY ; body of for3
			
			DECFSZ	CONTADOR3	; Decrement file, skip next line if 0
			GOTO	ATRASO3_BODY
		
		DECFSZ	CONTADOR2	; Decrement file, skip next line if 0
		GOTO	ATRASO2_BODY
		
	DECFSZ	CONTADOR1	; Decrement file, skip next line if 0
	GOTO	ATRASO1_BODY

	ATRASO4_HEADER ; header of for4
		MOVLW	48			; Move literal to accumulator
		MOVWF	CONTADOR2	; Move accumulator value to file
		
	ATRASO4_BODY ; body of for4
	
		ATRASO5_HEADER ; header of for5
			MOVLW	100			; Move literal to accumulator
			MOVWF	CONTADOR3	; Move accumulator value to file
			
			
		ATRASO5_BODY ; body of for5
			
			DECFSZ	CONTADOR3	; Decrement file, skip next line if 0
			GOTO	ATRASO5_BODY
		
		DECFSZ	CONTADOR2	; Decrement file, skip next line if 0
		GOTO	ATRASO4_BODY
	
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	NOP
	
	RETURN

END