import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Node{
	
	//arraylist to save the location(row) of queens at each column
	ArrayList<Integer> state = new ArrayList<Integer>();
	//the number of queens that are attackable each other in Node's state
	//if this value is lower, it is a better state
	int attackable = 10;
	
	//constructor
	public Node() {
		// TODO Auto-generated constructor stub
	}
	
	//set state array and attackable value
	void setNode(ArrayList<Integer> state) {
		this.state = state;
	}	
	void setvalue(int attackable) {
		this.attackable = attackable;
	}

	//get state information from node
	ArrayList<Integer> getstate() {
		return this.state;
	}	
	int getvalue() {
		return this.attackable;
	}

}

public class nqueens {
	
	//n value
	static int input;
		
	//convert to string form
	static String convert(ArrayList<Integer> state) {
		String result="";
		
		for(int i=0;i<state.size();i++)
			result += state.get(i)+" ";
		
		return result;
	}
	
	//calculate attackable value
	static int calculatevalue(ArrayList<Integer> state) {
		int count = 0;
		int size = state.size();
		//calculate the number of queens which are attackable for any of 3 side
		for(int i=0;i<size;i++) {
			for(int j=i+1;j<size;j++) {
				//row check, diagonal check
				if(state.get(i)==state.get(j) || Math.abs(state.get(i)-state.get(j))==Math.abs(i-j))
					count++;
			}
		}
			
		return count;
	}
	
	//make random state by locating every queen at each column randomly
	static Node randomstate() {
		ArrayList<Integer> state = new ArrayList<Integer>();
		Random rand = new Random();
		for(int i=0;i<input;i++) {
			state.add(rand.nextInt(input));
		}
		
		Node randomNode = new Node();
		randomNode.setNode(state);
		randomNode.setvalue(calculatevalue(randomNode.state));
		
		return randomNode;
	}
	
	//doing hill climbing to find the answer
	static Node hillclimbing(Node currentNode) {
		//new node which will be next node
		Node newNode = new Node();
		
		//current node we have
		ArrayList<Integer> currentState = currentNode.state;
		int currentvalue = currentNode.attackable;
		
		//each column
		for(int i=0;i<input;i++) {
			//current state and we will change this state for searching better state
			ArrayList<Integer> lookingState = (ArrayList<Integer>) currentState.clone();

			//each row
			for(int j=0;j<input-1;j++) {					
				//move to each row
				int curloctation = lookingState.get(i);
				lookingState.set(i, (curloctation+1)%input);
				int lookingvalue = calculatevalue(lookingState);
				
				//if there is better state, return it
				if(lookingvalue < currentvalue) {
					newNode.setNode(lookingState);
					newNode.setvalue(calculatevalue(lookingState));
					return newNode;
				}
			}
		}
		
		//or if there is no better state, restart with new random state
		return randomstate();
	}

	public static void main(String[] args) throws IOException {
		//start time
		double start = System.currentTimeMillis();
		
		//n value
		input = Integer.parseInt(args[0]);
		
		String result = "";
		
		//if input is smaller than 4, there is no solution
		if(input>3) {
			
			//initial node which is made randomly
			Node initialNode = new Node();
			initialNode = randomstate();
			
			//current node
			Node currentNode = initialNode;
			int currentvalue = currentNode.attackable;
	
			//until attackable value is 0, doing hill climbing to find better state
			while(currentvalue!=0) {
				//replace current node with result of hill climbing
				currentNode = hillclimbing(currentNode);
				currentvalue = currentNode.attackable;
			}
			
			//if we get out while clause, it means we have a node with attackable value 0 
			result = convert(currentNode.state);
	
		}
		else result = "No solution"; 
		
		//end time
		double end = System.currentTimeMillis();
		double time = (end-start)/(double)1000.0;


		//string to be written in the file
		String searchtime = "Total Elapsed time: "+time;
		
		//set path and filename and write to the text file
		String path = args[1];
		String filename = "result"+input+".txt";

		BufferedWriter file;
		file = new BufferedWriter(new FileWriter(new File(path, filename)));
				
		try {
			
			file.write(">Hill Climbing");
			file.newLine();
			file.write(result);
			file.newLine();
			file.write(searchtime);
			file.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

}
