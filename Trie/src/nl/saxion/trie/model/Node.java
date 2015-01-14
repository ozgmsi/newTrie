package nl.saxion.trie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * De class node, deze class is niet afhankelijk van andere classes. Omdat deze
 * class op zichzelf werkt, zijn er gebruik gemaakt van recursive methodes om
 * dieper te "graven"
 */
public class Node {

	/* Eventuele nodeID */
	private int nodeID;

	/* The value dat behoort tot deze node */
	private String value;

	/* Is de value van deze nog een woord(Het eind van een woord ) */
	private boolean isWord = false;

	/* De childenodes die deze node heeft */
	public List<Node> children;

	/* De data dat bij deze node hoort */
	private List<Data> data;
	
	/* Hoevaak komt dit, als dit een woord is voor?*/
	private int frequency;
	
	
	/* Constructor */
	public Node() {
		data = new ArrayList<Data>();
		children = new ArrayList<Node>();
		frequency = 0;
	}

	/* Contructor with args */
	public Node(String value, boolean isword, ArrayList<Integer> positie) {
		this.value = value;
		this.isWord = isword;
		this.data = new ArrayList<Data>();
		this.data.add(new Data(positie));
		this.children = new ArrayList<Node>();
	}

	/**
	 * Verkrijg de waarde dat bij deze node hoort
	 * @return de waarde van deze node
	 */
	private String getValue() {
		return value;
	}

	/**
	 * Heeft deze node child nodes onder zich?
	 * @return true als deze nog childnodes onder zich heeft
	 */
	private boolean hasChildren() {
		if (children != null) {
			return children.size() > 0;
		}
		return false;
	}


	/**
	 * Verkrijgt data van de zoekterm
	 * @see 
	 * @param s de zoekterm
	 * @return
	 */
	public ArrayList<Data> search(String s) {
		return search(s, new ArrayList<Data>());
	}
	
	/**
	 * Zoeken naar een string
	 * @param s de input string
	 * @param De lege lijst voor return
	 * @return De lijst met data over de inputstring
	 */
	private ArrayList<Data> search(String s, ArrayList<Data> output) {
		
		/* Als het punt is bereikt of dit een woord is, geef de output met posities terug */
		if (isWord) {
			return output;
		}
		
		/* Recursive : De input s wordt steeds verkleind, dit wordt gedaan zolang de lengte van s niet gelijk is aan 0 */
		if (!s.isEmpty()) {
			/* Is er een childnode met de eerste character van de input string */
			if (hasChild(s.substring(0, 1))) {
				/* Haal dan het kind op */
				Node child = getChild(s.substring(0, 1));

				/* Voeg child childnode data (nodig voor positites) dan toe aan de output */

				output.addAll(child.data);

				/* Doe een recursive call naar de childnode en voer het bovenste uit */

				return child.search(s.substring(1, s.length()), output);
			}
		}
		/* Null als het woord niet is gevonden */
		return null;
	}
	/**
	 * Verkrijg de positites van de string in de input
	 * @See Search() <= daaruit kan de dataobjecten worden opgehaald, dat is ook waar de posities staan
	 * @param s zoek term
	 * @return Lijst met positites
	 */
	public ArrayList<Integer> getPositions(String s){
		ArrayList<Data> search = search(s, new ArrayList<Data>());
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for(Data data : search){
			positions.addAll(data.getPositions());
		}
		return positions;
	}

	/**
	 * Verkrijg het pad dat doorgelopen om tot isWord te komen
	 * @param s De zoekterm
	 * @return De pad met node dat is doorgelopen
	 */
	public ArrayList<Node> getPath(String s) {
		return searchPath(s, new ArrayList<Node>());
	}

	/**
	 * Set aantal
	 * @param frequency
	 */
	private void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * Verkrijg het aantal frequenties van deze node, dus het aantal keer dat
	 * deze node Wordt alleen gebruikt als het een woord is
	 * @return het aantal keren dat deze woord voorkomt in de trie
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Verkrijgt het aantal woorden in de node trie
	 * @param word het woord waarvan het aantal verkrijgt moet worden
	 * @return het aantal keer dat de zoekterm in de trie staat
	 */
	public int getWordFrequency(String word) {

		/* Verkrijg de pad die gelopen is */
		ArrayList<Node> searchPath = searchPath(word, new ArrayList<Node>());
		/* Doorloop de pad totdat het een woord is */
		
		if (searchPath != null){
			
			for (Node inPath : searchPath) {
				if (inPath.isWord()) {
					/* Verkrijg het aantal keer van het woord in deze trie */
					return inPath.getFrequency();
				}
			}
		}
		/* Er is geen woord gevonden */
		return 0;
	}


	private ArrayList<Node> searchPath(String s, ArrayList<Node> output) {

		if (isWord) {
			this.setFrequency(this.getFrequency() + 1);
			return output;
		}

		if (!s.isEmpty()) {

			if (isWord) {
				frequency++;
			}
			if (hasChild(s.substring(0, 1))) {

				Node child = getChild(s.substring(0, 1));

				output.add(child);

				/* Ga dieper in de trie en doe hetzelfde ook voor de child nodes */
				return child.searchPath(s.substring(1, s.length()), output);
			}
		}
		/* Als het woord niet is gevonden wordt er een lege lijst gegevens */
		/*
		 * Het woord is pas gevonden als isWord true is, zo is het niet mogelijk
		 * om niet op woorden te zoeken die niet bestaan
		 */
		return null;
	}



	public void delete(String s) {
		ArrayList<Node> woord = searchPath(s, new ArrayList<Node>());
		if (woord != null) {
			System.out.println("woord gevonden, deleten in proces");
			Collections.reverse(woord);
			for (int i = 0; i < woord.size(); i++) {
				Node x = woord.get(i);
				x.children = null;
			}
		} else {
			System.out.println("woord kan je niet deleten want bestaat niet");
		}
	}

	
	/**
	 * Voer een woord te aan de trie
	 * @param s het woord dat aan de trie wordt toegevoerd
	 * @param d de data dat bij de input hoort (posities)
	 */
	public void insert(String s, Data d) {
		if (search(s) != null) {
			searchPath(s, new ArrayList<Node>());
		}
		insertInput(s, d);
	}

	/**
	 * Bekijkt of het woordt in de trie opgeslagen staat
	 * @param shet woord waar naar gezocht wordt
	 * @return true als woord bestaat in de trie
	 */
	public boolean bestaatWoord(String s) {
		ArrayList<Data> search = search(s);
		if (search != null){
			return !search.isEmpty();
		}
		return false;
	}
	
	public void insertNew(String input, Data data){
		
		/* Check if its a leaf */
		
		/* Add the rest, and add the boolean that it's a word */
		
	}
	
	public boolean isLeaf(){
		return children.size() == 0;
	}
	

	/**
	 * De methode dat een string in de trie opslaat Door middel van een
	 * substring(recursief) wordt van elk letter gekeken of het letter(child
	 * node) bestaat of niet Als de childnode niet bestaat wordt de rest van de
	 * string in één node opgeslagen (Reduced trie)
	 * @See insert()
	 * @param s De input string
	 * @param d  De data dat over de string gaat, in dit geval de positites er van
	 */
	private void insertInput(String s, Data d) {
		
		/* Base base */
		if (!s.isEmpty()) {
			
			/* De eerste character ophalen van de input string */
			String character = s.substring(0, 1);
			
			/* Haal de positities op van de data */
			Integer positie = d.getPositions().remove(0);
			
			ArrayList<Integer> posities = new ArrayList<Integer>();
			
			/* Voeg de posities toe aan de list */
			posities.add(positie);
			
			/* Als het kind met character niet bestaat */
			if (!hasChild(character)) {
				
				/* Zijn er child nodes ?*/
				if (hasChilds()) {
					
					/* Haal de childnode op */
					Node node = getChild(character);
					
					/* Check of collisie is onstaan */
					if (node == null && children.size() == 1 && children.get(0).value.equals(character)) {
						String alles = "";
						for (Node child : children) {
							if (child != null) {
								alles += getValue(child);
							}
						}
						/* Als deze node meer dan één letters heeft , expand deze dan */
						collapse(this);
						insertInput(s, d);
						
						/* Als het kind niet gevonden kan worden */
					} else if (node == null) {
						/* Voeg het kind dan toe */
						children.add(new Node(character, false, posities));
					} else {
						/* Het kind bestaat al */
					}
				} else {
					/* Op de laatste positie beland, einde van de input */
					if (s.length() == 1) {
						/* Voeg als kind toe en geef mee dat dit het eind van het woord is door true mee te geven */
						children.add(new Node(character, true, posities));
					} else {
						/* Ander is het nog geen woord en wordt er false meegegeven */
						children.add(new Node(character, false, posities));
					}
				}
			} else {
			}
			
			/* Doe hetzelfde recursief */
			Node node = getChild(character);
			String newInsert = s.substring(1, s.length());
			if (node != null) {
				node.insertInput(newInsert, d);
			}
		}
	}

	/**
	 * Bekijkt of deze node child nodes heeft
	 * 
	 * @see insert()
	 * @see getChild()
	 * @return true wanneer 1 of meer childnodes aanwezig zijn van deze node
	 */
	private boolean hasChilds() {
		/* Check of de childeren array niet null is */
		if (children != null) {
			/* Base case => zijn er chil nodes? */
			return children.size() > 0;
		} else {
			return false;
		}
	}

	/**
	 * Haal een child op van deze node
	 * 
	 * @param s De string waarmee gewerkt wordt, de eerste character zal worden gecheckt
	 * @See hasChild(), dekt deze methode
	 * @return De childnode van deze class
	 */
	private Node getChild(String s) {
		/* Doorloop de childeren van deze node */
		for (Node node : children) {
			
			 /* Als value gelijk is aan de character met die van de childnode, geef dan deze child terug */			 
			if (node.value.equals(s)
					|| node.value.charAt(0) == s.toCharArray()[0]) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Hee
	 * @param s
	 * @return
	 */
	private boolean hasChild(String s) {
		if (children != null) {
			for (Node node : children) {
				if (node.value.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Geeft een representatie van de node class en zijn child nodes
	 */
	public void prettyPrint() {
		/* Loop trough childer of this(parent) node */
		String childs = "Parent : " + this.value;

		/* Concat the childnode values */
		childs += " Childs : [ ";
		if (children != null) {

			for (Node n : children) {
				if (n != null) {

					/* Geef de value mee */
					childs += n.value;

					/* Geef aan of het een woord is */
					if (n.isWord) {
						childs += " is een woord ";
					}
				}
			}
			childs += " ]";

			/* Print actie */
			System.out.println(childs);

			/* Print recursive de childnodes */
			for (Node n : children) {
				n.prettyPrint();
			}
		}

	}


	/**
	 * Uitpakken van de value node, door meerder nodes aan te maken 
	 * @param node de node waarvan de value uitgepakt moet worden (van de value die langer is dan 1 sub nodes van maken)
	 */
	private void collapse(Node node) {
		if (node.children.size() == 1 && isLeaf(node.children.get(0))) {
			node.value += node.children.get(0).value;
			node.children = null;
			node.isWord = true;
			System.out.println("gevonden");
		} else {
			if (node.hasChildren()) {
				for (Node child : node.children) {
					collapse(child);
				}
			}
		}
	}

	/**
	 * Verkrijg de waarde van een node
	 * @param De node waarna gekeken moet worden
	 * @return De value van de input node
	 */
	private String getValue(Node data) {
		String status = "";
		if (data.value != null) {
			status = data.value;
		}
		if (data.hasChildren()) {
			for (Node node : data.children) {
				status = getValue(status, node);
			}
		}
		return status;
	}

	private String getValue(String status, Node data) {
		if (data.value != null) {
			status += data.value;
		}
		if (data.hasChildren()) {
			for (Node node : data.children) {
				status = getValue(status, node);
			}
		}
		return status;
	}

	/**
	 * Geeft aan of deze node een blad is
	 * @param de node waarvan informatie nodig is
	 * @return true als de de argument node een leaf is
	 */
	private boolean isLeaf(Node node) {
		if (node.children != null) {
			return node.children.size() == 0;
		}
		return true;
	}

	/**
	 * Is deze node het einde van een woord ?
	 * @return true als dit het einde van een woord is 
	 */
	private boolean isWord() {
		return this.isWord;
	}
}
