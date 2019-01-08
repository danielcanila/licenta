package com.daniel.licenta.calendargenerator.algorithm.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomGenerator {

    private int seed = new Random().nextInt();
    private Random randomGenerator = new Random(seed);

    public void setSeed(int seed) {
        this.seed = seed;
        randomGenerator = new Random(seed);
    }

    public int nextNumber() {
        return Math.abs(randomGenerator.nextInt());
    }

    public int nextInt(int min, int max) {
        return randomGenerator.nextInt(max - min + 1) + min;
    }

    public double nextDouble() {
        return randomGenerator.nextDouble();
    }

    public int getSeed() {
        return seed;
    }
}