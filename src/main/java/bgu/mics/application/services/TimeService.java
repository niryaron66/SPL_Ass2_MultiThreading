package bgu.mics.application.services;

import bgu.mics.MessageBus;
import bgu.mics.MessageBusImpl;
import bgu.mics.MicroService;
import bgu.mics.application.messages.TerminateBroadcast;
import bgu.mics.application.messages.TickBroadCast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using .
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class TimeService extends MicroService{
	private int speed;
	private int duration;
	private Timer globalTimer;
	private int currentTime;
	private TimerTask task;

	public TimeService(int _speed , int _duration) {
		super("Time-Service");
		speed = _speed;
		duration = _duration;
		currentTime = 0;
		globalTimer = new Timer();

	}

	@Override
	protected void initialize() {
		MessageBus msgbus = MessageBusImpl.getInstance();
//		msgbus.register(this);

		//callback instructions for TerminateBroadcast
		subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
			this.terminate();
		});

		task = new TimerTask() {
			@Override
			public void run() {
				currentTime++;
				System.out.println("Current Time: "+currentTime);
				synchronized (msgbus) {
					sendBroadcast(new TickBroadCast());
				}
			}
		};

		globalTimer.scheduleAtFixedRate(task , 0 , speed);

		try{
			Thread.currentThread().sleep(speed*duration);}
		catch(Exception e){}

		sendBroadcast(new TerminateBroadcast());
		System.out.println("Timer sent termination.");		//////////////////////////////////////
		globalTimer.cancel();
		Thread.currentThread().stop();
	}

}
//	private int time;
//	private int speed;
//	private int duration;
//	private java.util.Timer timer;
//
//
//	public TimeService( int speed, int duration) {
//		super("TimeService");
//		time = 0;
//		this.speed=speed;
//		this.duration=duration;
//		timer = new Timer();
//	}
//	@Override
//	protected void initialize() {
//		MessageBusImpl.getInstance().register(this);

//		timerTask = new TimerTask() {
//			@Override
//			public void run() {
//				time++;
//				System.out.println("Current Time: "+time);
//				sendBroadcast(new TickBroadCast(time));
//			}
//		};
//		timer.scheduleAtFixedRate(timerTask , 0 , speed);
//
//		try{
//			Thread.sleep(duration-50);}
//		catch(Exception e){
//			System.out.println("TimerException");
//		}
//
//		timer.cancel();
//		//sendBroadcast(new TerminateBroadcast());
//		System.out.println("Terminate!");
//		terminate();
//
//	}
//
//}
//		while(time < duration) {
//			time += speed;
//			//System.out.println("Time is: " + time/speed);
//			MessageBusImpl.getInstance().sendBroadcast(new TickBroadCast());
//			try {
//				Thread.sleep(speed);
//			}
//			catch (InterruptedException e) {
//			}
//		}
//		MessageBusImpl.getInstance().sendBroadcast(new TerminateBroadcast());
//	//	System.out.println("Terminate!");
//		this.terminate();
//	}
//
//	public int getSpeed() {
//		return speed;
//	}
//}