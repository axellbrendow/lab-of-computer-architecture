#define NUMBER_OF_LEDS 3
#define TIME_UNIT 1000
#define BLINK_DELAY 500

int redLed = 13;
int yellowLed = 12;
int greenLed = 11;
int blueLed = 10;

int leds[] = { redLed, yellowLed, greenLed, blueLed };

void setup()
{
	Serial.begin(9600);
	
	// liga os leds vermelho, amarelo e verde
	for (int i = 0; i < NUMBER_OF_LEDS; i++)
	{
		pinMode(leds[i], OUTPUT);
	}
	
	// liga o led azul
	pinMode(leds[NUMBER_OF_LEDS], OUTPUT);
}

void loop()
{
	if (Serial.available() > 3)
	{
		int redLedValue = Serial.parseInt();
		int yellowLedValue = Serial.parseInt();
		int greenLedValue = Serial.parseInt();
		int blueLedValue = Serial.parseInt();
		
		digitalWrite(redLed, redLedValue);
		digitalWrite(yellowLed, yellowLedValue);
		digitalWrite(greenLed, greenLedValue);
		digitalWrite(blueLed, blueLedValue);
	}
}