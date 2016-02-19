package com.mad.rpg;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

public class DragonAI {
    Map<Integer, Long> hashMap;
    private static final long moveDelta = 5; // in seconds
    private static final long moveDistance = 100; // in px

    public DragonAI () {
        hashMap = new HashMap<Integer, Long>();
    }

    public void moveDragon(Dragon dragon, float mapWidth, float mapHeight) {
        long timestamp = System.currentTimeMillis() / 1000L;
        int dragonId = dragon.getMyId();

        if (hashMap.containsKey(dragonId)) {
            if (hashMap.get(dragonId) + moveDelta < timestamp) {
                hashMap.remove(dragonId);
            }
        }
        else {
            Vector3 pos = dragon.getPosition();

            float minX = Math.max(0, pos.x - moveDistance);
            float maxX = Math.min(mapWidth, pos.x + moveDistance);

            float minY = Math.max(0, pos.y - moveDistance);
            float maxY = Math.min(mapHeight, pos.y + moveDistance);

            Vector2 movePoint = new Vector2(rand(minX, maxX), rand(minY, maxY));
            dragon.touchDown(movePoint.x, movePoint.y);
            hashMap.put(dragonId, timestamp);
        }
    }

    private float rand(float min, float max) {
        return (min + (int)(Math.random() * ((max - min) + 1))) * 1f;
    }
}
