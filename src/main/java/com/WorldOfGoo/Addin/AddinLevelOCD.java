package com.WorldOfGoo.Addin;

import com.WooGLEFX.Structures.EditorObject;
import com.WooGLEFX.Structures.InputField;
import com.WooGLEFX.Structures.SimpleStructures.MetaEditorAttribute;

public class AddinLevelOCD extends EditorObject {

    public AddinLevelOCD(EditorObject _parent) {
        super(_parent, "ocd");

        addAttribute("type", InputField.OCD_TYPE)            .assertRequired();
        addAttribute("value", InputField.NUMBER_NON_NEGATIVE).assertRequired();

        setMetaAttributes(MetaEditorAttribute.parse("type,value,"));

    }


    @Override
    public String getName() {
        String type = getAttribute("type").stringValue();
        String value = getAttribute("value").stringValue();
        return type + ", " + value;
    }

}
