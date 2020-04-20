package net.stuehler.utils.memoryutilize;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class ShortLifespanMemoryBean implements Serializable {

	private static final long serialVersionUID = 136920154537L;
	private static final Logger logger = Logger.getLogger(ShortLifespanMemoryBean.class.getName());
	private static ReentrantLock LOCK = new ReentrantLock();
	private final List<ShortLifespanMemory> memList = new LinkedList<>();

	public ShortLifespanMemoryBean() {
	}

	public void addMemory(ShortLifespanMemory slm) {
		LOCK.lock();
		try {
			memList.add(slm);
			logger.info("Added short lifespan memory " + slm);
		} finally {
			LOCK.unlock();
		}
	}

	@Schedule(second = "*", minute = "*", hour = "*")
	public void cleanupMemory() {
		logger.entering(ShortLifespanMemoryBean.class.getName(), "cleanupMemory");
		LOCK.lock();
		try {
			final long currentMillis = (new Date()).getTime();
			for (ShortLifespanMemory slm : memList) {
				if (currentMillis > (slm.getTimestamp().getTime() + slm.getLifeTimeSeconds() * 1000)) {
					memList.remove(slm);
					logger.info("Released short lifespan memory " + slm);
				}
			}
		} finally {
			LOCK.unlock();
		}
		logger.exiting(ShortLifespanMemoryBean.class.getName(), "cleanupMemory");
	}

}
