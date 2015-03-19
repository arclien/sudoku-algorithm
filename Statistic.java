
import java.util.ArrayList;

public class Statistic {
    
    private double mean = 0.0;
    private double min = 0.0;
    private double max = 0.0;
    private double standardDeviation = 0.0; 
    
    /** Creates a new instance of RuntimeStatistic */
    public Statistic() {
    }
    
    public void calculate(ArrayList<Double> runtimes){
        if (runtimes != null && runtimes.size() > 0) {
            boolean gotMax = false;
            boolean gotMin = false;
            double total = 0.0;
            double sdTotal = 0.0;
            
            for (double runtime : runtimes) {
                if (!gotMax) {
                    gotMax = true;
                    max = runtime;
                } else {
                    max = Math.max(runtime, max);
                }
                
                if (!gotMin) {
                    gotMin = true;
                    min = runtime;
                } else {
                    min = Math.min(runtime, min);
                }
                
                total += runtime;
            }
            mean = total / (double)runtimes.size();
            
            for (double runtime : runtimes) {
                sdTotal += Math.pow(runtime - mean, 2);
            }
            
            standardDeviation = Math.sqrt(sdTotal / (double)(runtimes.size() - 1));
        }
    }
    
    public double mean() {
        return mean;
    }
    
    public double min() {
        return min;
    }
    
    public double max() {
        return max;
    }
    
    public double standardDeviation() {
        return standardDeviation;
    }
    
    
}
