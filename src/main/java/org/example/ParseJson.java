package org.example;

import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ParseJson {
   public static JSONArray parse(int mapIndex) throws ParseException, IOException, FileNotFoundException {
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(new FileReader("src/main/resources/assets/images/map" + mapIndex + ".json"));
      JSONObject jsonObject = (JSONObject)obj;
      JSONArray data = (JSONArray)jsonObject.get("map");
      return data;
   }
}