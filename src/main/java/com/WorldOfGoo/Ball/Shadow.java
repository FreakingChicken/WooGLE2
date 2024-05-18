package com.WorldOfGoo.Ball;

import com.WooGLEFX.Structures.EditorObject;
import com.WooGLEFX.Structures.InputField;

public class Shadow extends EditorObject {

    public Shadow(EditorObject _parent) {
        super(_parent, "shadow");

        addAttribute("image",    InputField.ANY)  .assertRequired();
        addAttribute("additive", InputField.ANY);

    }

}
