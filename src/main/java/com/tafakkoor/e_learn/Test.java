package com.tafakkoor.e_learn;

public class Test {
    static String[] arr = {"th", "st", "nd", "rd"};

    public static void main(String[] args) {

    }

    public static String test(int num) {
        if (num > 10 && num < 14) {
            return arr[0];
        }
        return arr[(num % 10<4)?num % 10:0];

    }
}
