import java.util.ArrayList;


public class BacktrackSearch {

	 // structure of sudoku  N BY N
	 static ArrayList<Integer>[] cells;
	 static boolean timedout = false;
	 static int assignments = 0;
	
	  public void init(String puzzle, int N)
	  {
		  cells  = new ArrayList[N*N];
		  String[] cell = puzzle.split(",");
	    for (int i=0;i<(N*N);i++)
	    {
	      cells[i]=new ArrayList<Integer>();
	      
	      if (cell[i].equals("0"))  // unsolved cells, all values are possible
	        for (int digit=1;digit<=N;digit++) cells[i].add(digit);
	      else  // solved cells, only one possible value
	        cells[i].add(Integer.parseInt( cell[i] ));
	    }
	  }

	  // display all possible of the grid
	  public void printGrid(int N)
	  {
	    String delimiter="";

	    for (int i=0;i<(N*N);i++)
	    {
	      String s="";
	      for (int j=0;j<cells[i].size();j++)
	      {
	        s+=cells[i].get(j);
	      }
	      // make sure each cell contains exactly 8 character
	     
	     
	      if (s.length()>1) s=" ";  // overflow
	      s=delimiter+s;
	      delimiter="|";
	      System.out.print(s);
	      if ((i+1)%N==0) { System.out.println(); delimiter=""; }
	    }
	  }

	  // check whether the puzzle is in a conflict state
	  // if some cell contains no possible values at all, then it is in a conflict state
	  protected boolean isPossible(int N)
	  {
	    boolean impossible=false;
	    for (int i=0;i<(N*N);i++)
	      if (cells[i].size()==0) 
	      	{  
	    	  // indicate no solution can be found
	    	  impossible=true; 
	    	  break;
	    	}
	    return impossible;
	  }

	  // check whether the sudoku is solved
	  public boolean isSolved(int N)
	  {
	    boolean solved=true;
	    for (int i=0;i<(N*N);i++)
	      if (cells[i].size()!=1) 
	      	{ 
	    	  solved=false;
	    	  break; 
	    	}
	    return solved;
	  }

	  // backup or restore a puzzle
	  protected void backup(ArrayList<Integer>[] original,ArrayList<Integer>[] duplication, int N)
	  {
	    for (int i=0;i<(N*N);i++)
	    {
	      duplication[i].clear();
	      for (int j=0;j<original[i].size();j++)
	      {
	    	 duplication[i].add(original[i].get(j));
	      }
	    }

	  }

	  public boolean backtrackingSearch(int N,int P,int Q,double startTotalTime,double timeout)
	  {
	    // backup the puzzle first for backtracking search
	    ArrayList<Integer>[] backupState=new ArrayList[(N*N)];
	    for (int i=0;i<(N*N);i++) // allocate back structure
	      backupState[i]=new ArrayList();
	    backup(cells, backupState, N);  // backup
	    
	    // get next unassigned cell
	    int unsolvedCellIndex=-1;
	    for (int i=0;i<(N*N);i++)
	    	if (cells[i].size()>1) 
	    	{ 
	    		unsolvedCellIndex=i; 
	    		break; 
	    	}
	    
	    // If there is no unassigned cell, then the puzzle is solved
	    if (unsolvedCellIndex==-1) return true;

	    // iterate through the values in order 1-9
	    for (int j=0;j<cells[unsolvedCellIndex].size();j++) 
	    {       
		      Integer digit = cells[unsolvedCellIndex].get(j); 
		      // clear all possible values in that cell, and try selected value( 1-9 )
		      cells[unsolvedCellIndex].clear();
		      cells[unsolvedCellIndex].add(digit);  
		      assignments++;
		     
		      // check whether the sudoku has solved through recursion( backtracking )
		      if (isSolved(N)) return true;  
		     
		      if ( isValid(unsolvedCellIndex,N,P,Q) && !isPossible(N) ) 
		      {
		        // call backtrackSearch function( by recursion ), and check all cells(N*N)
		    	// until we can decide the sudoku is solvable or not
		    	  if( timeout > System.currentTimeMillis() - startTotalTime )
		    	  {
		    		  if (backtrackingSearch(N,P,Q,startTotalTime,timeout)) return true;  
		    	  }
		    	  else
		    	  {
		    		  timeout();
		    		  return false;
		    	  }
		    	
		    		    
		      }
		      
		      // the trial digit cannot solve the puzzle
		      backup(backupState,cells, N); // restore from backup, try next digit
	    }
	    // all values of a cell have been tried 
	  
	    return false;   // cannot solve this way.
	  }

	  

	  protected boolean isValid(int cellIndex, int N, int P, int Q) {
		  //get all values in the same row,col, and box with current cellIndex
		  ArrayList<Integer> rows =  getRow(cellIndex, N);
		  ArrayList<Integer> cols =  getCol(cellIndex, N);
		  ArrayList<Integer> boxes =  getBox(cellIndex,N,P,Q);
		  
		  //check whether currently assigned values are conflict with new value in cellIndex
		  //if any => return true, never conflict => return true => assign complete without any contraint violation
		  for (int i : rows)
			    if ( cells[cellIndex].contains(i) )
			    	return false;
		
		  for (int i : cols)
			    if ( cells[cellIndex].contains(i) )
			    	return false;
		  
		  for (int i : boxes)
			    if ( cells[cellIndex].contains(i) )
			    	return false;
		  
		  return true;
	}

		protected void timeout() {
		timedout = true;
		
	}
		protected boolean getTimeout() {
			return timedout;
			
		}
		protected int getAssignments() {
			return assignments;
			
		}
		protected void setAssignments() {
			assignments=0;
			
		}

	private ArrayList<Integer> getBox(int cellIndex, int N, int P, int Q) {
		
		ArrayList<Integer> box = new ArrayList<Integer>();
		int rowNum = cellIndex / N;
		int colNum = cellIndex % N;
		
		rowNum = rowNum - (rowNum % P);
		colNum = colNum - (colNum % Q);
	    int boxCells = 0;

	     for (int n = rowNum; n < rowNum+P; n++) {
	             for (int m = colNum; m < colNum+Q; m++) {
	            	 boxCells = (n*N) + m;
	            	 if( boxCells!=cellIndex && cells[boxCells].size() == 1)
	         		 {
	            		 box.add( cells[boxCells].get(0) );
	         		 }
	             }
	     }
		
		return box;
	}


	private ArrayList<Integer> getCol(int cellIndex, int N) {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		int colNum = cellIndex % N;
		
		// get all values in the same row
		for(int i = colNum; i < (N*N);)
		{
			//check whether this cell has been already assigned any value
			if( i!=cellIndex && cells[i].size() == 1)
			{
				cols.add( cells[i].get(0) );
			}
			//column index number increase with 9.
			i+=N;
		}
		
		return cols;
	}



	private ArrayList<Integer> getRow(int cellIndex, int N) {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		int rowNum = cellIndex / N;
		
		// get all values in the same row
		for(int i = rowNum*N; i < rowNum*N + N; i++)
		{
			//check whether this cell has been already assigned any value
			if( i!=cellIndex && cells[i].size() == 1)
			{
				rows.add( cells[i].get(0) );
			}
			
		}
		return rows;
	}

	public void setTimeout() {
		timedout = false;
		
	}


}
BacktrackSearch
