package io.github.closurebit.backend.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;


public class JavaSoundAudioCapture implements AudioCapture {
    private final AudioFormat format;
    private TargetDataLine line = null;

    public JavaSoundAudioCapture(AudioFormat format) {
        this.format = format;
    }

    @Override
    public void start() {
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine)AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot start microphone capture", e);
        }
    }

    @Override
    public int read(byte[] buffer) {
        return line.read(buffer, 0, buffer.length);
    }

    @Override
    public void stop() {
        if (line != null) {
            line.stop();
            line.flush();
        }
    }

    @Override
    public void close() {
        if (line != null)
            line.close();
    }
}