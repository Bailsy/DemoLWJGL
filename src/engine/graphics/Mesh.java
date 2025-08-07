package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjglx.BufferUtils;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	
	private int vaoID, vboID, eboID;
	private float[] vertexArray;
	private int[] elementArray;
	private Material material;
	
	public Mesh(float[] vertexArray, int[] elementArray, Material material) {
		this.vertexArray = vertexArray;
		this.elementArray = elementArray;
		this.material = material;
	}
	
	public void create() {
		
		material.create();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
	    
		// Create a float buffer of vertices because that's what OpenGL is expecting
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip(); //you got to flip that buffer for OpenGL
		
		// Create the VBO and upload the vertex buffer
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		
		// Create the indices and upload them to GPU
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		// Create the VBO and upload the vertex buffer
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
	
		// Add the vertex attribute pointers
		int positionSize = 3;
		int colorSize = 4;
		int textureSize = 2;
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionSize + colorSize + textureSize) * floatSizeBytes;
		
		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);  // Stride is byte offset between consecutive vertex attributes 
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*floatSizeBytes); // We are using the size in bytes
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, textureSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * floatSizeBytes); // We are using the size in bytes
		glEnableVertexAttribArray(2);
	}
	
	
	public void destroy() {
		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
		glDeleteVertexArrays(vaoID);	
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVboID() {
		return vboID;
	}

	public int getEboID() {
		return eboID;
	}
	
	public int getElementArrayLength() {
		return elementArray.length;
	}

}
