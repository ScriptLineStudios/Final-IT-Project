package org.example;

import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class World {
    public static List<Object[]> generateArea(int mapIndex) throws ParseException, IOException, FileNotFoundException {
        JSONArray worldData = ParseJson.parse(mapIndex);
        List<Object[]> returnData = new ArrayList<Object[]>();

        Iterator iterator = worldData.iterator();
        while (iterator.hasNext()) {
           Object data = iterator.next();
           long x = (long)(((JSONArray)(data)).get(0));
           long y = (long)(((JSONArray)(data)).get(1));
           String name = (String)(((JSONArray)(data)).get(4));
           
           returnData.add(new Object[]{((float)x * 8) - 400, -(((float)y * 8) - 400), (String)name});
        }

        return returnData;
    }
}
