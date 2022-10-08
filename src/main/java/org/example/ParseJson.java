package org.example;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
public class ParseJson {
   public static void parse() {
      JSONParser parser = new JSONParser();
      try {
         Object obj = parser.parse(new FileReader("src/main/resources/assets/images/map.json"));
         JSONObject jsonObject = (JSONObject)obj;
         JSONArray data = (JSONArray)jsonObject.get("map");
         System.out.println(data.get(0));
         Iterator iterator = data.iterator();
         while (iterator.hasNext()) {
            System.out.println(((JSONArray)(iterator.next())).get(0));
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}