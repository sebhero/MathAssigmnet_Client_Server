package com.seb.math.shared;

public enum MATH_OPERATOR
{
	plus, minus, divded, multiply;

	/***
	 * Convert char operator to enum
	 * 
	 * @param op
	 *          string input
	 * @return the operator or null if not found
	 */
	public static MATH_OPERATOR getFromChar(char op)
	{
		switch (op)
		{
		case '+':
			return plus;
		case '-':
			return minus;
		case '/':
			return divded;
		case '*':
			return multiply;
		default:
			break;
		}
		return null;
	}
}
