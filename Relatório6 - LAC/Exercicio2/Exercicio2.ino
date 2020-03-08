#define NUMBER_OF_LEDS 5

int aLed = 13;
int bLed = 12;
//int opCodeLed = 10;
int resultLed = 11;
int carryOutLed = 10;
/*
int aLed = 11;
int bLed = 10;
int opCodeLed = 7;
int resultLed = 9;
int carryOutLed = 8;*/

int leds[] = { aLed, bLed, 0, resultLed, carryOutLed };
char optors[] = { '&', '|', '~', '+' };

int a = 0;
int b = 0;
int opCode = 0;
int result = 0;
int carryOut = 0;
int *results = NULL;

int *operate(int a, int b, int opCode)
{
	int *localResults = new int[2];
	localResults[0] = 0;
	localResults[1] = 0;
	
	switch (opCode)
	{
		case 0: // and
			localResults[0] = a & b;
			break;
		
		case 1: // or
			localResults[0] = a | b;
			break;
		
		case 2: // not
			localResults[0] = ( a == 0 ? HIGH : LOW );
			break;
		
		case 3: // sum
			localResults[0] = a ^ b;
			localResults[1] = a & b;
			break;
		
		default:
			localResults[0] = 1;
			localResults[1] = 1;
			break;
	}
	
	return localResults;
}

void printExpression(int a, int b, int opCode, int result, int carryOut)
{
	Serial.print("a = ");
	Serial.print(a);
	Serial.print(", b = ");
	Serial.print(b);
	Serial.print(", ");
	
	if (opCode == 2)
	{
		Serial.print(optors[opCode]);
		Serial.print("a = ");
	}
	
	else
	{
		Serial.print("a ");
		Serial.print(optors[opCode]);
		Serial.print(" b = ");
	}
	
	Serial.print(result);
	Serial.print(", carryOut = ");
	Serial.println(carryOut);
}

int getLogicValue(int value)
{
	int logicValue = LOW;
	
	if (value > 0)
	{
		logicValue = HIGH;
	}
	
	return logicValue;
}

void setup()
{
	Serial.begin(9600);
	
	for (int i = 0; i < NUMBER_OF_LEDS; i++)
	{
		pinMode(leds[i], OUTPUT);
	}
}

void loop()
{
	if (Serial.available() > 2)
	{
		a = Serial.parseInt();
		b = Serial.parseInt();
		opCode = Serial.parseInt();
		
		results = operate(a, b, opCode);
		result = results[0];
		carryOut = results[1];
		
		//printExpression(a, b, opCode, result, carryOut);
		
		digitalWrite(aLed, getLogicValue(a));
		digitalWrite(bLed, getLogicValue(b));
		digitalWrite(resultLed, getLogicValue(result));
		digitalWrite(carryOutLed, getLogicValue(carryOut));
		
		delete [] results;
	}
}
