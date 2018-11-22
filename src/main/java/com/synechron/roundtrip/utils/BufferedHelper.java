package com.synechron.roundtrip.utils;

import com.synechron.roundtrip.exception.SynechronException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class BufferedHelper {

    private int messageSize;
    private char[] buffer;

    public BufferedHelper(int size) throws SynechronException {
        if(size <= 0)
            throw new SynechronException("Invalid message size");
        this.messageSize = size;
        buffer = new char[messageSize];
    }

    public String readStreem(BufferedReader in) throws IOException {
        in.read(buffer, 0, messageSize);
        return String.valueOf(buffer);
    }

    public void writeStream(BufferedWriter out, String msg) throws IOException, SynechronException {
        if(msg.length() < messageSize)
            throw new SynechronException("Invalid message size");
        out.write(msg , 0, messageSize);
        out.flush();
    }

}
