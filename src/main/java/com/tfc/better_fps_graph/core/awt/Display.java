package com.tfc.better_fps_graph.core.awt;

import com.tfc.better_fps_graph.core.Section;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Display {
	private Collection<Section> sections = new ArrayList<>();
	
	public void paint(Graphics g) {
		int y = 0;
		for (Section s : sections) {
			Color inv = new Color(
					(int) ((s.color[0] * 255)) & 255,
					(int) ((s.color[1] * 255)) & 255,
					(int) ((s.color[2] * 255)) & 255
			);
			
			Color col = new Color(
					255 - (int) ((s.color[0] * 255)) & 255,
					255 - (int) ((s.color[1] * 255)) & 255,
					255 - (int) ((s.color[2] * 255)) & 255
			);
			
			g.setColor(inv);
			g.fillRect(0, y, 1200, 17);
			g.setColor(col);
			g.drawString(s.name + "          Time Elapsed: " + s.time + " ms", 0, y + 14);
			y += 17;
		}
	}
	
	public static void main(String[] args) {
		Display disp = new Display();
		disp.sections.add(new Section(new float[]{0, 1, 0}, "test:hi1", 100));
		disp.sections.add(new Section(new float[]{1, 0, 0}, "test:hi2", 1000));
		disp.sections.add(new Section(new float[]{0, 0, 0}, "test:hi3", 1));
		disp.sections.add(new Section(new float[]{0, 0, 1}, "test:hi4", 3502));
		disp.sections = disp.sections.stream().sorted((section, t1) -> -Long.compare(section.time, t1.time)).collect(Collectors.toList());
		JFrame frame = new JFrame();
		frame.setSize(350, 100);
		JScrollPane bar = new JScrollPane();
		bar.add(bar.createVerticalScrollBar());
		BufferedImage image = new BufferedImage(800, disp.sections.size() * 17, BufferedImage.TYPE_INT_RGB);
		disp.paint(image.getGraphics());
		frame.setMaximumSize(new Dimension(image.getWidth() + 50, image.getHeight()));
		JLabel imageLabel = new JLabel(new ImageIcon(image));
		bar.getViewport().add(imageLabel);
		frame.add(bar);
		frame.setVisible(true);
	}
	
	public static void disp(Collection<Section> sections) {
		Display disp = new Display();
		disp.sections = sections;
		disp.sections = disp.sections.stream().sorted((section, t1) -> -Long.compare(section.time, t1.time)).collect(Collectors.toList());
		JFrame frame = new JFrame();
		frame.setSize(350, 100);
		JScrollPane bar = new JScrollPane();
		bar.add(bar.createVerticalScrollBar());
		BufferedImage image = new BufferedImage(800, disp.sections.size() * 17, BufferedImage.TYPE_INT_RGB);
		disp.paint(image.getGraphics());
		frame.setMaximumSize(new Dimension(image.getWidth() + 50, image.getHeight()));
		JLabel imageLabel = new JLabel(new ImageIcon(image));
		bar.getViewport().add(imageLabel);
		frame.add(bar);
		frame.setVisible(true);
	}
}
