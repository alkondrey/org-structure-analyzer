package com.swissre.helper;

import com.swissre.dto.Employee;
import com.swissre.exception.CannotParseFileException;
import com.swissre.exception.ExceedingFileSizeException;
import com.swissre.exception.InvalidFileHeaderException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CsvParser {

    private static final String FILE_HEADER_STRING = "Id,firstName,lastName,salary,managerId";
    private static final int MAX_EMPLOYEE_COUNT = 1000;

    private CsvParser() {
    }

    public static Map<Integer, Set<Employee>> getHierarchyFromFile(String filePath) {
        Map<Integer, Set<Employee>> managerIdToSubordinates = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            checkHeader(br);

            String line;
            int employeeCounter = 0;
            while ((line = br.readLine()) != null) {
                if (++employeeCounter > MAX_EMPLOYEE_COUNT)
                    throw new ExceedingFileSizeException(MAX_EMPLOYEE_COUNT);

                String[] values = line.split(",");
                Employee employee = parseEmployeeData(values);
                Integer managerId = values.length > 4 ? Integer.parseInt(values[4]) : null;
                managerIdToSubordinates
                        .computeIfAbsent(managerId, k -> new HashSet<>())
                        .add(employee);
            }
        } catch (Exception e) {
            throw new CannotParseFileException(filePath, e);
        }

        return managerIdToSubordinates;
    }

    private static void checkHeader(BufferedReader br) throws IOException {
        String header = br.readLine();
        if (header == null || !header.equals(FILE_HEADER_STRING))
            throw new InvalidFileHeaderException(br.readLine(), FILE_HEADER_STRING);
    }

    private static Employee parseEmployeeData(String[] values) {
        int id = Integer.parseInt(values[0]);
        String firstName = values[1];
        String lastName = values[2];
        BigDecimal salary = new BigDecimal(values[3]);

        return new Employee(id, firstName, lastName, salary);
    }
}
