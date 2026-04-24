package io.github.closurebit.backend.stt;


public class RecognitionResult {
    private final boolean finalResult;
    private final String text;

    private RecognitionResult(boolean finalResult, String text) {
        this.finalResult = finalResult;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isFinalResult() {
        return finalResult;
    }

    public static RecognitionResult of(boolean finalResult, String text) {
        return new RecognitionResult(finalResult, text);
    }
}