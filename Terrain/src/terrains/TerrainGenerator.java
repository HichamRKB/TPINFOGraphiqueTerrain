package terrains;

import generation.ColourGenerator;
import generation.PerlinNoise;
import utils.Colour;


public abstract class TerrainGenerator {

	private final PerlinNoise perlinNoise;
	private final ColourGenerator colourGen;

	public TerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen) {
		this.perlinNoise = perlinNoise;
		this.colourGen = colourGen;
	}


	public Terrain generateTerrain(int gridSize) {
		float[][] heights = generateHeights(gridSize, perlinNoise);
		Colour[][] colours = colourGen.generateColours(heights, perlinNoise.getAmplitude());
		return createTerrain(heights, colours);
	}


	public abstract void cleanUp();


	protected abstract Terrain createTerrain(float[][] heights, Colour[][] colours);


	private float[][] generateHeights(int gridSize, PerlinNoise perlinNoise) {
		float heights[][] = new float[gridSize + 1][gridSize + 1];
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				heights[z][x] = perlinNoise.getPerlinNoise(x, z);
			}
		}
		return heights;
	}
}
