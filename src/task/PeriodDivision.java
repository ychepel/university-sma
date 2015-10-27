package task;

import java.util.ArrayList;

/*
 деление в столбик
 найти период и вывести в поле результата в скобках. Типа 10/3 = 3, (3)
 период не длиннее 10 знаков. если он длинее - сичтаем,что периода нет
 */

public class PeriodDivision {

	private String dividendOrigin;
	private String dividerOrigin;
	private String dividendString;
	private Long dividerLong;
	private ArrayList<Long> dividentList = new ArrayList<>();
	private ArrayList<Long> productList = new ArrayList<>();
	
	
	public PeriodDivision(String dividend, String divider) throws NumberFormatException {
		Long.parseLong(dividend);
		this.dividerLong = Math.abs(Long.parseLong(divider));
		if(dividerLong.equals(0l))
			throw new NumberFormatException("Dividing by zero");
		this.dividendOrigin = dividend;
		this.dividerOrigin = divider;
		this.dividendString = dividend.replace("-", "");
	}
	
	public void visualDivision() {
		String result = this.division();
		this.outputResult(result);
	}
	
	public String division() {
		String divisionResult = "";
		int currentPosition = -1;
		Long currentDivident = 0l;
		int periodLength = 0;
		int decimalFirstElement = 0;
		
		while (!(currentPosition >= dividendString.length() && currentDivident==0)) {
			currentPosition++;
			
			int currentElement = 0;
			if (currentPosition == dividendString.length()) {
				divisionResult += ",";
				decimalFirstElement = dividentList.size();
			}
			else if (currentPosition < dividendString.length()) {
				currentElement = Character.getNumericValue(dividendString.charAt(currentPosition));
			}
			currentDivident = currentDivident*10 + currentElement;

			if (currentDivident >= dividerLong) {
				Long currentResult = currentDivident / dividerLong;
				divisionResult += currentResult;
				Long currentProduct = currentResult * dividerLong;
				
				for(int i = decimalFirstElement; i < dividentList.size(); i++) {
					if(dividentList.get(i).equals(currentDivident) && productList.get(i).equals(currentProduct)) {
						periodLength = dividentList.size() - i;
						break;
					}
				}
				
				dividentList.add(currentDivident);
				productList.add(currentProduct);
				currentDivident -= currentProduct;
			}
			else {
				divisionResult += "0";
				dividentList.add(0l);
				productList.add(0l);
			}
			
			if (periodLength > 0) {
				String part = divisionResult.substring(divisionResult.length() - periodLength, divisionResult.length());
				String pattern = "^\\d*,\\d*(" + part + "){2}$";
				if (divisionResult.matches(pattern)) {
					divisionResult = divisionResult.replaceFirst("(" + part + "){2}$", "(" + part + ")");
					break;
				}
			}

			if(currentPosition - dividendString.length() == 20) {
				divisionResult += "...";
				break;
			}
		}
		if(divisionResult.matches(".*,0$")) divisionResult = divisionResult.replaceFirst(",0", "");
		while(divisionResult.matches("^0\\d.*")) divisionResult = divisionResult.replaceFirst("^0", "");
		divisionResult = (('-' == dividendOrigin.charAt(0)) ^ ('-' == dividerOrigin.charAt(0)) ? "-" 	: "") + divisionResult;
		return divisionResult;
	}
	
	private void outputResult(String divisionResult) {
		System.out.println("******************************");
		int elementCount = dividentList.size(); 
		System.out.printf("%1$-" + elementCount + "s%2$s", (('-' == dividendOrigin.charAt(0)) ? "" : " ") + dividendOrigin, " | " + dividerOrigin + "\n");
		System.out.printf("%1$-" + elementCount + "s%2$s", "-", " +---------");

		int outputElement = 0;
		boolean firstRow = true;
		int shift = 2;
		while (outputElement < dividentList.size()) {
			if(dividentList.get(outputElement) == 0l) {
				outputElement++;
				shift++;
				continue;
			}
			
			if(outputElement > 0 && !firstRow) {
				System.out.printf("\n%" + shift + "s", dividentList.get(outputElement));
				System.out.printf("\n%" + (shift - dividentList.get(outputElement).toString().length()) + "s", "-");
			}
			System.out.printf("\n%" + shift + "s", productList.get(outputElement));
			if(firstRow) {
				if (elementCount > shift)
					System.out.printf("%1$" + (elementCount - shift) + "s", " ");
				System.out.print(" | " + divisionResult);
				firstRow = false;
			}
			String underline = new String(new char[productList.get(outputElement).toString().length()]).replace("\0", "_");
			System.out.printf("\n%" + shift + "s", underline);
			
			shift++;
			outputElement++;
		}
		
		Long reminder = dividentList.get(outputElement-1) - productList.get(outputElement-1);
		if("0".equals(divisionResult))
			System.out.printf("\n%1$" + elementCount + "s%2$s", " ", " | 0" + divisionResult);
		else
			System.out.printf("\n%" + (shift - 1) + "s", reminder);
	}
}