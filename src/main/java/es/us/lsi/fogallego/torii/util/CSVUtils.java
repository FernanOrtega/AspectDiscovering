package es.us.lsi.fogallego.torii.util;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtils {

    public static List<List<String>> loadFromCSVFile(String filename) throws IOException {
        List<List<String>> lstRowsAsLists;

        CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
        List<String[]> lstRows = csvReader.readAll();
        csvReader.close();
        lstRowsAsLists = lstRows.stream().map(l -> Arrays.asList(l)).collect(Collectors.toList());

        return lstRowsAsLists;
    }

    public static List<List<String>> loadFromCSVFile(String filename, char separator) throws IOException {
        CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"), separator);
        List<String[]> lstRows = csvReader.readAll();
        csvReader.close();

        return lstRows.stream().map(l -> Arrays.asList(l)).collect(Collectors.toList());
    }

    public static List<List<String>> loadFromCSVFile(String filename, char separator, char quotechar) throws IOException {
        CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"), separator, quotechar);
        List<String[]> lstRows = csvReader.readAll();
        csvReader.close();

        return lstRows.stream().map(l -> Arrays.asList(l)).collect(Collectors.toList());
    }

    public static List<List<String>> loadFromCSVFile(String filename, char separator, char quotechar, int line) throws IOException {
        CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"),
                separator, quotechar, line);
        List<String[]> lstRows = csvReader.readAll();
        csvReader.close();

        return lstRows.stream().map(l -> Arrays.asList(l)).collect(Collectors.toList());
    }
}
