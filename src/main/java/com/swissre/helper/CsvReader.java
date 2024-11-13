package com.swissre.helper;

import com.swissre.dto.Employee;
import com.swissre.exception.CannotParseFileException;
import com.swissre.exception.ExceedingFileSizeException;
import com.swissre.exception.InvalidFileHeaderException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CsvReader {

    private static final String FILE_HEADER_STRING = "Id,firstName,lastName,salary,managerId";
    private static final int MAX_EMPLOYEE_COUNT = 1000;

    private CsvReader() {
    }

    public static Map<Integer, Set<Employee>> getHierarchyFromFile(String filePath) {
        Map<Integer, Set<Employee>> managerIdToSubordinates = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            if (!header.equals(FILE_HEADER_STRING)) {
                throw new InvalidFileHeaderException(header, FILE_HEADER_STRING);
            }

            String line;
            int employeeCounter = 0;
            while ((line = br.readLine()) != null) {
                if (++employeeCounter > MAX_EMPLOYEE_COUNT) throw new ExceedingFileSizeException(MAX_EMPLOYEE_COUNT);
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String firstName = values[1];
                String lastName = values[2];
                BigDecimal salary = new BigDecimal(values[3]);
                Integer managerId = values.length > 4 ? Integer.parseInt(values[4]) : null;

                Employee employee = new Employee(id, firstName, lastName, salary);
                managerIdToSubordinates
                        .computeIfAbsent(managerId, k -> new HashSet<>())
                        .add(employee);
            }
        } catch (Exception e) {
            throw new CannotParseFileException(filePath, e);
        }

        return managerIdToSubordinates;
    }
}