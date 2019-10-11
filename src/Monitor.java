/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 */
public class Monitor  
{
	/*
	 * ------------    
	 * Data members 
	 * ------------
	 */
	private int philosopherCount = 0;
	private int phil[];
	private int chop[];
	private int talking = 1;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{	this.philosopherCount = piNumberOfPhilosophers;
		this.phil = new int[piNumberOfPhilosophers];
		this.chop = new int[piNumberOfPhilosophers];
		
		for(int i = 0; i < piNumberOfPhilosophers; i++) {
			//0 state means thinking.
			//1 is hungry.
			//2 is eating.
			//3 is talking.
			this.phil[i] = 0;
			//-1 is not used. Other values means it belongs to that philosopher.
			this.chop[i] = -1;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{	int philNumber = piTID - 1;
		this.phil[philNumber] = 1;
		
		//Checking If I have both the 
		this.test(philNumber);
		while (this.phil[philNumber] == 1) {
			//Still hungry so wait.
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int philNumber = piTID - 1;
		//From eating, go straight to thinking.
		this.phil[philNumber] = 0;
		
		//Put down chopsticks.
		this.chop[philNumber] = -1;
		this.chop[(philNumber + 1) % this.philosopherCount] = -1;
		
		for(int i = 1; i < this.philosopherCount; i++) {
			this.test((philNumber + i) % this.philosopherCount);
		}
		
		//Wake up everyone.
		notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		if(this.talking == 1) {
			this.talking = 0;
		}
		else {
			try {
				while(this.talking == 0) {
					wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		this.talking = 1;
		notifyAll();
	}
	
	private void test(int philNumber) {
		//pick up left chopstick
		if(this.phil[philNumber] == 1) {
			//Philosopher is not hungry. 
			if(this.chop[philNumber] == -1) {
				//picking the chopstick
				this.chop[philNumber] = philNumber;
			}
			
			//Try picking up the right chopstick
			if(this.chop[(philNumber + 1) % this.philosopherCount] == -1) {
				this.chop[(philNumber + 1) % this.philosopherCount] = philNumber;
			}
			
			if(this.chop[philNumber] == philNumber && this.chop[(philNumber + 1) % this.philosopherCount] == philNumber) {
				//I have both the chopsticks
				this.phil[philNumber] = 2;
			}
		}
	}
}

// EOF
