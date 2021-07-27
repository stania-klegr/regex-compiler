import java.util.ArrayList;

/******************************************************
 * Notes
 * - Stania Klegr 1339709
 * - Jason Tollilson 1319030
 
E -> A
E -> AE
A -> T
A -> T|A
T -> F
T -> F*
T -> F?
F -> \v
F -> (E)
F -> .
F -> [L]
F -> ^[L]
F -> v

E = Expression
A = Alternation
T =  Term
F = Factor
L = List
v = Literal
 ******************************************************/

class REcompiler{

    public static ArrayList<Integer> stateList = new ArrayList<>();
    public static ArrayList<String> toMatchList = new ArrayList<>();
    public static ArrayList<Integer> n1List = new ArrayList<>();
    public static ArrayList<Integer> n2List = new ArrayList<>();

    public static String p; //Regular expression
    public static int position, state; //Position keeps track of what character of the regex we are looking at

    public static void main(String []args)
    {
        try{
            if(args.length > 0){
                int initial;
                p = args[0];

                position = 0; //Index into pattern (Where we are in the regexp)
                state = 1;

                //Create start state which will be set after creating full FSM and returning from expression
                setState(0, "br", 1, 1);

                initial = expression();

                //If current phrase position not equal to length of regex or the last character was an alternation, regex must be invalid
                if(p.length() != position || p.charAt(position-1) == '|'){
                    showError();
                }

                //Can now set starting state of fsm
                n1List.set(0, initial);
                n2List.set(0, initial);

                //adding the final state
                setState(state, "fn", state, state);

                print();
            }
        }
        catch(Exception e){
            showError();
        }

    }

    /**
     * Finds and returns next state of an expression
     * @return
     */
    private static int expression()
    {
        int nextState, prevState, end2; //Will be returned as next state we are going to build (r in notes)
        nextState = alternation();
        prevState = state -1;

        if(p.length() == position){ //Reached end of expression
                return nextState;
        }

        if(isVocab(p.charAt(position)) || p.charAt(position) == '(' || p.charAt(position) == '.' || p.charAt(position) == '\\' || p.charAt(position) == '[' || p.charAt(position) == '^'){
            end2 = expression();

            //Check if the current machine needs to change its second pointer
            if(n1List.get(prevState) != n2List.get(prevState)){
                n2List.set(prevState, end2);
            }
        }

       return nextState;
    }

    /**
     * Finds and returns the state of an alternation.
     * Mainly checks for alternation between expressions
     * @return
     */
    private static int alternation()
    {
        int prevState, nextState;
        int n1, n2; //n1 and n2 are t1 and t2 from notes(need to store next states of this machine
        prevState = state - 1;
        nextState = n1 = term(); //nextState is r from notes

        //If reached end of regex
        if(p.length() == position){

            return nextState;
        }

        if(p.charAt(position) == '|')
            {
                //If previous machine was not branching set both its pointers to this machine
                if(n1List.get(prevState) == n2List.get(prevState))
                {
                    n2List.set(prevState, state);
                }
                //Otherwise just change one pointer to this machine
                n1List.set(prevState, state);

                if(state > 0){ //Make sure previous state isnt negative
                    prevState = state - 1;
                }

                position++;

                //Create this branching machine pointing to term just created(n1) and to next machine(state+1) so lists are in proper order
                setState(state, "br", n1, state+1);

                nextState = state; //State number of machine to return

                //Get ready to create another machine for next term
                state++;
                n2 = alternation();

                //Incase the next machine is branching update the current machines next pointers
                updateState(nextState, n1, n2);

                //Check if previous machine was branching and update so points at correct end state
                if(n1List.get(prevState) == n2List.get(prevState)){
                    n2List.set(prevState, state);
                }
                n1List.set(prevState, state);

                //create a dummy state cuz reasons
                setState(state, "br", state + 1, state + 1);
                //nextState = state;
                state ++;
            }

        return nextState;
    }

    /**
     * Finds and returns next state of a term
     * @return
     */
    private static int term()
    {
        int prevState, nextState;
        int n1, n2; //n1 and n2 are t1 and t2 from notes(need to store next states of this machine
        prevState = state - 1;
        nextState = n1 = factor(); //nextState is r from notes

        //If reached end of regex
        if(p.length() == position){

            return nextState;
        }

        if(p.charAt(position) == '*') //Closure
        {
            //If previous machine was not branching set its pointers to this machine
            if(n1List.get(prevState) == n2List.get(prevState))
            {
                n2List.set(prevState, state);
            }
            //Otherwise just change one pointer to this machine
            n1List.set(prevState, state);

            if(state > 0){ //Make sure previous state isnt negative
                prevState = state - 1;
            }
            position++;

            //Create this branching machine pointing to term just created(n1) and to next machine(state+1) so lists are in proper order
            setState(state, "br", n1, state+1);

            nextState = state; //State number of machine to return

            state++;
            n2 = state;

            //Incase the next machine is branching update the current machines next pointers
            updateState(nextState, n1, n2);

            //Set previous machines end state to this branch machine
            n2List.set(prevState, nextState);
            n1List.set(prevState, nextState);

            //create a dummy state cuz reasons
            setState(state, "br", state + 1, state + 1);
            nextState = state -1;
            state ++;

        }
        else if(p.charAt(position) == '?') //Repetition
        {
            //If previous machine was not branching set its pointers to this machine
            if(n1List.get(prevState) == n2List.get(prevState))
            {
            	n2List.set(prevState, state);
            }
            //Otherwise just change one pointer to this machine
            n1List.set(prevState, state);

            if(state > 0){ //Make sure previous state isnt negative
            	prevState = state - 1;
            }
            position++;

            //Create this branching machine pointing to term just created(n1) and to next machine(state+1) so lists are in proper order
            setState(state, "br", n1, state+1);

            nextState = state; //State number of machine to return

            state++;
            n2 = state;

            //Incase the next machine is branching update the current machines next pointers
            updateState(nextState, n1, n2);

            //Check if previous machine was branching and update so points at correct end state
            if(n1List.get(prevState) == n2List.get(prevState)){
            	n2List.set(prevState, state);
            }
            n1List.set(prevState, state);

            //create a dummy state cuz reasons
            setState(state, "br", state + 1, state + 1);
            nextState = state -1;
            state ++;
        }

        return nextState;
    }

    /**
     * Finds and returns next state of a factor
     * @return
     */
    private static int factor()
    {
        //Will be returned as next state we are going to build (r in notes)
        int nextState = state;
        //if we are out of input
        if(p.length() == position){

            return nextState;
        }

        if(p.charAt(position) == '\\') //Backslash
        {
            //skip past the backslash
            position++;
            //match anything
            setState(state, Character.toString(p.charAt(position)), state+1, state+1);
            position++;
            state++;
        }
        else if(p.charAt(position) == '(') //Brackets
        {
            position++;
            nextState = expression();

            try{
                if(p.charAt(position) == ')')
                {
                    position++;
                }
                else
                {
                    showError();
                }
            }
            catch(Exception e){
                showError();
            }
        }
        else if (p.charAt(position) == '.') { //Any literal
            setState(state, "wild", state+1, state+1);
            position++;
            state++;
            return nextState;
        }
        else if(p.charAt(position) == '['){ //List of literals

            position++;

            //Initialise list with opening bracket
            String list = "[";
            list(list);
        }
        else if(p.charAt(position) == '^'){ //NOT matching list of literals
            position++;
            //Initialise list with carrot and opening bracket
            String list = "^[";

            //Make sure next character is the opening bracket
            if(p.charAt(position) == '['){
                position++;
                list(list);
            }
            else{
                showError();
            }
        }
        else if(isVocab(p.charAt(position))) //literals
        {
            setState(state, Character.toString(p.charAt(position)), state+1, state+1);
            position++;
            state++;
            return nextState;
        }
        else
        {
            showError();
        }

        return nextState;
    }

    /**
     * Creates machine with all characters in a list.
     * carot symbol and starting open bracket are added prior to
     * calling this method
     * @param literalList
     */
    private static void list(String literalList)
    {
        try{
            //Check if closing bracket is at start of list
            if(p.charAt(position) == ']'){
                literalList += ']';
                position++;
            }

            while(p.charAt(position) != ']'){ //Add all characters to the list
                literalList += p.charAt(position);
                position++;

                if(position >= p.length()){ //If no ending bracket, invalid regex
                    showError();
                }
            }

            position++; //Consume the closing bracket

            setState(state, literalList, state+1, state+1);

            state++;
        }
        catch (Exception e){
            showError();
        }
    }

    //########################################################################
    //#Extra Methods
    /**
     * Display an error when factor method cant parse something
     */
    private static void showError(){
        System.err.println("Invalid regex: " + p);
        //print();
        System.exit(0);
    }

    /**
     * Add new state machine to the list
     * @param state
     * @param toMatch
     * @param n1
     * @param n2
     */
    private static void setState(int state, String toMatch, int n1, int n2)
    {
        stateList.add(state);
        toMatchList.add(toMatch);
        n1List.add(n1);
        n2List.add(n2);
    }

    /**
     * Updates an existing state with new pointers.
     * @param state
     * @param n1
     * @param n2
     */
    private static void updateState(int state, int n1, int n2)
    {
        n1List.set(state, n1);
        n2List.set(state, n2);
    }

    /**
     * Checks if a string is part of our grammar
     * @param toMatch
     * @return
     */
    private static boolean isVocab(char toMatch)
    {
        if(toMatch == '(' || toMatch == ')' || toMatch == '*' || toMatch == '|' || toMatch == '\\' || toMatch == '?' || toMatch == '.')
        {
            return false;
        }
        return true;
    }

    /**
     * Display complete FSM to console
     */
    private static void print(){
        int count = 0;
        while(count != stateList.size()){
            System.out.println(stateList.get(count) + " " + toMatchList.get(count) + " " + n1List.get(count) + " " + n2List.get(count));
            count ++;
        }
    }
}
