/******************************************************
 * Notes
 * - Stania Klegr 1339709
 * - Jason Tollilson 1319030
 ******************************************************/

public class Deque {
	
	public Node head = null;
	public Node tail = null;

	public void push(int state, String toMatch, int n1, int n2){

		//System.out.println("adding to front: " + value);
		Node nodeToAdd = new Node(state, toMatch, n1, n2);

		if(head == null)
		{
			head = nodeToAdd;
			tail = nodeToAdd;
		}
		else
		{
			head.previous = nodeToAdd;
			nodeToAdd.next = head;

			head = head.previous;
		}
	}
	
	public void add(int state, String toMatch, int n1, int n2){

		//System.out.println("adding to end: " + toMatch);
		Node nodeToAdd = new Node(state, toMatch, n1, n2);

		if(tail == null)
		{
			head = nodeToAdd;
			tail = nodeToAdd;
		}
		else
		{
			tail.next = nodeToAdd;	
			nodeToAdd.previous = tail;

			tail = tail.next;
		}
	}
	
	public Node pop(){	
	
		//System.out.println("removing from front");
		
		Node toReturn = head;
		
		if(head == null)
			return null;
		
		if(head.next != null)
		{

			head = head.next;
			head.previous = null; 		
		}
		else
		{
			head = null; 
			tail = null;
		}
		
		return toReturn;
	}
	
	public Node peek(){
		return head;
	}	
	
	public void printFromFront(){

		//System.out.println("printing from front");
		Node current = head;
			
		if(current != null)
		{
			while(current.next != null)
			{
				System.out.println(current.toMatch);
				current = current.next;
			}
			System.out.println(current.toMatch);
		}
	}
	
	public void printFromEnd(){

		//System.out.println("printing from end");
		Node current = tail;	

		if(current != null)
		{
			while(current.previous != null)
			{
				System.out.println(current.toMatch);
				current = current.previous;
			}
			System.out.println(current.toMatch);
		}
	}
}






