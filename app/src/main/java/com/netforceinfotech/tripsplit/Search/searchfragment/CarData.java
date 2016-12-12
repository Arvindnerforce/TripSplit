package com.netforceinfotech.tripsplit.Search.searchfragment;

/**
 * Created by John on 8/29/2016.
 */
public class CarData {

    public String trip_id, travelCost, source, destination, travelTime, carType, currency;

    CarData(String trip_id, String travelCost, String source, String destination, String travelTime, String carType, String currency) {
        this.travelCost = travelCost;
        this.currency = currency;
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;
        this.carType = carType;
        this.trip_id = trip_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CarData)) {
            return false;
        }

        CarData that = (CarData) obj;
        return this.trip_id.equals(that.trip_id);
    }
}
