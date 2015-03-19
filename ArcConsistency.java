import java.util.ArrayList;


public class ArcConsistency extends BacktrackSearch{
	
	// eliminate duplicate digit of the same region
	public boolean ConstraintPropagation(int N, int P, int Q, double startTotalTime, double timeout)
	{
			    boolean newSolved=false;
			    // loop over all cells
			    for (int i=0;i<(N*N);i++) 
			    {
			      // choose assigned cell and do constraint propagation on rows, cols, and boxes indexes
			      if (cells[i].size()==1)  
			      {
			        Integer newAssignedValue=cells[i].get(0);
			        
			        int rowNum = i / N;
					int colNum = i % N;
					
					// get all row indexes and check whether each cell contains newAssignedValue	
					for(int rowCells = rowNum*N; rowCells < rowNum*N + N; rowCells++)
						if( rowCells!=i )
							if ( cells[rowCells].contains(newAssignedValue) )
							 {
						          cells[rowCells].remove(newAssignedValue);
						          if (cells[rowCells].size()==1) 
						        	  newSolved=true;  // newly solved cell found
						     }			
					
					// get all column indexes and check whether each cell contains newAssignedValue
					for(int colCells = colNum; colCells < (N*N);)
					{
						if( colCells!=i )
							if ( cells[colCells].contains(newAssignedValue) )
							 {
						          cells[colCells].remove(newAssignedValue);
						          if (cells[colCells].size()==1) newSolved=true;  // newly solved cell found
						     }	
						
						colCells+=N;
					}
					
					// get all box indexes and check whether each cell contains newAssignedValue	
					rowNum = rowNum - (rowNum % P);
					colNum = colNum - (colNum % Q);
					int boxCells = 0;

					for (int z = rowNum; z < rowNum+P; z++) 
						 for (int m = colNum; m < colNum+Q; m++) 
						 {
						      boxCells = (z*N) + m;
						      if( boxCells!=i )
						       if ( cells[boxCells].contains(newAssignedValue) )
						       {
									cells[boxCells].remove(newAssignedValue);
									if (cells[boxCells].size()==1) 
										newSolved=true;  // newly solved cell found
						       }	
						 }

			      }
			    }
			    return newSolved;
	}


	public void solveByElimination(int cellIndex, int N, int P, int Q, double startTotalTime,double timeout)
	{
			boolean newCellSolved=false;
			while (true)
			{
			      newCellSolved=false;
			    
			      if (ConstraintPropagation(N, P, Q,startTotalTime,timeout)) newCellSolved=true;
			      if (!newCellSolved) break;  // no newly solved cell generated, quit
			}
	}

			  public boolean ACSolver(int N,int P,int Q, double startTotalTime, double timeout)
			  {
			    // backup the puzzle first for backtracking search
			    ArrayList<Integer>[] backupState=new ArrayList[(N*N)];
			    for (int i=0;i<(N*N);i++) // allocate back structure
			      backupState[i]=new ArrayList();
			    backup(cells, backupState, N);  // backup
			    
			    // get next unassigned cell
			    int unassignedCell=-1;
			    for (int i=0;i<(N*N);i++)
			    	if (cells[i].size()>1) 
			    	{ 
			    		unassignedCell=i; 
			    		break; 
			    	}
			    
			    // If there is no unassigned cell, then the puzzle is solved
			    if (unassignedCell==-1) return true;
 
			    // iterate through the values in order 1-9
			    for (int j=0;j<cells[unassignedCell].size();j++) 
			    {       
				      Integer digit = cells[unassignedCell].get(j); 
				      // clear all possible values in that cell, and try selected value( 1-9 )
				      cells[unassignedCell].clear();
				      cells[unassignedCell].add(digit);     
				      assignments++;
				      
				      solveByElimination(unassignedCell, N, P ,Q,startTotalTime,timeout);
				      // check whether the sudoku has solved through recursion( backtracking )
				      if (isSolved(N)) return true;  
				     
				      if ( isValid(unassignedCell,N,P,Q) && !isPossible(N)) 
				      { 
				    	 
				        // call backtrackSearch function( by recursion ), and check all cells(N*N)
				    	// until we can decide the sudoku is solvable or not
				    	  if( timeout > System.currentTimeMillis() - startTotalTime )
				    	  {
				    		  if (ACSolver(N,P,Q,startTotalTime,timeout)) return true;     
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
			

}
