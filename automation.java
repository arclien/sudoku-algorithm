
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;


public class automation
{
	static public final int RUN_TIME = 50;	
	
	public static void main(String[] args) throws Exception
	{
		//delete auto.txt file to create new csv file
		deleteAutoFile("auto.txt");
		for (int i = 0; i < RUN_TIME; i++) 
        {
			//automatically generate new sudoku
	    	SudokuGenerator sg = new SudokuGenerator();
	    	// generator takes output file name,N,P,Q,M
			sg.autorun("output.txt", 12,3,4,40);
			
			SudokuSolver ss = new SudokuSolver();
			// solver takes output file name, M, heuristic type num, timeout
			String out = ss.autosolving("output.txt",40,"1",60);
			/*
			 * 1 = Backtracking
			 * 2 = ForwardChecking
			 * 3 = Arc Consistency
			 */
			
			//create temporarily text file which is read by csv generator in order to generate .csv file
			autooutput("auto.txt",out);
			
        }
		// write csv file based on 50 times run
		autowriteCSV("auto.txt");
		
	}


	public static void autooutput(String fileName, String output) throws Exception
	  {
	   // PrintWriter writer=new PrintWriter(new FileWriter(fileName));
	    String puzzleString="";
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
		  try {
			  	
		     
		        StringBuilder puzzle = new StringBuilder();
		        String line = br.readLine();
		      
		              	
	            while (line != null) {
	            	line = line.replaceAll("\\[|\\]", "");
		        	puzzle.append(line);
		        	puzzle.append("\n");
		            line = br.readLine();
		            
		        }
	            output = output.replaceAll("\\[|\\]", "");
	            puzzle.append(output);
	        	
		        // this variable contains actual sudoku puzzle
		        puzzleString = puzzle.toString();
		      
		  } finally {
	        br.close();
	    }
		
		 
		  PrintWriter writer=new PrintWriter(new FileWriter(fileName));
		  writer.println(puzzleString);
		  writer.close();
		        
		     
	
	    //System.out.println( fileName +" is generated");
	  }






	public static void autowriteCSV(String fileName) throws Exception
	  {
	   // PrintWriter writer=new PrintWriter(new FileWriter(fileName));
	    String puzzleString="";
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
		  try {
			  	
		     
		        StringBuilder puzzle = new StringBuilder();
		        String line = br.readLine();
		      
		              	
	            while (line != null) {
	            	line = line.replaceAll("\\[|\\]", "");
		        	puzzle.append(line);
		        	puzzle.append("\n");
		            line = br.readLine();
		            
		        }
	           
	        	
		        // this variable contains actual sudoku puzzle
		        puzzleString = puzzle.toString();
		      
		  } finally {
	        br.close();
	    }
		
		  GenerateCsv csv = new GenerateCsv();
		  csv.passString(puzzleString);
		        
		     
	
	    //System.out.println( fileName +" is generated");
	  }


	public static void deleteAutoFile(String fileName) throws Exception
	  {
	   		 
		  PrintWriter writer=new PrintWriter(new FileWriter(fileName));
		  writer.println("M,Assignments,Total Time,Avg Search Time,Solution,Timeout");
		  writer.close();
		        
		     
	
	    //System.out.println( fileName +" is generated");
	  }
}
