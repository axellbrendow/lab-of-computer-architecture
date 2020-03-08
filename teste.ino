#include <Arduino.h>
#define NUM_LEDS 3
#define DEFAULT_DELAY 300
#define BOTAO 7

int leds[] = { 11, 10, 9, 6 };
int toggle = 0;

void piscarLed(int ledIndex, int ms) // pisca o led várias vezes no seu estado
{
    for (size_t i = 0; i < 3; i++)
    {
        digitalWrite(leds[ledIndex], HIGH);

        delay(100);

        digitalWrite(leds[ledIndex], LOW);

        delay(ms / 3 - 100);
    }
}

void ligarLedEPiscar(int ledIndex, int ms) // liga o led desejado e pisca ele 3 vezes no estado dele
{
    int energy;

    for (size_t i = 0; i < NUM_LEDS; i++)
    {
        energy = ( i != ledIndex ? LOW : HIGH );

        digitalWrite(leds[i], energy);
    }

    piscarLed(ledIndex, ms);
}

void setup()
{
    for (size_t i = 0; i < NUM_LEDS; i++)
    {
        pinMode(leds[i], OUTPUT);
    }

    pinMode(BOTAO, INPUT);

    Serial.begin(9600);
}

void loop()
{
    int botao = digitalRead(BOTAO);

    Serial.print("Botão = ");
    Serial.println(botao);

    ligarLedEPiscar(0, DEFAULT_DELAY * 1);
    ligarLedEPiscar(1, DEFAULT_DELAY * 1);
    ligarLedEPiscar(2, DEFAULT_DELAY * 1);

    digitalWrite(leds[3], botao);

    // ligarLedEPiscar(3, DEFAULT_DELAY);
}
