package com.company;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ValueState valueState = new ValueState();
        ThreadOne threadOne = new ThreadOne(valueState);
        ThreadTwo threadTwo = new ThreadTwo(valueState);
        ThreadThree threadThree = new ThreadThree(valueState);
        new Thread(threadOne).start();
        new Thread(threadTwo).start();
        new Thread(threadThree).start();
    }
}

class ValueState{
    private int working_value = 0;
    private int initial_state = 30;

    public synchronized void get() {
        while (working_value < initial_state) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        if (working_value > initial_state) {
            working_value--;
        }

        System.out.println("Состояние реактора " + working_value + " " + initial_state);
        notifyAll();
    }

    public synchronized void put() {
        while (working_value > initial_state) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        if (working_value < initial_state) {
            working_value++;
        }
        System.out.println("Состояние реактора " + working_value + " " + initial_state);
        notifyAll();
    }

    public synchronized void input() {
        if (working_value == initial_state) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        Scanner in = new Scanner(System.in);
        if (working_value == initial_state) {
            initial_state = in.nextInt();
        }
        System.out.println("Состояние реактора " + working_value + " " + initial_state);
        notifyAll();
    }
}

class ThreadOne implements Runnable {
    ValueState valueState;
    ThreadOne(ValueState valueState) {
        this.valueState = valueState;
    }
    public void run() {
        while (true) {
            valueState.put();
        }
    }
}

class ThreadTwo implements Runnable {
    ValueState valueState;
    ThreadTwo(ValueState valueState) {
        this.valueState = valueState;
    }
    public void run(){
        while (true) {
            valueState.get();
        }
    }
}

class ThreadThree implements Runnable {
    ValueState valueState;
    ThreadThree(ValueState valueState) {
        this.valueState = valueState;
    }
    public void run() {
        while (true) {
            valueState.input();
        }
    }
}