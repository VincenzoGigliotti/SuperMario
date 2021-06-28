package supermario.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	private Clip clip;

	public Sound(String name) {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/supermario/resources/sound/" + name));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (Exception e) {
			System.out.println("Sound not found!");
			clip = null;
		}
	}

	public void start() {
		if(clip != null) {			
			if(clip.getFramePosition() == clip.getFrameLength())
				clip.setFramePosition(0);
			clip.start();			
		}		
	}
	
	public void stop() {
		if(clip != null) {
			clip.stop();
		}
	}
	
	public void restart() {
		if(clip != null) {
			clip.stop();
			clip.setFramePosition(0);			
			clip.start();
		}
	}
	
	public void decreaseVolume() {
		// metodo per diminuire il volume di una clip, usato per abbassare il volume della musica di sottofondo
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-15.0f);
	}

}
