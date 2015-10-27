package task;

import static org.junit.Assert.*;

import org.junit.Test;

public class PeriodDivisionTest {

	@Test
	public void divisionTest1() {
		PeriodDivision pd = new PeriodDivision("10", "3");
		String result = pd.division();
		assertEquals("3,(3)", result);
	}
	
	@Test
	public void divisionTest2() {
		PeriodDivision pd = new PeriodDivision("2", "-99");
		String result = pd.division();
		assertEquals("-0,(02)", result);
	}
	
	@Test
	public void divisionTest3() {
		PeriodDivision pd = new PeriodDivision("7", "12");
		String result = pd.division();
		assertEquals("0,58(3)", result);
	}
	
	@Test
	public void divisionTest4() {
		PeriodDivision pd = new PeriodDivision("-17", "11");
		String result = pd.division();
		assertEquals("-1,(54)", result);
	}
	
	@Test
	public void divisionTest5() {
		PeriodDivision pd = new PeriodDivision("25", "39");
		String result = pd.division();
		assertEquals("0,(641025)", result);
	}
	
	@Test
	public void divisionTest6() {
		PeriodDivision pd = new PeriodDivision("-9200", "-3");
		String result = pd.division();
		assertEquals("3066,(6)", result);
	}
	
	@Test
	public void divisionTest7() {
		PeriodDivision pd = new PeriodDivision("65", "34");
		String result = pd.division();
		assertEquals("1,911764705882352941176...", result);
	}
	
	@Test
	public void divisionTest8() {
		PeriodDivision pd = new PeriodDivision("250", "5");
		String result = pd.division();
		assertEquals("50", result);
	}
	
	@Test
	public void divisionTest9() {
		PeriodDivision pd = new PeriodDivision("0", "13");
		String result = pd.division();
		assertEquals("0", result);
	}
	
	@Test (expected = NumberFormatException.class)
	public void divisionTest10() {
		PeriodDivision pd = new PeriodDivision("20", "0");
		String result = pd.division();
		assertEquals("", result);
	}
	
	@Test (expected = NumberFormatException.class)
	public void divisionTest11() {
		PeriodDivision pd = new PeriodDivision("0", "abc");
		String result = pd.division();
		assertEquals("", result);
	}
	
	@Test
	public void divisionTest12() {
		PeriodDivision pd = new PeriodDivision("123", "1");
		String result = pd.division();
		assertEquals("123", result);
	}

}
