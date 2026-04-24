package io.github.closurebit.backend.app;


public class AppConfig {
    private final String modelPath;
    private final float sampleRate;

    public AppConfig(String modelPath, float sampleRate) {
        this.modelPath = modelPath;
        this.sampleRate = sampleRate;
    }

    public String getModelPath() {
        return modelPath;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public static AppConfig defaultConfig() {
        return new AppConfig("models/vosk-model-small-ru-0.22", 16_000.0f);
    }
}