package com.synechron.roundtrip.catcher;

import com.synechron.roundtrip.exception.SynechronException;
import com.synechron.roundtrip.utils.BufferedHelper;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class CatcherProcessor implements Runnable {

    private final static Logger logger = Logger.getLogger(CatcherProcessor.class);

    private BufferedHelper bufferedHelper;

    private Socket client;

    public CatcherProcessor(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            int size = in.read();
            bufferedHelper = new BufferedHelper(size);
            while (client.isConnected()) {
                String message = bufferedHelper.readStreem(in);
                logger.info("Accepted message: " + message);
                bufferedHelper.writeStream(out, message);
            }
        } catch (IOException | SynechronException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

    }
}
