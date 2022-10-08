package org.example;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static List<float[]> generateArea() {
        ParseJson.parse();
        List<float[]> world = new ArrayList<float[]>();  
        SimplexNoise noise = new SimplexNoise(4, 500, 4);
        for (int y = -10; y < 10; y+=1) {
            for (int x = -10; x < 10; x+=1) {
                double val = noise.getNoise2D(x, y);
                //System.out.println(val);
                if (val > 100) {
                    world.add(new float[]{x*64, y*64});
                }
            }
        }

        return world;
    }
}
