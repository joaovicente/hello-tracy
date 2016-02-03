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

    private int randomStatus() {
    	long random = new Double(Math.random() * 100).longValue();
    	int status = 200;
    	if      ( random < 79 )	{ status = 200; }//  80%  200: OK
    	else if ( random > 99 ) { status = 500; }//   1%  202: Accepted
    	else if ( random > 97 ) { status = 429; }//   1%  307: Temp redirect
    	else if ( random > 87 ) { status = 404; }//   2%  400: Bad request
    	else if ( random > 84 ) { status = 401; }//   3%  401: Unauthorized
    	else if ( random > 82 ) { status = 400; }//  10%  404: not found
    	else if ( random > 81 ) { status = 307; }//   2%  429: Too many requests
    	else if ( random > 80 ) { status = 201; }//   1%  500: Internal server error
    	return status;
    }
    
	private void outer() throws InterruptedException {		
    	for (;;)	{
    		Tracy.setContext(null, null, COMPONENT);
    		Tracy.before(OUTER);
    		Tracy.annotate("status", randomStatus());
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
