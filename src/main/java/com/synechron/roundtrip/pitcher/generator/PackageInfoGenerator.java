package com.synechron.roundtrip.pitcher.generator;

import com.synechron.roundtrip.exception.SynechronException;

public class PackageInfoGenerator implements MessageGenerator {

    private String packageInfo = (PackageInfoGenerator.class.toString() + "@\n" + PackageInfoGenerator.class.hashCode());

    public String generate(int size) throws SynechronException {
        checkSize(size);
        StringBuilder builder = new StringBuilder();
        while (builder.length() < size) {
            builder.append(packageInfo);
        }
        return builder.toString().substring(0, size);
    }

}
