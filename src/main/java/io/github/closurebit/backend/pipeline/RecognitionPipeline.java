package io.github.closurebit.backend.pipeline;

import io.github.closurebit.backend.audio.AudioCapture;
import io.github.closurebit.backend.stt.RecognitionResult;
import io.github.closurebit.backend.stt.SpeechRecognizer;
import io.github.closurebit.backend.stt.WakeWordRecognizer;
import io.github.closurebit.frontend.ConsoleTranscriptPrinter;


public class RecognitionPipeline {
    private final AudioCapture audioCapture;
    private final WakeWordRecognizer wakeWordRecognizer;
    private final SpeechRecognizer commandRecognizer;
    private final ConsoleTranscriptPrinter printer;

    private AssistantState state = AssistantState.WAITING_WAKE_WORD;
    private long commandModeStartedAt = 0L;
    private static final long commandModeTimeoutMs = 6000L;

    public RecognitionPipeline(AudioCapture audioCapture,
                               WakeWordRecognizer wakeWordRecognizer,
                               SpeechRecognizer commaRecognizer,
                               ConsoleTranscriptPrinter printer) {
        this.audioCapture = audioCapture;
        this.wakeWordRecognizer = wakeWordRecognizer;
        this.commandRecognizer = commaRecognizer;
        this.printer = printer;
    }

    public void run() {
        audioCapture.start();
        byte[] buffer = new byte[4096];

        while(true) {
            int len = audioCapture.read(buffer);
            if (len <= 0)
                continue;

            if (state == AssistantState.WAITING_WAKE_WORD) {
                if (wakeWordRecognizer.accept(buffer, len)) {
                    printer.printWakeWordDetected();
                    wakeWordRecognizer.reset();
                    state = AssistantState.LISTENING_COMMAND;
                    commandModeStartedAt = System.currentTimeMillis();
                }
                continue;
            }

            if (state == AssistantState.LISTENING_COMMAND) {
                RecognitionResult result = commandRecognizer.accept(buffer, len);
                if (result.isFinalResult()) {
                    printer.printFinal(result.getText());
                    state = AssistantState.WAITING_WAKE_WORD;
                    continue;
                }

                printer.printPartial(result.getText());

                if (System.currentTimeMillis() - commandModeStartedAt > commandModeTimeoutMs)
                    state = AssistantState.WAITING_WAKE_WORD;
            }
        }
    }
}