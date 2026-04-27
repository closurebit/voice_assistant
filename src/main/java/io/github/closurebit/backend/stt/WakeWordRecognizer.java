package io.github.closurebit.backend.stt;

import org.vosk.Model;
import org.vosk.Recognizer;


public class WakeWordRecognizer implements AutoCloseable {
	private final Model model;
	private final Recognizer recognizer;
	private final String wakeWord;

	private WakeWordRecognizer(Model model, Recognizer recognizer, String wakeWord) {
		this.model = model;
		this.recognizer = recognizer;
		this.wakeWord = wakeWord;
	}

	public static WakeWordRecognizer create(String modelPath, float sampleRate, String wakeWord) {
		try {
            Model model = new Model(modelPath);
            String grammar = "[\"" + wakeWord + "\", \"[unk]\"]";
            Recognizer recognizer = new Recognizer(model, sampleRate, grammar);
			return new WakeWordRecognizer(model, recognizer, wakeWord);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot initialize wake word recognizer", e);
		}
	}

	public boolean accept(byte[] data, int len) {
		try {
            if (recognizer.acceptWaveForm(data, len)) {
                String text = extractText(recognizer.getResult());
                return wakeWord.equalsIgnoreCase(text.trim());
            }

            String partial = extractText(recognizer.getPartialResult());
            return wakeWord.equalsIgnoreCase(partial.trim());
            
		} catch (Exception e) {
			throw new IllegalStateException("Wake word recognition failed", e);
		}
	}

	public void reset() {
		recognizer.reset();
	}

	@Override
	public void close() {
		recognizer.close();
		model.close();
	}

    private String extractText(String json) {
        int textIndex = json.indexOf("\"text\"");
        if (textIndex < 0)
            return "";
        int colon = json.indexOf(':', textIndex);
        int firstQuote = json.indexOf('"', colon + 1);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        return json.substring(firstQuote + 1, secondQuote);
    }
}