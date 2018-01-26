package com.cplatform.jx.induce.server;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stopwatch {

	private Lock lock = new ReentrantLock();

	private Map<Object, Long> marks = new LinkedHashMap<Object, Long>() {

		/** serialVersionUID */
		private static final long serialVersionUID = -7761828723131744175L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<Object, Long> eldest) {
			return size() > 100;
		};
	};

	private boolean pause = true;

	private long runningMark;

	private long time = 0;

	public double getMarkInMilliseconds(Object mark) {
		return (getMarkInNanosecond(mark)) / 1000000.00;
	}

	public long getMarkInNanosecond(Object mark) {
		lock.lock();
		try {
			Long time = marks.get(mark);
			if (time == null) {
				return -1;
			} else {
				return time.longValue();
			}
		}
		finally {
			lock.unlock();
		}
	}

	public double getMarkInSeconds(Object mark) {
		return (getMarkInNanosecond(mark)) / 1000000000.00;
	}

	public double getTimeInMilliseconds() {
		return (getTimeInNanosecond()) / 1000000.00;
	}

	public long getTimeInNanosecond() {
		if (pause == true) {
			return time;
		} else {
			return time + System.nanoTime() - runningMark;
		}
	}

	public double getTimeInSeconds() {
		return (getTimeInNanosecond()) / 1000000000.00;
	}

	public boolean isRunning() {
		return pause = false;
	}

	public void reset() {
		pause = true;
		time = 0;
		runningMark = 0;
	}

	public void setMark(Object mark) {
		lock.lock();
		try {
			long time = getTimeInNanosecond();
			marks.put(mark, time);
		}
		finally {
			lock.unlock();
		}
	}

	public void start() {
		lock.lock();
		try {
			if (pause == true) {
				runningMark = System.nanoTime();
				pause = false;
			}
		}
		finally {
			lock.unlock();
		}
	}

	public void stop() {
		lock.lock();
		try {
			if (pause == false) {
				time = time + (System.nanoTime() - runningMark);
				pause = true;
			}
		}
		finally {
			lock.unlock();
		}
	}

}
