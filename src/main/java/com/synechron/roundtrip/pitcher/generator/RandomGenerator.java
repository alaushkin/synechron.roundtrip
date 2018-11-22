package com.synechron.roundtrip.pitcher.generator;

import com.synechron.roundtrip.exception.SynechronException;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator implements MessageGenerator {

    public String generate(int size) throws SynechronException {
        checkSize(size);
        return RandomStringUtils.randomAlphabetic(size, size).substring(0, size);
    }

}
