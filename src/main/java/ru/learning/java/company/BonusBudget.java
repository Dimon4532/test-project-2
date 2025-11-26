package ru.learning.java.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BonusBudget {

  private int badCounter = 0;
  private final AtomicInteger goodCounter = new AtomicInteger(0);

  private final List<String> badLog = new ArrayList<>();

  private final List<String> goodLog = new CopyOnWriteArrayList<>();

  public void runRaceConditionDemo() throws InterruptedException {
    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Thread t = new Thread(() -> {
        badCounter++;
        try {
          badLog.add("Log");
        } catch (Exception ignored) {}

        goodCounter.incrementAndGet();
        goodLog.add("Log");
      });
      threads.add(t);
      t.start();
    }

    for (Thread t : threads) t.join();

    System.out.println("Мы ожидаем значение: 1000");
    System.out.println("Bad Counter (обычный int): " + badCounter);
    System.out.println("Good Counter (Atomic): " + goodCounter.get());
    System.out.println("Good Log size: " + goodLog.size());
  }
}