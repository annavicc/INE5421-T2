package ContextFreeLanguage;

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
	private HashSet<Character> vt;	// terminal symbols
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
		vt = new HashSet<Character>();
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
	
	public String getDefinition() {
		return this.grammar;
	}
	
	public static ContextFreeGrammar isValidCFG(String inp) {
		ContextFreeGrammar cfg = new ContextFreeGrammar(inp);
		// Verify invalid symbols
		if (!isLexicallyValid(inp)) {
			return null;
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
		int i = 0;
		for (String s : prod) {
			prod[i++] = s.replaceAll("\\s+", ""); // Remove spaces
		}
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
				
				if (vn.length() > 1) { // if vn = AA || vn = A1A
					if (!vn.substring(1).matches("^[0-9]+")) {
						System.out.println("wr");
					}
				}
				
				// if first symbol of vn is terminal
				if (Character.isLowerCase(vn.charAt(0))
						|| Character.isDigit(vn.charAt(0))) {
					cfg.vn.clear();
					vnScan.close();
					return null;
				}
				
				for (int j = 0; j < vn.length(); j++) {
					if (!Character.isLetterOrDigit(vn.charAt(j))
							|| Character.isLowerCase(vn.charAt(j))) {
						cfg.vn.clear();
						vnScan.close();
						return null;
					}
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
		int prodLength = 0;
		prodScan = new Scanner(prod);
		prodScan.useDelimiter("[|]");
		if (prod.length() < 1) {
			cfg.vn.clear();
			prodScan.close();
			return false;
		}
		
		while (prodScan.hasNext()) {
			prod = prodScan.next();
			prodLength = prod.length();
			if (prodLength < 1) { // |prod| = 0
				cfg.vn.clear();
				prodScan.close();
				return false;
			}
			for (int i = 0; i < prodLength; i++) {
				char c = prod.charAt(i);
				if (!Character.isLetterOrDigit(c)
						&& c != '&') { // if it's not a terminal
					cfg.vn.clear();
					prodScan.close();
					return false;
				} else if (Character.isUpperCase(c)) { // get vns
					cfg.vn.add(Character.toString(c));
				}
				if (c == '&' || Character.isLetterOrDigit(c)) { // get vts
					if (Character.isLetter(c) && Character.isLowerCase(c) || c == '&') {
						cfg.vt.add(c);
					}
				}
			}
			prodList.add(prod);
			cfg.productions.put(vn, prodList);
		}
		prodScan.close();
		return true;
	}
	
	
	
	
}
