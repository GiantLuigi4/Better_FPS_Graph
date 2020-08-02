package com.tfc.better_fps_graph;

import com.tfc.better_fps_graph.API.Profiler;
import com.tfc.better_fps_graph.core.Frame;
import com.tfc.better_fps_graph.core.Section;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Util;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
				int cap=Minecraft.getInstance().getMainWindow().getWidth()/10;
				while (frames.size()>=cap) {
					frames.remove(0);
				}
				int x=1;
				frames.add(Profiler.getFrame());
				int height = 60;
				long y1 = Minecraft.getInstance().getMainWindow().getHeight()/2;
				drawRect(0,y1-height,1,height,1,1,1,1);
				drawRect(x,y1,cap*2+1,1,1,1,1,1);
				for (Frame frame : frames) {
					long y = Minecraft.getInstance().getMainWindow().getHeight()/2;
					for (Section section : frame.sections.values()) {
						y-=(section.time*2);
						drawRect(x,y,2,section.time*2,section.color[0],section.color[1],section.color[2],1);
					}
					x+=2;
				}
				drawRect(cap*2+1,y1-height,1,height,1,1,1,1);
				Profiler.reset();
				event.setCanceled(event.isCancelable());
				Profiler.endSection();
			}
		}
	}
	
	private void onDrawWorldLast() {
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
