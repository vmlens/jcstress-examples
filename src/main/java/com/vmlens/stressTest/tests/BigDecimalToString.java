package com.vmlens.stressTest.tests;
import java.math.BigDecimal;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.IntResult1;
@JCStressTest
@Outcome(id = "0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class BigDecimalToString {
	private final  BigDecimal testBigDecimal = new BigDecimal("0.56");
	@Actor
	public void actor1(IntResult1 r) {
        testBigDecimal.toString().length();
		
	}
	@Actor
	public void actor2(IntResult1 r) {
		testBigDecimal.toString().length();
	}	
}
