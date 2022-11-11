package rendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import utils.Colour;


public class Light {

	private Vector3f direction;
	private Colour colour;
	private Vector2f lightBias;// how much ambient light and how much diffuse
								// light

	public Light(Vector3f direction, Colour colour, Vector2f lightBias) {
		this.direction = direction;
		this.direction.normalise();
		this.colour = colour;
		this.lightBias = lightBias;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Colour getColour() {
		return colour;
	}


	public Vector2f getLightBias() {
		return lightBias;
	}

}
