package desktopimpl.sound;

import gameengine.GameEngine;
import gameengine.soundengine.BackgroundMusicManager;
import gameengine.soundengine.Music;

import javax.sound.sampled.Clip;

public class AWTMusic extends Music {
	private Clip sound;
	
	public AWTMusic(Clip sound, int soundId, BackgroundMusicManager bmm) {
		super(soundId, bmm);
		this.sound = sound;
	}

	@Override
	public int getDuration() {
		return 0;
	}
	
	@Override
	public void play() {
		GameEngine.sendDebugMessage("MUSIC", "Zaczynam grac.");
		sound.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	@Override
	public void pause() {
		GameEngine.sendDebugMessage("MUSIC", "Chwilowo przerywam grac.");

		sound.stop();
	}
	
	@Override
	public void stop() {
		GameEngine.sendDebugMessage("MUSIC", "Koncze grac.");

		sound.stop();
	}

}
