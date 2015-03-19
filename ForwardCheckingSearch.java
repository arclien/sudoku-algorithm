import java.util.ArrayList;


public class ForwardCheckingSearch extends BacktrackSearch{
	  
	 public void init(String puzzle, int N, int P, int Q)
	  {
		  cells  = new ArrayList[N*N];
		  String[] cell = puzzle.split(",");
	    for (int i=0;i<(N*N);i++)
	    {
	      cells[i]=new ArrayList<Integer>();
	      
	      if (cell[i].equals("0"))  // unsolved cells, all values are possible
	        for (int digit=1;digit<=N;digit++) cells[i].add(digit);
	      else
	        cells[i].add(Integer.parseInt( cell[i] ));
	    }
	    // pre-propagation
	    for (int i=0;i<(N*N);i++)
	    {
	    	if(cells[i].size() == 1)
	    		elimination(i,N,P,Q);
	    }
	  }
	 
	public boolean forwardChecking(int N,int P,int Q, double startTotalTime, double timeout)
	  {
	    // backup the puzzle first for backtracking search
	    ArrayList<Integer>[] backupState=new ArrayList[(N*N)];
	    for (int i=0;i<(N*N);i++) // allocate back structure
	      backupState[i]=new ArrayList();
	    backup(cells, backupState, N);  // backup
	    
	    // get next unassigned cell
	    int CellIndex=-1;
	    for (int i=0;i<(N*N);i++)
	    	if (cells[i].size()>1) 
	    	{ 
	    		
	    		CellIndex=i; 
	    		break; 
	    	}
	    	else if(cells[i].size()==1)
	    	{
	    		if( !duplicationCheck(i,N,P,Q) )
	    			return false;
	    	}
	    
	    // If there is no unassigned cell, then the puzzle is solved
	    if (CellIndex==-1) return true;

	    // iterate through the values in order 1-9
	    for (int j=0;j<cells[CellIndex].size();j++) 
	    {       
		      Integer digit = cells[CellIndex].get(j); 
		      
		      // clear all possible values in that cell, and try selected value( 1-9 )
		      cells[CellIndex].clear();
		      cells[CellIndex].add(digit);    
		      assignments++;
		     
		      elimination(CellIndex, N, P, Q);
		      
		      // check whether the sudoku has solved through recursion( backtracking )
		      if (isSolved(N)) return true;  
		    
		      if ( !isPossible(N) && isValid(CellIndex,N,P,Q)  ) 
		      {
		    	 
		        // call backtrackSearch function( by recursion ), and check all cells(N*N)
		    	// until we can decide the sudoku is solvable or not
		    	  if( timeout > System.currentTimeMillis() - startTotalTime )
		    	  {
		    		  if (forwardChecking(N,P,Q,startTotalTime,timeout)) return true;  
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
	

		protected void elimination(int cellIndex, int N, int P, int Q) {
			//get all values in the same row,col, and box with current cellIndex
			ArrayList<Integer> rows =  getRow(cellIndex, N);
			ArrayList<Integer> cols =  getCol(cellIndex, N);
			ArrayList<Integer> boxes =  getBox(cellIndex,N,P,Q);
			int newAssignedValue = cells[cellIndex].get(0);
			//check whether currently assigned values are conflict with new value in cellIndex
			//if any => return true, never conflict => return true => assign complete without any contraint violation
			for (int i : rows)
				 if ( cells[i].contains(newAssignedValue) )
					  cells[i].remove(Integer.valueOf(newAssignedValue));
			    	
			for (int i : cols)
				if (  cells[i].contains(newAssignedValue) )
					 cells[i].remove(Integer.valueOf(newAssignedValue));
				  
			for (int i : boxes)
				if ( cells[i].contains(newAssignedValue) )
					 cells[i].remove(Integer.valueOf(newAssignedValue));
		
		}

		private ArrayList<Integer> getBox(int cellIndex, int N, int P, int Q) {
					
			ArrayList<Integer> box = new ArrayList<Integer>();
			int rowNum = cellIndex / N;
			int colNum = cellIndex % N;
					
			rowNum = rowNum - (rowNum % P);
			colNum = colNum - (colNum % Q);
			int boxCells = 0;

			for (int n = rowNum; n < rowNum+P; n++) 
			{
				     for (int m = colNum; m < colNum+Q; m++) 
				     {
				          boxCells = (n*N) + m;
				          if( boxCells!=cellIndex )
				          {
				            // add indexes of cells within the same region
				            box.add( boxCells );
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
				if( i!=cellIndex )
				{	 // add indexes of cells within the same column
					cols.add( i );
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
				if( i!=cellIndex )
				{
					 // add indexes of cells within the same row
					rows.add( i );
				}
						
			}
			return rows;
		}	
		

		private boolean duplicationCheck(int i, int N, int P, int Q) {
			
			int rowNum = i / N;
    		int colNum = i % N;
			// get all values in the same row
			for(int check = rowNum*N; check < rowNum*N + N; check++)
			{
				//check whether this cell has been already assigned any value
				if( check!=i && cells[check].size() == 1 && cells[check].get(0) == cells[i].get(0))
				{
					return false;
				}
						
			}
			
			for(int check = colNum; check < (N*N);)
			{
				
				if( check!=i && cells[check].size() == 1 && cells[check].get(0) == cells[i].get(0))
				{	 
					return false;
				}
				
				check+=N;
			}
			
			rowNum = rowNum - (rowNum % P);
			colNum = colNum - (colNum % Q);
			int boxCells = 0;

			for (int n = rowNum; n < rowNum+P; n++) 
			{
				     for (int m = colNum; m < colNum+Q; m++) 
				     {
				          boxCells = (n*N) + m;
				          if( boxCells!=i && cells[boxCells].size() == 1 && cells[boxCells].get(0) == cells[i].get(0))
				          {
					           return false;
				          }
				      }
			}
			
			return true;
		}
		
}
