package org.example;

import java.io.IOException;
import java.util.HashMap;

public class Text {
    float x, y, size;
    String text;
    Engine engine;
    HashMap<String, Texture> lookup;
    
    Text(String _text, float _x, float _y, float _size, Engine en) throws IOException {
        x = _x;
        y = _y;
        text = _text;
        size =  _size;
        engine = en;

        lookup = new HashMap<String, Texture>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        for (int x = 1; x <= alphabet.length(); x++) {
            String _char = Character.toString(alphabet.charAt(x - 1));
            String path = "src/main/resources/assets/images/alphabet/alphabet" + x + ".png";
            System.out.println(path);
            lookup.put(_char, engine.loadTex(path));
        }
        lookup.put(" ", engine.loadTex("src/main/resources/assets/images/alphabet/space.png"));
    }

    void render_text() {
        for (int i = 0; i < text.length(); i++) {
            String _char = Character.toString(text.charAt(i));
            lookup.get(_char).render(x + i * (size - 40), y, size, size);
        }
    }
}
