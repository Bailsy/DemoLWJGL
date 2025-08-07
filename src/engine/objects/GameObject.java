package engine.objects;

import engine.graphics.Mesh;

public class GameObject {
	private float[] position, rotation, scale;
	private Mesh mesh;
	private double temp;
	
	public GameObject(float[] position, float[] rotation, float[] scale, Mesh mesh) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
	}
	
	public void update() {
	    temp += 0.002;
	    float value = (float) Math.sin(temp);
	    float angle = value * 360;

	    position[0] = 0f;
	    position[1] = 0f;   // or whatever value you need
	    position[2] = 0f;

	    rotation[0] = 0f;
	    rotation[1] = angle;
	    rotation[2] = 0f;

	    scale[0] = 1.0f;
	    scale[1] = 1.0f;
	    scale[2] = 1.0f;
	}

	public float[] getPosition() {
		return position;
	}

	public float[] getRotation() {
		return rotation;
	}

	public float[] getScale() {
		return scale;
	}

	public Mesh getMesh() {
		return mesh;
	}
}