package io.github.closurebit.backend.audio;

import javax.sound.sampled.AudioFormat;


public final class AudioFormatFactory {
    private AudioFormatFactory() { }

    public static AudioFormat voskFormat() {
        return new AudioFormat(
            16_000.0f,
            16,
            1,
            true,
            false
        );
    }
}