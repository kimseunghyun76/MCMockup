package com.neonex.mc.model;

/**
 * Created by dennis on 2017-06-27.
 */
public class LogViewerParameter {
    private String date_input;

    public LogViewerParameter(SearchCriteria message) {
        this.date_input = message.getDate_input();
    }

    public String getDate_input() {
        return date_input;
    }

    public void setDate_input(String date_input) {
        this.date_input = date_input;
    }

}
