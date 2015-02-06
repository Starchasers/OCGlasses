package com.bymarcin.openglasses.testRender;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;

public class TestSurface {

	@SubscribeEvent
	public void renderInWorld(RenderWorldLastEvent event) {

	}

	@SubscribeEvent
	public void renderOnGameOverlay(RenderGameOverlayEvent evt) {
	
	}
}
