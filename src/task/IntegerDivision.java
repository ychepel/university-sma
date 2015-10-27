package task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * целочисленное деление в столбик с выводом столбика в консоль
 */

public class IntegerDivision {
	private static Long dividend;
	private static Long divider;
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String dividendString = "";
		String dividerString = "";
		Long remainder = 0l;
		Long result = 0l;
		
		while("".equals(dividendString)) {
			try {
				System.out.println("Please enter dividend:");
				dividendString = reader.readLine();
				System.out.println("Please enter divider:");
				dividerString = reader.readLine();
		
				dividend = Long.parseLong(dividendString);
				divider = Long.parseLong(dividerString);
				
				remainder = dividend % divider;
				result = dividend / divider;
			}
			catch (NumberFormatException e) {
				System.out.println("Your entered wrong number. Please try again.");
				dividendString = "";
			}
			catch (ArithmeticException e) {
				System.out.println("You are trying to divide by zero. Please enter another numbers.");
				dividendString = "";
			}
		}
		
		System.out.println("******************************");
		System.out.println(" " + dividendString + " |" + dividerString);
		printLine(dividendString.length(), result, remainder);
		System.out.printf(" %"+dividendString.length()+"d\n", +remainder);
	}
	
	private static void printLine(Integer elemetPosition, Long resultPart, Long remainder) {
		Long secondNumber = divider * (resultPart % 10);
		Long firstNumber = secondNumber + remainder;
		if (resultPart/10 > 0) {
			printLine(elemetPosition-1, resultPart/10, firstNumber/10);
			System.out.printf(" %"+elemetPosition+"d\n", firstNumber);
		}
		int countSymbol = String.valueOf(secondNumber).length();
		int shift = elemetPosition - countSymbol + 1;
		System.out.printf("%" + shift  + "s", "-");
		
		if(resultPart/10 == 0) {
			shift = String.valueOf(dividend).length();
			System.out.printf("%1$" + shift + "s%2$s", " ", " +––––––––");
		}
		
		System.out.printf("\n %"+elemetPosition+"d", secondNumber);
		
		if(resultPart/10 == 0) {
			shift = String.valueOf(dividend).length() - String.valueOf(secondNumber).length();
			if(shift > 0) {
				System.out.printf("%1$" + shift + "s", " ");
			}
			System.out.print(" |"+ (dividend / divider));
		}
		
		String underline = new String(new char[countSymbol]).replace("\0", "_");
		System.out.printf("\n%" + (elemetPosition + 1) + "s\n", underline);
		
	}
}
