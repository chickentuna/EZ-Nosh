package test;

import static org.junit.Assert.*;

import model.Amounts;
import model.Pair;

import org.junit.Test;
import org.junit.Before;


public class AmountsTest {

	Amounts amounts;
	
	@Before
	public void setUp() {
		amounts = new Amounts();
		
	}
	
	@Test
	public void pairs() {
		Pair<String, String> p1 = new Pair<String, String>("A", ".");
		Pair<String,String> p2 = new Pair<String, String>("A", ".");
		
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void test1() {
		amounts.put("A", 1, "o");
		amounts.put("B", 2, "o");
		System.out.println(amounts);
		assertTrue(amounts.containsKey("A", "o"));
	}

}
