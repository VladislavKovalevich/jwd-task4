package by.vlad.multithreading.parser.impl;

import by.vlad.multithreading.entity.Ship;
import by.vlad.multithreading.entity.ShipTypeEnum;
import by.vlad.multithreading.parser.ApplicationDataParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationDataParserImpl implements ApplicationDataParser {
    private static final String SHIP_DATA_DELIMITER = ",";
    private static final String PORT_DATA_DELIMITER = "=";
    private static ApplicationDataParserImpl instance;

    private ApplicationDataParserImpl(){
    }

    public static ApplicationDataParserImpl getInstance() {
        if (instance == null){
            instance = new ApplicationDataParserImpl();
        }
        return instance;
    }

    @Override
    public List<Ship> parseShipData(List<String> lines) {
        List<Ship> ships = new ArrayList<>();

        ships = lines.stream()
                .map(l -> Stream.of(l.split(SHIP_DATA_DELIMITER)).collect(Collectors.toList()))
                .map(init -> new Ship(init.get(0),
                        Integer.parseInt(init.get(1)),
                        Integer.parseInt(init.get(2)),
                        ShipTypeEnum.valueOf(init.get(3).toUpperCase())))
        .collect(Collectors.toList());
        return ships;
    }

    @Override
    public Map<String, Integer> parsePortParams(List<String> lines) {
        Map<String, Integer> map = new HashMap<>();

        for (String line : lines) {
            String[] l = line.split(PORT_DATA_DELIMITER);
            map.put(l[0], Integer.parseInt(l[1]));
        }

        return map;
    }
}
