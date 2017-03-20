package com.vmlens.stressTest.tests;

import java.lang.reflect.Type;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult2;


//@JCStressTest
//Outline the outcomes here. The default outcome is provided, you need to remove it:
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class DataRaceTest {

    private Type[] instance;

	public void callContainsDataRace() {

			if (instance == null) {

				Type[] ts = new Type[1];

				ts[0] = Object.class;

				instance = ts;
     	}
		

		instance[0].getTypeName();
	
	}
	

	    @Actor
	    public void actor1(IntResult2 r) {
		    callContainsDataRace();
	    }

	    @Actor
	    public void actor2(IntResult2 r) {
	    	callContainsDataRace();
	    }
	
	
	
	
}
