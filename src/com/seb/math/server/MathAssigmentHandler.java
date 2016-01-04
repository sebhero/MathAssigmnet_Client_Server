package com.seb.math.server;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.seb.math.shared.MATH_OPERATOR;
import com.seb.math.shared.MathAssigment;

public class MathAssigmentHandler
{

	/***
	 * Checks a assigment is its correct
	 * 
	 * @param assigmentAnsweard
	 *          a instance of a assigment
	 * @return true if the assigment is correct.
	 */
	public static boolean check(MathAssigment assigmentAnsweard)
	{
		// System.out.println("from client: " + address);
		System.out.println("assigmenet: " + assigmentAnsweard);

		double answear = 0;

		switch (assigmentAnsweard.getOperator())
		{
		case divded:
			answear = assigmentAnsweard.dived(assigmentAnsweard.getNumber1(), assigmentAnsweard.getNumber2());
			break;
		case minus:
			answear = assigmentAnsweard.minus(assigmentAnsweard.getNumber1(), assigmentAnsweard.getNumber2());
			break;
		case multiply:
			answear = assigmentAnsweard.multiply(assigmentAnsweard.getNumber1(), assigmentAnsweard.getNumber2());
			break;
		case plus:
			answear = assigmentAnsweard.plus(assigmentAnsweard.getNumber1(), assigmentAnsweard.getNumber2());
			break;
		default:
			break;
		}

		final double clientAnsweard = Double.parseDouble(assigmentAnsweard.getAnswear());
		if (answear == clientAnsweard)
		{
			System.out.println("client answear is correct");
			return true;
		}
		else
		{
			System.out.println("client answear is wrong");
			return false;
		}
	}

	/***
	 * Creates a Mathassigment from a given string tex. 22*42=46 Splits this into
	 * string and creates a Mathassigment from this.
	 * 
	 * @param assigmentText
	 * @return
	 */
	public static MathAssigment create(String assigmentText)
	{
		// regex delimiter
		final String delim = "\\d[-+/\\*]\\d=\\d";
		final StringTokenizer tokenizer = new StringTokenizer(assigmentText, delim);
		final ArrayList<String> numbers = new ArrayList<>();

		// get the numbers
		while (tokenizer.hasMoreElements())
		{
			numbers.add(tokenizer.nextToken());
		}
		// get the Math operator, due to that tokenizer need to split out the
		// number. I need to find the Operator
		// which is after the first number
		final char op = assigmentText.charAt(numbers.get(0).length());

		// convert char to enum
		final MATH_OPERATOR operator = MATH_OPERATOR.getFromChar(op);
		// return
		return new MathAssigment(Integer.parseInt(numbers.get(0)), Integer.parseInt(numbers.get(1)), operator,
				numbers.get(2));
	}
}
