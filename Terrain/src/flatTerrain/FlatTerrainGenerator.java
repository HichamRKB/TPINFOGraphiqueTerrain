package flatTerrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.util.vector.Vector3f;

import generation.ColourGenerator;
import generation.PerlinNoise;
import generation.SmoothNormalsGenerator;
import indicesGenerators.IndicesGenerator;
import openglObjects.Vao;
import rendering.TerrainRenderer;
import rendering.TerrainShader;
import terrains.Terrain;
import terrains.TerrainGenerator;
import utils.Colour;
import utils.MyFile;
import vertexDataStoring.DataStoring;
import vertexDataStoring.VaoLoader;


public class FlatTerrainGenerator extends TerrainGenerator {

	private static final MyFile VERTEX_SHADER = new MyFile("flatTerrain", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("flatTerrain", "terrainFragment.glsl");

	private static final int VERTEX_SIZE_BYTES = 12 + 4 + 4;// position + normal
															// + colour

	private final IndicesGenerator indicesGenerator;
	private final TerrainRenderer renderer;


	public FlatTerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen, IndicesGenerator indicesGenerator) {
		super(perlinNoise, colourGen);
		this.indicesGenerator = indicesGenerator;
		this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER), true);
	}

	@Override
	public Terrain createTerrain(float[][] heights, Colour[][] colours) {
		Vector3f[][] normals = SmoothNormalsGenerator.generateNormals(heights);
		byte[] meshData = getMeshData(heights, normals, colours);
		int[] indices = indicesGenerator.generateIndexBuffer(heights.length);
		Vao vao = VaoLoader.createVao(meshData, indices);
		return new Terrain(vao, indices.length, renderer);
	}

	@Override
	public void cleanUp() {
		renderer.cleanUp();
	}


	private static byte[] getMeshData(float[][] heights, Vector3f[][] normals, Colour[][] colours) {
		int byteSize = VERTEX_SIZE_BYTES * heights.length * heights[0].length;
		ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				DataStoring.packVertexData(x, heights[z][x], z, normals[z][x], colours[z][x], buffer);
			}
		}
		return buffer.array();
	}

}
