package net.stuehler.utils.memoryutilize;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;

@ManagedBean
@ApplicationScoped
public class MemoryLeakBean implements Serializable {
	
	private static final long serialVersionUID = 573446928342L;

	private final List<byte[][]> leak = new LinkedList<byte[][]>();

	public MemoryLeakBean() {
	}

	public void addArrays(byte[][] b) {
		leak.add(b);
	}

	public long clear() {
		long bytes = 0;
		for (byte[][] ba : leak) {
			for (byte[] b : ba) {
				bytes += b.length;
				b = null;
			}
			ba = null;
		}
		leak.clear();
		return bytes;
	}

}
