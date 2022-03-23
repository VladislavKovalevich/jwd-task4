package by.vlad.multithreading.reader;

import by.vlad.multithreading.exception.CustomException;

import java.util.List;

public interface CustomDataReader {
    List<String> loadDataFromFile(String filename) throws CustomException;
}
