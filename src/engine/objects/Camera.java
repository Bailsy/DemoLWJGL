package engine.objects;

import org.lwjgl.glfw.GLFW;

import engine.io.KeyListener;
import engine.io.MouseListener;
import engine.maths.Vector3f;

public class Camera {
	private Vector3f position, rotation;
	
	private float moveSpeed = 0.1f, mouseSensitivity = 0.1f;
	private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
	
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public void update() {
	    // Only when dragging
	    if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
	        newMouseX = MouseListener.getX();
	        newMouseY = MouseListener.getY();

	        float dx = (float) (newMouseX - oldMouseX);
	        float dy = (float) (newMouseY - oldMouseY);

	        rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

	        oldMouseX = newMouseX;
	        oldMouseY = newMouseY;
	    } else {
	        // Reset old mouse position only when click starts next time
	        oldMouseX = MouseListener.getX();
	        oldMouseY = MouseListener.getY();
	    }

	    float x = (float)Math.sin(Math.toRadians(rotation.getY())) * moveSpeed;
	    float z = (float)Math.cos(Math.toRadians(rotation.getY())) * moveSpeed;

	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0, x));
	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0, -x));
	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0, -z));
	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0, z));
	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) position = Vector3f.add(position, new Vector3f(0, moveSpeed, 0));
	    if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) position = Vector3f.add(position, new Vector3f(0, -moveSpeed, 0));
	}

	
	public void update(GameObject object) {
		
	}

	public float[] getPosition() {
		return new float[] {position.getX(), position.getY(), position.getZ()};
	}

	public  float[]  getRotation() {
		return new float[] {rotation.getX(), rotation.getY(), rotation.getZ()};
	}

}