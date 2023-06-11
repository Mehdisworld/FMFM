package com.io.filemanager.freefileexplorer.easily.edit;

import java.io.PrintStream;

public class Matrix3 {
    private float[] data;

    public Matrix3() {
        this.data = new float[9];
    }

    public Matrix3(float[] fArr) {
        this();
        setValues(fArr);
    }

    public void setValues(float[] fArr) {
        int length = fArr.length;
        for (int i = 0; i < length; i++) {
            this.data[i] = fArr[i];
        }
    }

    public float[] getValues() {
        float[] fArr = new float[9];
        System.arraycopy(this.data, 0, fArr, 0, 9);
        return fArr;
    }

    public Matrix3 copy() {
        return new Matrix3(getValues());
    }

    public void multiply(Matrix3 matrix3) {
        float[] values = copy().getValues();
        float[] values2 = matrix3.copy().getValues();
        float[] fArr = this.data;
        fArr[0] = (values[0] * values2[0]) + (values[1] * values2[3]) + (values[2] * values2[6]);
        fArr[1] = (values[0] * values2[1]) + (values[1] * values2[4]) + (values[2] * values2[7]);
        fArr[2] = (values[0] * values2[2]) + (values[1] * values2[5]) + (values[2] * values2[8]);
        fArr[3] = (values[3] * values2[0]) + (values[4] * values2[3]) + (values[5] * values2[6]);
        fArr[4] = (values[3] * values2[1]) + (values[4] * values2[4]) + (values[5] * values2[7]);
        fArr[5] = (values[3] * values2[2]) + (values[4] * values2[5]) + (values[5] * values2[8]);
        fArr[6] = (values[6] * values2[0]) + (values[7] * values2[3]) + (values[8] * values2[6]);
        fArr[7] = (values[6] * values2[1]) + (values[7] * values2[4]) + (values[8] * values2[7]);
        fArr[8] = (values[6] * values2[2]) + (values[7] * values2[5]) + (values[8] * values2[8]);
    }

    public Matrix3 inverseMatrix() {
        float[] values = copy().getValues();
        float f = values[0];
        float f2 = values[4];
        values[0] = 1.0f / f;
        values[1] = 0.0f;
        float[] fArr = this.data;
        values[2] = (fArr[2] / f) * -1.0f;
        values[3] = 0.0f;
        values[4] = 1.0f / f2;
        values[5] = (fArr[5] / f2) * -1.0f;
        values[6] = 0.0f;
        values[7] = 0.0f;
        values[8] = 1.0f;
        return new Matrix3(values);
    }

    public void println() {
        PrintStream printStream = System.out;
        printStream.println("data--->" + this.data[0] + "  " + this.data[1] + "  " + this.data[2]);
        PrintStream printStream2 = System.out;
        printStream2.println("              " + this.data[3] + "  " + this.data[4] + "  " + this.data[5]);
        PrintStream printStream3 = System.out;
        printStream3.println("              " + this.data[6] + "  " + this.data[7] + "  " + this.data[8]);
    }
}
