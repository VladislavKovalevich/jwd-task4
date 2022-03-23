package by.vlad.multithreading.reader.impl;

import by.vlad.multithreading.exception.CustomException;
import by.vlad.multithreading.reader.CustomDataReader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomDataReaderImpl implements CustomDataReader {
    private static CustomDataReaderImpl instance;

    private CustomDataReaderImpl(){
    }

    public static CustomDataReaderImpl getInstance() {
        if (instance == null){
            instance = new CustomDataReaderImpl();
        }
        return instance;
    }

    @Override
    public List<String> loadDataFromFile(String filename) throws CustomException {
        URL url = getClass().getClassLoader().getResource(filename);
        if (url == null){
            throw  new CustomException("File " + filename + " does not exists or is not available");
        }

        String path = url.getPath();
        List<String> lines = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(path))){
            lines = bf.lines()
            .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
