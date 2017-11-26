package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ContextFreeLanguage.CFGOperator;
import ContextFreeLanguage.ContextFreeGrammar;

class FollowSetTest {
	private ContextFreeGrammar grammar[];


	/**
	 * Set up grammars
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		grammar = new ContextFreeGrammar[12];
		grammar[0] = ContextFreeGrammar.isValidCFG(
				"S -> x y z | a B C\n" + 
				"B -> c | c d\n" + 
				"C -> e g | d f\n");
		grammar[1] = ContextFreeGrammar.isValidCFG(
				"S -> A B C D E\n" + 
				"A -> a | &\n" + 
				"B -> b | &\n" + 
				"C -> c\n" + 
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
	}
	
	/**
	 * Test follow set for G0
	 */
	@Test
	void testFollowSetG0() {
		CFGOperator op = new CFGOperator(grammar[0]);

		Set<String> followS = op.getFollow("S");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> expectedFollow = new HashSet<String>();
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("e");
		expectedFollow.add("d");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();

		expectedFollow.add("$");
		assertTrue(followC.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G1
	 */
	@Test
	void testFollowSetG1() {
		CFGOperator op = new CFGOperator(grammar[1]);
		
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> followD = op.getFollow("D");
		Set<String> followE = op.getFollow("E");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("b");
		expectedFollow.add("c");
		assertTrue(followA.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("c");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("d");
		expectedFollow.add("e");
		expectedFollow.add("$");
		assertTrue(followC.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("e");
		expectedFollow.add("$");
		assertTrue(followD.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followE.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G2
	 */
	@Test
	void testFollowSetG2() {
		CFGOperator op = new CFGOperator(grammar[2]);
		
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		assertTrue(followA.equals(expectedFollow));
		assertTrue(followB.equals(expectedFollow));
	}
	
	/**
	 * Test follow set for G3
	 */
	@Test
	void testFollowSetG3() {
		CFGOperator op = new CFGOperator(grammar[3]);
		
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("B");
		Set<String> followB = op.getFollow("C");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("b");
		assertTrue(followA.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("d");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();
	}

	/**
	 * Test follow set for G4
	 */
	@Test
	void testFollowSetG4() {
		CFGOperator op = new CFGOperator(grammar[4]);

		Set<String> followS = op.getFollow("S");
		Set<String> expectedFollow = new HashSet<String>();

		expectedFollow.add("$");
		expectedFollow.add(")");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G5
	 */
	@Test
	void testFollowSetG5() {
		CFGOperator op = new CFGOperator(grammar[5]);
		
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> followD = op.getFollow("D");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("b");
		assertTrue(followA.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followC.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("b");
		assertTrue(followD.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G6
	 */
	@Test
	void testFollowSetG6() {
		CFGOperator op = new CFGOperator(grammar[6]);
		
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		expectedFollow.add("a");
		expectedFollow.add("b");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		expectedFollow.add("a");
		expectedFollow.add("b");
		assertTrue(followS.equals(expectedFollow));
		
		expectedFollow.add("d");
		assertTrue(followA.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		expectedFollow.add("a");
		expectedFollow.add("b");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();
	}

	/**
	 * Test follow set for G7
	 */
	@Test
	void testFollowSetG7() {
		CFGOperator op = new CFGOperator(grammar[7]);
		
		Set<String> followE = op.getFollow("E");
		Set<String> followE1 = op.getFollow("E1");
		Set<String> followT = op.getFollow("T");
		Set<String> followT1 = op.getFollow("T1");
		Set<String> followF = op.getFollow("F");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		expectedFollow.add(")");
		assertTrue(followE.equals(expectedFollow));
		
		assertTrue(followE1.equals(expectedFollow));
		
		expectedFollow.add("+");
		assertTrue(followT.equals(expectedFollow));
		
		assertTrue(followT1.equals(expectedFollow));
		
		expectedFollow.add("*");
		assertTrue(followF.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G8
	 */
	@Test
	void testFollowSetG8() {
		CFGOperator op = new CFGOperator(grammar[8]);
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> followD = op.getFollow("D");
		Set<String> followE = op.getFollow("E");
		Set<String> followG = op.getFollow("G");
		Set<String> followH = op.getFollow("H");
		Set<String> followJ = op.getFollow("J");
		Set<String> followZ = op.getFollow("Z");
		Set<String> followK = op.getFollow("K");
		Set<String> followM = op.getFollow("M");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followA.equals(expectedFollow));
		expectedFollow.clear();

		expectedFollow.add("a");
		expectedFollow.add("e");
		expectedFollow.add("f");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followC.equals(expectedFollow));
		
		assertTrue(followE.equals(expectedFollow));

		expectedFollow.add("e");
		assertTrue(followH.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("a");
		assertTrue(followG.equals(expectedFollow));
		expectedFollow.clear();

		expectedFollow.add("$");
		assertTrue(followM.equals(expectedFollow));
		assertTrue(followD.equals(expectedFollow));
		assertTrue(followJ.equals(expectedFollow));
		assertTrue(followZ.equals(expectedFollow));
		assertTrue(followK.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G9
	 */
	@Test
	void testFollowSetG9() {
		CFGOperator op = new CFGOperator(grammar[9]);
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> followD = op.getFollow("D");
		Set<String> followE = op.getFollow("E");
		Set<String> followG = op.getFollow("G");
		Set<String> followH = op.getFollow("H");
		Set<String> followJ = op.getFollow("J");
		Set<String> followZ = op.getFollow("Z");
		Set<String> followK = op.getFollow("K");
		Set<String> followM = op.getFollow("M");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followA.equals(expectedFollow));
		assertTrue(followC.equals(expectedFollow));
		assertTrue(followE.equals(expectedFollow));
		assertTrue(followD.equals(expectedFollow));
		assertTrue(followM.equals(expectedFollow));
		assertTrue(followJ.equals(expectedFollow));
		assertTrue(followZ.equals(expectedFollow));
		assertTrue(followK.equals(expectedFollow));
		expectedFollow.clear();

		expectedFollow.add("a");
		expectedFollow.add("f");
		assertTrue(followB.equals(expectedFollow));
		expectedFollow.clear();

		expectedFollow.add("e");
		expectedFollow.add("$");
		assertTrue(followH.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("a");
		assertTrue(followG.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G10
	 */
	@Test
	void testFollowSetG10() {
		CFGOperator op = new CFGOperator(grammar[10]);
		
		Set<String> followX = op.getFollow("X");
		Set<String> followY = op.getFollow("Y");
		Set<String> followZ = op.getFollow("Z");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followX.equals(expectedFollow));
		expectedFollow.clear();
		expectedFollow.add("m");
		assertTrue(followY.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followZ.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	/**
	 * Test follow set for G11
	 */
	@Test
	void testFirstSetG11() {
		CFGOperator op = new CFGOperator(grammar[11]);
		Set<String> followS = op.getFollow("S");
		Set<String> followA = op.getFollow("A");
		Set<String> followB = op.getFollow("B");
		Set<String> followC = op.getFollow("C");
		Set<String> followD = op.getFollow("D");
		Set<String> expectedFollow = new HashSet<String>();
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		
		assertTrue(followA.equals(expectedFollow));

		assertTrue(followB.equals(expectedFollow));
		
		expectedFollow.add("a");
		assertTrue(followC.equals(expectedFollow));
		expectedFollow.clear();
		
		expectedFollow.add("$");
		assertTrue(followD.equals(expectedFollow));
		expectedFollow.clear();
	}
	
}
