package com.seb.math.shared;

import java.io.Serializable;
import java.util.Random;

public class MathAssigment implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int number1, number2;
	MATH_OPERATOR operator;
	String answear;
	boolean corrected, isCorrect, isAnsweard;

	/***
	 * Generates a math assigment. with a range between 1-20
	 */
	public MathAssigment()
	{
		final Random rnd = new Random();
		this.number1 = rnd.nextInt(19) + 1;
		this.number2 = rnd.nextInt(19) + 1;
		this.operator = MATH_OPERATOR.values()[rnd.nextInt(3)];
	}

	/**
	 * creates a math assignment with set values
	 * 
	 * @param number1
	 * @param number2
	 * @param operator
	 */
	public MathAssigment(int number1, int number2, MATH_OPERATOR operator)
	{
		this.number1 = number1;
		this.number2 = number2;
		this.operator = operator;
	}

	public MathAssigment(int number1, int number2, MATH_OPERATOR operator, String answear)
	{
		this.number1 = number1;
		this.number2 = number2;
		this.operator = operator;
		this.answear = answear;
	}

	/**
	 * @return the number1
	 */
	public int getNumber1()
	{
		return number1;
	}

	/**
	 * @return the number2
	 */
	public int getNumber2()
	{
		return number2;
	}

	/**
	 * @return the operator
	 */
	public MATH_OPERATOR getOperator()
	{
		return operator;
	}

	public String getOperatorString()
	{
		switch (this.operator)
		{
		case divded:
			return "/";
		case minus:
			return "-";
		case multiply:
			return "*";
		case plus:
			return "+";
		default:
			break;

		}
		return "";
	}

	/**
	 * @return the answear
	 */
	public String getAnswear()
	{
		return answear;
	}

	/**
	 * @param number1
	 *          the number1 to set
	 */
	public void setNumber1(int number1)
	{
		this.number1 = number1;
	}

	/**
	 * @param number2
	 *          the number2 to set
	 */
	public void setNumber2(int number2)
	{
		this.number2 = number2;
	}

	/**
	 * @param operator
	 *          the operator to set
	 */
	public void setOperator(MATH_OPERATOR operator)
	{
		this.operator = operator;
	}

	/**
	 * @param answear
	 *          the answear to set
	 */
	public void setAnswear(String answear)
	{
		this.answear = answear;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String theOperator = "";
		switch (operator)
		{
		case divded:
			theOperator = "/";
			break;
		case minus:
			theOperator = "-";
			break;
		case multiply:
			theOperator = "*";
			break;
		case plus:
			theOperator = "+";
			break;
		default:
			break;
		}
		return number1 + "" + theOperator + "" + number2;
	}

	public static double dived(int num1, int num2)
	{
		return num1 / num2;
	}

	public static double minus(int num1, int num2)
	{
		return num1 - num2;
	}

	public static double plus(int num1, int num2)
	{
		return num1 + num2;
	}

	public static double multiply(int num1, int num2)
	{
		return num1 * num2;
	}
}
