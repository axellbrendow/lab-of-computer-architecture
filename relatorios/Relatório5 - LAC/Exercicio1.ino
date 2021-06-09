#define NUMBER_OF_LEDS 3
#define TIME_UNIT 1000
#define BLINK_DELAY 500

int redLed = 13;
int yellowLed = 12;
int greenLed = 11;
int blueLed = 10;

int leds[] = { redLed, yellowLed, greenLed, blueLed };

void turnOnLed(int ledIndex, int cicles)
{
	digitalWrite(blueLed, HIGH);
	
	for (int i = 0; i < NUMBER_OF_LEDS; i++)
	{
		if (i != ledIndex)
		{
			digitalWrite(leds[i], LOW);
		}
		
		else
		{
			digitalWrite(leds[i], HIGH);
		}
	}
	
	for (int i = 0; i < cicles; i++)
	{
		delay(BLINK_DELAY);
		digitalWrite(blueLed, LOW);
		delay(TIME_UNIT - BLINK_DELAY);
		digitalWrite(blueLed, HIGH);
	}
}

void setup()
{
	Serial.begin(9600);
	
	for (int i = 0; i < NUMBER_OF_LEDS; i++)
	{
		pinMode(leds[i], OUTPUT);
	}
	
	pinMode(leds[NUMBER_OF_LEDS], OUTPUT);
}

void loop()
{
	turnOnLed(0, 3);
	turnOnLed(2, 4);
	turnOnLed(1, 2);
}