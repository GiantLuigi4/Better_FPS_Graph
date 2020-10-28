package com.tfc.better_fps_graph.core;

public class Section {
	public final float[] color;
	public final String name;
	public long time = 0;
	
	public Section(float[] color, String name, long time) {
		this.color = color;
		this.color[0] = ((int) (this.color[0] * 255) & 255) / 255f;
		this.color[1] = ((int) (this.color[1] * 255) & 255) / 255f;
		this.color[2] = ((int) (this.color[2] * 255) & 255) / 255f;
		this.name = name;
		this.time = time;
	}
	
	public void addTime(long time) {
		this.time += time;
	}
}
