package com.neonex.mc.model;

/**
 * Created by dennis on 2017-05-23.
 */
public class MccParameter {
    private String srcMRN;
    private String dstMRN;
    private String s100;

    public MccParameter(MessageCriteria message) {
        this.srcMRN = message.getDstMRN();
        this.dstMRN = message.getSrcMRN();
        this.s100 =  message.getS100();
    }

    public String getSrcMRN() {
        return srcMRN;
    }

    public void setSrcMRN(String srcMRN) {
        this.srcMRN = srcMRN;
    }

    public String getDstMRN() {
        return dstMRN;
    }

    public void setDstMRN(String dstMRN) {
        this.dstMRN = dstMRN;
    }

    public String getS100() {
        return s100;
    }

    public void setS100(String s100) {
        this.s100 = s100;
    }

}
