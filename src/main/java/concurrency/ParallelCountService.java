package concurrency;

import collection.CarArray;
import model.Car;

import java.util.Objects;

public class ParallelCountService {

    public int count(CarArray array, Car target, int threadCount) {
        Objects.requireNonNull(array, "Array must not be null");
        Objects.requireNonNull(target, "Target car must not be null");
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount must be positive. Invalid value: " + threadCount);
        }

        int size = array.size();
        CountTask[] tasks = new CountTask[threadCount];
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int from = i * size / threadCount;
            int to = (i + 1) * size / threadCount;

            tasks[i] = new CountTask(array, target, from, to);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        int total = 0;
        try {
            for (int i = 0; i < threadCount; i++) {
                threads[i].join();
                total += tasks[i].getResult();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Counting was interrupted", e);
        }
        return total;
    }

    public void countAndPrint(CarArray array, Car target, int threadCount) {
        int result = count(array, target, threadCount);
        System.out.println("Количество вхождений " + target + ": " + result);
    }

    private static class CountTask implements Runnable {
        private final CarArray array;
        private final Car target;
        private final int from;
        private final int to;
        private int result;

        private CountTask(CarArray array, Car target, int from, int to) {
            this.array = array;
            this.target = target;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            int local = 0;
            for (int i = from; i < to; i++) {
                if (target.equals(array.get(i))) {
                    local++;
                }
            }
            result = local;
        }

        private int getResult() {
            return result;
        }
    }
}