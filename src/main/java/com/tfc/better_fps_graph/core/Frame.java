package com.tfc.better_fps_graph.core;

import java.util.HashMap;

public class Frame {
	public HashMap<String, Section> sections = new HashMap<>();
	
	public void addSection(String name, Section section) {
		sections.put(name, section);
	}
	
	public Section getSection(String name) {
		return sections.get(name);
	}
}
