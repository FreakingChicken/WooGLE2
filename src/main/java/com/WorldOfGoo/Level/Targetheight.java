package com.WorldOfGoo.Level;

import com.WooGLEFX.EditorObjects.objectcomponents.LineComponent;
import com.WooGLEFX.Engine.Depth;
import com.WooGLEFX.Functions.LevelManager;
import com.WooGLEFX.EditorObjects.EditorObject;
import com.WooGLEFX.EditorObjects.InputField;
import com.WooGLEFX.Structures.SimpleStructures.MetaEditorAttribute;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Targetheight extends EditorObject {

    public Targetheight(EditorObject _parent) {
        super(_parent, "targetheight", "level\\targetheight");

        addAttribute("y", InputField.NUMBER).setDefaultValue("1000").assertRequired();

        addObjectComponent(new LineComponent() {
            public double getX() {
                return 0;
            }
            public double getY() {
                return -getAttribute("y").doubleValue();
            }
            public void setY(double y) {
                setAttribute("y", -y);
            }
            public double getLineWidth() {
                return 3;
            }
            public double getDepth() {
                return Depth.GEOMETRY;
            }
            public Paint getColor() {
                return new Color(1.0, 0.0, 1.0, 1.0);
            }
            public boolean isVisible() {
                return LevelManager.getLevel().getVisibilitySettings().getShowGeometry() != 0;
            }
        });

        setMetaAttributes(MetaEditorAttribute.parse("y,"));

    }


    @Override
    public String getName() {
        return getAttribute("y").stringValue();
    }

}
