package ContextFreeLanguage;

import java.util.ArrayList;
import java.util.Collections;
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
	

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getInitialSymbol() {
		return this.s;
	}
	
	public String toString() {
		return this.id;
	}
		
	/**
	 * Get set of non terminal symbols (Vn)
	 * @return Vn
	 */
	public Set<String> getVn() {
		return Collections.unmodifiableSet(vn);
	}
	
	/**
	 * Get set of terminal symbols (Vt)
	 * @return Vt
	 */
	public Set<String> getVt() {
		return Collections.unmodifiableSet(vt);
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
		return Collections.unmodifiableSet(prod);
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
			aux = aux.substring(0, aux.length()-2);
			if (vN.equals(this.s)) {
				grammar = vN + " -> " + aux + "\n" + grammar;
			} else {
				grammar += vN + " -> " + aux + "\n";
			}
			aux = "";
		}
		return this.grammar;
	}
	
	
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
	
	public static boolean isLexicallyValid(String input) {
		String formatted =  input.replaceAll("\\s+", ""); // Remove white spaces
		if (!formatted.matches("^[a-zA-Z0-9\\->|&]+")) { // Verify invalid symbols
			return false;
		}
		return true;
	}
	
	private static String[] getProductions(String str) {
		String[] prod = str.split("[\\r\\n]+");	// Split by line break
		return prod;
	}
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
	
	
}
