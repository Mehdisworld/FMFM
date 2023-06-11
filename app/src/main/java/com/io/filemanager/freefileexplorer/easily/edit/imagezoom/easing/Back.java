package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.easing;

public class Back implements Easing {
    public double easeIn(double d, double d2, double d3, double d4, double d5) {
        if (d5 == 0.0d) {
            d5 = 1.70158d;
        }
        double d6 = d / d4;
        return (d3 * d6 * d6 * (((1.0d + d5) * d6) - d5)) + d2;
    }

    public double easeInOut(double d, double d2, double d3, double d4, double d5) {
        double d6;
        if (d5 == 0.0d) {
            d5 = 1.70158d;
        }
        double d7 = d / (d4 / 2.0d);
        if (d7 < 1.0d) {
            double d8 = d5 * 1.525d;
            d6 = (d3 / 2.0d) * d7 * d7 * (((1.0d + d8) * d7) - d8);
        } else {
            double d9 = d7 - 2.0d;
            double d10 = d5 * 1.525d;
            d6 = (d3 / 2.0d) * ((d9 * d9 * (((1.0d + d10) * d9) + d10)) + 2.0d);
        }
        return d6 + d2;
    }

    public double easeOut(double d, double d2, double d3, double d4, double d5) {
        if (d5 == 0.0d) {
            d5 = 1.70158d;
        }
        double d6 = (d / d4) - 1.0d;
        return (d3 * ((d6 * d6 * (((d5 + 1.0d) * d6) + d5)) + 1.0d)) + d2;
    }

    public double easeOut(double d, double d2, double d3, double d4) {
        return easeOut(d, d2, d3, d4, 0.0d);
    }

    public double easeIn(double d, double d2, double d3, double d4) {
        return easeIn(d, d2, d3, d4, 0.0d);
    }

    public double easeInOut(double d, double d2, double d3, double d4) {
        return easeInOut(d, d2, d3, d4, 0.9d);
    }
}
