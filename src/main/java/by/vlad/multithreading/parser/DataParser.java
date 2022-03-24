package by.vlad.multithreading.parser;

import by.vlad.multithreading.entity.Ship;

import java.util.List;
import java.util.Map;

public interface DataParser {
    List<Ship> parseShipData(List<String> lines);
    Map<String, Integer> parsePortParams(List<String> lines);
}
