package com.synechron.roundtrip.pitcher.generator;

import com.synechron.roundtrip.exception.SynechronException;

@FunctionalInterface
public interface MessageGenerator {

    String generate(int size) throws SynechronException;

    default void checkSize(int size) throws SynechronException {
        if(size < 0)
            throw new SynechronException("Invalid message size");
    }
}
