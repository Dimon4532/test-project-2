package ru.learning.java.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BonusBudget {

  private final AtomicInteger atomicCounter = new AtomicInteger(0);
  private final List<String> log = new CopyOnWriteArrayList<>();

  public void runRaceConditionDemo() throws InterruptedException {
    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Thread t = new Thread(() -> {

        atomicCounter.incrementAndGet();
        log.add("Log");
      });
      threads.add(t);
      t.start();
    }

    for (Thread t : threads) t.join();

    System.out.println("Мы ожидаем значение: 1000");
    System.out.println("Atomic Counter: " + atomicCounter.get());
    System.out.println("Log size: " + log.size());
  }
}