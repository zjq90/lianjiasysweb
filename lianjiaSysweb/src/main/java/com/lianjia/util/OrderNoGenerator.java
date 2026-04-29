package com.lianjia.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    public static String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        int sequence = SEQUENCE.getAndIncrement() % 10000;
        int random = RANDOM.nextInt(1000);
        return String.format("O%s%04d%03d", timestamp, sequence, random);
    }

    public static String generateTaskNo() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        int sequence = SEQUENCE.getAndIncrement() % 10000;
        int random = RANDOM.nextInt(1000);
        return String.format("T%s%04d%03d", timestamp, sequence, random);
    }

    public static String generateCardNo() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        int random = RANDOM.nextInt(10000);
        return String.format("C%s%04d", timestamp, random);
    }

    public static String generateCouponCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
        int random = RANDOM.nextInt(10000);
        return String.format("CP%s%04d", timestamp, random);
    }
}
