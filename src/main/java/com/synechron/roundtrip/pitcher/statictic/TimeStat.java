package com.synechron.roundtrip.pitcher.statictic;

public class TimeStat {

    private final long sendAt;

    private final long acceptAt;

    private final long responseAt;

    public TimeStat(long sendAt, long acceptAt, long responseAt) {
        this.sendAt = sendAt;
        this.acceptAt = acceptAt;
        this.responseAt = responseAt;
    }

    public long getSendAt() {
        return sendAt;
    }

    public long getAcceptAt() {
        return acceptAt;
    }

    public long getResponseAt() {
        return responseAt;
    }
}
