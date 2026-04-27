package io.github.closurebit.backend.app;

import io.github.closurebit.backend.audio.AudioFormatFactory;
import io.github.closurebit.backend.audio.JavaSoundAudioCapture;
import io.github.closurebit.backend.pipeline.RecognitionPipeline;
import io.github.closurebit.backend.stt.VoskSpeechRecognizer;
import io.github.closurebit.backend.stt.WakeWordRecognizer;
import io.github.closurebit.frontend.ConsoleTranscriptPrinter;

public class AssistantRun {
    public static void main(String[] args) {
        AppConfig config = AppConfig.defaultConfig();

        try (JavaSoundAudioCapture audioCapture = new JavaSoundAudioCapture(AudioFormatFactory.voskFormat());
             WakeWordRecognizer wakeWordRecognizer = WakeWordRecognizer.create(config.getModelPath(), config.getSampleRate(), config.getWakeWord());
             VoskSpeechRecognizer commandRecognizer = VoskSpeechRecognizer.create(config.getModelPath(), config.getSampleRate())) {
            
            RecognitionPipeline pipeline = new RecognitionPipeline(audioCapture, wakeWordRecognizer, commandRecognizer, new ConsoleTranscriptPrinter());

            pipeline.run();
        }
    }
}