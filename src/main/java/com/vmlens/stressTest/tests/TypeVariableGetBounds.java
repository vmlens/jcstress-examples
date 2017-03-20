package com.vmlens.stressTest.tests;

import java.lang.reflect.TypeVariable;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult2;

import com.vmlens.stressTest.util.StressTestClassLoader;

@JCStressTest
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class TypeVariableGetBounds {

	private final Class cl;

	public TypeVariableGetBounds() {
		try {
			cl = (new StressTestClassLoader(TypeVariableGetBounds.class.getClassLoader()))
					.loadClass("com.vmlens.stressTest.util.GenericInterface");

		} catch (Exception e) {
			throw new RuntimeException("Test setup incorrect", e);
		}

	}

	public void callContainsDataRace() {
		TypeVariable typeVariable = cl.getTypeParameters()[0];
		typeVariable.getBounds()[0].getTypeName();

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
