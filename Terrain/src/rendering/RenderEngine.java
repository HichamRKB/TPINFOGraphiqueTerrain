package rendering;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import display.Window;
import terrains.Terrain;
import utils.OpenGlUtils;

public class RenderEngine {

	private final Window window;

	public RenderEngine(int fps) {
		this.window = Window.newWindow(1280, 720, fps).antialias(true).create();
	}

	public void render(Terrain terrain, ICamera camera, Light light) {
		prepare();
		terrain.render(camera, light);
		window.update();
	}

	public Window getWindow() {
		return window;
	}

	public void close() {
		window.destroy();
	}


	private void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
		OpenGlUtils.cullBackFaces(true);
		OpenGlUtils.enableDepthTesting(true);
		OpenGlUtils.antialias(true);
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}

}
