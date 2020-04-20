package net.stuehler.utils.memoryutilize;

import java.util.Date;

public class ShortLifespanMemory {

	private final Date ts = new Date();
	private final int lifeTimeSeconds;
	private final byte[][] memory;

	public ShortLifespanMemory(final int lifeTimeSeconds, final byte[][] memory) {
		this.lifeTimeSeconds = lifeTimeSeconds;
		this.memory = memory;
	}

	public Date getTimestamp() {
		return this.ts;
	}

	public int getLifeTimeSeconds() {
		return this.lifeTimeSeconds;
	}

	public byte[][] getMemory() {
		return this.memory;
	}

	@Override
	public String toString() {
		int numBytes = 0;
		for (byte[] bytes : memory) {
			numBytes += bytes.length;
		}
		return "[" + ts + ";" + lifeTimeSeconds + ";" + numBytes + "]";
	}

}
