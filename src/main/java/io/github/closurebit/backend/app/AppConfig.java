package io.github.closurebit.backend.app;


public class AppConfig {
    private final String modelPath;
    private final float sampleRate;
    private final String wakeWord;

    public AppConfig(String modelPath, float sampleRate, String wakeWord) {
        this.modelPath = modelPath;
        this.sampleRate = sampleRate;
        this.wakeWord = wakeWord;
    }

    public String getModelPath() {
        return modelPath;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public String getWakeWord() {
        return wakeWord;
    }

    public static AppConfig defaultConfig() {
        return new AppConfig("models/vosk-model-small-ru-0.22", 16_000.0f, "джарвис");
    }
}