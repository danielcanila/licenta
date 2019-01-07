package com.daniel.licenta.calendargenerator.algorithm.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferHandlingUtil {

    public static String[] readLineFromBufferAndSplitIt(BufferedReader buffer, String s, Object... arguments) {
        try {
            return buffer.readLine().split(" ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fprintf(StringBuffer buffer, String s, Object... arguments) {
        String format = String.format(s, arguments);
        buffer.append(format);
    }

    public static BufferedReader fopenRead(String nameOfDataFile) {
        try {
            return new BufferedReader(new FileReader(nameOfDataFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
