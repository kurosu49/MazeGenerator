import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/*
 * TCSS342 - Autumn 2013
 * Assignment 5 - Maze Generator
 * 
 */

/**
 * A class that generates a 2 dimensional 
 * maze with no cycles.
 * 
 * @author Crystal Miraflor
 *
 */
public class Maze {
	
	private class Node {
		List<Node> adjacencyList;
		boolean visited;
		int x;
		int y;
		
		/**
		 * Constructor
		 * @param the_x x-coordinate on 2d board
		 * @param the_y y-coordinate on 2d board
		 */
		public Node(final int the_x, final int the_y) {
			visited = false;
			x = the_x;
			y = the_y;
			adjacencyList = new ArrayList<Node>();
		}
		
		/**
		 * Identifies the x-coordinate of 
		 * the edge between two nodes
		 * 
		 * @param other_x the other node's x
		 * @return x-coordinate of edge
		 */
		public int xEdge(final int other_x) { //helps identify edge
			if(x - other_x > 0) {				
				return x-1;
			} else if (x - other_x < 0) {
				return x+1;
			} else {
				return x;
			}
			
		}
		
		/**
		 * Identifies the y-coordinate of 
		 * the edge between two nodes
		 * 
		 * @param other_y the other nodes y
		 * @return y-coordinate of edge
		 */
		public int yEdge(final int other_y) { //helps identify edge
			if(y - other_y > 0) {				
				return y-1;
			} else if (y - other_y < 0) {
				return y+1;
			} else {
				return y;
			}
			
		}
		
		public String toString() {
			if(visited) {
				return "V ";
			} else {
				return "  ";
			}
		}
		
		//for test
//		public String display() {
//			return "(" + x + ", " + y + ")";
//		}
		
	}

	/**
	 * Width of the maze. (the columns
	 * in the maze)
	 */
	private final int my_width;
	
	/**
	 * Depth of the maze. (the rows in
	 * the maze)
	 */
	private final int my_depth;
	
	/**
	 * Boolean, if true displays maze
	 * as being created.
	 */
	private final boolean my_debug;
	
	/**
	 * Maze board.
	 */
	private final String[][] my_maze;
	
	/**
	 * The board containing the positions
	 * of the nodes. 
	 */
	private final Node[][] my_nodes;
	
	/**
	 * Board position of edges
	 */
	private final String[][] my_edges;
	
	/**
	 * Stack for depth-first-search
	 */
	private final Stack<Node> my_stack;
	
	/**
	 * Constructs a 2-d maze with no cycles
	 * 
	 * @param width the width(n)
	 * @param depth	the depth(m)
	 * @param debug a boolean, if true show steps of maze
	 * creation.
	 */
	public Maze(final int width, final int depth, final boolean debug) {
		my_width = width;
		my_depth = depth;
		my_debug = debug;
		my_maze = new String[my_depth*2+1][my_width*2+1];
		my_nodes = new Node[my_depth*2+1][my_width*2+1];
		my_edges = new String[my_depth*2+1][my_width*2+1];
		my_stack = new Stack<Node>();
		setupNodes();
		setTopAndBottom();
		setupBoard();
		findAdjacentNodes();
		dfs(my_nodes[1][1]);
	}
	
	
	/**
	 * Sets up the nodes
	 */
	private void setupNodes() {
		for(int row = 0; row < my_depth; row++) {
			for(int col = 0; col < my_width; col++) {
				my_nodes[row*2+1][col*2+1] = new Node((row*2+1), (col*2+1));
			}
		}
		
	}
	
	
	/**
	 * Sets up the top and bottom
	 * of maze.
	 */
	private void setTopAndBottom() {
		my_maze[0][0] = "X ";
		my_maze[0][1] = "  ";
		for(int i = 2; i < my_maze[0].length; i++) {
			my_maze[0][i] = "X ";
		}
		for(int j = 0; j < my_maze[my_maze.length-1].length - 2; j++) {
			my_maze[my_maze.length-1][j] = "X ";
		}
		my_maze[my_maze.length-1][my_maze[my_maze.length-1].length - 2] = "  ";
		my_maze[my_maze.length-1][my_maze[my_maze.length-1].length - 1] = "X ";
	}
	
	
	/**
	 * Sets up the maze board
	 */
	private void setupBoard() {
		for(int row = 1; row < my_maze.length - 1; row++) {
			for(int col = 0; col < my_maze[row].length; col++) {
				if(my_nodes[row][col] != null) {
					my_maze[row][col] = (my_nodes[row][col]).toString();
				} else if(my_edges[row][col] != null){
					my_maze[row][col] = my_edges[row][col];
				} else {
					my_maze[row][col] = "X ";
				}
			}
		}
		
	}
	
	
	/**
	 * find the adjacent nodes of each node.
	 */
	private void findAdjacentNodes() {
		for(int row = 1; row < my_maze.length - 1; row++) {
			for(int col = 0; col < my_maze[row].length; col++) {
				if(my_nodes[row][col] != null) {
					findAdjacentNodes(my_nodes[row][col]);
				}
			}
		}
	}
	
	
	/**
	 * helper method 1
	 * 
	 * @param the_node node
	 */
	private void findAdjacentNodes(Node the_node) {

		for(int row = -2; row <= 2; row+=4) {
			findAdjacentNodes(the_node, the_node.y + row, the_node.x);
		}
		for(int col = -2; col <= 2; col+=4) {
			findAdjacentNodes(the_node, the_node.y, the_node.x + col);
		}
	}
	
	
	/**
	 * helper method 2 
	 * 
	 * @param the_node the node
	 * @param the_y y
	 * @param the_x x
	 * @return the adjacency list
	 */
	private List<Node> findAdjacentNodes(final Node the_node, final int the_y, final int the_x) {
		if(the_x<0||the_y<0||the_y>my_nodes[my_nodes.length-1].length-1||the_x>my_nodes.length-1) {
			return the_node.adjacencyList;
		} else {
			the_node.adjacencyList.add(my_nodes[the_x][the_y]);
		}
		return the_node.adjacencyList;
	}
	
	/**
	 * Conducts a depth-first-search to
	 * create the maze.
	 * 
	 * @param node the starting node
	 */
	private void dfs(final Node node) {  // depth-first search 
		//start at first node
		node.visited = true;//mark as visited
		if(my_debug) {//display process if debug is true
			display();
		}
		my_stack.push(node); //push node into stack                

		while(!my_stack.isEmpty()) { //until stack is empty,
			//retrieve an adjacent unvisited node of the node
			//at the top of the stack
			Node n = getAdjUnvisitedNode(my_stack.peek());
			if(n == null) {//if no more adjacent nodes, pop                
				my_stack.pop();
			} else {                          
				n.visited = true;//mark visited
				drawEdge(my_stack.peek(), n);//insert an edge between
				if(my_debug) {				// the connecting nodes
					display(); 
				}
				my_stack.push(n);//then push this node to the top                
			}
		} 
     	
		//reset the visited flag of each node to false
		for(int i=1; i < my_depth*2+1; i+=2) {
			for(int j=1; j < my_width*2+1; j+=2) {
				my_nodes[i][j].visited = false;
			}
		}

     } 
	
	
	/**
	 * Draws the edge between connecting nodes on 
	 * the maze board. 
	 * 
	 * @param start first node
	 * @param dest second node being connected to
	 */
	private void drawEdge(final Node start, final Node dest) {
		my_edges[start.xEdge(dest.x)][start.yEdge(dest.y)] = "  ";
	}
	
	
	/**
	 * Returns an adjacent node to the node
	 * inserted in the parameter.
	 * 
	 * @param node the Node
	 * @return an adjacent node that wasn't visited
	 * yet.
	 */
	private Node getAdjUnvisitedNode(final Node node) {
		Collections.shuffle(node.adjacencyList);
		for(int i=0; i < node.adjacencyList.size(); i++) {
			if(!node.adjacencyList.get(i).visited) {
				return node.adjacencyList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Displays the maze.
	 */
	public void display() {
		setupBoard();
		for(int i = 0; i < my_maze.length; i++) {
			for(int j =0; j < my_maze[i].length; j++) {
				System.out.print(my_maze[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
//	
//	public Node getNode(int x, int y) {
//		return my_nodes[x][y];
//	}
//	
	
//	//test to see the adjacency list of each node.
//	public void printAdjList() {
//		for(int row = 1; row < my_maze.length - 1; row++) {
//			for(int col = 0; col < my_maze[row].length; col++) {
//				if(my_nodes[row][col] != null) {
//					ArrayList<Node> list = (ArrayList<Node>) my_nodes[row][col].adjacencyList;
//					System.out.println("Node at " + my_nodes[row][col].display());
//					for(Node n : list) {
//						System.out.print(n.display() + ", ");
//					}
//					System.out.println();
//					System.out.println("-------------");
//				}
//			}
//		}
//	}
	
	
}
