package main.java.utils;


import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxLoader;
import im.bci.tmxloader.TmxMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TmxMapLoader {

    private static volatile TmxMapLoader instance;
    private Map<String, TmxMap> tmxHashMap;

    TmxMapLoader() {
        tmxHashMap = new HashMap<String, TmxMap>();
    }

    public static TmxMapLoader getInstance() {
        TmxMapLoader localInstance = instance;
        if (localInstance == null) {
            synchronized (TmxMapLoader.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TmxMapLoader();
                }
            }
        }
        return localInstance;
    }

    public void loadTmxFile(String identifier, String fileName) throws java.io.IOException {

        String xml = new String(Files.readAllBytes(Paths.get(fileName)));

        TmxLoader loader = new TmxLoader();
        TmxMap map = new TmxMap();
        loader.parseTmx(map, xml);

        tmxHashMap.put(identifier, map);

    }

    public TmxMap getMap(String identifier) {
        return tmxHashMap.get(identifier);
    }

    public Set<String> getMapIds() {
        return tmxHashMap.keySet();
    }

    private TmxLayer getLayerByName(String layerName, TmxMap map) {

        /**
         * Необходимо создать сетку соответствия
         */
//        for (String id: TmxMapLoader.getInstance().getMapIds()) {
//
//            TmxMap map = TmxMapLoader.getInstance().getMap(id);
//            TmxLayer layer = getLayerByName("Ground", map);
//
//            System.out.println("Map [" + id + "]: " + map.getTilewidth() + "x" + map.getTileheight() + " | " + layer.getWidth() + "x" + layer.getHeight());
//        }

        for (TmxLayer layer: map.getLayers()) {
            if (layer.getName().equals(layerName)) {
                return layer;
            }
        }

        return null;
    }
}
