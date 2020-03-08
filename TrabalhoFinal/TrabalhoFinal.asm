;#INCLUDE <P16F628A.INC>		;ARQUIVO PADRÃO MICROCHIP PARA 16F628A
;;RADIX DEC
;__CONFIG _CP_OFF & _LVP_OFF & _BOREN_OFF & _MCLRE_ON & _WDT_OFF & _PWRTE_ON & _INTOSC_OSC_NOCLKOUT
;;__CONFIG H'3F10'
;
;; O BOTAO SERÁ CONECTADO NO TERCEIRO BIT DE PORTA
;#DEFINE BTN PORTA, 2
;#DEFINE LED_C PORTB, 5 ; LED CONTROLE
;#DEFINE LED_A4 PORTB, 4 ; LED ANDAR 4
;#DEFINE LED_A3 PORTB, 3 ; LED ANDAR 3
;#DEFINE LED_A2 PORTB, 2 ; LED ANDAR 2
;#DEFINE LED_A1 PORTB, 1 ; LED ANDAR 1
;#DEFINE LED_T PORTB, 0 ; LED TÉRREO
;
;	; AS VARIÁVEIS ABAIXO SERÃO RESERVADAS A
;	; PARTIR DO ENDEREÇO 0X20, QUE É UM ENDEREÇO
;	; DO BANCO ZERO DO PIC
;	CBLOCK	0x20
;	CONTADOR1
;	CONTADOR2
;	CONTADOR3
;	CONTADOR_DE_DELAYS
;	ENDC	;FIM DO BLOCO DE MEMÓRIA
;	
;	; AS INSTRUÇÕES ABAIXO SERÃO COLOCADAS A
;	; PARTIR DO ENDEREÇO DE MEMÓRIA 0X00. É
;	; IMPORTANTE NOTAR QUE A ARQUITETURA DO PIC
;	; É DE HARVARD, OU SEJA, O ENDEREÇO 0X20 DA
;	; DIRETIVA CBLOCK SE REFERE A UMA MEMÓRIA
;	; DIFERENTE DO ENDEREÇO 0X00 DA DIRETIVA ORG.
;	ORG	0x00
;	; ESTA INSTRUÇÃO FICARÁ NO PRIMEIRO ENDEREÇO
;	; DA MEMÓRIA PARA PROGRAMA. CASO QUEIRA
;	; VISUALIZAR A MEMÓRIA PARA PROGRAMA DO PIC:
;	; (View -> Program Memory -> Symbolic)
;	GOTO	INICIO
;
;	; LINKS ÚTEIS:
;	; https://en.wikipedia.org/wiki/PIC_instruction_listings
;	; https://pictutorials.com/The_Status_Register.htm
;	; DATASHEET:
;	; http://web.mit.edu/6.115/www/document/16f628.pdf
;
;INICIO
;	CLRF	PORTA		; LIMPA O PORTA
;	CLRF	PORTB		; LIMPA O PORTB
;	; RP0 = REGISTER PAGE Bit 0
;	; RP0 É UM BIT DE SELEÇÃO DE BANCO DE MEMÓRIA
;	; RP0 É O SEXTO BIT DO REGISTRADOR STATUS
;	; "BSF STATUS, RP0" É IGUAL A "BSF, STATUS, 5"
;	; QUE EQUIVALE A COLOCAR O SEXTO BIT DE STATUS
;	; COMO 1 (BSF = BIT SET FILE)
;	;          B7	B6	B5	B4	B3	B2	B1	B0
;	; STATUS = IRP	RP1	RP0	TO	PD	Z	DC	C
;	; B6 E B5 SERVEM PARA SELECIONAR UM DOS 4
;	; BANCOS DE MEMÓRIA.
;	; Z = ZERO FLAG
;	; DC = DIGIT CARRY
;	; C = CARRY
;	BSF STATUS, RP0
;	; AGORA, OS BITS DE SELEÇÃO DO BANCO DE
;	; MEMÓRIA FORMAM O NUMERO 01, POIS
;	; RP1 = 0 (ESSE É O BIT 6 DO STATUS)
;	; RP0 = 1 (BSF LIGOU O BIT RP0 (BIT 5))
;	; QUE EQUIVALE A ACESSAR O BANCO DE MEMÓRIA 01
;
;	; CLRF LIMPA O REGISTRADOR TRISB.
;	; O REGISTRADOR TRISB ESTÁ NO BANCO 1
;	CLRF TRISB
;	CLRF TRISA
;	BSF TRISA, 2
;	CLRF INTCON ; TODAS AS INTERRUPÇÕES DESLIGADAS
;
;	; BCF LIMPA O BIT RP0(5) DO REGISTRADOR STATUS,
;	; OU SEJA, VOLTAMOS AO BANCO DE MEMÓRIA 00.
;	BCF STATUS, RP0
;	MOVLW B'00000111'
;	MOVWF CMCON ; DESATIVA O COMPARADOR ANALÓGICO
;
;CICLO_NORMAL
;	CALL LIGAR_LED_T
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_T
;
;	CALL LIGAR_LED_A1
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A1
;
;	CALL LIGAR_LED_A2
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A2
;
;	CALL LIGAR_LED_A3
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A3
;
;	CALL LIGAR_LED_A4
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A4
;
;	CALL LIGAR_LED_A4
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A4
;
;	CALL LIGAR_LED_A3
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A3
;
;	CALL LIGAR_LED_A2
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A2
;
;	CALL LIGAR_LED_A1
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_A1
;
;	CALL LIGAR_LED_T
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DELAY_1SEC ; 1 second delay with event trigger
;	CALL DESLIGAR_LED_T
;
;GOTO CICLO_NORMAL
;
;DELAY_1SEC
;ATRASO1_HEADER_FOR1 ; header of for1
;	MOVLW	21			; Move literal to accumulator
;	MOVWF	CONTADOR1	; Move accumulator value to file
;	
;ATRASO1_BODY_FOR1 ; body of for1
;	
;	ATRASO1_HEADER_FOR2 ; header of for2
;		BTFSS	BTN ; Skip next line if BTN = 1
;		GOTO	CICLO_EMERGENCIA ; Goto if BTN = 1
;		MOVLW	20			; Move literal to accumulator
;		MOVWF	CONTADOR2	; Move accumulator value to file
;
;	ATRASO1_BODY_FOR2 ; body of for2
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		ATRASO1_HEADER_FOR3 ; header of for3
;			MOVLW	255			; Move literal to accumulator
;			MOVWF	CONTADOR3	; Move accumulator value to file
;			
;		ATRASO1_BODY_FOR3 ; body of for3
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			DECFSZ	CONTADOR3	; Decrement file, skip next line if 0
;			GOTO	ATRASO1_BODY_FOR3
;		
;		DECFSZ	CONTADOR2	; Decrement file, skip next line if 0
;		GOTO	ATRASO1_BODY_FOR2
;		
;	DECFSZ	CONTADOR1	; Decrement file, skip next line if 0
;	GOTO	ATRASO1_BODY_FOR1
;	
;	BTFSS LED_C ; skip next line if LED_C = 1
;	GOTO LIGAR_LED_C_ATRASO1
;	GOTO DESLIGAR_LED_C_ATRASO1
;MYRETURN_ATRASO1
;RETURN
;
;LIGAR_LED_C_ATRASO1
;	BSF LED_C
;	GOTO MYRETURN_ATRASO1
;
;DESLIGAR_LED_C_ATRASO1
;	BCF LED_C
;	GOTO MYRETURN_ATRASO1
;
;LIGAR_LED_A4
;	BSF LED_A4
;RETURN
;
;DESLIGAR_LED_A4
;	BCF LED_A4
;RETURN
;
;LIGAR_LED_A3
;	BSF LED_A3
;RETURN
;
;DESLIGAR_LED_A3
;	BCF LED_A3
;RETURN
;
;LIGAR_LED_A2
;	BSF LED_A2
;RETURN
;
;DESLIGAR_LED_A2
;	BCF LED_A2
;RETURN
;
;LIGAR_LED_A1
;	BSF LED_A1
;RETURN
;
;DESLIGAR_LED_A1
;	BCF LED_A1
;RETURN
;
;LIGAR_LED_T
;	BSF LED_T
;RETURN
;
;DESLIGAR_LED_T
;	BCF LED_T
;RETURN
;
;
;DELAY_1SEC_NO_TRIGGER
;ATRASO2_HEADER_FOR1 ; header of for1
;	MOVLW	21			; Move literal to accumulator
;	MOVWF	CONTADOR1	; Move accumulator value to file
;	
;ATRASO2_BODY_FOR1 ; body of for1
;	
;	ATRASO2_HEADER_FOR2 ; header of for2
;		MOVLW	20			; Move literal to accumulator
;		MOVWF	CONTADOR2	; Move accumulator value to file
;
;	ATRASO2_BODY_FOR2 ; body of for2
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		NOP
;		ATRASO2_HEADER_FOR3 ; header of for3
;			MOVLW	255			; Move literal to accumulator
;			MOVWF	CONTADOR3	; Move accumulator value to file
;			
;		ATRASO2_BODY_FOR3 ; body of for3
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			NOP
;			DECFSZ	CONTADOR3	; Decrement file, skip next line if 0
;			GOTO	ATRASO2_BODY_FOR3
;		
;		DECFSZ	CONTADOR2	; Decrement file, skip next line if 0
;		GOTO	ATRASO2_BODY_FOR2
;		
;	DECFSZ	CONTADOR1	; Decrement file, skip next line if 0
;	GOTO	ATRASO2_BODY_FOR1
;	
;	BTFSS LED_C ; skip next line if LED_C = 1
;	GOTO LIGAR_LED_C_ATRASO2
;	GOTO DESLIGAR_LED_C_ATRASO2
;MYRETURN_ATRASO2
;RETURN
;
;LIGAR_LED_C_ATRASO2
;	BSF LED_C
;	GOTO MYRETURN_ATRASO2
;
;DESLIGAR_LED_C_ATRASO2
;	BCF LED_C
;	GOTO MYRETURN_ATRASO2
;
;CICLO_EMERGENCIA
;	BTFSC LED_A4 ; skip next line if LED_A4 = 0
;	CALL CICLO_EMERGENCIA_A4
;	
;	BTFSC LED_A3 ; skip next line if LED_A3 = 0
;	CALL CICLO_EMERGENCIA_A3
;	
;	BTFSC LED_A2 ; skip next line if LED_A2 = 0
;	CALL CICLO_EMERGENCIA_A2
;	
;	BTFSC LED_A1 ; skip next line if LED_A1 = 0
;	CALL CICLO_EMERGENCIA_A1
;	
;	BTFSC LED_T ; skip next line if LED_T = 0
;	CALL CICLO_EMERGENCIA_T
;	
;GOTO CICLO_NORMAL
;
;CICLO_EMERGENCIA_A4
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DESLIGAR_LED_A4
;	CALL LIGAR_LED_A3
;RETURN
;     
;CICLO_EMERGENCIA_A3
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DESLIGAR_LED_A3
;	CALL LIGAR_LED_A2
;RETURN
;     
;CICLO_EMERGENCIA_A2
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DESLIGAR_LED_A2
;	CALL LIGAR_LED_A1
;RETURN
;     
;CICLO_EMERGENCIA_A1
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DESLIGAR_LED_A1
;	CALL LIGAR_LED_T
;RETURN
;     
;CICLO_EMERGENCIA_T
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;	CALL DELAY_1SEC_NO_TRIGGER
;RETURN
;	
END
