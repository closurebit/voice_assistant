package io.github.closurebit.backend.stt;


public interface SpeechRecognizer extends AutoCloseable {
    RecognitionResult accept(byte[] data, int len);

    RecognitionResult finish();
}