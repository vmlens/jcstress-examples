package com.vmlens.stressTest.tests;

import java.util.TimeZone;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult1;

//@JCStressTest
@Outcome(id = "0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class TimeZoneClone {
	
	
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");
	
	
	private void runTest()
	{
		 String value =    ((TimeZone)timeZone.clone()).getID(); 
		 
		 if( ! "GMT".equals(value) )
		 {
			 throw new RuntimeException(value);
		 }
		 
	}
	
	
	
	@Actor
	public void actor1(IntResult1 r) {
		
	 
		runTest();
		
	}

	@Actor
	public void actor2(IntResult1 r) {
	
		runTest();
	}
	
	

}
