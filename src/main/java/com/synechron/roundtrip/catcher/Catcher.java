package com.synechron.roundtrip.catcher;

import com.synechron.roundtrip.exception.SynechronPropertyException;
import com.synechron.roundtrip.utils.PropertyHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.*;

public class Catcher {

    private final static Logger logger = Logger.getLogger(Catcher.class);

    private InetSocketAddress address;

    private ExecutorService executor;

    public Catcher(int port, String bind) throws SynechronPropertyException {
        this.address = new InetSocketAddress(bind, port);
        executor = Executors.newFixedThreadPool(PropertyHelper.getIntProperty("server.thread.count"));
    }

    public void startListening() {
        logger.info("Catcher started");
        try (ServerSocket server = new ServerSocket()) {
            server.bind(address);
            while (true) {
                executor.submit(new CatcherProcessor(server.accept()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
