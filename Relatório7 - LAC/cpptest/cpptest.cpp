// cpptest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <string>

using namespace std;

#define NUMBER_OF_LEDS 4

int led13 = 13;
int led12 = 12;
int led11 = 11;
int led10 = 10;

int leds[] = { led13, led12, led11, led10 };

string a = "0000";
string b = "1111";


template<class BitByBitAction>
string bitAction(string a, string b, BitByBitAction bitByBitAction)
{
	string result = "";
	int length = a.length();

	for (int i = 0; i < length; i++)
	{
		result += bitByBitAction(i, a[i], b[i]);
	}

	return result;
}

char negate_bit(char bit)
{
	return (bit == '0' ? '1' : '0');
}

char and_bits(char bitA, char bitB)
{
	return (bitA == '1' && bitB == '1' ? '1' : '0');
}

char a_negated_and_b(char bitA, char bitB)
{
	return and_bits(negate_bit(bitA), bitB);
}

char a_and_b_negated(char bitA, char bitB)
{
	return and_bits(bitA, negate_bit(bitB));
}

char a_negated_and_b_negated(char bitA, char bitB)
{
	return and_bits(negate_bit(bitA), negate_bit(bitB));
}

char nand_bits(char bitA, char bitB)
{
	return negate_bit(and_bits(bitA, bitB));
}

char or_bits(char bitA, char bitB)
{
	return (bitA == '0' && bitB == '0' ? '0' : '1');
}

char a_negated_or_b(char bitA, char bitB)
{
	return or_bits(negate_bit(bitA), bitB);
}

char a_or_b_negated(char bitA, char bitB)
{
	return or_bits(bitA, negate_bit(bitB));
}

char a_negated_or_b_negated(char bitA, char bitB)
{
	return or_bits(negate_bit(bitA), negate_bit(bitB));
}

char nor_bits(char bitA, char bitB)
{
	return negate_bit(or_bits(bitA, bitB));
}

char xor_bits(char bitA, char bitB)
{
	return (bitA == bitB ? '0' : '1');
}

char xnor_bits(char bitA, char bitB)
{
	return negate_bit(xor_bits(bitA, bitB));
}

string zeroL()
{
	return "0000";
}

string umL()
{
	return "1111";
}

string An()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return negate_bit(bitA);
			}
		);
}

string Bn()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return negate_bit(bitB);
			}
		);
}

string AouB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return or_bits(bitA, bitB);
			}
		);
}

string AeB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return and_bits(bitA, bitB);
			}
		);
}

string AxorB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return xor_bits(bitA, bitB);
			}
		);
}

string AnandB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return nand_bits(bitA, bitB);
			}
		);
}

string AnorB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return nor_bits(bitA, bitB);
			}
		);
}

string AxnorB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return xnor_bits(bitA, bitB);
			}
		);
}

string AnouB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return or_bits(negate_bit(bitA), bitB);
			}
		);
}

string AouBn()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return or_bits(bitA, negate_bit(bitB));
			}
		);
}

string AneB()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return and_bits(negate_bit(bitA), bitB);
			}
		);
}

string AeBn()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return and_bits(bitA, negate_bit(bitB));
			}
		);
}

string AnouBn()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return or_bits(negate_bit(bitA), negate_bit(bitB));
			}
		);
}

string AneBn()
{
	return
		bitAction
		(
			a, b,
			[](int index, char bitA, char bitB)
			{
				return and_bits(negate_bit(bitA), negate_bit(bitB));
			}
		);
}

string operate(char opcode)
{
	string result = "1111";

	switch (opcode)
	{
	case '0':
		result = zeroL();
		break;

	case '1':
		result = umL();
		break;

	case '2':
		result = An();
		break;

	case '3':
		result = Bn();
		break;

	case '4':
		result = AouB();
		break;

	case '5':
		result = AeB();
		break;

	case '6':
		result = AxorB();
		break;

	case '7':
		result = AnandB();
		break;

	case '8':
		result = AnorB();
		break;

	case '9':
		result = AxnorB();
		break;

	case 'A':
		result = AnouB();
		break;

	case 'B':
		result = AouBn();
		break;

	case 'C':
		result = AneB();
		break;

	case 'D':
		result = AeBn();
		break;

	case 'E':
		result = AnouBn();
		break;

	case 'F':
		result = AneBn();
		break;

	default:
		break;
	}

	return result;
}

string hexToBin(char hex)
{
	string result = "1111";

	switch (hex)
	{
	case '0':
		result = "0000";
		break;

	case '1':
		result = "0001";
		break;

	case '2':
		result = "0010";
		break;

	case '3':
		result = "0011";
		break;

	case '4':
		result = "0100";
		break;

	case '5':
		result = "0101";
		break;

	case '6':
		result = "0110";
		break;

	case '7':
		result = "0111";
		break;

	case '8':
		result = "1000";
		break;

	case '9':
		result = "1001";
		break;

	case 'A':
		result = "1010";
		break;

	case 'B':
		result = "1011";
		break;

	case 'C':
		result = "1100";
		break;

	case 'D':
		result = "1101";
		break;

	case 'E':
		result = "1110";
		break;

	case 'F':
		result = "1111";
		break;

	default:
		break;
	}

	return result;
}

int main()
{
	char opcode;

	while (true)
	{
		cout << "Informe o valor de a: ";
		cin >> a;
		//a = hexToBin(a[0]);

		cout << "Informe o valor de b: ";
		cin >> b;
		//b = hexToBin(b[0]);

		cout << "Informe o opcode: ";
		cin >> opcode;

		cout << endl;

		if (opcode == '/')
		{
			break;
		}

		else
		{
			cout << "resultado = " << operate(opcode) << endl << endl;
		}
	}

    return 0;
}
