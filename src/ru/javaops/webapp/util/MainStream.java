package ru.javaops.webapp.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(numbers));

        List<Integer> integers = Arrays.asList(1, 5, 3, 8, 6, 4, 3, 7, 5, 6, 9, 2, 34, 765, 23, 54, 76);
        System.out.println(oddOrEven(integers));
        System.out.println(oddOrEvenOptional_1(integers));
        System.out.println(oddOrEvenOptional_2(integers));
        System.out.println(oddOrEvenOptional(integers));
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (n1, n2) -> n1 * 10 + n2);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        boolean oddOrEven = integers.stream().reduce(0, Integer::sum) % 2 == 0;
        return integers.stream()
                .filter(n -> oddOrEven == (n % 2 == 0))
                .collect(Collectors.toList());
    }

    public static List<Integer> oddOrEvenOptional_1(List<Integer> integers) {
        Map<Boolean, Integer> oddAndEvenCountMap = new HashMap<>();
        return integers.stream()
                .peek(n -> oddAndEvenCountMap.merge(n % 2 == 0, 0, Integer::sum))
                .collect(Collectors.partitioningBy(n -> n % 2 == 0))
                .get(oddAndEvenCountMap.get(false) % 2 != 0);
    }

    public static List<Integer> oddOrEvenOptional_2(List<Integer> integers) {
        Map<Boolean, Long> oddOrEvenMap = integers.stream()
                .collect(Collectors.partitioningBy(n -> (n % 2 == 0), Collectors.counting()));
        boolean oddOrEven = oddOrEvenMap.get(false) % 2 == 0;
        return integers.stream().filter(n -> oddOrEven == (n % 2 == 0)).collect(Collectors.toList());
    }

    public static List<Integer> oddOrEvenOptional(List<Integer> integers) {
        return integers.stream().collect(new CustomCollector());
    }

    public static class CustomCollector implements Collector<Integer, List<Integer>, List<Integer>> {
        List<Integer> filteredList = new ArrayList<>();
        AtomicInteger sum = new AtomicInteger();

        @Override
        public Supplier<List<Integer>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<Integer>, Integer> accumulator() {
            return (list, n) -> {
                sum.addAndGet(n);
                list.add(n);
            };
        }

        @Override
        public BinaryOperator<List<Integer>> combiner() {
            return (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            };
        }

        @Override
        public Function<List<Integer>, List<Integer>> finisher() {
            return list -> {
                list.forEach(n -> {
                    if ((sum.get() % 2 == 0) == (n % 2 == 0)) {
                        filteredList.add(n);
                    }
                });
                return filteredList;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.CONCURRENT);
        }
    }
}



