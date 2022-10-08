package org.example;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static List<float[]> generateArea() {
        SimplexNoise noiseGen = new SimplexNoise(10, 5.0f, 123445);
        List<float[]> world = new ArrayList<float[]>();  
        for (int y = -800; y < 800; y+=256) {
            for (int x = -800; x < 800; x+=256) {
                double val = noiseGen.getNoise2D(x, y);
                if (val > 200) {
                    world.add(new float[]{x, y});
                }
            }
        }

        return world;
    }
}
