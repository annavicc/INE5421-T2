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
	
	@Test
	void testFollowSetG0() {
		CFGOperator op = new CFGOperator(grammar[0]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");

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
	
//	@Test
	void testFollowSetG1() {
		CFGOperator op = new CFGOperator(grammar[1]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");
		Set<String> followD = follow.get("D");
		Set<String> followE = follow.get("E");
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
	
	@Test
	void testFollowSetG2() {
		CFGOperator op = new CFGOperator(grammar[2]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> expectedFollow = new HashSet<String>();
		
		expectedFollow.add("$");
		assertTrue(followS.equals(expectedFollow));
		assertTrue(followA.equals(expectedFollow));
		assertTrue(followB.equals(expectedFollow));
	}
	
	@Test
	void testFollowSetG3() {
		CFGOperator op = new CFGOperator(grammar[3]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("B");
		Set<String> followB = follow.get("C");
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

	@Test
	void testFollowSetG4() {
		CFGOperator op = new CFGOperator(grammar[4]);
		HashMap<String, Set<String>> follow = op.getFollowSet();

		Set<String> followS = follow.get("S");
		Set<String> expectedFollow = new HashSet<String>();

		expectedFollow.add("$");
		expectedFollow.add(")");
		assertTrue(followS.equals(expectedFollow));
		expectedFollow.clear();
	}
	
	@Test
	void testFollowSetG5() {
		CFGOperator op = new CFGOperator(grammar[5]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");
		Set<String> followD = follow.get("D");
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
	
	@Test
	void testFollowSetG6() {
		CFGOperator op = new CFGOperator(grammar[6]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
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

	@Test
	void testFollowSetG7() {
		CFGOperator op = new CFGOperator(grammar[7]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followE = follow.get("E");
		Set<String> followE1 = follow.get("E1");
		Set<String> followT = follow.get("T");
		Set<String> followT1 = follow.get("T1");
		Set<String> followF = follow.get("F");
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
	
	@Test
	void testFollowSetG8() {
		CFGOperator op = new CFGOperator(grammar[8]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");
		Set<String> followD = follow.get("D");
		Set<String> followE = follow.get("E");
		Set<String> followG = follow.get("G");
		Set<String> followH = follow.get("H");
		Set<String> followJ = follow.get("J");
		Set<String> followZ = follow.get("Z");
		Set<String> followK = follow.get("K");
		Set<String> followM = follow.get("M");
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
	
	@Test
	void testFollowSetG9() {
		CFGOperator op = new CFGOperator(grammar[9]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");
		Set<String> followD = follow.get("D");
		Set<String> followE = follow.get("E");
		Set<String> followG = follow.get("G");
		Set<String> followH = follow.get("H");
		Set<String> followJ = follow.get("J");
		Set<String> followZ = follow.get("Z");
		Set<String> followK = follow.get("K");
		Set<String> followM = follow.get("M");
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
	
	@Test
	void testFollowSetG10() {
		CFGOperator op = new CFGOperator(grammar[10]);
		HashMap<String, Set<String>> follow = op.getFollowSet();
		
		Set<String> followX = follow.get("X");
		Set<String> followY = follow.get("Y");
		Set<String> followZ = follow.get("Z");
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
	
	@Test
	void testFirstSetG11() {
		CFGOperator op = new CFGOperator(grammar[11]);
		HashMap<String, Set<String>> follow = op.getFollowSet();

		Set<String> followS = follow.get("S");
		Set<String> followA = follow.get("A");
		Set<String> followB = follow.get("B");
		Set<String> followC = follow.get("C");
		Set<String> followD = follow.get("D");
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
