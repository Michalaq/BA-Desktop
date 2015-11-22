package desktopimpl.sound;

import gameengine.soundengine.SoundEffect;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AWTSoundEffect extends SoundEffect {
	private byte[] data;
	
	private static Clip getClip(byte[] data) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
	        // getAudioInputStream() also accepts a File or InputStream
	        AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
	        clip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return clip;
	}
	
	public AWTSoundEffect(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void playLoop(int times) {
		getClip(data).loop(times);
		

	}

	@Override
	public void play() {
		getClip(data).start();

	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

}
