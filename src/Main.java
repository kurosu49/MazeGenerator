
public class Main {

	public static void main(String[] args) {
		// generate a maze with n by m cells
//		Maze maze = new Maze(5,5, true);
//		maze.display();
//		maze = new Maze(5,5, false);
//		maze.display();
//		maze = new Maze(10,15, false);
//		maze.display();
		
		//my own tests below
		
		Maze test = new Maze(5,8,true);
		test.display();
		Maze test2 = new Maze(6,10,false);
		test2.display();
		Maze test3 = new Maze(21,21,false);
		test3.display();
	}

}
