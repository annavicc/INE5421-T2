package ContextFreeLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for
 * First, Follow and Follow-NT sets for a CFG
 * as well as factoring and eliminating
 * left-recursion for a CFG
 *
 */
public class CFGOperator {
	private ContextFreeGrammar grammar; // the grammar it operates on
	private Set<String> vn;	// non terminal symbols
	private Set<String> vt;	// terminal symbols
	private HashMap<String, Set<String>> grammarFollows;
	
	public CFGOperator(ContextFreeGrammar g) {
		this.grammar = g;
		this.vn = grammar.getVn();
		this.vt = grammar.getVt();
		this.grammarFollows = new HashMap<String, Set<String>>();
	}
	
	/**
	 * Helper method to verify if a production of a
	 * given vn is only &
	 */
	private static boolean  isEpsilonProduction(String prod) {
		for (int i = 0; i < prod.length(); i++) {
			if (prod.charAt(i) != '&') {
				return false;
			}
		}
		return true;
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
	 * Return the first set
	 * First(alfa)
	 * @param alfa the string to get the set from
	 * @return the first set of alfa
	 */
	public Set<String> getFirstSet(String alfa) {
		Set<String> first = new HashSet<String>();
		ArrayList<String> l = breakSententialForm(alfa); // separate each production symbol

		if (l.isEmpty()) { // alfa is empty ""
			return first;
		}
		 
		// if first symbol of alfa is terminal or &, it is the first
		String f = l.get(0);

		if ((vt.contains(f) || isEpsilonProduction(f))
				&& l.size() == 1) {
			first.add(f);
			return first;
		} else {
			// first symbol is vn
			return getFirst(l, new ArrayList<String>());
		}
	}
	
	private Set<String> getFirst(List<String> alfa, List<String> visitedVn) {
		// First(S)
		Set<String> first = new HashSet<String>();
		
		String sForm = alfa.get(0); // A <-     B
		// terminal
		if (this.vt.contains(sForm)) { // first(a) = a
			first.add(sForm);
		}
		
		if (this.vn.contains(sForm)) { //first(S) = ?
			visitedVn.add(sForm); // visitedVn = {S}
			for (String prod : grammar.getGrammarProductions(sForm)) { // A B C D E
		
				ArrayList<String> listProdSymbols = breakSententialForm(prod);
				for (String prodSymbol : listProdSymbols) { // A
					if (vt.contains(prodSymbol)) { // a
						first.add(prodSymbol);
						break;
					}
					if (vn.contains(prodSymbol)) { // A
						Set<String> firstProdSymb = getFirstSet(prodSymbol);
						if (!firstProdSymb.contains("&") && !visitedVn.contains(prodSymbol)) { // no epsilon
							first.addAll(firstProdSymb);
							break;
						} else if (firstProdSymb.contains("&")){ // contains epsilon
							if (listProdSymbols.size() > 1) {
								firstProdSymb.remove("&");
								first.addAll(firstProdSymb);
								Set<String> fst = getFirst(listProdSymbols.subList(1, listProdSymbols.size()), visitedVn);
								if (listProdSymbols.size() > 3) {
									fst.remove("&");
								}
								first.addAll(fst);
							} else if (listProdSymbols.size() == 1){
								first.addAll(firstProdSymb);
							}
						}
					}
				}
			}
		}
		if (first.contains("&") && alfa.size() > 1) {
			sForm = "";
			for (String s : alfa) {
				sForm += s + " ";
			}
			sForm = sForm.substring(1);
			if (vt.contains(sForm)) {
				first.add(sForm);
			} else {
				if (!visitedVn.contains(sForm)) {
					first.addAll(getFirstSet(sForm));
				}
			}
		}
		
		return first;
	}
	
	public Set<String> getFirstNTSet(String alfa) {
		Set<String> firstNT = new HashSet<String>();
		ArrayList<String> listSymbols = breakSententialForm(alfa);
		//		ArrayList<String> l = breakSententialForm(alfa);
		if (listSymbols.isEmpty()) {
			firstNT.add("$");
			return firstNT;
		}
		// if first symbol of alfa is terminal or alfa is &
		String firstSymbol = listSymbols.get(0);
		if (vt.contains(firstSymbol)
				|| (isEpsilonProduction(alfa) && listSymbols.size() == 1)) {
			return firstNT;
		} else {
			// first symbol is vn
			return getFirstNT(listSymbols, new ArrayList<String>());
		}
	}
	
	private Set<String> getFirstNT(List<String> alfa, List<String> visitedVn) {
		Set<String> firstNT = new HashSet<String>();
		
		String sForm = alfa.get(0);
		// if it is the last vN from the sequential form
		// and it is &
		if (alfa.size() == 1 && isEpsilonProduction(sForm)) {
			return firstNT;
		}
		
		// if it is vn
		if (this.vn.contains(sForm)) {
			visitedVn.add(sForm);
			// for every production from vn
			for (String prod : this.grammar.getGrammarProductions(sForm)) {
				ArrayList<String> listSymbols = breakSententialForm(prod);
				// A -> a | & 
				if(vt.contains(listSymbols.get(0)) ||
						(isEpsilonProduction(listSymbols.get(0))
								&& listSymbols.size() ==1)) {
					continue;
				}
				firstNT.add(listSymbols.get(0));
				
				if (!visitedVn.contains(listSymbols.get(0))) {
					firstNT.addAll(getFirstNT(listSymbols, visitedVn));
				}
				if (getFirstSet(listSymbols.get(0)).contains("&")) {
					// If there is &, go to the next
					if (listSymbols.size() > 1) {
						firstNT.addAll(getFirstNT(listSymbols.subList(1, listSymbols.size()), visitedVn));
					}
				}
			}
		}
		// terminal
		return firstNT;
	}
	
	private void setFollowSet() {
		// Initializes follow sets for every vn and add $ to S
		HashSet<String> set, set2;
		for(String nt : this.vn) {
			set = new HashSet<String>();
			if(nt.equals(grammar.getInitialSymbol())) { // add $ to S
				set.add("&");
			}
			grammarFollows.put(nt, set);
		}
		
		// Step 2:
		for(String nt : this.vn) {
			for(String prod : grammar.getGrammarProductions(nt)) {
				ArrayList<String> symbols = breakSententialForm(prod);
				for (String symb : symbols) {
					if (this.vn.contains(symb)) { //vn
						if (prod.indexOf(symb)+1 < prod.length()) {
							Set<String> first = getFirstSet(prod.substring(prod.indexOf(symb)+1));
							first.remove("&");
							set = (HashSet<String>) grammarFollows.get(symb);
							set.addAll(first);
							grammarFollows.put(symb, set);
						}
					}
				}
			}
		}
		
		// Step 3:
		boolean modified = true;
		while(modified) {
			modified = false;
			for(String nt : this.vn) {
				for(String prod : grammar.getGrammarProductions(nt)) {
					ArrayList<String> symbols = breakSententialForm(prod);
					String symb = symbols.get(symbols.size()-1);
					if (vn.contains(symb)) { // it's a vn
						if (getFirstSet(symb).isEmpty()) {
							set = new HashSet<String>();
							set.add("$");
							grammarFollows.put(symb, set);
							continue;
						}
						set = (HashSet<String>) grammarFollows.get(nt);
						set2 = (HashSet<String>) grammarFollows.get(symb);
						modified = modified | set2.addAll(set);
						boolean loop = true;
						while(loop) {
							if (symbols.size() <= 1) {
								loop = false;
							}
							for (int i = symbols.size()-1; i > 0 && loop; i--) {
								if(vn.contains(symbols.get(i-1))
										&& vn.contains(symbols.get(i))
										&& getFirstSet(symbols.get(i)).contains("&")) { // if current and before are vn and & E first(i)
									set = (HashSet<String>) grammarFollows.get(nt);
									set2 = (HashSet<String>) grammarFollows.get(symbols.get(i-1));
									modified = modified | set2.addAll(set);
									grammarFollows.put(symb, set);
								} else {
									loop = false;
								}
							}
							loop = false;
						}
					} 
				}
			}
		}
		for (String nt : this.vn) {
			set = (HashSet<String>) grammarFollows.get(nt);
			if(set.contains("&")) {
				set.remove("&");
				set.add("$");
			}
			grammarFollows.put(nt, set);
		}
	}
	
	public HashMap<String, Set<String>> getFollowSet() {
		if (this.grammarFollows.isEmpty()) {
			setFollowSet();
		}
		return this.grammarFollows;
	}
	
	public void printFollowSet() {
		if (this.grammarFollows.isEmpty()) {
			setFollowSet();
		}
		System.out.println("Follow:");
		for (String s : grammarFollows.keySet()) {
			System.out.print(s + ": ");
			for (String c : grammarFollows.get(s)) {
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
	public void printFirstSet(String nt) {
		System.out.print("First(" + nt + "): ");
		for (String s : getFirstSet(nt)) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
//	"S -> A B | C D\n" +
//	"A -> & | c\n" +
//	"B -> d\n" +
//	"C -> & | b\n" +
//	"D -> d"
	public boolean isFactored() {
		
		Set<String> aux;
		Set<String> first;
		for (String nt : this.vn) {
			first = new HashSet<String>();
			Set<String> pr = grammar.getGrammarProductions(nt);
			for (String prod: pr) {
				aux = getFirstSet(prod);
				for (String str : aux) {
					if (first.contains(str)) {
						if (str.equals("&")) {
							if (aux.size() == 1) {
								return false;
							}
						}
						else if (!str.equals("&")) {
							return false;
						}
					}
				}
				first.addAll(aux);
			}
		}
		return true;
	}
	
	public boolean hasLeftRecursion() {
		for (String nt: this.vn) {
			if (getFirstNTSet(nt).contains(nt)) {
				return true;
			}
		}
		return false;
	}

		
	
}
