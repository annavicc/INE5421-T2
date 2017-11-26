package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ContextFreeLanguage.CFGOperator;
import ContextFreeLanguage.ContextFreeGrammar;

class FactoringTest {
	private ContextFreeGrammar grammar[];
	
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

}
