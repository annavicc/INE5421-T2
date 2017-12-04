package ContextFreeLanguage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * Representation of a Context Free Language
 */

public class ContextFreeGrammar {

	protected String grammar; // The input grammar entered by the user
	private String id; // an unique ID for the CGF
	private HashSet<String> vn;	// non terminal symbols
	private HashSet<String> vt;	// terminal symbols
	private HashMap<String, HashSet<String>> productions; // production rules
	private String s;	// initial S
	private static Scanner prodScan;
	
	/**
	 * Public constructor
	 * @param inp the regular grammar entered by the user
	 */
	public ContextFreeGrammar(String inp) {
		this.grammar = inp;
		vn = new HashSet<String>();
		vt = new HashSet<String>();
		productions = new HashMap<String, HashSet<String>>();
	}
	
	/**
	 * Constructor
	 * Creates a new grammar based on the parameter
	 * @param g the grammar to have data copied from
	 */
	public ContextFreeGrammar(ContextFreeGrammar g) {
		String init = g.getInitialSymbol();
		Set<String> newVn = new HashSet<String>();
		Set<String> newVt = new HashSet<String>();
		productions = new HashMap<String, HashSet<String>>();
		
		String gr = g.grammar;
		for (String s : g.getVn()) {
			newVn.add(s);
		}
		for (String s : g.getVt()) {
			newVt.add(s);
		}
		
		this.s = init;
		this.vn = (HashSet<String>) newVn;
		this.vt = (HashSet<String>)newVt;
		this.grammar = gr;
		for (String pr : this.vn) {
			productions.put(pr, new HashSet<>());
			for (String prod : g.getGrammarProductions(pr)) {
				this.addProduction(pr, prod);
				
			}
		}
	}
	
	/**
	 * Default constructor
	 */
	public ContextFreeGrammar() {
		vn = new HashSet<String>();
		vt = new HashSet<String>();
		productions = new HashMap<String, HashSet<String>>();
	}
	
	/**
	 * Set all the productions of all non terminals
	 * @param p the map of non terminals and productions
	 */
	public void setProductions(HashMap<String, HashSet<String>> p) {
		this.productions = p;
	}
	
	
	
	/**
	 * Remove all productions of a non terminal
	 * @param nt the non terminal
	 */
	public void removeProductions(String nt) {
		if (this.vn.contains(nt) && this.productions.containsKey(nt)) {
			this.productions.remove(nt);
			this.vn.remove(nt);
		}
	}
	
	/**
	 * Add a vt to the grammar
	 * @param t
	 */
	public void addVt(String t) {
		this.vt.add(t);
	}
	
	/**
	 * Get the id of the grammar
	 * @return the grammar id
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Set the id of the grammar
	 * @param id the id to be set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get the initial symbol
	 * @return the initial symbol
	 */
	public String getInitialSymbol() {
		return this.s;
	}
	
	/**
	 * Set the initial symbol of the grammar
	 * @param s the initial symbol
	 */
	public void setInitialSymbol(String s) {
		this.s = s;
	}
	
	public String toString() {
		return this.id;
	}
		
	/**
	 * Add a non terminal to the grammar
	 * @param nt the non terminal
	 */
	public void addVn(String nt) {
		if (!this.vn.contains(nt)) {
			this.vn.add(nt);
			this.productions.put(nt, new HashSet<String>());
		}
	}
	
	/**
	 * Remove a production of a non terminal
	 * @param nt the non terminal
	 * @param prod the production
	 */
	public void removeProduction(String nt, String prod) {
		HashSet<String> pSet = this.productions.get(nt);
		pSet.remove(prod);
		this.productions.put(nt, pSet);
	}
	
	/**
	 * Add a production to a non terminal
	 * @param nt the non terminal
	 * @param prod the production
	 */
	public void addProduction(String nt, String prod) {
		if (!this.vn.contains(nt)) {
			this.vn.add(nt);
			this.productions.put(nt, new HashSet<String>());
		}
		HashSet<String> p = this.productions.get(nt);
		p.add(prod);
		this.productions.put(nt, p);
	}
	
	/**
	 * Get set of non terminal symbols (Vn)
	 * @return Vn
	 */
	public Set<String> getVn() {
		return vn;
	}
	
	/**
	 * Get set of terminal symbols (Vt)
	 * @return Vt
	 */
	public Set<String> getVt() {
		return vt;
	}
	
	/**
	 * Get production rules from non terminal
	 * @param vn production rule input
	 * @return production rules output set
	 */
	public Set<String> getGrammarProductions(String vn) {
		Set<String> prod = productions.get(vn);
		if (prod == null) {
			prod = new HashSet<String>();
		}
		return prod;
	}
	
	/**
	 * String representation of a regular grammar
	 * @return representation of a RG
	 */
	public String getDefinition() {
		String grammar = "";
		String aux = "";
		HashSet<String> prodList;
		
		for (String vN : this.productions.keySet()) {
			prodList = this.productions.get(vN);
			
			for (String prod : prodList) {
				aux += prod + " | ";
			}
			if (aux.length() > 0) { 
				aux = aux.substring(0, aux.length()-2);
			}
			aux = aux.trim().replaceAll(" +", " ");
			if (vN.equals(this.s)) {
				grammar = vN + " -> " + aux + "\n" + grammar;
			} else {
				grammar += vN + " -> " + aux + "\n";
			}
			aux = "";
		}
		this.grammar = grammar;
		return grammar;
	}
	
	
	/**
	 * Verify if a given input grammar is valid for a cfg
	 * @param inp the input grammar
	 * @return a new grammar if valid, null if invalid
	 */
	public static ContextFreeGrammar isValidCFG(String inp) {
		ContextFreeGrammar cfg = new ContextFreeGrammar(inp);
		// Verify invalid symbols
		if (!isLexicallyValid(inp)) {
//			return null;
		}
		
		// Get productions with no blanks for every vn
		String[] productions = getProductions(inp);
		
		
		// Verify productions correctness
		validateProductions(productions, cfg);
		// Validate non terminals
		if (cfg.vn.isEmpty()) {
			return null;
		}
		return cfg;
	}
	
	/**
	 * Verify if the symbols are lexically valid
	 * @param input the list of symbols
	 * @return true if they are valid
	 */
	public static boolean isLexicallyValid(String input) {
		String formatted =  input.replaceAll("\\s+", ""); // Remove white spaces
		if (!formatted.matches("^[a-zA-Z0-9\\->|&]+")) { // Verify invalid symbols
			return false;
		}
		return true;
	}
	
	/**
	 * Break a string production into an array
 	 * @param str the production string
	 * @return the array with a symbol per position
	 */
	private static String[] getProductions(String str) {
		String[] prod = str.split("[\\r\\n]+");	// Split by line break
		return prod;
	}
	
	/**
	 * Validate the productions for every non terminal vn
	 * @param nt the list of non terminals
	 * @param cfg the grammar
	 * @return a new grammar if the productions are valid, null if invalid
	 */
	private static ContextFreeGrammar validateProductions(String[] nt, ContextFreeGrammar cfg) {
		Scanner vnScan = null;
		String vn = "";
		String prod = "";
		HashSet<String> pr = new HashSet<String>();
		boolean isSDefined = false;
		
		// Iterate every non terminal symbol
		for (int i = 0; i < nt.length; i++) {
			prod = nt[i];
			vnScan = new Scanner(prod);
			vnScan.useDelimiter("->");
			
			if(vnScan.hasNext()) {
				vn = vnScan.next();
				pr = cfg.productions.get(vn);
				if (pr == null ) {
					pr = new HashSet<String>();
				}
				
				vn = vn.replaceAll("\\s+", "");
				if (vn.length() > 1) { // if vn = AA || vn = A1A
					if (!vn.substring(1).matches("^[0-9\\s+]+")) {
						cfg.vn.clear();
						vnScan.close();
						return null;
					}
				}
				
				// if first symbol of vn is terminal
				if (!Character.isUpperCase(vn.charAt(0))) {
					cfg.vn.clear();
					vnScan.close();
					return null;
				}
				
				cfg.vn.add(vn);
				if (!isSDefined) {
					cfg.s = vn;
					isSDefined = true;
					
				}
				if (!validateProduction(vn, prod, pr, cfg)) {
					cfg.vn.clear();
					vnScan.close();
					return null;
				}
			}
			vnScan.close();
		}
		return cfg;
	}

	/**
	 * Verify if all the productions are valid for a CFG
	 * @param vn the non terminal the productions belong to
	 * @param productions the productions
	 * @param prodList the list of productions
	 * @param cfg the grammar
	 * @return true if they are valid
	 */
	private static boolean validateProduction(String vn, String productions,
			HashSet<String> prodList, ContextFreeGrammar cfg) {
		// Iterate every production for every vN
		String prod = productions.substring(productions.indexOf("->")+2);
		if (prod.replaceAll("\\s+", "").length() < 1) {
			cfg.vn.clear();
			prodScan.close();
			return false;
		}
		prodScan = new Scanner(prod);
		prodScan.useDelimiter("[|]");
		
		while (prodScan.hasNext()) {
			prod = prodScan.next();
			if (prod.replaceAll("\\s+", "").length() < 1) { // |prod| = 0
				cfg.vn.clear();
				prodScan.close();
				return false;
			}
			String[] symbols = prod.split("[\\s\\r]+"); // + E T
			for (String symb : symbols) {
				if(symb.isEmpty()) {
					continue;
				}
				if (Character.isUpperCase(symb.charAt(0))) {
					for (int i = 1; i < symb.length(); i++) {
						if (!Character.isDigit(symb.charAt(i))) { // E1E
							cfg.vn.clear();
							prodScan.close();
							return false;
						}
					}
					cfg.vn.add(symb);
				} else {
					for (int i = 1; i < symb.length(); i++) {
						if (Character.isUpperCase(symb.charAt(i))) {
							cfg.vn.clear();
							prodScan.close();
							return false;
						}
					}
					cfg.vt.add(symb);
				}
			}
			prodList.add(prod);
			cfg.productions.put(vn, prodList);
		}
		prodScan.close();
		return true;
	}

	/**
	 * Add a set of productions to a vn
	 * @param nt the vn to add the productions to
	 * @param hashSet the set of productions
	 */
	public void addProduction(String nt, Set<String> hashSet) {
		HashSet<String> p;
		if (this.vn.contains(nt)) {
			p = productions.get(nt);
		} else {
			addVn(nt);
			p = new HashSet<>();
		}
		p.addAll(hashSet);
		productions.put(nt, p);
	}

	
	
}
