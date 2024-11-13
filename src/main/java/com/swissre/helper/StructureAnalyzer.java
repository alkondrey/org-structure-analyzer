package com.swissre.helper;

import com.swissre.dto.Employee;
import com.swissre.exception.CannotFindCeoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StructureAnalyzer {
    private static final int MAX_REPORTING_LEVEL_COUNT = 4;
    private static final BigDecimal MIN_SALARY_MULTIPLIER = new BigDecimal("1.2");
    private static final BigDecimal MAX_SALARY_MULTIPLIER = new BigDecimal("1.5");

    private StructureAnalyzer() {
    }

    // NOTE: Using non-recursive DFS to prevent `StackOverflowError` in large hierarchies
    public static void printAnalysis(Map<Integer, Set<Employee>> hierarchy) {
        Employee ceoEmployee = hierarchy.getOrDefault(null, new HashSet<>())
                .stream().findAny()
                .orElseThrow(CannotFindCeoException::new);
        Deque<Employee> stack = new ArrayDeque<>();
        stack.push(ceoEmployee);
        Map<Integer, Integer> employeeToReportingLevelCount = new HashMap<>();
        employeeToReportingLevelCount.put(ceoEmployee.id(), -1);

        while (!stack.isEmpty()) {
            Employee employee = stack.pop();
            printReportingLineAnalysis(employee, employeeToReportingLevelCount);

            Set<Employee> subordinates = hierarchy.get(employee.id());
            if (subordinates == null) continue;

            BigDecimal subordinatesSalarySum = BigDecimal.ZERO;
            for (Employee subordinate : subordinates) {
                stack.push(subordinate);
                employeeToReportingLevelCount.put(subordinate.id(), employeeToReportingLevelCount.get(employee.id()) + 1);
                subordinatesSalarySum = subordinatesSalarySum.add(subordinate.salary());
            }
            BigDecimal avgSubordinatesSalary = subordinatesSalarySum.divide(new BigDecimal(subordinates.size()), 1, RoundingMode.HALF_UP);
            printSalaryAnalysis(employee, avgSubordinatesSalary);
        }
    }

    private static void printReportingLineAnalysis(Employee employee, Map<Integer, Integer> employeeToReportingLevelCount) {
        int reportingLevelCount = employeeToReportingLevelCount.get(employee.id());
        if (reportingLevelCount > MAX_REPORTING_LEVEL_COUNT) {
            System.out.printf("Employee #%s (%s %s) has %d levels of reporting higher than allowed%n",
                    employee.id(),
                    employee.firstName(),
                    employee.lastName(),
                    reportingLevelCount - MAX_REPORTING_LEVEL_COUNT
            );
        }
    }

    private static void printSalaryAnalysis(Employee employee, BigDecimal avgSubordinatesSalary) {
        BigDecimal minSalary = avgSubordinatesSalary.multiply(MIN_SALARY_MULTIPLIER);
        if (employee.salary().compareTo(minSalary) < 0) {
            System.out.printf("Employee #%s (%s %s) is underpaid by %s%n",
                    employee.id(),
                    employee.firstName(),
                    employee.lastName(),
                    minSalary.subtract(employee.salary())
            );
            return;
        }
        BigDecimal maxSalary = avgSubordinatesSalary.multiply(MAX_SALARY_MULTIPLIER);
        if (employee.salary().compareTo(maxSalary) > 0) {
            System.out.printf("Employee #%s (%s %s) is overpaid by %s%n",
                    employee.id(),
                    employee.firstName(),
                    employee.lastName(),
                    employee.salary().subtract(maxSalary)
            );
        }
    }
}
