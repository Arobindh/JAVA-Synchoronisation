
import java. util. Scanner;
/**
 * Class DiningPhilosophers 
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca     
 */
public class DiningPhilosophers 
{
	/*
	 * ------------
	 * Data members   
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			String s;
			int iPhilosophers=0;
			//getting the number of the philosophers
			System.out.println("Please enter the number of the philosophers.");
			Scanner in = new Scanner(System. in);
			s = in. nextLine();
			if(s!=null && s.length()==0)
				iPhilosophers=DEFAULT_NUMBER_OF_PHILOSOPHERS;
			else if(s!=null && isInteger(s) && Integer.parseInt(s)>0)
			{
				iPhilosophers=Integer.parseInt(s);
			}
			else if(s!=null && isInteger(s) && Integer.parseInt(s)<=0)
			{
				System.out.println("Please enter a positive number.");
			}

			// Make the monitor intialised to the number of the philosophers
			soMonitor = new Monitor(iPhilosophers);

		
			Philosophers aoPhilosophers[] = new Philosophers[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosophers();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	}
	// main()
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	    	System.out.println("Please enter a positive integer");
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
