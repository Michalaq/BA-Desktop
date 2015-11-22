package desktopimpl.sound;

import gameengine.levels.LevelCreator;
import gameengine.soundengine.BackgroundMusicManager;
import gameengine.soundengine.Music;
import gameengine.soundengine.MusicManager;
import gameengine.soundengine.SoundEffect;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import balloonadventure.world.Balloon;
import balloonadventure.world.Spiky;

public class AWTMusicManager extends MusicManager implements BackgroundMusicManager {
	private Music currentlyPlayedMusic;
	private final String soundPath;
	private final String extension = ".wav";
	
	public AWTMusicManager(String soundPath) {
		this.soundPath = soundPath;
		soundEffects = new HashMap<String, SoundEffect>();
	}
	
	@Override
	public void setBackgroundMusic(Music music) {
		if (currentlyPlayedMusic != null) {
			currentlyPlayedMusic.stop();
		}
		currentlyPlayedMusic = music;
	}

	@Override
	public void changeBackgroundMusic(Music toMusic) {
		setBackgroundMusic(toMusic);
		currentlyPlayedMusic.play();

	}

	@Override
	public void playBackgroundMusic() {
		currentlyPlayedMusic.play();

	}

	@Override
	public void stopBackgroundMusic() {
		currentlyPlayedMusic.stop();

	}

	@Override
	public void pauseBackgroundMusic() {
		currentlyPlayedMusic.pause();

	}

	@Override
	public void resumeBackgroundMusic() {
		currentlyPlayedMusic.play();

	}

	private Clip getClip(String name) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
	        // getAudioInputStream() also accepts a File or InputStream
	        AudioInputStream ais = AudioSystem.getAudioInputStream(
	        		new File(soundPath + name.toLowerCase() + extension)
	        		);
	        clip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return clip;
	}
	
	private byte[] getSoundDataRaw(String name) {
		byte ret[] = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(soundPath + name.toLowerCase() + extension));
			ret = new byte[fis.available()];
			fis.read(ret);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public void loadSoundData() {
		levelMusic = new Music[LevelCreator.getLevelCount()+2];
		currentlyPlayedMusic = new AWTMusic(getClip("MUSIC_LEVEL_ONE"), 0, this);
		for(int i=1; i<levelMusic.length; i++)
			levelMusic[i] = currentlyPlayedMusic;
		String[] soundNames = new String[] {"BALLOON_POP", "BALLOON_FLYHIGH", "SPIKY_LAUGH" };
		for (String s : soundNames)
			soundEffects.put(s, new AWTSoundEffect(getSoundDataRaw(s)));
	}

	@Override
	public void endWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initWorldObjectSounds() {
		Balloon.setSoundEffects(soundEffects.get("BALLOON_POP"),
							   soundEffects.get("BALLOON_FLYHIGH"));
		Spiky.setSoundEffects(soundEffects.get("SPIKY_LAUGH"));
	}

}
