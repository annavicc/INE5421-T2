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
	
	/**
	 * Attempt to factor the grammar in n steps
	 * @return the resulting grammars attempts of factoring
	 */
	public ArrayList<ContextFreeGrammar> factorGrammar(int steps) {
		ArrayList<ContextFreeGrammar> attempts = new ArrayList<>();
		if (isFactored()) {
			return attempts;
		}
		
		int i = 1;
		ContextFreeGrammar previous = grammar;
		while (i <= steps) {
			ContextFreeGrammar g = factorGrammar(previous);
			g.setId(grammar.getId() + " [F" + i + "]");
			CFGOperator op = new CFGOperator(g);
			if (op.isFactored()) {
				break;
			}
			attempts.add(g);
			previous = g;
			i++;
		}
		return attempts;
	}
	
	
	/**
	 * Transforms production set into an array list 
	 * @param p the production set
	 * @return the set into an arraylist
	 */
	private ArrayList<String> getProdList(Set<String> p) {
		ArrayList<String> l = new ArrayList<>();
		for (String s : p) {
			l.add(s);
		}
		return l;
	}
	
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
		boolean goToTheNext = true; // if there is indirect non factoring
		
		for(String nonTerminal : newG.getVn()) {
			prod = newG.getGrammarProductions(nonTerminal);
			ArrayList<String> prods = getProdList(prod);
			for(int i = 0; i < prods.size(); i++){
				nfProd1 = breakSententialForm(prods.get(i));
				for(int j = i+1; j<prods.size(); j++){
					nfProd2 = breakSententialForm(prods.get(j));
					if(nfProd1.get(0).equals(nfProd2.get(0))) { // Direct
						newG.removeProduction(nonTerminal, prods.get(i));
						newG.removeProduction(nonTerminal, prods.get(j));
						newProductions.add(directFactor(nonTerminal, nfProd1, nfProd2));
						if (!(vt.contains(nfProd1.get(0)) && vt.contains(nfProd2.get(0)))) {
							goToTheNext = true;
						}
					}
					// Indirect
					if(goToTheNext) {
						Set<String> firstTmp1 = getProductionFirstSet(nfProd1);
						Set<String> firstTmp2 = getProductionFirstSet(nfProd2);
						Set<String> firstVn1 = getProductionFirstNT(nonTerminal, nfProd1);
						Set<String> firstVn2 = getProductionFirstNT(nonTerminal, nfProd2);
						 
						if (!intersectionisEmpty(firstTmp1, firstTmp2) || !intersectionisEmpty(firstVn1, firstVn2)) {
							HashMap<String, HashSet<String>> ind1 = indirectFactor(nonTerminal, nfProd1.get(0));
							HashMap<String, HashSet<String>> ind2 = indirectFactor(nonTerminal, nfProd2.get(0));
							newProductions.add(ind1);
							newProductions.add(ind2);
							newG.removeProduction(nonTerminal, prods.get(i));
							newG.removeProduction(nonTerminal, prods.get(j));
						}
					}
				}
				goToTheNext = true;
			}
		}
		
		for (HashMap<String, HashSet<String>>  map : newProductions) {
				for (String key: map.keySet()) {
					g.addVn(key);
					for (String pp : map.get(key)){ 
						if (pp.equals("&")) { // if an epsilon was added, add & to the grammar vt set
							g.addVt("&");
						}
						Set<String> s = g.getGrammarProductions(key);
						s.add(pp);
						g.addProduction(key, s);
					}
				}
		}
		System.out.println(newG.getDefinition());
		return newG;
	}
	
	/**
	 * Factor indirect not factoring situations
	 * @param vn the non terminal whose productions are not factored
	 * @param nt the not factored symbol
	 * @return the new set of productions after 1 step of fatoration
	 */
	private HashMap<String, HashSet<String>> indirectFactor(String vn, String nf) {
			HashMap<String, HashSet<String>> prod = new HashMap<String, HashSet<String>>();
			Set<String> newS = grammar.getGrammarProductions(vn);
			for (String vnP : newS) {
				ArrayList<String> symb = breakSententialForm(vnP);
				if (symb.get(0).equals(nf)) {
					String newP = "";;
					for (int i = 0; i < symb.size(); i++) {
						if (i == 0) {
							newP = "";
						} else {
							newP += symb.get(i) + " ";
						}
					}
				HashSet<String> existing = (HashSet<String>) grammar.getGrammarProductions(nf);
				HashSet<String> set = new HashSet<>();
				for (String s : existing) {
					String str = s + " " + newP;
					set.add(str);
				}
				prod.put(vn, set);
				}
			}
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
	private HashMap<String, HashSet<String>> directFactor(String vn, ArrayList<String> prod1, ArrayList<String> prod2) {
		HashMap<String, HashSet<String>> prod = new HashMap<String, HashSet<String>>();
		String common = "";
		String p1 = "";
		String p2 = "";
		int length = prod2.size();
		
		if (prod1.size() < prod2.size()) {
			length = prod1.size();
		}
		int i = 0;
		for (; i < length; i++) {
			if(prod1.get(i).equals(prod2.get(i))) {
				common += prod1.get(i) + " ";
			} else {
				common = common.substring(0, common.length()-1);
				break;
			}
		}
		
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
		
		prod.put(vn+""+1, new HashSet<>());
		HashSet<String> pset = new HashSet<>();
		pset.add(common + " " + vn + "" + 1);
		prod.put(vn, pset);
		
		if (p2.equals("") || p1.equals("")) {
			pset = prod.get(vn + "" + 1);
			pset.add("&");
			prod.put(vn + "" + 1, pset);
		}
		if (!p1.equals("")) {
			pset = prod.get(vn + "" + 1);
			pset.add(p1);
			prod.put(vn + "" + 1, pset);
		}
		if (!p2.equals("")) {
			pset = prod.get(vn + "" + 1);
			pset.add(p2);
			prod.put(vn + "" + 1, pset);
		}
		return prod;
	}
	
	// TODO transform G in proper
	public ArrayList<ContextFreeGrammar> eliminateLeftRecursion() {
		ArrayList<ContextFreeGrammar> results = new ArrayList<>();
		if (!hasLeftRecursion()) {
			return results;
		}
		return results;
		
	}
	
}