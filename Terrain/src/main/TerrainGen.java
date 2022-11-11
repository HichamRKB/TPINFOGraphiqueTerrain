package main;

import org.lwjgl.input.Keyboard;

import flatTerrain.FlatTerrainGenerator;
import generation.ColourGenerator;
import generation.PerlinNoise;
import geometryTerrain.GeometryTerrainGenerator;
import indicesGenerators.PatternIndexGenerator;
import rendering.Light;
import rendering.RenderEngine;
import specialTerrain.SpecialTerrainGenerator;
import splitTerrain.SplitTerrainGenerator;
import terrains.Terrain;
import terrains.TerrainGenerator;

public class TerrainGen {

	public static void main(String[] args) {
		

		RenderEngine engine = new RenderEngine(Configs.FPS_CAP);
		Camera camera = new Camera();
		Light light = new Light(Configs.LIGHT_POS, Configs.LIGHT_COL, Configs.LIGHT_BIAS);


		PerlinNoise noise = new PerlinNoise(Configs.OCTAVES, Configs.AMPLITUDE, Configs.ROUGHNESS);
		ColourGenerator colourGen = new ColourGenerator(Configs.TERRAIN_COLS, Configs.COLOUR_SPREAD);


		TerrainGenerator geomGenerator = new GeometryTerrainGenerator(noise, colourGen, new PatternIndexGenerator());
		TerrainGenerator flatGenerator = new FlatTerrainGenerator(noise, colourGen, new PatternIndexGenerator());
		TerrainGenerator specialGenerator = new SpecialTerrainGenerator(noise, colourGen);
		TerrainGenerator splitGenerator = new SplitTerrainGenerator(noise, colourGen);

		Terrain splitTerrain = splitGenerator.generateTerrain(Configs.TERRAIN_SIZE);
		Terrain geometryTerrain = geomGenerator.generateTerrain(Configs.TERRAIN_SIZE);
		Terrain flatTerrain = flatGenerator.generateTerrain(Configs.TERRAIN_SIZE);
		Terrain hybridTerrain = specialGenerator.generateTerrain(Configs.TERRAIN_SIZE);

		while (!engine.getWindow().isCloseRequested()) {
			camera.move();
			

			if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
				engine.render(splitTerrain, camera, light);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
				engine.render(geometryTerrain, camera, light);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
				engine.render(flatTerrain, camera, light);
			} else {
				engine.render(hybridTerrain, camera, light);
			}

		}

		geomGenerator.cleanUp();
		flatGenerator.cleanUp();
		splitGenerator.cleanUp();
		specialGenerator.cleanUp();
		
		splitTerrain.delete();
		geometryTerrain.delete();
		flatTerrain.delete();
		hybridTerrain.delete();

		engine.close();

	}

}
