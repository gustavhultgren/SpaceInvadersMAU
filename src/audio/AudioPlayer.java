package audio;

/**
 *  This class makes is possible to play music and 
 *  other sounds. Uses the Jar files in libs-folder 
 *  as decoders. Gives the developer the possibility
 *  to adjust volume, stop, play-back and other methods.
 *  
 * @author Gustav Georgsson
 */

import javax.sound.sampled.*;

public class AudioPlayer {

	private Clip clip;
	private BooleanControl bc;

	/**
	 * Takes parameter String as source.
	 * 
	 * @param s
	 */
	public AudioPlayer(String s) {

		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void play() {
		if (clip == null)
			return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void stop() {
		if (clip.isRunning())
			clip.stop();
	}

	public void close() {
		stop();
		clip.close();
	}

	public void loop() {

		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Adjusts the volume of the music playing.
	 * 
	 * @param vol
	 */
	public void setVolume(double vol) {

		FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float db = (float) (Math.log(vol) / Math.log(10) * 20);
		gain.setValue(db);

	}
}