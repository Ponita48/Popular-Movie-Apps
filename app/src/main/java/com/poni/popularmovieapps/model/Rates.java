package com.poni.popularmovieapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Rates {

    @SerializedName("IDR")
    @Expose
    private Double iDR;

    /**
     *
     * @return
     * The iDR
     */
    public Double getIDR() {
        return iDR;
    }

    /**
     *
     * @param iDR
     * The IDR
     */
    public void setIDR(Double iDR) {
        this.iDR = iDR;
    }

}
