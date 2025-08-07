package engine.sceneManager;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import org.lwjglx.BufferUtils;

import engine.audio.Audio;
import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.io.KeyListener;
import engine.io.Window;
import engine.maths.Matrix4f;
import engine.maths.Vector3f;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.util.FileUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelScene extends Scene {
	
	
	private String vertexShaderSrc;
	private String fragmentShaderSrc;
	
	private int vertexID, fragmentID, shaderProgram;
	
	private double temp;
	
	private Audio audio;
	
	private float[] position, rotation, scale;
	
	public Camera camera = new Camera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
	
	
	public Mesh mesh = new Mesh(new float[] {
		    // Back face
		    -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 0, color 1
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 1, color 2
		     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 2, color 3
		     0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f,  // vertex 3, color 4

		    // Front face
		    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 4, color 1
		    -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 5, color 2
		     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 6, color 3
		     0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f,  // vertex 7, color 4

		    // Right face
		     0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 8, color 1
		     0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 9, color 2
		     0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 10, color 3
		     0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f,  // vertex 11, color 4

		    // Left face
		    -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 12, color 1
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 13, color 2
		    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 14, color 3
		    -0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f,  // vertex 15, color 4

		    // Top face
		    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 16, color 1
		    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 17, color 2
		     0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 18, color 3
		     0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f,  // vertex 19, color 4

		    // Bottom face
		    -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,  0.0f, 0.0f,  // vertex 20, color 1
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f,  // vertex 21, color 2
		     0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f,  // vertex 22, color 3
		     0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f   // vertex 23, color 4
		},new int[] {
				// Back face
				0, 1, 3, 3, 1, 2,

				// Front face
				4, 5, 7, 7, 5, 6,

				// Right face
				8, 9, 11, 11, 9, 10,

				// Left face
				12, 13, 15, 15, 13, 14,

				// Top face
				16, 17, 19, 19, 17, 18,

				// Bottom face
				20, 21, 23, 23, 21, 22 }
	, new Material("/textures/opengl.png"));

	
	private GameObject object;

	@Override
	public void init() {
		
		
		
		vertexShaderSrc  = FileUtils.loadAsString("/shaders/mainVertex.glsl");
		fragmentShaderSrc  = FileUtils.loadAsString("/shaders/mainFragment.glsl");
		
		
		object = new GameObject(new float[] {0,0,0}, new float[] {0,0,0}, new float[] {1,1,1}, mesh);
		// compile and link our shaders
		
		// First load and compile the vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		// Pass the shader source code to the GPU
		glShaderSource(vertexID, vertexShaderSrc);
		// Compile the shader using the attached vertexID
		glCompileShader(vertexID);
		
		// Check for errors in compilation 
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.out.println("Bruuuu! Your vertex shader failed to load");
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false: "";
		}
		
		// First load and compile the fragment shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		// Pass the shader source code to the GPU
		glShaderSource(fragmentID, fragmentShaderSrc);
		// Compile the shader using the attached vertexID
		glCompileShader(fragmentID);
		
		// Check for errors in compilation 
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.out.println("Bruuuu! Your fragment shader failed to load");
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false: "";
		}
		
		// Link the compiled shaders and check for errors
		
		shaderProgram = glCreateProgram();
		
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		// Then we can finally do the linking to the program
		glLinkProgram(shaderProgram);
		
		
		// Then we check for linking errors
		success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
			System.out.println("Bruuuu! Your linking failed");
			System.out.println(glGetProgramInfoLog(shaderProgram, len));
		}
		
		
		mesh.create();
		
		audio = new Audio("/sounds/bizet.wav");
		audio.loadSound();
		audio.play();
		
	
	}
	
	public LevelScene() {
		System.out.println("Inside Level Scene");
	    Window.get().r = 0.0f;
	    Window.get().g = 0.0f;
	    Window.get().b = 0.0f;   
	}
	
	@Override
	public void update(float dt) {
		// Bind the shader program
		
		glUseProgram(shaderProgram);
		// Bind the VAO we are using
		
		glBindVertexArray(mesh.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		if(KeyListener.isKeyPressed(GLFW_KEY_P)) {
			audio.pause();
		}
		
		object.update();
		
		setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
		setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		setUniform("projection", Window.projection);
		
		glDrawElements(GL_TRIANGLES, mesh.getElementArrayLength(), GL_UNSIGNED_INT, 0);
		
		camera.update();

	}
	
	public void setUniform(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, boolean value) {
	    glUniform1i(getUniformLocation(name), value ? 1 : 0);
	}
	
	public void setUniform(String name, Matrix4f value) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
		matrix.put(value.getAll()).flip();
		glUniformMatrix4fv(getUniformLocation(name), true, matrix);
	}
	
	public int getUniformLocation(String name) {
		return glGetUniformLocation(shaderProgram, name);
	}

}
