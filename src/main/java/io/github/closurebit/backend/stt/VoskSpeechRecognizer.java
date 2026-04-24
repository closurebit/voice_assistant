package io.github.closurebit.backend.stt;

import org.vosk.Model;
import org.vosk.Recognizer;


public class VoskSpeechRecognizer implements SpeechRecognizer {
    private Model model;
    private Recognizer recognizer;

    public static VoskSpeechRecognizer create(String modelPath, float sampleRate) {
        try {
            Model model = new Model(modelPath);
            Recognizer recognizer = new Recognizer(model, sampleRate);
            return new VoskSpeechRecognizer(model, recognizer);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create Vosk", e);
        }
    }

    private VoskSpeechRecognizer(Model model, Recognizer recognizer) {
        this.model = model;
        this.recognizer = recognizer;
    }

    @Override
    public RecognitionResult accept(byte[] data, int len) {
        try {
            boolean isFinal = recognizer.acceptWaveForm(data, len);
            if (isFinal)
                return RecognitionResult.of(isFinal, extractText(recognizer.getResult()));
            return RecognitionResult.of(isFinal, extractText(recognizer.getPartialResult()));
        } catch (Exception e) {
            throw new IllegalStateException("Vosk recognition failed", e);
        }
    }

    @Override
    public RecognitionResult finish() {
        return RecognitionResult.of(true, extractText(recognizer.getFinalResult()));
    }

    @Override
    public void close() {
        recognizer.close();
        model.close();
    }

    public String extractText(String json) {
        int textIndex = json.indexOf("\"text\"");
        if (textIndex < 0) {
            return "";
        }
        int colon = json.indexOf(':', textIndex);
        int firstQuote = json.indexOf('"', colon + 1);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        return json.substring(firstQuote + 1, secondQuote);
    }
}