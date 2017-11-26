package ContextFreeLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for
 * First, Follow and First-NT sets for a CFG
 * as well as factoring and eliminating
 * left-recursion for a CFG
 *
 */
public class CFGOperator {
	private ContextFreeGrammar grammar; // the grammar it operates on
	private Set<String> vn;	// non terminal symbols
	private Set<String> vt;	// terminal symbols
	private HashMap<String, Set<String>> grammarFollows; // the grammar follow
	private HashMap<String, Set<String>> first; // the grammar first set
	private HashMap<String, Set<String>> firstNT; // the grammar firstNT SET
	
	public CFGOperator(ContextFreeGrammar g) {
		this.grammar = g;
		this.vn = grammar.getVn();
		this.vt = grammar.getVt();
		// Initializes sets
		setFirst();
		setFirstNT();
		setFollow();
	}
	
	/**
	 * Transform a production in the form "a A B c"
	 * into an array X with separated symbols
	 * X [0] = a
	 * X [1] = A
	 * X [2] = B
	 * X [3] = c
	 * @param s the production
	 * @return the array with symbols of the production
	 */
	private ArrayList<String> breakSententialForm(String s) {
		String[] br = s.split("[\\s\\r]+");
		ArrayList<String> list = new ArrayList<String>();
		for (String str : br) {
			if (!str.isEmpty()) {
				list.add(str);
			}
		}
		return list;
	}
	
	/**
	 * Get first set for all sentencial form alfa
	 * @return the first set for every string
	 */
	public HashMap<String, Set<String>> getFirst() {
		return first;
	}
	
	/**
	 * Get the first set for a specific alfa
	 * @param alfa the string to get the first set from
	 * @return the first set
	 */
	public Set<String> getFirst(String alfa) {
		if(!first.containsKey(alfa)) {
			Set<String> f= new HashSet<>();
			f.add(alfa);
			return f;
		}
		return first.get(alfa);
	}
	
	/**
	 * Private function to initialize the first set
	 */
	private void setFirst(){
		Set<String> set, aux;
		ArrayList<String> list;
		first = new HashMap<>();
		boolean wasChanged = true; // avoid infinite loop from recursion
		
		// Initializes first set for every non terminal from the grammar
		for(String nonTerminal : vn) {
			first.put(nonTerminal, new HashSet<>());
		}
		
		// Construct first set to dynamically check it later
		while(wasChanged) {
			wasChanged = false;
			for(String nonTerminal : vn) {
				set = new HashSet<>();
				aux = new HashSet<>();
				list = new ArrayList<>();
				// For every production of non terminal nt, get the first set of each
				for(String prod : grammar.getGrammarProductions(nonTerminal)){
					list = breakSententialForm(prod); // get every symbol from the production
					aux = getProductionFirstSet(list); // first set for the current symbol
					set.addAll(aux); // Add to the first set of nt
				}
				
				// Construct the set until it is being changed
				if (first.get(nonTerminal).addAll(set)) {
					wasChanged = true;
				}
			}
		}
	}
	
	/**
	 * Get the first set for a given production prod E P
	 * @param production the production from a given vN
	 * @return the first set of the production
	 */
	public Set<String> getProductionFirstSet(List<String> production){
		Set<String> firstSet = new HashSet<>();
		Set<String> nextFirst = null;
		String prodSymbol;
		boolean goToTheNext;
		int symbolCount = 0; // count the number of symbols traversed
		
		prodSymbol = production.get(symbolCount); // first symbol of the production
		goToTheNext = true;
		
		// If it is a terminal
		if(vt.contains(prodSymbol)) {
			firstSet.add(prodSymbol); // the first set is the terminal
		} else {
			// while there is epsilon and prod symbol is a vN, go to the next
			while(goToTheNext) {
				// if it's a terminal
				if(vt.contains(prodSymbol)) {
					nextFirst = null;
					firstSet.add(prodSymbol);
					goToTheNext = false;
				} else { // non terminal
					nextFirst = getFirst().get(prodSymbol); // get the first set for the vn
					firstSet.addAll(nextFirst);
					firstSet.remove("&"); // Epsilon belongs to first only if it was the last production
					if (nextFirst.contains("&")) { // There's epsilon production
						if(++symbolCount >= production.size()){
							goToTheNext = false;
							firstSet.add("&");
						} else {
							prodSymbol = production.get(symbolCount);
						}
					} else { // No epsilon
						goToTheNext = false;
					}
				}
			}
		}
		return firstSet;
	}

	/**
	 * Get the firstNT for every vN from the grammar
	 * @return the set of firstNT
	 */
	public HashMap<String, Set<String>> getFirstNT() {
		return firstNT;
	}
	
	/**
	 * Get the firstNT for a given vn from the grammar
	 * @param nt the non terminal to get the firstNT set from
	 * @return the firstNT set
	 */
	public Set<String> getFirstNT(String nt){
		return getFirstNT().get(nt);
	}
	
	/**
	 * Constructs the firstNT set for every vn from the grammar
	 */
	private void setFirstNT(){
		firstNT = new HashMap<>();
		Set<String> set;
		boolean wasChanged = true; // avoid infinite loop from recursion
		
		// Initializes firstNT set for every vn from the grammar
		for(String nt : vn) {
			firstNT.put(nt, new HashSet<>());			
		}
		
		// Do it only while the set was changed
		while(wasChanged){
			wasChanged = false;
			// for every vn from the grammar
			for(String nt : vn) {
				set = new HashSet<>();
				// for every prod from the vn
				for(String prod : grammar.getGrammarProductions(nt)) {
					set.addAll(getProductionFirstNT(nt, breakSententialForm(prod)));
				}
				// if the set was changed
				if (firstNT.get(nt).addAll(set)) {
					wasChanged = true;
				}
			}
		}
	}
		
	/**
	 * Get the firstNT set for a given production of the grammar
	 * @param vn the non terminal the production belongs to
	 * @param production of the grammar
	 * @return the firstNT set for the production of vn
	 */
	private Set<String> getProductionFirstNT(String vn, ArrayList<String> production){
		Set<String> firstNTSet = new HashSet<>();
		Set<String> nextFirstNT = null;
		String prodSymbol;
		boolean goToTheNext;
		int symbolCount = 0;
		
		prodSymbol = production.get(symbolCount);
		goToTheNext = true;
				
		while(goToTheNext) {
			goToTheNext = false;
			if(!vt.contains(prodSymbol)) { // symbol is not a terminal
				firstNTSet.add(prodSymbol);
				nextFirstNT = getFirstNT().get(prodSymbol); // get the firstNT set for the symbol
				firstNTSet.addAll(nextFirstNT); // add the set to the firstNTSet
				// for every production of the symbol
				for(String prod : grammar.getGrammarProductions(prodSymbol)){
					if(breakSententialForm(prod).contains("&")) { // if it contains epsilon
						goToTheNext = true; // go to the next symbol to get the firstNT
						if(++symbolCount >= production.size()) { // verify the size of the production
							goToTheNext = false; // if it is the last vn from the production, return
						}
						else {
							prodSymbol = production.get(symbolCount); // get the next symbol
						}
					}
				}
				
			}
		}
		return firstNTSet;
	}

	/**
	 * Get the follow set for every vn in the grammar
	 * @return the follow set for every vn
	 */
	public HashMap<String, Set<String>> getFollow() {
		return this.grammarFollows;
	}
	
	/**
	 * Get the follow set for a given vn
	 * @param vn the vn to get the follow set from
	 * @return the follow set of vn
	 */
	public Set<String> getFollow(String vn){
		return getFollow().get(vn);
	}
	
	/**
	 * Verify if a given non terminal nt contains useless symbols
	 * @param nt the nt from the grammar
	 * @return true if it contains useless symbols
	 */
	private boolean isUselessSymbol(String nt) {
		Set<String> productions = grammar.getGrammarProductions(nt);
		if (productions.size() < 1) { // if nt doesn't have productions
			return true;
		}
		// for every production of the nt
		for (String s : productions) {
			ArrayList<String> sForm = breakSententialForm(s);
			for (String str : sForm) {
				if (vn.contains(str)) {
					if(grammar.getGrammarProductions(str).size() < 1) {
						return true;
					}
				}
			}
		}	
		return false;
	}
	
	/**
	 * Constructs the follow set for every vn from the grammar
	 */
	private void setFollow(){
		grammarFollows = new HashMap<>();
		ArrayList<String> production;
		String prodSymbol;
		boolean wasChanged = true;
		boolean goToTheNext = true;
		int symbolCount = 0;
		//Initializes non terminals
		for (String nonTerminal : vn) {
			grammarFollows.put(nonTerminal, new HashSet<>());
		}
		// Add empty symbol to the initial symbol of the grammar
		grammarFollows.get(grammar.getInitialSymbol()).add("$");
		//for every vn from the grammar
		for (String nonTerminal : vn) {
			// For every production from the non terminal
			for(String prod : grammar.getGrammarProductions(nonTerminal)) {
				production = breakSententialForm(prod);
				// Check every symbol from the production
				for (int i = 0; i < production.size(); i++) {
					prodSymbol = production.get(i);
					if (!vt.contains(prodSymbol)) { // symbol is not a terminal
						if (i < production.size() - 1) {
							// Add to the set the first set of the symbol
							grammarFollows.get(prodSymbol).addAll(
									getProductionFirstSet(
											production.subList(i+1, production.size())));
						}
					}
				}
			}
		}
		while (wasChanged) {
			wasChanged = false;
			// for every non terminal
			for (String nonTerminal : vn) {
				// for every production of the non terminal
				for (String prod : grammar.getGrammarProductions(nonTerminal)) {
					production = breakSententialForm(prod);
					symbolCount = production.size() - 1; // last symbol of the production
					goToTheNext = true;
					while (goToTheNext) {
						prodSymbol = production.get(symbolCount); // get the current symbol
						if(vn.contains(prodSymbol)) { // symbol is vn
							if (isUselessSymbol(prodSymbol)) { // verify if it is useless
								if (grammarFollows.get(prodSymbol).add("$")) { // add empty symbol if so
									wasChanged = true;
								}
							} else { // not useless symbol
								// add to the follow set the follow set from the non terminal
								if (grammarFollows.get(prodSymbol).addAll(grammarFollows.get(nonTerminal))) {
									wasChanged = true;
								}
							}
							// Verify if it contains epsilon
							if (first.get(prodSymbol).contains("&")){
								// if the whole production was traversed
								if(--symbolCount < 0) {
									goToTheNext = false;
								}
							} else { // go to the previous symbol
								goToTheNext = false;
							}
						} else { // terminal
							break;
						}
					}
				}
			}
		}
		// Remove epsilon productions from the follow set
		for(String nonTerminal : vn) {
			grammarFollows.get(nonTerminal).remove("&");
		}
	}
	
	/**
	 * Prints the follow set for every vn
	 */
	public void printFollowSet() {
		System.out.println("Follow:");
		for (String s : grammarFollows.keySet()) {
			System.out.print(s + ": ");
			for (String c : grammarFollows.get(s)) {
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints the first set for every vn
	 * @param nt
	 */
	public void printFirstSet(String nt) {
		System.out.print("First(" + nt + "): ");
		for (String s : getFirst(nt)) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
	/**
	 * Prints the firstNT set for every vn
	 * @param nt
	 */
	public void printFirstNTSet(String nt) {
		System.out.print("FirstNT(" + nt + "): ");
		for (String s : getFirstNT().get(nt)) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
	/**
	 * Verify if the grammar is factored
	 * @return true if it is factored
	 */
	public boolean isFactored() {
		Set<String> aux, firstSet, productions;
		// For every non terminal
		for (String nt : this.vn) {
			firstSet = new HashSet<String>();
			productions = grammar.getGrammarProductions(nt);
			// For every production from the vn
			for (String prod: productions) {
				aux = getProductionFirstSet(breakSententialForm(prod));
				// For every symbol of the production
				for (String prodSymbol : aux) {
					// If the first set already contains the symbol
					if (firstSet.contains(prodSymbol)) {
						if (prodSymbol.equals("&")) {
							// only not factored if both productions go to epsilon
							if (aux.size() == 1) {
								return false;
							}
						}
						else if (!prodSymbol.equals("&")) {
							return false;
						}
					}
				}
				firstSet.addAll(aux);
			}
		}
		return true;
	}
	
	/**
	 * Verify if the grammar has leftRecurion
	 * @return true if it has left recursion
	 */
	public boolean hasLeftRecursion() {
		// For every vn
		for (String nt: this.vn) {
			// If the firstNT set from the current non terminal contains itself
			if (getFirstNT(nt).contains(nt)) {
				return true;
			}
		}
		return false;
	}
	
}
