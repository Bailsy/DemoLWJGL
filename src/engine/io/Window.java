package engine.io;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import engine.maths.Matrix4f;
import engine.sceneManager.LevelEditorScene;
import engine.sceneManager.LevelScene;
import engine.sceneManager.Scene;
import engine.util.Time;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
	// The window handle
	private long window;
	private static int frames, fps, width, height;
	public float r,g,b,a;
	private float beginTime, endTime, dt;
	private static long time;
	private static int currentSceneIndex = -1; 
	private static Scene currentScene;
	private static Window instance;
	private boolean fadeToBlack = false;
	public static Matrix4f projection;
	
	private Window() {
		Window.width = 700;
		Window.height = 300;
		
		r = 1.0f;
		g = 1.0f;
		b = 1.0f;
		a = 0.0f;
	}
	
	public static Window get() {
		if(Window.instance==null) {
			Window.instance = new Window();
		}
		return Window.instance;
	}
	

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
		    break;
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			break;
		default:
			assert false: "Unknown scene '" + newScene + "'";
			break;
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(width, height, "Starting...", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		createCallbacks();

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		
		

		// Make the window visible
		glfwShowWindow(window);
	}
	
	private void createCallbacks() {
		glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(window, KeyListener::keyCallback);
		
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			glScissor(0, 0, width, height);
		    glViewport(0, 0, width, height);
		    Window.width = width;
		    Window.height = height;
		    update();
		    
		});
	}
	

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		Window.changeScene(0);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		
		beginTime = Time.getTime();
		dt = -1.0f;
		
		while ( !glfwWindowShouldClose(window) ) {
			update();
		}
	}
	
	private void countFrames() {
		frames++;
		if(System.currentTimeMillis()> time+ 1000) {
			fps = frames;
			time = System.currentTimeMillis();
			frames = 0;
		}
	}
	
	private void update() {
		
		countFrames();
		glfwSetWindowTitle(window, "my window"+" FPS| "+fps);
		
		projection = Matrix4f.projection(70.0f, (float) width/ (float) height, 0.1f, 1000.0f);
		
		if ( KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(window, true); 
		}
        
		render();
		
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
		endTime = Time.getTime();
		dt = endTime-beginTime;
		beginTime = endTime;
	}
	
	private void render() {
		glClearColor(r,g,b,a);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		if(dt>=0) {
		    currentScene.update(dt);
		}
		
		glfwSwapBuffers(window); // swap the color buffers
	}
	
	

	public static void main(String[] args) {
		Window.get().run();
	}

}
