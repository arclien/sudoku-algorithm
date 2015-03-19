
public class Timer{

    public double startTime() {
        return System.currentTimeMillis();
    }
    
    public double endTime( double dStart ) {
        double endTime = System.currentTimeMillis() - dStart;
        // prevent 0 runtime
        if (endTime < 1) {
            endTime = 1;
        }
        return endTime;
    }
    
  
}
