package com.swissre;

import com.swissre.dto.Employee;
import com.swissre.helper.CsvParser;
import com.swissre.helper.StructureAnalyzer;

import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        System.out.println("Analysis started...");
        String filePath = getFilePathFromArgs(args);
        Map<Integer, Set<Employee>> hierarchy = CsvParser.getHierarchyFromFile(filePath);
        StructureAnalyzer.printAnalysis(hierarchy);
        System.out.println("Analysis finished!");
    }

    private static String getFilePathFromArgs(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide a command line file path argument");
        }
        return args[0];
    }
}