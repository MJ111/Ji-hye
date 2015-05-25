package jihye.TTS;

import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;

import com.gtranslate.Audio;
import com.gtranslate.Language;

public class TextToSpeech extends Thread {
	private String readText;
	private Audio audio;
	InputStream sound;

	public TextToSpeech() {
		audio = Audio.getInstance();
	}

	public void setText(String text) {
		this.readText = text.replace("_", " ");
		
//		try {
//			sound = audio.getAudio(readText, Language.KOREAN);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private void readText() {
//		try {			
//			sound = audio.getAudio(readText, Language.KOREAN);
//			audio.play(sound);
//		} catch (JavaLayerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void run() {
		readText();
	}
}