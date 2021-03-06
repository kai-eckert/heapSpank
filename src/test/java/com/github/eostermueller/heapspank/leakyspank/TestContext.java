package com.github.eostermueller.heapspank.leakyspank;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.heapspank.leakyspank.LeakySpankContext;
import com.github.eostermueller.heapspank.leakyspank.Model;
import com.github.eostermueller.heapspank.leakyspank.LeakySpankContext.LeakResult;
import com.github.eostermueller.heapspank.util.LimitedSizeQueue;

public class TestContext {
	Model run_1 = new Model(TEST_RUN_1);
	Model run_2 = new Model(TEST_RUN_2);
	Model run_3 = new Model(TEST_RUN_3);
	Model run_4 = new Model(TEST_RUN_4);
	private static final String TEST_RUN_1 = 
			  " 1: 50 500 sun.reflect.GeneratedMethodAccessor8\n"
			+ " 2: 40 400 sun.reflect.GeneratedMethodAccessor7\n"
			+ " 3: 30 300 sun.reflect.GeneratedMethodAccessor6\n"
			+ " 4: 20 200 com.acme.Leak\n";

	private static final String TEST_RUN_2 = 
			  " 1: 50 500 sun.reflect.GeneratedMethodAccessor8\n"
			+ " 2: 40 400 sun.reflect.GeneratedMethodAccessor7\n"
			+ " 3: 35 350 com.acme.Leak\n"
			+ " 4: 30 300 sun.reflect.GeneratedMethodAccessor6\n";
	
	private static final String TEST_RUN_3 = 
			  " 1: 50 500 sun.reflect.GeneratedMethodAccessor8\n"
			+ " 2: 45 450 com.acme.Leak\n"
			+ " 3: 40 400 sun.reflect.GeneratedMethodAccessor7\n"
			+ " 4: 30 300 sun.reflect.GeneratedMethodAccessor6\n";
	
	private static final String TEST_RUN_4 = 
			  " 1: 55 550 com.acme.Leak\n"
			+ " 2: 50 500 sun.reflect.GeneratedMethodAccessor8\n"
			+ " 3: 40 400 sun.reflect.GeneratedMethodAccessor7\n"
			+ " 4: 30 300 sun.reflect.GeneratedMethodAccessor6\n";
	
	@Test
	public void canFindLeakSuspects() {
		
		LeakySpankContext ldc = new LeakySpankContext(-1, 60, 4, 0);
		
		ldc.addJMapHistoRun(run_1);
//		run_2.getAllOrderByMostUpwardlyMobileAsComparedTo(run_1);
		
		//The above call calculates the JMapHistoLine.rankIncrease for every line in the model
		//With rankings calculated, the LDC can do its work.
		
		ldc.addJMapHistoRun(run_2);
//		run_3.getAllOrderByMostUpwardlyMobileAsComparedTo(run_2);
		
		//Ditto, comment above.
		
		ldc.addJMapHistoRun(run_3);
//		run_4.getAllOrderByMostUpwardlyMobileAsComparedTo(run_3);
		
		//Ditto, comment above.
		
		ldc.addJMapHistoRun(run_4);
		
		LeakResult[] leakSuspects = ldc.getLeakSuspectsOrdered();
		
		for(LeakResult l : leakSuspects)
			System.out.println(l.humanReadable());
		
		assertEquals("Put a four suspected leaks into LeakDectectorContext, but didn't find right count", 4, leakSuspects.length );
		
		//LeakResult culprit = leakSuspects[leakSuspects.length-1];
		LeakResult culprit = leakSuspects[0];
		assertEquals("We accused the wrong class of being the leakiest!", "com.acme.Leak", culprit.line.className);
	}

	@Test
	public void canTheLinkedListIterateForward() {
		 LimitedSizeQueue<String> lsq = new LimitedSizeQueue<String>(3);
		 
		 String firstAdded = "foo";
		 String secondAdded = "bar";
		 String thirdAdded = "foobar";
		 String fourthAdded = "foobaz";
		 
		 lsq.add(firstAdded);
		 lsq.add(secondAdded);
		 lsq.add(thirdAdded);
		 lsq.add(fourthAdded);
		 
		 int count = 0;
		 for(String s : lsq) {
			 
			 switch(++count) {
			 case 1:
				 assertEquals("'for' loop iteration unable to find the earliest String added",secondAdded, s);
				 break;
			 case 2:
				 assertEquals("'for' loop iteration unable to find second earliest String added",thirdAdded, s);
				 break;
			 case 3:
				 assertEquals("'for' loop iteration unable to find most recent String added",fourthAdded, s);
				 break;
			 default:
				 fail("'for' loop is not behaving as expected on a linked list");
				 break;
			
			 }
			 
			 
			 //So the answer is yes.  The 'for' loop iterates from the oldest to the newest items in the queue.
		 }
		
	}
	
	

}
