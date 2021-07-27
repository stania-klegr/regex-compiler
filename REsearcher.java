import java.io.*;
import java.util.*;

/******************************************************
 * Notes
 * - Stania Klegr 1339709
 * - Jason Tollilson 1319030
 ******************************************************/

class REsearcher {
    public static void main(String []args) throws IOException {
    
    	try
    	{
		    //check args are correct
			if(args.length != 1)
			{
				System.out.println("Usage <file to search>");
				System.exit(0);
			}

			Scanner s = new Scanner(System.in);
		    BufferedReader reader = new BufferedReader(new FileReader(args[0]));

			ArrayList<Node> nodeList = new ArrayList<Node>();
		    Node node, current;

			int i = 0;
			String[] str2;
		    String line, str, line2, fullLine;

		    //add all of the nodes read from standard in to the list
			while (s.hasNext())
			{
		        //read and split a line
				str = s.nextLine();
				str2 = str.split(" ");

				//normal case
				if(str2.length == 4)
				{
					node = new Node(Integer.parseInt(str2[0]), str2[1], Integer.parseInt(str2[2]), Integer.parseInt(str2[3]));
				}
		        //if weare matching a literal space
				else
				{
					node = new Node(Integer.parseInt(str2[0]), " ", Integer.parseInt(str2[3]), Integer.parseInt(str2[4]));
				}
				//add the new node to the list
				nodeList.add(node);
				i++;
			}

		    //loop for each line in the file
		    while ((line = reader.readLine()) != null)
		    {
		        fullLine = line2 = line;

		        Deque que = new Deque();
		        boolean found = false;

		        //loop til the end of the line
		        for(int n = 0; n <= line.length(); n++)
		        {
		            //initilize the deque with the scan and first node in the list
		            Node tmp = nodeList.get(0);
		            que.push(0, "scan", 0, 0);
		            que.push(tmp.state, tmp.toMatch, tmp.n1, tmp.n2);
		        
		            //if we haven't found a match in this line yet
		            if(!found)
		            {
		                //loops trying to get through the fsm
		    			while(true)
		    			{
		                    current = que.pop();

		                    //if the current node is the final state
		                    if(current.toMatch.equals("fn"))
		                    {
		                    	//print the whole line to the terminal
		                        System.out.println(fullLine);
		                        found = true;
		                        break;
		                    }
		                    //if the current node is the scan
		                    else if(current.toMatch.equals("scan"))
		                    {
		                        //put the scan back at the end of the que
		                        que.add(0, "scan", 0, 0);

		                        //check to see if the scan is the only thing left in the que
		                        current = que.peek();
		                        if(current.toMatch.equals("scan"))
		                        {
		                            break;
		                        }
		                    }
		                    //if the current node is a branching state
		                    else if(current.toMatch.equals("br"))
		                    {
		                        //get the nodes from the node list that matches the branches next states and add them to the que
		                        Node current2 = nodeList.get(current.n1);
		                        que.push(current2.state, current2.toMatch, current2.n1, current2.n2);
		                        current2 = nodeList.get(current.n2);
		                        que.push(current2.state, current2.toMatch, current2.n1, current2.n2);
		                    }
		                    //if the current node is a wildcard state
		                    else if(current.toMatch.equals("wild"))
		                    {
		                        //if there is something there to match
		                        if(line2.length() >= 1)
		                        {
		                            //get the node from the node list that matches the first of the current states next states and add it to the end of the que
		                            Node current2 = nodeList.get(current.n1);
		                            que.push(current2.state, current2.toMatch, current2.n1, current2.n2);
		                            break;
		                        }
		                    }
		                    //if the current node is a []/^[] state
		                    else if(current.toMatch.length() > 1)
		                    {
		                    	//if we are not going to go off the end of the line
		                        if(line2.length() > n)
		                        {
		                            String st = Character.toString(line2.charAt(n));

		                            //if it's a not whatever is in the barckets case
		                            if(current.toMatch.charAt(0) == '^')
		                            {
		                                //if the current char from the line is not in the string that was inside the brackets
		                                if(!current.toMatch.substring(2).contains(st))
		                                {
		                                    //get the node from the node list that matches the first of the current states next states and add it to the end of the que
		                                    Node current2 = nodeList.get(current.n1);
		                                    que.push(current2.state, current2.toMatch, current2.n1, current2.n2);
		                                    break;
		                                }
		                            }
		                            //if it's a match whatever is in the barckets case
		                            else
		                            {
		                                //if the current char from the line is not in the string that was inside the brackets
		                                if(current.toMatch.substring(1).contains(st))
		                                {
		                                    //get the node from the node list that matches the first of the current states next states and add it to the end of the que
		                                    Node current2 = nodeList.get(current.n1);
		                                    que.push(current2.state, current2.toMatch, current2.n1, current2.n2);
		                                    break;
		                                }
		                            }
		                        }
		                    }
		                    //general match the literal case
		                    else
		                    {
		                        //check if line is not empty then check for a match
		                        if(line2.length() > n && line2.charAt(n) == current.toMatch.charAt(0))
		                        {
		                            //get the node from the node list that matches the first of the current states next states and add it to the end of the que
		                            Node current2 = nodeList.get(current.n1);
		                            que.push(current2.state, current2.toMatch, current2.n1, current2.n2);

		                            break;
		                        }
		                    }
		                }
		            }
		        }
		    }
		}	
		catch(Exception ex)
		{
			System.err.println("Bad input or args");
		}
	}		
}


