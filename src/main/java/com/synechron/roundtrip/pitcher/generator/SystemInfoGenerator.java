package com.synechron.roundtrip.pitcher.generator;

import com.synechron.roundtrip.exception.SynechronException;

import java.time.LocalDateTime;

public class SystemInfoGenerator implements MessageGenerator {

    public String generate(int size) throws SynechronException {
        checkSize(size);
        StringBuilder builder = new StringBuilder();
        while(builder.length() < size) {
            builder
                    .append("System Time: ").append(LocalDateTime.now()).append("\n")
                    .append("Available processors (cores): ").append(Runtime.getRuntime().availableProcessors())
                    .append("\n")
                    .append("Free memory (bytes): ").append(Runtime.getRuntime().freeMemory()).append("\n")
                    .append("Maximum memory (bytes): ").append(Runtime.getRuntime().maxMemory()).append("\n")
                    .append("Total memory available to JVM (bytes): ").append(Runtime.getRuntime().totalMemory())
                    .append("\n")
                    .append("Operating system:").append(System.getProperty("os.name")).append("\n")
                    .append("CPU cores count: ").append(Runtime.getRuntime().availableProcessors()).append("\n")
                    .append("JVM implementation version: ").append(System.getProperty("java.vm.version")).append("\n")
                    .append("JVM implementation vendor: ").append(System.getProperty("java.vm.vendor")).append("\n")
                    .append("JVM  implementation name: ").append(System.getProperty("java.vm.name")).append("\n")
                    .append("The name of specification version Java Runtime Environment: ").append(System.getProperty("java.specification.version")).append("\n");
        }
        return builder.toString().substring(0, size);
    }

}
