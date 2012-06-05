package physics_game;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundCache {
	public static class Sound {
		private final Clip c;

		public Sound(Clip c) {
			this.c = c;
		}

		public void play() {
			c.setFramePosition(0);
			c.start();
		}

		public void stop() {
			c.stop();
		}

		public void flush() {
			c.close();
		}

		public double getDuration() {
			return c.getMicrosecondLength() / 1000000d;
		}

		public static Sound read(String file, InputStream is) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			AudioInputStream s = AudioSystem.getAudioInputStream(is);
			AudioFormat f = s.getFormat();
			if (f.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				f = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, f.getSampleRate(), f.getSampleSizeInBits() * 2, f.getChannels(), f.getFrameSize() * 2, f.getFrameRate(), true);
				s = AudioSystem.getAudioInputStream(f, s);
			}
			Clip c = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, f));

			try {
				c.open(s);
				s.close();
				return new Sound(c);
			} catch (LineUnavailableException e) {
				System.err.println("Error opening " + file);
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Error opening " + file);
				e.printStackTrace();
			}
			return null;
		}
	}

	private static final Map<String, Sound> loaded = new HashMap<String, Sound>();

	public static Sound getSound(String key) {
		return loaded.get(key);
	}

	public static void setSound(String key, Sound value) {
		loaded.put(key, value);
	}

	public static void flush() {
		System.out.println();
		for (Sound sound : loaded.values())
			sound.flush();
		loaded.clear();
	}
}
