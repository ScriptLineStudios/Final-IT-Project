package org.example;

import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class World {
    public static List<float[]> generateArea() throws ParseException, IOException, FileNotFoundException {
        JSONArray worldData = ParseJson.parse();
        List<float[]> returnData = new ArrayList<float[]>();

        Iterator iterator = worldData.iterator();
        while (iterator.hasNext()) {
           ///System.out.println();
           Object data = iterator.next();
           long x = (long)(((JSONArray)(data)).get(0));
           long y = (long)(((JSONArray)(data)).get(1));

           returnData.add(new float[]{(float)x * 2, (float)y * 2});
        }

        return returnData;
    }
}
