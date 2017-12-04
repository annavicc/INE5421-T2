package ContextFreeLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		if (!isVnVt(alfa)) { // if a symbol does not belong to vn vt
			return null;
		}
		if(!first.containsKey(alfa)) {
			Set<String> f = new HashSet<>();
			ArrayList<String> symbols = breakSententialForm(alfa);
			for (int i = 0; i < symbols.size(); i++) {
				if (vt.contains(symbols.get(i)) || isEpsilonProductions(alfa)) {
					f.add(alfa);
					break;
				} else {
					if (vn.contains(symbols.get(i))) {
						f.addAll(getProductionFirstSet(symbols));
						if (f.contains("&") && i+1 < symbols.size()) {
							f.remove("&");
							continue;
						} else {
							break;
						}
					}
				}
			}
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
					if (firstSet.contains("&")) {
						firstSet.remove("&"); // Epsilon belongs to first only if it was the last production
					}
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
	public Set<String> getFirstNT(String nt) {
		if (!isVnVt(nt)) { // if a symbol does not belong to vn vt
			return null;
		}
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
		if (!isVnVt(vn)) { // if a symbol does not belong to vn vt
			return null;
		}
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
							return false;
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
	
	/**
	 * Attempt to factor the grammar in n steps
	 * @return the resulting grammars attempts of factoring
	 */
	public ArrayList<ContextFreeGrammar> factorGrammar(int steps) {
		ArrayList<ContextFreeGrammar> attempts = new ArrayList<>();
		if (isFactored()) {
			return attempts;
		}
		
		ContextFreeGrammar previous = new ContextFreeGrammar(grammar);
		CFGOperator newOp = new CFGOperator(previous);
		int i = 1;
		while (i <= steps) {
			ContextFreeGrammar g = new ContextFreeGrammar(newOp.factorGrammar(previous));
			g.setId(grammar.getId() + " [F" + i + "]");
			newOp = new CFGOperator(g);
			attempts.add(g);
			if (newOp.isFactored()) {
				break;
			}
			previous = new ContextFreeGrammar(g);
			i++;
		}
		return attempts;
	}
	
	
	/**
	 * Transforms production set into an array list 
	 * @param p the production set
	 * @return the set into an array list
	 */
	private ArrayList<String> getProdList(Set<String> p) {
		ArrayList<String> l = new ArrayList<>();
		for (String s : p) {
			if (s.isEmpty()) {
				continue;
			}
			l.add(s);
		}
		return l;
	}
	
	/**
	 * Helper method to create a next vn S1, S2... when factoring
	 * @param s the current vn
	 * @return vn + 1
	 */
	private String createNextVN(String s) {
		if (s.length() == 1) {
			return s + "" + 1;
		}
		String newVn = s.substring(1, s.length());
		
		int number = Integer.parseInt(newVn) + 1;
		return s.charAt(0) + "" + number;
	}
	
	//TODO: Factoring not working for some grammars (infinite loop)
	/*
	 	Eg.:
	  	S -> B b | C d 
		B -> C a B | & 
		C -> c C | & | B
	*/
	
	/**
	 * Factor a given grammar in 1 step.
	 * If not factored in 1 step, call this function again
	 * @param g the grammar to be factored
	 * @return a 1 step factored grammar (can be factored or not)
	 */
	public ContextFreeGrammar factorGrammar(ContextFreeGrammar g) {
		ContextFreeGrammar newG = new ContextFreeGrammar(g); // The new grammar
		Set<String> prod = null;
		ArrayList<String> nfProd1 = null; // not factored prod1
		ArrayList<String> nfProd2 = null; // not factored prod2		
		// New productions
		ArrayList<HashMap<String, HashSet<String>>> newProductions = new ArrayList<HashMap<String, HashSet<String>> >();

		// Indirect
		for(String nonTerminal : newG.getVn()) {
			prod = newG.getGrammarProductions(nonTerminal);
			ArrayList<String> prods = getProdList(prod);
			for(int i = 0; i < prods.size(); i++) {
				nfProd1 = breakSententialForm(prods.get(i));
				String symbP1 = nfProd1.get(0);
				for(int j = i+1; j < prods.size(); j++) {
					nfProd2 = breakSententialForm(prods.get(j));
					String symbP2 = nfProd2.get(0);
					if (vn.contains(symbP1) || vn.contains(symbP2)) {
						if (symbP1.equals(symbP2)) {
							continue;
						}
						if (symbP1.equals(nonTerminal)) {
							continue;
						}
						Set<String> firstTmp1 = getProductionFirstSet(nfProd1);
						Set<String> firstTmp2 = getProductionFirstSet(nfProd2);
						Set<String> firstVn1 = getProductionFirstNT(nonTerminal, nfProd1);
						Set<String> firstVn2 = getProductionFirstNT(nonTerminal, nfProd2);
						if (!intersectionisEmpty(firstTmp1, firstTmp2) || !intersectionisEmpty(firstVn1, firstVn2)) {
							
							if (vn.contains(symbP1)) {
								HashMap<String, HashSet<String>> ind1 = indirectFactor(newG, nonTerminal, nfProd1);
								newG.removeProduction(nonTerminal, prods.get(i));
								newProductions.add(ind1);
							}
							if (vn.contains(symbP2)) {
								HashMap<String, HashSet<String>> ind2 = indirectFactor(newG, nonTerminal, nfProd2);
								newG.removeProduction(nonTerminal, prods.get(j));
								newProductions.add(ind2);
							}
							
						}
					}
				}
			}
		}
		for (HashMap<String, HashSet<String>>  map : newProductions) {
			for (String key: map.keySet()) {
				newG.addVn(key);
				for (String pp : map.get(key)){ 
					if (isEpsilonProductions(pp)) { // if an epsilon was added, add & to the grammar vt set
						newG.addVt("&");
					}
					Set<String> s = newG.getGrammarProductions(key);
					s.add(pp);
					newG.addProduction(key, s);
				}
			}
		}
		
		newProductions.clear();
		
		for(String nonTerminal : newG.getVn()) {
			HashMap<String, String> created = new HashMap<>();
			prod = newG.getGrammarProductions(nonTerminal);
			ArrayList<String> prods = getProdList(prod);
			String newNT = createNextVN(nonTerminal);
			for(int i = 0; i < prods.size(); i++) {
				nfProd1 = breakSententialForm(prods.get(i));
				for(int j = i+1; j < prods.size(); j++) {
					nfProd2 = breakSententialForm(prods.get(j));
					if(nfProd1.get(0).equals(nfProd2.get(0))) { // Direct
						if(created.containsKey(nfProd1.get(0))) {
							newNT = created.get(nfProd1.get(0));
						} else {
							created.put(nfProd1.get(0), newNT); // last prod created S -> eS1
							newNT = createNextVN(newNT);
						}
						newG.removeProduction(nonTerminal, prods.get(i));
						newG.removeProduction(nonTerminal, prods.get(j));
						newProductions.add(directFactor(created.get(nfProd1.get(0)), nonTerminal, nfProd1, nfProd2));
					}
				}
			}
		}
		for (HashMap<String, HashSet<String>>  map : newProductions) {
			for (String key: map.keySet()) {
				newG.addVn(key);
				for (String pp : map.get(key)){ 
					if (isEpsilonProductions(pp)) { // if an epsilon was added, add & to the grammar vt set
						newG.addVt("&");
					}
					Set<String> s = newG.getGrammarProductions(key);
					s.add(pp);
					newG.addProduction(key, s);
				}
			}
		}
		return newG;
	}
	
	/**
	 * Factor indirect not factoring situations
	 * @param vn the non terminal whose productions are not factored
	 * @param nt the not factored symbol
	 * @return the new set of productions after 1 step of factoring
	 */
	private HashMap<String, HashSet<String>> indirectFactor(ContextFreeGrammar newG, String vn, ArrayList<String> nfProd) {
			HashMap<String, HashSet<String>> prod = new HashMap<String, HashSet<String>>();
			HashSet<String> set = new HashSet<>();
			Set<String> nfPr = newG.getGrammarProductions(nfProd.get(0));
			for (String vnP : nfPr) {
				String newP = vnP + " ";
				if (newP.trim().equals("&") && nfProd.size() > 1) {
					newP = "";
				}
				for (int i = 1; i < nfProd.size(); i++) {
					newP = newP + nfProd.get(i) + " ";
				}
				
				newP = newP.trim().replaceAll(" +", " ");
				
				set.add(newP);
			}
			
			prod.put(vn, set);
			return prod;	
	}
	
	/**
	 * Verify if the intersection between 2 sets is empty
	 * @param s1 the first set
	 * @param s2 the second set
	 * @return true if the intersection is empty
	 */
	public boolean intersectionisEmpty(Set<String> s1, Set<String> s2) {
		for (String s : s1) {
			if (s2.contains(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Factor direct not factoring situations
	 * @param vn the non terminal whose productions are not factored
	 * @param prod1 the production that is not factored
	 * @param prod2 the production that is not factored
	 * @return the new set of productions after the factoring process
	 */
	private HashMap<String, HashSet<String>> directFactor(String index, String vn, ArrayList<String> prod1, ArrayList<String> prod2) {
		HashMap<String, HashSet<String>> prod = new HashMap<String, HashSet<String>>();
		String common = "";
		String p1 = "";
		String p2 = "";
		HashSet<String> pset = new HashSet<>();
		common = prod1.get(0);
		if (common.equals("&")) {
			pset.add("&");
			prod.put(vn, pset);
			return prod;
			
		}

		int i = 1;

		for (int j = i; j < prod1.size(); j++) {
			p1 += prod1.get(j) + " ";
		}
		for (int j = i; j < prod2.size(); j++) {
			p2 += prod2.get(j) + " ";
		}
		if (p2.length() > 1) {
			p2 = p2.substring(0, p2.length() - 1);
		}
		if (p1.length() > 1) {
			p1 = p1.substring(0, p1.length()-1);
		}
		
		String newP = common + " " + index;
		newP = newP.trim().replaceAll(" +", " ");
		pset.add(newP);
		prod.put(vn, pset);
		prod.put(index, new HashSet<>());
		
		if (p2.equals("") || p1.equals("")) {
			pset = prod.get(index);
			pset.add("&");
			prod.put(index, pset);
		}
		if (!p1.equals("")) {
			pset = prod.get(index);
			pset.add(p1);
			prod.put(index, pset);
		}
		if (!p2.equals("")) {
			pset = prod.get(index);
			pset.add(p2);
			prod.put(index, pset);
		}
		return prod;
	}
	
	/**
	 * Eliminate left recursion of the grammar
	 * @return all the resulting grammars from every step of the recursion elimination process
	 */
	public ContextFreeGrammar eliminateLeftRecursion() {
		ArrayList<ContextFreeGrammar> results = new ArrayList<>();
		ContextFreeGrammar newG = grammar;
		ArrayList<HashMap<String, HashSet<String>>> newProd;
		ArrayList<String> numberedVn = new ArrayList<>();
		for (String nt : grammar.getVn()) { // ordered vn
			numberedVn.add(nt);
		}
		
		if (!hasLeftRecursion()) {
			return grammar;
		}
		
		for (int i = 0; i < numberedVn.size(); i++) { // for every Ai
			newG = ContextFreeGrammar.isValidCFG(newG.getDefinition()); // new grammar
			CFGOperator newOp = new CFGOperator(newG);
			
			// Indirect Left Recursion
			for (int j = 0; j <= i-1; j++) { // For every Aj
				ArrayList<String> productions = newOp.getProdList(newG.getGrammarProductions(numberedVn.get(i)));
				for (String aiProd : productions) { // Ai -> ... 
					String firstSymbolAi = newOp.breakSententialForm(aiProd).get(0); // first symbol of prod in Ai
					if(newOp.vt.contains(firstSymbolAi)) { // terminal
						continue;
					}
					if  (firstSymbolAi.equals(numberedVn.get(j))) { // Ai -> Ajalfa
						newProd = (replaceIndirectRecursionProduction(numberedVn.get(i), numberedVn.get(j)));
						newG.removeProduction(numberedVn.get(i), aiProd);
						for (HashMap<String, HashSet<String>>  map : newProd) {
							for (String key: map.keySet()) {
								newG.addVn(key);
								for (String pp : map.get(key)){ 
									if (newOp.isEpsilonProductions(pp)) { // if an epsilon was added, add & to the grammar vt set
										newG.addVt("&");
									}
									Set<String> s = newG.getGrammarProductions(key);
									if (!pp.isEmpty()) {
										s.add(pp);
									}
									newG.addProduction(key, s);
								}
							}
						}
					}
				}
			}
			// Eliminate direct recursion of Ai productions
			if (newOp.hasDirectRecursion(numberedVn.get(i))) {
				newProd = newOp.replaceDirectRecursionProduction(numberedVn.get(i));
				newG.removeProductions(numberedVn.get(i)); // remove productions with recursion
				for (HashMap<String, HashSet<String>>  map : newProd) {
					for (String key: map.keySet()) {
						newG.addVn(key);
						for (String pp : map.get(key)){ 
							if (isEpsilonProductions(pp)) { // if an epsilon was added, add & to the grammar vt set
								newG.addVt("&");
							}
							Set<String> s = newG.getGrammarProductions(key);
							if (!pp.isEmpty()) {
								s.add(pp);
							}
							newG.addProduction(key, s);
						}
					}
				}
			}
			results.add(newG);
		}
		ContextFreeGrammar result = results.get(results.size()-1);
		result.setId(grammar.getId() + " [-LR]");
		return results.get(results.size()-1);
	}
	
	/**
	 * Verify if a given non terminal contains direct left recursion
	 * @param ai the non terminal to verify
	 * @return true if there is direct recursion
	 */
	public boolean hasDirectRecursion(String ai) {
		for (String prod : grammar.getGrammarProductions(ai)) {
			ArrayList<String> aiProdSymbols = breakSententialForm(prod);
			String firstSymbolAi = aiProdSymbols.get(0);
			if (firstSymbolAi.equals(ai)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Replace Direct recursion
	 * A -> Aalfa1 | Aalfa2 | beta1 | ...
	 * A -> beta1A1 | ...
	 * A1 -> alfa1A1 | ... | &
	 * @param ai the non terminal to verify the productions
	 * @return the new production rules for ai
	 */
	ArrayList<HashMap<String, HashSet<String>>> replaceDirectRecursionProduction(String ai) {
		ArrayList<HashMap<String, HashSet<String>>> ar = new ArrayList<HashMap<String, HashSet<String>>  >();
		HashMap<String, HashSet<String>> newProd = new HashMap<String, HashSet<String>>();
		ArrayList<String> betaProductions = new ArrayList<String>();
		ArrayList<String> alfaProductions = new ArrayList<String>();
		HashSet<String> set;
		String newVn = ai + "" + 1;
		newProd.put(ai, new HashSet<>());
		newProd.put(newVn, new HashSet<>());
		
		for (String prod : grammar.getGrammarProductions(ai)) {
			ArrayList<String> aiProdSymbols = breakSententialForm(prod);
			String firstSymbolAi = aiProdSymbols.get(0);
			if (!firstSymbolAi.equals(ai)) {
				betaProductions.add(prod); // beta production
				continue;
			}
			// recursive symbol
			// Get the alfa part
			String alfa = "";
			for (int i = 1; i < aiProdSymbols.size(); i++) {
				if(!aiProdSymbols.get(i).isEmpty()) {
					alfa += aiProdSymbols.get(i) + " ";
				}
			}
			alfa = alfa.trim().replaceAll(" +", " ");
			alfaProductions.add(alfa);
		}
		
		for (String s: betaProductions) {
			if (isEpsilonProductions(s)) {
				set = newProd.get(ai);
				set.add(newVn);
				newProd.put(ai, set);
				continue;
			}
			set = newProd.get(ai);
			set.add(s + " " + newVn);
			newProd.put(ai, set);
		}
		for (String s : alfaProductions) {
			set = newProd.get(newVn);
			String newP = s + " " + newVn;
			newP = newP.trim().replaceAll(" +", " ");
			set.add(newP);
			newProd.put(newVn, set);
		}
		set = newProd.get(newVn);
		set.add("&");
		newProd.put(newVn, set);
		ar.add(newProd);
		return ar ;
	}
	
	/**
	 * Replace the indirect recursion for the Ai and Aj non terminals
	 * Ai -> Ajgama
	 * Aj -> delta1gama | delta2gama |...
	 * delta = right side of Aj productions (Aj -> delta1 | delta2)
	 * @param ai the terminal with indirect recursion productions
	 * @param aj the terminal with delta productions
	 * @return the new production rules for Ai
	 */
	ArrayList<HashMap<String, HashSet<String>>> replaceIndirectRecursionProduction(String ai, String aj) {
		HashMap<String, HashSet<String>> prod = new HashMap<String, HashSet<String>>();
		prod.put(ai, new HashSet<String>());
		ArrayList<String> aiProd = getProdList(grammar.getGrammarProductions(ai));
		Set<String> ajProd = grammar.getGrammarProductions(aj);

		for (String aiP : aiProd) {
			aiP = aiP.substring(1);
			ArrayList<String> newAiProd = breakSententialForm(aiP);
			if (newAiProd.size() <= 0) {
				continue;
			}
			if (!newAiProd.get(0).equals(aj)) {
				continue;
			} else {
				for (String ajP : ajProd) {
					ajP = ajP.substring(1);
					newAiProd.remove(0);
					if (!isEpsilonProductions(ajP)) {
						newAiProd.add(0, ajP);
					}
					HashSet<String> p = prod.get(ai);
					String newProd = "";
					for (String pr : newAiProd) {
						newProd += pr + " ";
					}
					if (newProd.length() > 0) {
						newProd = newProd.substring(0, newProd.length()-1);
					}
					newProd = newProd.trim().replaceAll(" +", " ");
					p.add(newProd);
					prod.put(ai, p);
				}
			}
		}		
		ArrayList<HashMap<String, HashSet<String>>> ar = new ArrayList<HashMap<String, HashSet<String>>  >();
		ar.add(prod);
		return ar;
	}
	
	public boolean isEpsilonProductions(String s) {
		boolean hasEps = true;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '&') {
				if(!Character.isSpaceChar(s.charAt(i))) {
					hasEps = false;
				}
			}
		}
		return hasEps;
	}
	
	/**
	 * Verify if a given sequence of symbols
	 * belongs to vN U vt
	 * @param str the string to be verified
	 * @return true if all symbols belong to vn U vt
	 */
	private boolean isVnVt(String str) {
		boolean onlySpaces = true;
		for (String s : breakSententialForm(str)) {
			if (s.matches("[\\s]+")) {
				continue;
			} else {
				onlySpaces = false;
				if (!vn.contains(s) && !vt.contains(s)) {
					return false;
				}
			}
		}
		if (onlySpaces) {
			return false;
		}
		return true;
	}
}
