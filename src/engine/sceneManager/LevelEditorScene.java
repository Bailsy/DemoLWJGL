package engine.sceneManager;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import java.awt.event.KeyEvent;

import engine.io.KeyListener;
import engine.io.Window;

public class LevelEditorScene extends Scene {
	
	private boolean changingScene = false;
	private float timeToChangeScene = 2.0f;
	private int count = 1;
	
	public LevelEditorScene() {
		System.out.println("Inside Level editor Scene");
	}
	
	@Override
	public void update(float dt) {
		
		if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			System.out.println(dt);
			changingScene = true;
		}
		
		if(changingScene && timeToChangeScene > 0) {
			    timeToChangeScene -= dt;
			    float fadeSpeed = 0.5f; // Instead of 5.0f
			    Window.get().r -= dt * fadeSpeed;
			    Window.get().g -= dt * fadeSpeed;
			    Window.get().b -= dt * fadeSpeed;
		}
		else if(changingScene) {
			Window.changeScene(1);
		}
		
	}
}
