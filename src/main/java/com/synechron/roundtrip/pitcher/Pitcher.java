package com.synechron.roundtrip.pitcher;

import com.synechron.roundtrip.exception.SynechronPropertyException;
import com.synechron.roundtrip.pitcher.generator.MessageGenerator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class Pitcher {

    private final static Logger logger = Logger.getLogger(PitcherProcessor.class);

    private int port;
    private String hostName;

    private PitcherProcessor pitcherProcessor;

    public Pitcher(MessageGenerator generator, String hostName, int port, int mps, int size) {
        this.hostName = hostName;
        this.port = port;
        pitcherProcessor = new PitcherProcessor(generator, 1000 / mps, size);
    }

    public void startSending() throws SynechronPropertyException {
        try (Socket socket = new Socket(hostName, port)) {
            pitcherProcessor.process(socket);
        } catch (IOException e) {
            logger.error(e);
        }
    }

}
