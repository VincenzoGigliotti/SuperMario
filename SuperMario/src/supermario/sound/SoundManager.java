package supermario.sound;

public class SoundManager {
	private static SoundManager soundManager = null;
	private Sound music;
	private Sound jump;
	private Sound brick;
	private Sound die;
	private Sound powerUp;
	private Sound smashedGoomba;
	private Sound newLevel;
	private Sound powerDown;
	
	private SoundManager() {
		music = new Sound("music.wav");
		jump = new Sound("jump.wav");
		brick = new Sound("break.wav");
		die = new Sound("die.wav");
		powerUp = new Sound("power_up.wav");
		smashedGoomba = new Sound("smashed_goomba.wav");
		newLevel = new Sound("new_level.wav");
		powerDown = new Sound("power_down.wav");
	}
	
	
	public static SoundManager getInstance() {
		if(soundManager == null)
			soundManager = new SoundManager();
		return soundManager;
	}
	
	public void startMusic() {
		music.decreaseVolume();
		music.restart();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void jumpSound() {
		jump.restart();
	}

	public void brick() {
		brick.restart();
	}
	
	public void die() {
		music.stop();
		die.restart();
	}
	
	public void powerUp() {
		powerUp.start();
	}
	
	public void smashedGoomba() {
		smashedGoomba.start();
	}
	
	public void newLevel() {
		newLevel.start();
	}
	
	public void powerDown() {
		powerDown.start();
	}
	
	
}
