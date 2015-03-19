import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;


public class SudokuSolver
{
	static public final int RUN_TIME = 1;	
	
	public static void printLog(double time, int assignments, boolean solution, boolean timeout)
	{
		
		String hasSolution = (solution)?"Yes":"No";
		String hasTimeout = (timeout)?"Yes":"No";
		System.out.println("Time: "+time);
		System.out.println("Assignments: "+assignments);
		System.out.println("Solution: "+hasSolution);
		System.out.println("Timeout: "+hasTimeout);
		 System.out.println();
		
	}

	public static void showCalculation(ArrayList<Double> allRuntimes,Statistic statistic, ArrayList<Double> runtimes)
	{
		   allRuntimes.addAll(runtimes);
                   
           statistic.calculate(allRuntimes);
           
           
         
         
	}
	public static void showCalculationSearchTime(ArrayList<Double> allRuntimes,Statistic statistic, ArrayList<Double> runtimes)
	{
		   allRuntimes.addAll(runtimes);
                   
           statistic.calculate(allRuntimes);
          
           System.out.println(statistic.mean() );
          
         
	}
	
	public static void main(String[] args) throws Exception
	{
		  BufferedReader br = new BufferedReader(new FileReader(args[0]));
		  try {
		        StringBuilder parameter = new StringBuilder();
		        StringBuilder puzzle = new StringBuilder();
		        String line = br.readLine();
		        
		        //read the first line which specifies the size of sudoku puzzle
		        parameter.append(line);
		        parameter.append('\n');
	            line = br.readLine();
	            // remove whitespace and initialize three parameters, N, P, and Q
	            String parameterString = parameter.toString();
	            StringTokenizer stt = new StringTokenizer(parameterString," ");
	            int N=0;
	            int P=0;
	            int Q=0;
	            if (stt.hasMoreTokens())
	                N = Integer.parseInt(stt.nextToken().trim());
	            if (stt.hasMoreTokens())
	                P = Integer.parseInt(stt.nextToken().trim());
	            if (stt.hasMoreTokens())
	                Q = Integer.parseInt(stt.nextToken().trim());
	           
	            // check whether input parameters are valid. i.e, N = P*Q
	            if(N != P*Q)
	            {
	            	System.out.println("Your input parameters are invalid.");
	            	System.out.println("N should be equal to P*Q");
	            }
	            else
	            {     
	            	
	    	        
	                Scanner scan = new Scanner( System.in );
	                String heuristic="0";
	                double timeout = 0.0;
	                
	                while(Integer.parseInt(heuristic) != 4)
	                {
	                	System.out.print("Enter default Timeout(sec) : ");
	                	timeout = scan.nextDouble();
	                	timeout*=1000;// change it to milliseconds
	                	
		                	
	                	System.out.println("1 : Backtracking Search. 2 : ForwardChecking Search. 3.AC 4.Stop");
	 	                System.out.print("Enter your heuristic: ");
	                	heuristic = scan.next( );
		                switch(Integer.parseInt(heuristic)) {
		                case 1:
		                {
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	    
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this variable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	                ArrayList<Double> searchTimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
			                	
			                	BacktrackSearch bs = new BacktrackSearch();
			                	bs.init(puzzleString, N);
			                	double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime();
			        	        if (!bs.isSolved(N))
			        	        {
			        	        	bs.backtrackingSearch(N,P,Q,startTotalTime,timeout);
			        	        	
			        	        	 endSearchTime = timer.endTime(startSearchTime);
					        	     endTotalTime = timer.endTime(startTotalTime);
					                 runtimes.add(endTotalTime);
					                 searchTimes.add(endSearchTime);
			        	        	//bs.printGrid(N);
					                 boolean solved = bs.isSolved(N);
					                 boolean timedout = bs.getTimeout();
					                printLog( endTotalTime, bs.getAssignments(),solved , timedout);
					                bs.setAssignments();// set assignment count to 0
			        	        }
			        	       
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	showCalculationSearchTime(allRuntimes, statistic, searchTimes );
		                	break;
		                }
		                case 2:
		                {
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this var\iable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
			                	ForwardCheckingSearch fc = new ForwardCheckingSearch();
			                	//BacktrackSearch bs = new BacktrackSearch();
			                	fc.init(puzzleString, N,P,Q);
			                	double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime();
			        	        if (!fc.isSolved(N))
			        	        {
			        	        	
			        	        	//bs.backtrackingSearch(N,P,Q);
			        	        	fc.forwardChecking(N, P, Q,startTotalTime,timeout);
			        	        	
			        	        	endSearchTime = timer.endTime(startSearchTime);
				        	        endTotalTime = timer.endTime(startTotalTime);
				                    runtimes.add(endTotalTime);
			        	        	//fc.printGrid(N);
				                    boolean solved = fc.isSolved(N);
					                 boolean timedout = fc.getTimeout();
					                printLog( endTotalTime, fc.getAssignments(),solved , timedout);
					                fc.setAssignments();// set assignment count to 0
			        	        }
			        	        
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	break;
		                }
		                case 3:
		                {
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	  
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this var\iable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
		                		ArcConsistency ac = new ArcConsistency();
			                	//BacktrackSearch bs = new BacktrackSearch();
		                		ac.init(puzzleString, N);
		                		double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime(); 
			        	        if (!ac.isSolved(N))
			        	        {
			        	        	//bs.backtrackingSearch(N,P,Q);
			        	        	ac.ACSolver(N, P, Q,startTotalTime,timeout);
			        	        	endSearchTime = timer.endTime(startSearchTime);
				        	        endTotalTime = timer.endTime(startTotalTime);
				                    runtimes.add(endTotalTime);
				                    boolean solved =  ac.isSolved(N);
				                    boolean timedout = ac.getTimeout();
				                    printLog( endTotalTime, ac.getAssignments(),solved , timedout);
				                    ac.setAssignments();// set assignment count to 0
			        	      
			        	        }
			        	        
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	break;
		                }
		                case 4:
		                {
		                	System.out.println("BYE");
		                	break;
		                }
		                default:
		                	System.out.println("Invalid input. Please select 1 or 2");
		                	break;
		                }
	                }
	                
	          
	            }
	            
		    } finally {
		        br.close();
		    }

  }
	
	
	
	
	// function for automatically run all program
	public static String autosolving(String string, int M, String heuristicNum, double timeSet) throws Exception
	{
		
		  BufferedReader br = new BufferedReader(new FileReader(string));
		  try {
			  	
				String returnOutput = "";
		        StringBuilder parameter = new StringBuilder();
		        StringBuilder puzzle = new StringBuilder();
		        String line = br.readLine();
		        
		        //read the first line which specifies the size of sudoku puzzle
		        parameter.append(line);
		        parameter.append('\n');
	            line = br.readLine();
	            // remove whitespace and initialize three parameters, N, P, and Q
	            String parameterString = parameter.toString();
	            StringTokenizer stt = new StringTokenizer(parameterString," ");
	            int N=0;
	            int P=0;
	            int Q=0;
	            if (stt.hasMoreTokens())
	                N = Integer.parseInt(stt.nextToken().trim());
	            if (stt.hasMoreTokens())
	                P = Integer.parseInt(stt.nextToken().trim());
	            if (stt.hasMoreTokens())
	                Q = Integer.parseInt(stt.nextToken().trim());
	           
	            // check whether input parameters are valid. i.e, N = P*Q
	            if(N != P*Q)
	            {
	            	System.out.println("Your input parameters are invalid.");
	            	System.out.println("N should be equal to P*Q");
	            }
	            else
	            {     
	            	
	    	        
	                Scanner scan = new Scanner( System.in );
	                String heuristic="0";
	                double timeout = 0.0;
	                
	                while(Integer.parseInt(heuristic) != 4)
	                {
	                              	    				
	                	//System.out.print("Enter default Timeout(sec) : ");
	                	timeout = timeSet;
	                	//timeout = scan.nextDouble();
	                	timeout*=1000;// change it to milliseconds
	                	
		                	
	                	//System.out.println("1 : Backtracking Search. 2 : ForwardChecking Search. 3.AC 4.Stop");
	 	               // System.out.print("Enter your heuristic: ");
	                	heuristic = heuristicNum;
	 	               
		                switch(Integer.parseInt(heuristic)) {
		                case 1:
		                {
		                	heuristic="4";
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	    
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this variable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	                ArrayList<Double> searchTimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
			                	
			                	BacktrackSearch bs = new BacktrackSearch();
			                	bs.init(puzzleString, N);
			                	bs.setTimeout();
			                	double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime();
			        	        if (!bs.isSolved(N))
			        	        {
			        	        	bs.backtrackingSearch(N,P,Q,startTotalTime,timeout);
			        	        	
			        	        	 endSearchTime = timer.endTime(startSearchTime);
					        	     endTotalTime = timer.endTime(startTotalTime);
					                 runtimes.add(endTotalTime);
					                 searchTimes.add(endSearchTime);
			        	        	//bs.printGrid(N);
					                 boolean solved = bs.isSolved(N);
					                 
					                 boolean timedout = bs.getTimeout();
					                printLog( endTotalTime, bs.getAssignments(),solved , timedout);
					                returnOutput+= M + "," + bs.getAssignments() + "," + runtimes + "," + searchTimes + "," 
					                		+ solved+","+ timedout;
					                bs.setAssignments();// set assignment count to 0
			        	        }
			        	       
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	showCalculationSearchTime(allRuntimes, statistic, searchTimes );
		                	
		                	break;
		                }
		                case 2:
		                {
		                	heuristic="4";
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this var\iable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	                ArrayList<Double> searchTimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
			                	
		                		ForwardCheckingSearch fc = new ForwardCheckingSearch();
		                		fc.init(puzzleString, N);
		                		fc.setTimeout();
			                	double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime();
			        	        if (!fc.isSolved(N))
			        	        {
			        	        	fc.backtrackingSearch(N,P,Q,startTotalTime,timeout);
			        	        	
			        	        	 endSearchTime = timer.endTime(startSearchTime);
					        	     endTotalTime = timer.endTime(startTotalTime);
					                 runtimes.add(endTotalTime);
					                 searchTimes.add(endSearchTime);
			        	        	//bs.printGrid(N);
					                 boolean solved = fc.isSolved(N);
					                 
					                 boolean timedout = fc.getTimeout();
					                printLog( endTotalTime, fc.getAssignments(),solved , timedout);
					                returnOutput+= M + "," + fc.getAssignments() + "," + runtimes + "," + searchTimes + "," 
					                		+ solved+","+ timedout;
					                fc.setAssignments();// set assignment count to 0
			        	        }
			        	       
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	showCalculationSearchTime(allRuntimes, statistic, searchTimes );
		                	
		                	break;
		                }
		                case 3:
		                {
		                	heuristic="4";
		                	//START: SEARCH_TIME
			    	        Timer timer = new Timer(); 	  
			    	        double startTotalTime = 0.0;
			    	        double endTotalTime = 0.0;
			    	        startTotalTime = timer.startTime();
			            	// read sudoku puzzle by each line
			    	        while (line != null) {
			    	        	puzzle.append(line.replaceAll("\\s+",","));
			    	        	puzzle.append(",");
			    	            line = br.readLine();
			    	        }
			    	        // this var\iable contains actual sudoku puzzle
			    	        String puzzleString = puzzle.toString();
			    	        
		                	ArrayList<Double> allRuntimes = new ArrayList<Double>();
		                	Statistic statistic = new Statistic();
		 	                ArrayList<Double> runtimes = new ArrayList<Double>();
		 	               ArrayList<Double> searchTimes = new ArrayList<Double>();
		 	                
		                	for (int i = 0; i < RUN_TIME; i++) 
			                {
			                	// sudoku puzzle initialize
			                	
		                		ArcConsistency ac = new ArcConsistency();
		                		ac.setTimeout();
		                		ac.init(puzzleString, N);
			                	double startSearchTime = 0.0;
			                    double endSearchTime = 0.0;
			                    startSearchTime = timer.startTime();
			        	        if (!ac.isSolved(N))
			        	        {
			        	        	
			        	        	ac.backtrackingSearch(N,P,Q,startTotalTime,timeout);
			        	        	
			        	        	 endSearchTime = timer.endTime(startSearchTime);
					        	     endTotalTime = timer.endTime(startTotalTime);
					                 runtimes.add(endTotalTime);
					                 searchTimes.add(endSearchTime);
			        	        	//bs.printGrid(N);
					                 boolean solved = ac.isSolved(N);
					                 
					                 boolean timedout = ac.getTimeout();
					                printLog( endTotalTime, ac.getAssignments(),solved , timedout);
					                returnOutput+= M + "," + ac.getAssignments() + "," + runtimes + "," + searchTimes + "," 
					                		+ solved+","+ timedout;
					                ac.setAssignments();// set assignment count to 0
			        	        }
			        	       
			                }
		                	showCalculation(allRuntimes, statistic, runtimes );
		                	showCalculationSearchTime(allRuntimes, statistic, searchTimes );
		                	
		                	break;
		                }
		                case 4:
		                {
		                	System.out.println("BYE");
		                	break;
		                }
		                default:
		                	System.out.println("Invalid input. Please select 1 or 2");
		                	break;
		                }
	                }
	                
	          
	            }
	            return returnOutput;
		    } finally {
		        br.close();
		        
		    }

  }
}
