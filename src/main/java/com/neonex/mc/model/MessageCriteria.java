package com.neonex.mc.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by dennis on 2017-05-23.
 */
public class MessageCriteria {

    @NotEmpty(message = "srcMRN can't empty")
    String srcMRN;

    @NotEmpty(message = "dstMRN can't empty")
    String dstMRN;

    @NotEmpty(message = "s100 can't empty")
    String s100;

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
