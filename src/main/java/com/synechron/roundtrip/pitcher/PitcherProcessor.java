package com.synechron.roundtrip.pitcher;

import com.synechron.roundtrip.exception.SynechronException;
import com.synechron.roundtrip.exception.SynechronPropertyException;
import com.synechron.roundtrip.pitcher.generator.MessageGenerator;
import com.synechron.roundtrip.pitcher.statictic.StatisticTask;
import com.synechron.roundtrip.pitcher.statictic.TimeStat;
import com.synechron.roundtrip.utils.BufferedHelper;
import com.synechron.roundtrip.utils.PropertyHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicReference;

public class PitcherProcessor {

    private final static Logger logger = Logger.getLogger(PitcherProcessor.class);

    private MessageGenerator generator;

    private int delay;
    private int size;

    private long idSequence;

    private AtomicReference<ArrayList<TimeStat>> statistic = new AtomicReference<>(new ArrayList<>());

    private BufferedHelper bufferedHelper;

    private Timer timer = new Timer();

    public PitcherProcessor(MessageGenerator generator, int delay, int size) {
        this.generator = generator;
        this.delay = delay;
        this.size = size;
    }

    public void process(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            logger.info("Pitcher started.");

            bufferedHelper = new BufferedHelper(size);

            sendSize(out, size);

            startTimer();
            while (true) {
                Thread.sleep(delay);

                long id = idSequence++;
                String idFormat = "ID=" + id + ". ";
                String message = idFormat + generator.generate(size - idFormat.length());

                long sendAt = System.nanoTime();
                bufferedHelper.writeStream(out, message);
                long acceptAt = System.nanoTime();
                String response = bufferedHelper.readStreem(in);
                long responseAt = System.nanoTime();
                checkResponse(response, id);
                statistic.get().add(new TimeStat(sendAt, acceptAt, responseAt));
            }

        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        } catch (SynechronException e) {
            logger.error(e.getMessage(), e);
        } finally {
            timer.cancel();
        }
    }

    private void sendSize(BufferedWriter out, int size) throws IOException {
        out.write(size);
        out.flush();
    }

    private void checkResponse(String message, long id) {
        try {
            String strID = message.substring(0, message.indexOf("."));
            String[] splt = strID.split("=");
            if(splt.length < 2 || !StringUtils.isNumeric(splt[1]) || Long.valueOf(splt[1]) != id){
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("Message with ID " + id + " is lost");
        }
    }

    private void startTimer() throws SynechronPropertyException {
        Integer period = PropertyHelper.getIntProperty("period");
        timer.scheduleAtFixedRate(new StatisticTask(statistic), period, period);
    }

}
