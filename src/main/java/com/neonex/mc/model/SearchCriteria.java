package com.neonex.mc.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by dennis on 2017-05-23.
 */
public class SearchCriteria {

    @NotEmpty(message = "date_input can't empty")
    String date_input;

    public String getDate_input() {
        return date_input;
    }

    public void setDate_input(String date_input) {
        this.date_input = date_input;
    }
}
