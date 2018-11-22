package com.synechron.roundtrip.pitcher.statictic;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

public class StatisticTask extends TimerTask {

    private static final String TIME = "Time: ";
    private static final String TOTAL_NUMBER = "Total number: ";
    private static final String SPEED = "Speed: ";
    private static final String ROUND_TIME = "Average last second round time: ";
    private static final String MAX_ROUND_TIME = "Max round time: ";
    private static final String ACCEPT_TIME = "Average last second request time: ";
    private static final String RESPONSE_TIME = "Average last second response time: ";

    private final static Logger logger = Logger.getLogger(StatisticTask.class);

    private AtomicReference<ArrayList<TimeStat>> statisticRef;

    private long maxRoundTime;

    private long totalMessages;

    public StatisticTask(AtomicReference<ArrayList<TimeStat>> statistic) {
        this.statisticRef = statistic;
    }

    @Override
    public void run() {
        List<TimeStat> timeStats = statisticRef.getAndSet(new ArrayList<>());

        long lastSecondMessages = timeStats.size();
        totalMessages += lastSecondMessages;

        long averageRoundTime = 0;
        long averageAcceptTime = 0;
        long averageResponseTime = 0;

        if(timeStats.size() != 0) {
            for (TimeStat timeStat : timeStats) {
                long roundTime = timeStat.getResponseAt() - timeStat.getSendAt();
                long acceptTime = timeStat.getAcceptAt() - timeStat.getSendAt();
                long responseTime = timeStat.getResponseAt() - timeStat.getAcceptAt();
                if (maxRoundTime < roundTime) {
                    maxRoundTime = roundTime;
                }
                averageRoundTime += roundTime;
                averageAcceptTime += acceptTime;
                averageResponseTime += responseTime;
            }

            averageRoundTime /= lastSecondMessages;
            averageAcceptTime /= lastSecondMessages;
            averageResponseTime /= lastSecondMessages;
        }

        StringBuilder builder = new StringBuilder();
        builder
                .append(TIME).append(LocalTime.now()).append(" ")
                .append(TOTAL_NUMBER).append(totalMessages).append(" ")
                .append(SPEED).append(lastSecondMessages).append("msgs/s ")
                .append(ROUND_TIME).append(TimeUnit.NANOSECONDS.toMillis(averageRoundTime)).append("ms ")
                .append(MAX_ROUND_TIME).append(TimeUnit.NANOSECONDS.toMillis(maxRoundTime)).append("ms ")
                .append(ACCEPT_TIME).append(TimeUnit.NANOSECONDS.toMillis(averageAcceptTime)).append("ms ")
                .append(RESPONSE_TIME).append(TimeUnit.NANOSECONDS.toMillis(averageResponseTime)).append("ms");
        logger.info(builder);

    }

}
