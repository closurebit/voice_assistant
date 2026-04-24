package io.github.closurebit.backend.pipeline;

import io.github.closurebit.backend.audio.AudioCapture;
import io.github.closurebit.backend.stt.RecognitionResult;
import io.github.closurebit.backend.stt.SpeechRecognizer;
import io.github.closurebit.frontend.ConsoleTranscriptPrinter;


public class RecognitionPipeline {
    private final AudioCapture audioCapture;
    private final SpeechRecognizer speechRecognizer;
    private final ConsoleTranscriptPrinter printer;

    public RecognitionPipeline(AudioCapture audioCapture,
                               SpeechRecognizer speechRecognizer,
                               ConsoleTranscriptPrinter printer) {
        this.audioCapture = audioCapture;
        this.speechRecognizer = speechRecognizer;
        this.printer = printer;
    }

    public void run() {
        audioCapture.start();
        byte[] buffer = new byte[4096];

        while(true) {
            int len = audioCapture.read(buffer);
            if (len <= 0)
                continue;

            RecognitionResult result = speechRecognizer.accept(buffer, len);
            if (result.isFinalResult())
                printer.printFinal(result.getText());
            else
                printer.printPartial(result.getText());
        }
    }
}