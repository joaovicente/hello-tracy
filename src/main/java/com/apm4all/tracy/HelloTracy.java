package com.apm4all.tracy;
import com.apm4all.tracy.Tracy;


public class HelloTracy {
	static final String COMPONENT = "hello-tracy";
	static final String OUTER = "outer";
	static final String INNER = "inner";
	static final int DEFAULT_DELAY = 300;
	private int delay;


	public static void main(String[] args) throws InterruptedException  {
    	// With no delay this utility generates ~5 Tracy segments Per Second (~300 per minute)
    	// But to slow down Tracy frame generation a default delay of 300 msec is added (slightly above 100 per minute)
    	// To change this delay, simply pass the msec delay value as a CLI argument
    	HelloTracy helloTracy = new HelloTracy(parseDelay(args));
    	helloTracy.outer();
    }

    public static int parseDelay(String[] args)	{
    	int delay = DEFAULT_DELAY;
    	if (args.length > 0) {
    		try {
    			delay = Integer.parseInt(args[0]);
    		} catch (NumberFormatException e) {
    			System.err.println("Delay argument must be an integer expressing delay required in milliseconds.");
    			System.exit(1);
    		} finally {
    		}
    	}
		return delay;
    }

    public HelloTracy(int delay) {
    	this.delay = delay;
    }

	private void outer() throws InterruptedException {
		
    	for (;;)	{
    		
    		Tracy.setContext(null, null, COMPONENT);
    		Tracy.before(OUTER);
    		inner();
    		Tracy.after(OUTER);
    		for (String event : Tracy.getEventsAsJson())	{
    			System.out.println(event);
    		}
    		Thread.sleep(delay);
    		Tracy.clearContext();
    	}
		
	}
    
	private void inner() throws InterruptedException {
    	Tracy.before(INNER);
    	// Sleep between 100-300 ms
    	long delayInMsec = new Double(Math.random() * 200).longValue() + 100;
    	Tracy.annotate("delayInMsec", delayInMsec);
    	Thread.sleep(delayInMsec);
    	Tracy.after(INNER);
	}
}
