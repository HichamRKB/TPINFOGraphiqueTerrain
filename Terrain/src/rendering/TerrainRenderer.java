package rendering;

import org.lwjgl.opengl.GL11;

import terrains.Terrain;


public class TerrainRenderer {

	private final TerrainShader shader;
	private final boolean hasIndices;


	public TerrainRenderer(TerrainShader shader, boolean usesIndices) {
		this.shader = shader;
		this.hasIndices = usesIndices;
	}


	public void render(Terrain terrain, ICamera camera, Light light) {
		prepare(terrain, camera, light);
		if (hasIndices) {
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		} else {
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, terrain.getVertexCount());
		}
		finish(terrain);
	}


	public void cleanUp() {
		shader.cleanUp();
	}


	private void prepare(Terrain terrain, ICamera camera, Light light) {
		terrain.getVao().bind();
		shader.start();
		shader.lightBias.loadVec2(light.getLightBias());
		shader.lightDirection.loadVec3(light.getDirection());
		shader.lightColour.loadVec3(light.getColour().getVector());
		shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
	}

	/**
	 * End the rendering process by unbinding the VAO and stopping the shader
	 * program.
	 * 
	 * @param terrain
	 */
	private void finish(Terrain terrain) {
		terrain.getVao().unbind();
		shader.stop();
	}

}
