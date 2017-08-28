package com.vmlens.stressTest.tests;



import java.math.BigDecimal;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult1;

/**
 * 
 * führt auf einem rasberrypi nicht zu fehlern
 * 
 * 
 * @author thomas
 *
 */

//@JCStressTest
@Outcome(id = "0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class BigDecimalToString2 {

	private  BigDecimal testBigDecimal = new BigDecimal("0.56777565566445556455");
	private  String expected = new String("0.56777565566445556455"); 
	
	
	
	
	
	
	
	@Actor
	public void actor1(IntResult1 r) {
String value = testBigDecimal.toString();
		
		
		//value.toCharArray()
		
		if(  ! value.equals(expected) )
		{
			throw new RuntimeException(value + " " + value.getClass());
		}
		
	}

	@Actor
	public void actor2(IntResult1 r) {
String value = testBigDecimal.toString();
		
		
		//value.toCharArray()
		
		if(  ! value.equals(expected) )
		{
			throw new RuntimeException(value + " " + value.getClass());
		}
	}

	
	
	
}
