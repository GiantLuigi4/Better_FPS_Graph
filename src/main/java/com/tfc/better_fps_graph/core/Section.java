package com.tfc.better_fps_graph.core;

import java.util.ArrayList;

public class Section {
	public final float[] color;
	public long time = 0;
	
	public Section(float[] color, long time) {
		this.color = color;
		this.time = time;
	}
	
	public void addTime(long time) {
		this.time += time;
	}
}
