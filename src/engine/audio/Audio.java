package engine.audio;


import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjglx.util.WaveData;

public class Audio {
	private String path;
	private int buffer, source;
	private WaveData data;
	private long audioContext;
	private long audioDevice;

	public Audio(String path) {
		this.path=path;
		
		init();
	}
	
	private void init() {
		
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		audioDevice = ALC10.alcOpenDevice(defaultDeviceName);
		int[] attributes = {0};
		audioContext = ALC10.alcCreateContext(audioDevice, attributes);
		ALC10.alcMakeContextCurrent(audioContext);
		ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
		AL.createCapabilities(alcCapabilities);

	}
	

	public void loadSound(){
	    data = WaveData.create(Audio.class.getResource(path));
	   	    
	    buffer = AL11.alGenBuffers();
	    AL11.alBufferData(buffer, data.format, data.data, data.samplerate);//load your data using the bufferID generated with format and sample rate
	    source = AL11.alGenSources();
	    AL11.alSourcei(source, AL11.AL_BUFFER, buffer); //generate an audio source


	}
	
	public void play() {
		AL11.alSourcePlay(source);
	}
	
	public void pause() {
		AL11.alSourcePause(source);
	}
	
	
	public void destroy() {
		pause();
		AL11.alDeleteBuffers(buffer);
		data.dispose();
	}

}
