/******************************************************
 * Notes
 * - Stania Klegr 1339709
 * - Jason Tollilson 1319030
 ******************************************************/
//self referential node for the client queue
public class Node {
	

	String toMatch;
	int n1, n2, state;
	Node next;
	Node previous;

	//constructor
	public Node(int state ,String toMatch, int n1, int n2){
		this.state = state;
		this.toMatch = toMatch;
		this.n1 = n1;
		this.n2 = n2;
	}
}
