package com.tfc.better_fps_graph.core;

public class Section {
	public final float[] color;
	public final String name;
	public long time = 0;
	
	public Section(float[] color, String name, long time) {
		this.color = color;
		this.name = name;
		this.time = time;
	}
	
	public void addTime(long time) {
		this.time += time;
	}
}
