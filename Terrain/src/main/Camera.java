package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import rendering.ICamera;
import utils.SmoothFloat;


public class Camera implements ICamera {

	private static final float PITCH_SENSITIVITY = 0.3f;
	private static final float YAW_SENSITIVITY = 0.3f;
	private static final float MAX_PITCH = 90;

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.4f;
	private static final float FAR_PLANE = 2500;

	private static final float Y_OFFSET = 5;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix = new Matrix4f();

	private Vector3f position = new Vector3f(0, 0, 0);

	private float yaw = 0;
	private SmoothFloat pitch = new SmoothFloat(10, 10);
	private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
	private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);

	public Camera() {
		this.projectionMatrix = createProjectionMatrix();
	}

	public void move() {
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateZoom();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 360 - angleAroundPlayer.get();
		yaw %= 360;
		updateViewMatrix();
	}

	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Matrix4f getProjectionViewMatrix() {
		return Matrix4f.mul(projectionMatrix, viewMatrix, null);
	}

	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch.get()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	}

	private static Matrix4f createProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = angleAroundPlayer.get();
		position.x = Configs.TERRAIN_SIZE / 2f + (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		position.y = verticDistance + Y_OFFSET;
		position.z = Configs.TERRAIN_SIZE / 2f + (float) (horizDistance * Math.cos(Math.toRadians(theta)));
	}


	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
	}


	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
	}


	private void calculatePitch() {
		if (Mouse.isButtonDown(0)) {
			float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;
			pitch.increaseTarget(-pitchChange);
			clampPitch();
		}
		pitch.update(1f / 60);
	}

	private void calculateZoom() {
		float targetZoom = distanceFromPlayer.getTarget();
		float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
		targetZoom -= zoomLevel;
		if (targetZoom < 1) {
			targetZoom = 1;
		}
		distanceFromPlayer.setTarget(targetZoom);
		distanceFromPlayer.update(0.01f);
	}


	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
			angleAroundPlayer.increaseTarget(-angleChange);
		}
		angleAroundPlayer.update(1f / 60);
	}


	private void clampPitch() {
		if (pitch.getTarget() < 0) {
			pitch.setTarget(0);
		} else if (pitch.getTarget() > MAX_PITCH) {
			pitch.setTarget(MAX_PITCH);
		}
	}

}
