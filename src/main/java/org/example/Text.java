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

        for (int x = 0; x < alphabet.length(); x++) {
            String _char = Character.toString(alphabet.charAt(x));
            String path = "src/main/resources/assets/alphabet/" + _char + ".png";
            lookup.put(_char, engine.loadTex(path));
        }
        lookup.put(" ", engine.loadTex("src/main/resources/assets/alphabet/space.png"));
    }

    void render_text() {
        for (int i = 0; i < text.length(); i++) {
            String _char = Character.toString(text.charAt(i));
            lookup.get(_char).render(x * i * 2, y, 128, 128);
        }
    }
}
