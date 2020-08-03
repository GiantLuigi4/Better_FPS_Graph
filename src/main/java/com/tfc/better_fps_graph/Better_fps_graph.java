package com.tfc.better_fps_graph;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.better_fps_graph.API.Profiler;
import com.tfc.better_fps_graph.core.Frame;
import com.tfc.better_fps_graph.core.Section;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Util;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("better_fps_graph")
public class Better_fps_graph {
	
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	
	ArrayList<Frame> frames = new ArrayList<>();
	
	long lastRender = Util.milliTime();
	
	public Better_fps_graph() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.addListener(this::onDrawFPSGraph);
		MinecraftForge.EVENT_BUS.addListener(this::onDrawWorldLast);
		
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	int framesWithout = 0;
	
	private void onDrawFPSGraph(RenderGameOverlayEvent event) {
		if (event instanceof RenderGameOverlayEvent.Pre) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.FPS_GRAPH) {
				framesWithout=0;
				Profiler.addSection("Render Graph", 0,1,0);
				lastRender = Util.milliTime();
				double amp = 1;
				double scale = 1;
				if (Minecraft.getInstance().gameSettings.guiScale!=0) {
					amp = 1f/(Minecraft.getInstance().gameSettings.guiScale);
					scale=Minecraft.getInstance().gameSettings.guiScale;
				}
				int cap=(int)(Minecraft.getInstance().getMainWindow().getWidth()*(amp/7));
				while (frames.size()>=cap) {
					frames.remove(0);
				}
				int x=1;
				frames.add(Profiler.getFrame());
				long y1 = (int)(Minecraft.getInstance().getMainWindow().getHeight()/scale);
				long min = 9999999999L;
				long max = 0;
				for (Frame frame : frames) {
					long time=0;
					for (Section section : frame.sections.values()) {
						time+=section.time;
						drawRect(x,y1-(time*2),2,section.time*2,section.color[0],section.color[1],section.color[2],1);
					}
					min = Long.min(min,time);
					max = Long.max(max,time);
					x+=2;
				}
				long max_render = max*2;
				int left = x-(cap*2);
				//Boundries of fps graph
				drawRect(left,y1,cap*2+1,1,1,1,1,1);
				drawRect(left-1,y1-max_render,1,max_render,1,1,1,1);
				drawRect(x,y1-max_render,1,max_render,1,1,1,1);
				int max1 = Math.max(cap * 2 + 4, x + 4);
				drawRect(left,y1-((1000f/Minecraft.getInstance().gameSettings.framerateLimit)*2),cap*2,1,0,0,1,1);
				drawRect(left,y1-(min*2),cap*2,1,0,1,0,1);
				drawRect(left,y1-(max_render),cap*2,1,1,0,0,1);
				Minecraft.getInstance().fontRenderer.drawString(Minecraft.getInstance().gameSettings.framerateLimit+" FPS", max1,y1-((1000f/Minecraft.getInstance().gameSettings.framerateLimit)*2)-3,255);
				Minecraft.getInstance().fontRenderer.drawString("Minimum " + min + " ms", max1,y1-(min*2)-3,65280);
				Minecraft.getInstance().fontRenderer.drawString("Maximum " + max + " ms", max1,y1-(max_render)-3,16711680);
				Profiler.reset();
				event.setCanceled(event.isCancelable());
				Profiler.endSection();
			}
		}
	}
	
	private void onDrawWorldLast(RenderWorldLastEvent event) {
		framesWithout++;
		if (framesWithout>=4) {
			frames.clear();
			Profiler.fulReset();
		}
	}
	
	public static void drawRect(double x,double y,double width,double height,double red,double green,double blue,double alpha) {
		Tessellator tessellator=Tessellator.getInstance();
		BufferBuilder buffer=tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(x,(y+height),0).color((float)red,(float)green,(float)blue,(float)alpha).endVertex();
		buffer.pos((x+width),(y+height),0).color((float)red,(float)green,(float)blue,(float)alpha).endVertex();
		buffer.pos((x+width),y,0).color((float)red,(float)green,(float)blue,(float)alpha).endVertex();
		buffer.pos(x,y,0).color((float)red,(float)green,(float)blue,(float)alpha).endVertex();
		tessellator.draw();
	}
}
