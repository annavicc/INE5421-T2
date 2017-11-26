package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ContextFreeLanguage.CFGOperator;
import ContextFreeLanguage.ContextFreeGrammar;

class FirstNTTest {
	private ContextFreeGrammar grammar[];

	/**
	 * Set up grammars
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		grammar = new ContextFreeGrammar[11];
		grammar[0] = ContextFreeGrammar.isValidCFG(
				"S -> x y z | a B C\n" + 
						"B -> c | c d\n" + 
				"C -> e g | d f\n");
		grammar[1] = ContextFreeGrammar.isValidCFG(
				"S -> A B C D E\n" + 
				"A -> a | &\n" + 
				"B -> b | &\n" + 
				"C -> c\n"	+ 
				"D -> d | &\n" + 
				"E -> e | &");
		grammar[2] = ContextFreeGrammar.isValidCFG(
				"S -> A B\n" + 
				"A -> &\n" + 
				"B -> &");
		grammar[3] = ContextFreeGrammar.isValidCFG(
				"S -> B b | C d\n" + 
						"B -> a B | &\n" + 
				"C -> c C | &\n");
		grammar[4] = ContextFreeGrammar.isValidCFG(
				"S -> ( S ) | &");
		grammar[5] = ContextFreeGrammar.isValidCFG(
				"S -> A B | C\n" + 
						"A -> D | a | &\n" + 
						"B -> b\n" + 
						"C -> &\n" + 
				"D -> d");
		grammar[6] = ContextFreeGrammar.isValidCFG(
				"S -> A S B | d\n" + 
						"A -> a\n" + 
				"B -> A B |b | &");
		grammar[7] = ContextFreeGrammar.isValidCFG(
				"E -> T E1\n" + 
						"E1 -> + T E1 | &\n" + 
						"T -> F T1\n" + 
						"T1 -> * F T1 | &\n" + 
				"F -> id | ( E )");
		grammar[8] = ContextFreeGrammar.isValidCFG(
				"A -> B C | a b | H\n" + 
						"B -> D | &\n" + 
						"C -> f | G a | H E\n" + 
						"E -> e | J\n" + 
						"H -> K | & | a M\n" + 
				"M -> Z");
		
		grammar[9] = ContextFreeGrammar.isValidCFG(
				"X -> Y Z\n" + 
						"Y -> m | n | &\n" + 
				"Z -> m");
		
		grammar[10] = ContextFreeGrammar.isValidCFG(""
				+ "S -> A B | C D\n" + 
				"A -> &\n" + 
				"B -> &\n" + 
				"C -> &\n" + 
				"D -> a | &");
	}
	
	/**
	 * Test firstNT set for G0
	 */
	@Test
	void firstNTG0Test() {
		CFGOperator op = new CFGOperator(grammar[0]);
		
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("S"));
		assertEquals(expected, op.getFirstNT().get("B"));
		assertEquals(expected, op.getFirstNT().get("C"));
	}
	
	/**
	 * Test firstNT set for G1
	 */
	@Test
	void firstNTG1Test() {
		CFGOperator op = new CFGOperator(grammar[1]);
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("A"));
		assertEquals(expected, op.getFirstNT().get("B"));
		assertEquals(expected, op.getFirstNT().get("C"));
		assertEquals(expected, op.getFirstNT().get("D"));
		assertEquals(expected, op.getFirstNT().get("E"));
		expected.add("A");
		expected.add("B");
		expected.add("C");
		assertEquals(expected, op.getFirstNT().get("S"));
	}
	
	/**
	 * Test firstNT set for G2
	 */
	@Test
	void firstNTG2Test() {
		CFGOperator op = new CFGOperator(grammar[2]);
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("A"));
		assertEquals(expected, op.getFirstNT().get("B"));
		
		expected.add("A");
		expected.add("B");
		
		assertEquals(expected, op.getFirstNT().get("S"));
	}
	
	/**
	 * Test firstNT set for G3
	 */
	@Test
	void firstNTG3Test() {
		CFGOperator op = new CFGOperator(grammar[3]);
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("B"));
		assertEquals(expected, op.getFirstNT().get("C"));
		
		expected.add("B");
		expected.add("C");
		
		assertEquals(expected, op.getFirstNT().get("S"));
	}
	
	/**
	 * Test firstNT set for G4
	 */
	@Test
	void firstNTG4Test() {
		CFGOperator op = new CFGOperator(grammar[4]);
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("S"));
	}
	
	/**
	 * Test firstNT set for G5
	 */
	@Test
	void firstNTG5Test() {
		CFGOperator op = new CFGOperator(grammar[5]);
		Set<String> expected = new HashSet<String>();
		
		assertEquals(expected, op.getFirstNT().get("B"));
		assertEquals(expected, op.getFirstNT().get("C"));
		assertEquals(expected, op.getFirstNT().get("D"));
		
		expected.add("D");
		assertEquals(expected, op.getFirstNT().get("A"));
		
		expected.add("A");
		expected.add("B");
		expected.add("C");
		assertEquals(expected, op.getFirstNT().get("S"));
	}
	
	/**
	 * Test firstNT set for G6
	 */
	@Test
	void firstNTG6Test() {
		CFGOperator op = new CFGOperator(grammar[6]);
		Set<String> expected = new HashSet<String>();
		assertEquals(expected, op.getFirstNT().get("A"));
		
		expected.add("A");
		assertEquals(expected, op.getFirstNT().get("S"));
		assertEquals(expected, op.getFirstNT().get("B"));
	}
	
	/**
	 * Test firstNT set for G7
	 */
	@Test
	void firstNTG7Test() {
		CFGOperator op = new CFGOperator(grammar[7]);
		Set<String> expected = new HashSet<String>();
		assertEquals(expected, op.getFirstNT().get("E1"));
		assertEquals(expected, op.getFirstNT().get("T1"));
		assertEquals(expected, op.getFirstNT().get("F"));
		
		expected.add("T");
		expected.add("F");
		assertEquals(expected, op.getFirstNT().get("E"));
		expected.remove("T");
		assertEquals(expected, op.getFirstNT().get("T"));
	}
	
	/**
	 * Test firstNT set for G8
	 */
	@Test
	void firstNTG8Test() {
		CFGOperator op = new CFGOperator(grammar[8]);
		Set<String> expected = new HashSet<String>();

		expected.add("B");
		expected.add("C");
		expected.add("D");
		expected.add("E");
		expected.add("G");
		expected.add("H");
		expected.add("J");
		expected.add("K");
		assertEquals(expected, op.getFirstNT().get("A"));
		expected.clear();
		expected.add("D");
		assertEquals(expected, op.getFirstNT().get("B"));
		expected.clear();
		expected.add("G");
		expected.add("H");
		expected.add("K");
		expected.add("E");
		expected.add("J");
		assertEquals(expected, op.getFirstNT().get("C"));
		expected.clear();

		expected.add("J");
		assertEquals(expected, op.getFirstNT().get("E"));
		expected.clear();
		
		expected.add("K");
		
		assertEquals(expected, op.getFirstNT().get("H"));
		expected.clear();
		
		expected.add("Z");
		assertEquals(expected, op.getFirstNT().get("M"));
	}
	
	/**
	 * Test firstNT set for G9
	 */
	@Test
	void firstNTG9Test() {
		CFGOperator op = new CFGOperator(grammar[9]);
		Set<String> expected = new HashSet<String>();
		assertEquals(expected, op.getFirstNT().get("Y"));
		assertEquals(expected, op.getFirstNT().get("Z"));
		expected.add("Z");
		expected.add("Y");
		assertEquals(expected, op.getFirstNT().get("X"));
	}
	
	/**
	 * Test firstNT set for G10
	 */
	@Test
	void firstNTG10Test() {
		CFGOperator op = new CFGOperator(grammar[10]);
		Set<String> expected = new HashSet<String>();
		assertEquals(expected, op.getFirstNT().get("A"));
		assertEquals(expected, op.getFirstNT().get("B"));
		assertEquals(expected, op.getFirstNT().get("C"));
		assertEquals(expected, op.getFirstNT().get("D"));
		
		expected.add("A");
		expected.add("B");
		expected.add("C");
		expected.add("D");

		assertEquals(expected, op.getFirstNT().get("S"));
	}
	

}
