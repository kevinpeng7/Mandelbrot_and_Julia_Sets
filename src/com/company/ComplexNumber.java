package com.company;

public class ComplexNumber {
    double a, b;
    static int SCREEN_WIDTH = 1800;
    static int SCREEN_HEIGHT = 1000;

    public ComplexNumber(double eh, double bee) {
        this.a = eh;
        this.b = bee;
    }

    public void display() {
        if (this.a == 0) {
            if (b == 1) System.out.println("i");
            else if (b != 0) System.out.println(b + "i");
            else System.out.println(0);
        } else if (this.b == 0) {
            if (a != 0) System.out.println(a);
            else System.out.println(0);
        } else if (b == 1) System.out.println(a + " + i");
        else if (b >= 0) System.out.println(a + " + " + b + "i");
        else System.out.println(a + " - " + Math.abs(b) + "i");
    }

    public ComplexNumber add(ComplexNumber other) {
        double new_a = this.a + other.a;
        double new_b = this.b + other.b;
        ComplexNumber z = new ComplexNumber(new_a, new_b);
        return z;
    }

    public ComplexNumber subtract(ComplexNumber other){
        double new_a = this.a - other.a;
        double new_b = this.b - other.b;
        return new ComplexNumber(new_a,new_b);
    }

    public double absValue() {
        return Math.sqrt(Math.pow(this.a, 2) + Math.pow(this.b, 2));
    }

    public ComplexNumber multByCN(ComplexNumber other) {
        double real = this.a * other.a - this.b * other.b;
        double imaginary = this.a * other.b + this.b * other.a;
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber multByScaler(double scaler) {
        return new ComplexNumber(scaler * this.a, scaler * this.b);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.a, -this.b);
    }

    public ComplexNumber divideByCN(ComplexNumber other) {
        return this.multByCN(other.conjugate()).multByScaler(1 / Math.pow(other.absValue(), 2));
    }
}