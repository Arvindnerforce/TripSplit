package com.netforceinfotech.tripsplit.Search.searchfragment.subfragment;

/**
 * Created by John on 8/29/2016.
 */
public class CarData {

    public String stringDate, travelCost, ETD, ETA, source, destination, travelTime, carType;

    CarData(String stringDate, String travelCost, String ETD, String ETA, String source, String destination, String travelTime, String carType) {
        this.stringDate = stringDate;
        this.travelCost = travelCost;
        this.ETA = ETA;
        this.ETD = ETD;
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;
        this.carType = carType;
    }
}
