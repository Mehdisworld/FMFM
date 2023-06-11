package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.easing;

public class Circ implements Easing {
    public double easeOut(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (d3 * Math.sqrt(1.0d - (d5 * d5))) + d2;
    }

    public double easeIn(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return ((-d3) * (Math.sqrt(1.0d - (d5 * d5)) - 1.0d)) + d2;
    }

    public double easeInOut(double d, double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7 = d / (d4 / 2.0d);
        if (d7 < 1.0d) {
            d5 = (-d3) / 2.0d;
            d6 = Math.sqrt(1.0d - (d7 * d7)) - 1.0d;
        } else {
            d5 = d3 / 2.0d;
            double d8 = d7 - 2.0d;
            d6 = Math.sqrt(1.0d - (d8 * d8)) + 1.0d;
        }
        return (d5 * d6) + d2;
    }
}
