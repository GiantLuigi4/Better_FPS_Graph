package com.tfc.better_fps_graph.API;

import com.tfc.better_fps_graph.core.Frame;
import com.tfc.better_fps_graph.core.Section;
import net.minecraft.util.Util;

public class Profiler {
	static Frame frame = new Frame();
	
	static double sectionR = 0;
	static double sectionG = 0;
	static double sectionB = 0;
	
	static String lastName = "better_fps_graph:Unknown";
	
	static long start = 0;
	
	public static void reset() {
		frame = new Frame();
	}
	
	public static void fulReset() {
		frame = new Frame();
		start = Util.milliTime();
		sectionR = 0;
		sectionG = 0;
		sectionB = 0;
	}
	
	public static void addSection(String name, double r, double g, double b) {
		Section section = frame.getSection(lastName);
		
		if (Util.milliTime() - start != 0) {
			if (section == null) {
				frame.addSection(lastName, new Section(new float[]{(float) sectionR, (float) sectionG, (float) sectionB}, lastName, Util.milliTime() - start));
			} else {
				section.addTime(Util.milliTime() - start);
			}
		}
		
		sectionR = r;
		sectionG = g;
		sectionB = b;
		lastName = name;
		start = Util.milliTime();
	}
	
	public static void addSection(String name) {
		Section section = frame.getSection(lastName);
		
		if (Util.milliTime() - start != 0) {
			if (section == null) {
				frame.addSection(lastName, new Section(new float[]{(float) sectionR, (float) sectionG, (float) sectionB}, lastName, Util.milliTime() - start));
			} else {
				section.addTime(Util.milliTime() - start);
			}
		}
		
		sectionR = (name.length() % 255) / 255f;
		sectionG = name.lastIndexOf(name.charAt(name.length() / 2));
		sectionB = name.indexOf(name.charAt(name.length() / 3));
		lastName = name;
		start = Util.milliTime();
	}
	
	public static void endSection() {
		Section section = frame.getSection(lastName);
		
		if (Util.milliTime() - start != 0) {
			if (section == null) {
				frame.addSection(lastName, new Section(new float[]{(float) sectionR, (float) sectionG, (float) sectionB}, lastName, Util.milliTime() - start));
			} else {
				section.addTime(Util.milliTime() - start);
			}
		}
		
		sectionR = 0;
		sectionG = 0;
		sectionB = 0;
		lastName = "better_fps_graph:Unknown";
		start = Util.milliTime();
	}
	
	public static Frame getFrame() {
		return frame;
	}
}
