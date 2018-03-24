package com.example.charlie.tickr;

/**
 * Created by Austin Tyler on 3/24/2018.
 */

public class Calculations {

    private double netValue, userNetWorth;

    //Takes amount of stock owned and value per stock as inputs and returns total value
    public double calckNetValue(int amount, double value){
        netValue = amount * value;
        return netValue;
    }

    //Takes net value of users stocks, cryptocurrencies, and savings as inputs and returns their net value.
    public double calcUserNetWorth(double stockNet, double cryptoNet, double bankNet){
        userNetWorth = stockNet + cryptoNet + bankNet;
        return userNetWorth;
    }
}
