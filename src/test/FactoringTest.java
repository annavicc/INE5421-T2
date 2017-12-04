package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ContextFreeLanguage.CFGOperator;
import ContextFreeLanguage.ContextFreeGrammar;

class FactoringTest {
	private ContextFreeGrammar grammar[];
	
	/**
	 * Initializes grammars
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		grammar = new ContextFreeGrammar[13];
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
		grammar[9] = ContextFreeGrammar.isValidCFG(""
				+ "A -> B C | a b | H\n" + 
				"B -> D | &\n" + 
				"C -> f | G a | H E\n" + 
				"E -> e | J\n" + 
				"H -> K | a M\n" + 
				"M -> Z");
		
		grammar[10] = ContextFreeGrammar.isValidCFG(
				"X -> Y Z\n" + 
				"Y -> m | n | &\n" + 
				"Z -> m");
		grammar[11] = ContextFreeGrammar.isValidCFG(""
				+ "S -> A B | C D\n" + 
				"A -> &\n" + 
				"B -> &\n" + 
				"C -> &\n" + 
				"D -> a | &");
		grammar[12] = ContextFreeGrammar.isValidCFG(
				"S -> A B | C D\n" +
				"A -> & | c\n" +
				"B -> d\n" +
				"C -> & | b\n" +
				"D -> d"
				);
	}

	/**
	 * Test if the grammar is factored
	 */
	@Test
	void isFactoredtest() {
		CFGOperator op = new CFGOperator(grammar[0]);
		assertFalse(op.isFactored());
		op = new CFGOperator(grammar[1]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[2]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[3]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[4]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[5]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[6]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[7]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[8]);
		assertFalse(op.isFactored());
		op = new CFGOperator(grammar[9]);
		assertFalse(op.isFactored());
		op = new CFGOperator(grammar[10]);
		assertTrue(op.isFactored());
		op = new CFGOperator(grammar[11]);
		assertFalse(op.isFactored());
		op = new CFGOperator(grammar[12]);
		assertFalse(op.isFactored());
	}
	
	/**
	 * Test the factoring process of a non factored grammar
	 */
	@Test
	void testFactorG0() {
		ContextFreeGrammar g = ContextFreeGrammar.isValidCFG(
				"S -> B y z C | C y z B C\n" + 
				"B -> b | c d\n" + 
				"C -> e g | e f | c\n");
		g.setId("G1");
		CFGOperator op = new CFGOperator(g);
		ArrayList<ContextFreeGrammar> results = new ArrayList<>();
		results = op.factorGrammar(3);
		assertEquals("S -> c S2 | e S1 | b y z C\n" + 
				"B -> c d | b\n" + 
				"C -> c | e C1\n" + 
				"S1 -> f y z B C | g y z B C\n" + 
				"C1 -> f | g\n" + 
				"S2 -> d y z C | y z B C\n" + 
				"" , results.get(results.size()-1).getDefinition());
	}
	
	
	/**
	 * Test the factoring process of a non factored grammar
	 */
	@Test
	void testFactorG1() {
		ContextFreeGrammar g = ContextFreeGrammar.isValidCFG(
				"S -> b c D | B c d\n" + 
				"B -> b B | b\n" + 
				"D -> d D | d");
		g.setId("G1");
		CFGOperator op = new CFGOperator(g);
		ArrayList<ContextFreeGrammar> results = new ArrayList<>();
		assertFalse(op.isFactored()); // not factored
		results = op.factorGrammar(10);
		op = new CFGOperator(results.get(results.size()-1));
		assertTrue(op.isFactored()); // must be factored

		assertEquals("S -> b S1\n" + 
				"S3 -> D | &\n" + 
				"B -> b B1\n" + 
				"D -> d D1\n" + 
				"D1 -> D | &\n" + 
				"S1 -> c S2 | B c d\n" + 
				"B1 -> B | &\n" + 
				"S2 -> d S3\n" + 
				"", results.get(results.size()-1).getDefinition());
	}

	@Test 
	void testFactorG2() {
		ContextFreeGrammar g = ContextFreeGrammar.isValidCFG(
				"S -> B b | C d \n" + 
				"B -> C a B | & \n" + 
				"C -> c C | &");
		g.setId("G1");
		CFGOperator op = new CFGOperator(g);
		ArrayList<ContextFreeGrammar> results = new ArrayList<>();
		assertFalse(op.isFactored()); // not factored
		results = op.factorGrammar(6);
		op = new CFGOperator(results.get(results.size()-1));
		assertTrue(op.isFactored()); // must be factored
		
		assertEquals("S -> b | a B b | d | c S1\n" + 
				"B -> C a B | &\n" + 
				"C -> & | c C\n" + 
				"S1 -> C S2\n" + 
				"S2 -> a B b | d\n" + 
				"", results.get(results.size()-1).getDefinition());
	}
	
	
	
}
