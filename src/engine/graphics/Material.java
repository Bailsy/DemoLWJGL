package engine.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Material {
	private String path;
	private Texture texture;
	private float width, height;
	private int textureID;
	


	public Material(String path) {
		this.path = path;
	}
	
	public void create() {
		try {
			//texture is created by OpenGL using the texture loading class
			texture = TextureLoader.getTexture(path.split("[.]")[1], Material.class.getResourceAsStream(path), GL30.GL_LINEAR);
			width = texture.getWidth();
			height = texture.getHeight();
			textureID = texture.getTextureID();
		} catch (IOException e) {
			System.err.println("ERROR: IMAGE FILE NOT FOUND!");

		}

	}
	
	public void destroy() {
		GL30.glDeleteTextures(textureID);
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

}
