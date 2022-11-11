package geometryTerrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import generation.ColourGenerator;
import generation.PerlinNoise;
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


public class GeometryTerrainGenerator extends TerrainGenerator {

	private static final MyFile VERTEX_SHADER = new MyFile("geometryTerrain", "terrainVertex.glsl");
	private static final MyFile GEOMETRY_SHADER = new MyFile("geometryTerrain", "terrainGeometry.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("geometryTerrain", "terrainFragment.glsl");

	private static final int VERTEX_SIZE_BYTES = 12 + 4;// position + colour

	private final IndicesGenerator indicesGenerator;
	private final TerrainRenderer renderer;


	public GeometryTerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen,
			IndicesGenerator indicesGenerator) {
		super(perlinNoise, colourGen);
		this.indicesGenerator = indicesGenerator;
		this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, GEOMETRY_SHADER, FRAGMENT_SHADER), true);
	}

	@Override
	public Terrain createTerrain(float[][] heights, Colour[][] colours) {
		byte[] meshData = getMeshData(heights, colours);
		int[] indices = indicesGenerator.generateIndexBuffer(heights.length);
		Vao vao = VaoLoader.createVaoNoNormals(meshData, indices);
		return new Terrain(vao, indices.length, renderer);
	}

	@Override
	public void cleanUp() {
		renderer.cleanUp();
	}

	
	private static byte[] getMeshData(float[][] heights, Colour[][] colours) {
		int byteSize = VERTEX_SIZE_BYTES * heights.length * heights[0].length;
		ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				DataStoring.packVertexData(x, heights[z][x], z, colours[z][x], buffer);
			}
		}
		return buffer.array();
	}

}
