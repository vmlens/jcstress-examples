package com.vmlens.stressTest.tests;

import java.math.BigDecimal;

public class TestBigDecimal {

	
	private  BigDecimal bigDecimal;
	private  String expectedString;
	
	
	public TestBigDecimal(String decimal)
	{
		bigDecimal = new BigDecimal(decimal);
		expectedString = decimal;
	}
	
	
	
	
	
	public void runTest()
	{
		
		String value = bigDecimal.toString();
		
		if( ! value.equals( expectedString ) )
		{
			throw new RuntimeException( "'" + value  + "' " + value.getClass());
		}
		
		
	}
	
	
}
