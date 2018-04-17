package util.taskExecutors;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
	// References -->
	// https://stackoverflow.com/questions/20387881/how-to-run-certain-task-every-day-at-a-particular-time-using-scheduledexecutorse
	// Fields
	private static final int TIMEOUT = 2; // Minutes
	private final ScheduledExecutorService executor;
	private Runnable task;
	
	//Constructors
	public TaskExecutor(Runnable task) {
		this.executor = Executors.newSingleThreadScheduledExecutor();
		setTask(task);
	}
	
	//Methods
	public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
		//Create a new runnable which executes the set one's run method and schedules itself for another run
		Runnable runner = new Runnable() {
			
			@Override
			public void run() {
				task.run();
				startExecutionAt(targetHour, targetMin, targetSec);
			}
		};
		
		//Calculate the delay until the next execution in seconds
		long delaySeconds = computeNextDelayInSeconds(targetHour, targetMin, targetSec);
		
		//Schedule the executor to execute the task
		executor.schedule(runner, delaySeconds, TimeUnit.SECONDS);
	};
	

	private long computeNextDelayInSeconds(int targetHour, int targetMin, int targetSec) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
		if (zonedNow.compareTo(zonedNextTarget) > 0)
			zonedNextTarget = zonedNextTarget.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNextTarget);
		System.out.println("Next task is scheduled to start after: " + duration +" seconds");
		return duration.getSeconds();
	}

	public void stopTask() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(TIMEOUT, TimeUnit.MINUTES);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Setters
	public void setTask(Runnable task) {
		if(task != null) {
			this.task = task;
		}
	}
}
