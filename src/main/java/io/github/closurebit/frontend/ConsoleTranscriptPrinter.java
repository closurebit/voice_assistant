package io.github.closurebit.frontend;


public class ConsoleTranscriptPrinter {
    public void printPartial(String text) {
        if (text != null && !text.isBlank())
            System.out.println("partial: " + text);
    }

    public void printFinal(String text) {
        if (text != null && !text.isBlank())
            System.out.println("final: " + text);
    }

    public void printWakeWordDetected() {
        System.out.println("WAKE WORD DETECTED");
    }
}