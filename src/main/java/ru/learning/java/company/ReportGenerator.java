package ru.learning.java.company;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReportGenerator {

    public void generateReports(int employeeCount) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("Начинаем генерацию отчетов...");
        long start = System.currentTimeMillis();

        for (int i = 0; i < employeeCount; i++) {
            final int empId = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(100);
                    System.out.println("Отчет для сотрудника " + empId + " готов. Поток: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Всего времени: " + (end - start) + " мс");
    }
}
