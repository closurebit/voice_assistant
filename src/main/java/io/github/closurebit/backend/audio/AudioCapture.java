package io.github.closurebit.backend.audio;


public interface AudioCapture extends AutoCloseable {
    void start();

    int read(byte[] buffer);

    void stop();
}