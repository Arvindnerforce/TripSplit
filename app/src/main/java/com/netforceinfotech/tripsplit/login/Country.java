package com.netforceinfotech.tripsplit.login;

/**
 * Created by Netforce on 10/17/2016.
 */

public class Country implements Comparable<Country> {
    String country, code;

    Country(String country, String code) {
        this.code = code;
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Country)) {
            return false;
        }

        Country that = (Country) obj;
        return this.country.equals(that.country);
    }

    @Override
    public int compareTo(Country country) {
        return this.country.compareTo(country.country);
    }
}
