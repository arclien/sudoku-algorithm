
 
import java.io.FileWriter;
import java.io.IOException;
 
public class GenerateCsv
{
   public static void main(String [] args)
   {
	   
   }
   
   public void passString(String output)
   {
	   generateCsvFile("output.csv",output); 
   }
 
   private static void generateCsvFile(String sFileName, String output)
   {
	try
	{
	    FileWriter writer = new FileWriter(sFileName);
 
	    writer.append(output);
	    
 
 
	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
    }
}
