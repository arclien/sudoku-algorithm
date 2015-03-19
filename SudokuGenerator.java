
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class SudokuGenerator
{
  // structure for holding sudoku 
  static ArrayList<Integer>[] cells;
  int assignedCellCount = 0;
  static boolean puzzleCreated = false;
  

  public void init(String puzzle, int N)
  {
    for (int i=0;i<(N*N);i++)
    {
      cells[i]=new ArrayList<Integer>();
      String cell=puzzle.substring(i,i+1);
      if (cell.equals("0"))  // unsolved cells, all values are possible
        for (int digit=1;digit<=N;digit++) cells[i].add(digit);
      else  // solved cells, only one possible value
        cells[i].add(Integer.parseInt(cell));
    }
  }

 
  public void generate(int N, int P, int Q, int M)
  {
  
	Random random = new Random( System.nanoTime() );
	int numOfAssignedVal = M;
	
	//int numOfAssignedVal = 20;
	while ( numOfAssignedVal > 0)
	{
		 int cellIndex=Math.abs(random.nextInt()) % (N*N);  // 0-80
		 boolean isAssigned = false;
		// System.out.println(N+" " +cellIndex);
		 if ( cells[cellIndex].size()!= N) continue;  // try unassigned cell only
		
		 // initialize possible values for each cells( 1~9 )
		 ArrayList<Integer> possibleDigit = new ArrayList<Integer>();
		 for (int i=1;i<=N;i++) 
			 possibleDigit.add(i);
	
		 while( isAssigned == false && possibleDigit.size() > 0 )
		 {
			 int trialDigit=(Math.abs(random.nextInt()) % N)+1;  // 1~9
			 if(possibleDigit.contains(trialDigit))
			 {
				 //the last adding part. 
				 possibleDigit.remove(Integer.valueOf(trialDigit));
				 cells[cellIndex].clear();
				 cells[cellIndex].add(trialDigit);
			 }
			 else
				 continue;
		
			 if( isValid(cellIndex, N, P, Q) )
			 {
				assignedCellCount++;
				numOfAssignedVal--;	 
				isAssigned = true;
			 }
			
		 }
		 
		 if( !isAssigned )
		 {
			 /*
			 cells[cellIndex].clear();
			 cells[cellIndex].add(0);
			 System.out.println("no possible choice : "+ assignedCellCount);
			 */
			 break;
		 }
	}
	
	// check whether required cells are all assigned
	if(numOfAssignedVal == 0)
		puzzleCreated = true;
  }

  private boolean isValid(int cellIndex, int N, int P, int Q) {
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



public void createPuzzle(String fileName, int N, int P, int Q) throws Exception
  {
    PrintWriter writer=new PrintWriter(new FileWriter(fileName));
    writer.println(N + " " + P + " " + Q);
    for (int i=0,row=0;row<N;row++)
    {
      String s="";
      for (int column=0;column<N;column++,i++)
      {
        if (cells[i].size()>1)      
          s+="0 ";
        else 
          s+=cells[i].get(0)+" ";
      } // next column
     
      writer.println( s.substring(0,s.length() - 1) );
    } // next row
    
 
 
    writer.close();

    //System.out.println( fileName +" is generated");
  }

// function for automatically run all program
public static void autorun(String fileName, int N, int P, int Q,int M)
{
	
	 try {
		 
		 // initialize an empty puzzle
	        String puzzle="";
	        for(int i=0; i< (N*N);i++)
	        	puzzle+="0";
	        
	    	cells  = new ArrayList[N*N];
	    	SudokuGenerator generator=new SudokuGenerator();
	        
	        do
	        {
	        //	System.out.println("again");
	        	generator.init(puzzle,N);
	        	generator.generate(N,P,Q,M);
	        }while(!puzzleCreated);
	              
	        generator.createPuzzle(fileName, N, P, Q);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
  public static void main(String[] args) throws Exception
  {
    // get command line parameter
    int N=0;
    int P=0;
    int Q=0;
    int M=0;
    String fileName = "";
    
    try {
      N=Integer.parseInt(args[0]);
      P=Integer.parseInt(args[1]);
      Q=Integer.parseInt(args[2]);
      M=Integer.parseInt(args[3]); 
      fileName = args[4];
    } catch (Exception e)
    {
      System.out.println("Inputs are invalid");
      System.out.println("Sample : ");
      System.out.println("9 3 3 15 output.txt");   
      System.out.println("First number represents parameter N");   
      System.out.println("the next two parameters indicate  P & Q repectively. NOTICE N=P*Q");   
      System.out.println("The last number is for the number of pre assigned values");   
      
      System.exit(0);
    }

   
    if( N != P*Q)
    {
    	System.out.println("Your input parameters are invalid. N != P*Q ");	
    }
    else if( M > (N*N) )
    {
    	System.out.println("Your input parameters are invalid. M cannot exceed N^2 ");	
    }
    else
    {
    	 // initialize an empty puzzle
        String puzzle="";
        for(int i=0; i< (N*N);i++)
        	puzzle+="0";
        
    	cells  = new ArrayList[N*N];
    	SudokuGenerator generator=new SudokuGenerator();
        
        do
        {
        	//System.out.println("again");
        	generator.init(puzzle,N);
        	generator.generate(N,P,Q,M);
        }while(!puzzleCreated);
              
        generator.createPuzzle(fileName, N, P, Q);
   
        	
    }  
  }
}
