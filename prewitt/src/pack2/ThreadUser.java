package pack2;

//abstract class that implements runnable for using the threads
// also implements the interface User and extends the ImageUser class
//(further so we can derive consumer and producer from this)
public abstract class ThreadUser extends ImageUser implements User, Runnable{
	private Thread thread;
	
	public void start() {
		thread = new Thread(this);//creates a new thread
		thread.start();//the thread is starting
	}
	
	public abstract void run();
	
	public abstract void processImage();
}
