package com.WorldOfGoo.Level;

import com.WooGLEFX.EditorObjects.ImageUtility;
import com.WooGLEFX.EditorObjects.objectcomponents.ImageComponent;
import com.WooGLEFX.Functions.LevelLoader;
import com.WooGLEFX.Functions.LevelManager;
import javafx.scene.image.Image;

import com.WooGLEFX.EditorObjects.EditorObject;
import com.WooGLEFX.EditorObjects.InputField;
import com.WooGLEFX.Structures.SimpleStructures.Color;
import com.WooGLEFX.Structures.SimpleStructures.MetaEditorAttribute;

public class Signpost extends EditorObject {

    private Image image;


    public Signpost(EditorObject _parent) {
        super(_parent, "signpost", "level\\signpost");

        addAttribute("name",      InputField.ANY)                                  .assertRequired();
        addAttribute("depth",     InputField.NUMBER).setDefaultValue("0")          .assertRequired();
        addAttribute("x",         InputField.NUMBER).setDefaultValue("0")          .assertRequired();
        addAttribute("y",         InputField.NUMBER).setDefaultValue("0")          .assertRequired();
        addAttribute("scalex",    InputField.NUMBER).setDefaultValue("1")          .assertRequired();
        addAttribute("scaley",    InputField.NUMBER).setDefaultValue("1")          .assertRequired();
        addAttribute("rotation",  InputField.NUMBER).setDefaultValue("0")          .assertRequired();
        addAttribute("alpha",     InputField.NUMBER).setDefaultValue("1")          .assertRequired();
        addAttribute("colorize",  InputField.COLOR) .setDefaultValue("255,255,255").assertRequired();
        addAttribute("image",     InputField.IMAGE)                                .assertRequired();
        addAttribute("text",      InputField.TEXT)                                 .assertRequired();
        addAttribute("particles", InputField.PARTICLES);
        addAttribute("pulse",     InputField.ANY);

        addObjectComponent(new ImageComponent() {
            public double getX() {
                return getAttribute("x").doubleValue();
            }
            public void setX(double x) {
                setAttribute("x", x);
            }
            public double getY() {
                return -getAttribute("y").doubleValue();
            }
            public void setY(double y) {
                setAttribute("y", -y);
            }
            public double getRotation() {
                return -Math.toRadians(getAttribute("rotation").doubleValue());
            }
            public void setRotation(double rotation) {
                setAttribute("rotation", -Math.toDegrees(rotation));
            }
            public double getScaleX() {
                return getAttribute("scalex").doubleValue();
            }
            public void setScaleX(double scaleX) {
                setAttribute("scalex", scaleX);
            }
            public double getScaleY() {
                return getAttribute("scaley").doubleValue();
            }
            public void setScaleY(double scaleY) {
                setAttribute("scaley", scaleY);
            }
            public double getDepth() {
                return getAttribute("depth").doubleValue();
            }
            public Image getImage() {
                return image;
            }
            public boolean isVisible() {
                return LevelManager.getLevel().getVisibilitySettings().isShowGraphics();
            }
        });

        setMetaAttributes(MetaEditorAttribute.parse("name,text,particles,pulse,Image<x,y,scalex,scaley,image,rotation,depth,alpha,colorize>"));

        getAttribute("image").addChangeListener((observable, oldValue, newValue) -> updateImage());
        getAttribute("colorize").addChangeListener((observable, oldValue, newValue) -> updateImage());

    }


    @Override
    public String getName() {
        return getAttribute("name").stringValue();
    }


    @Override
    public void update() {
        updateImage();
    }


    private void updateImage() {

        if (LevelManager.getLevel() == null) return;

        try {
            image = getAttribute("image").imageValue(LevelManager.getVersion());
            if (image == null) return;
            Color color = getAttribute("colorize").colorValue();
            image = ImageUtility.colorize(image, color);
        } catch (Exception e) {
            // TODO make this cleaner
            if (!LevelLoader.failedResources.contains("From signpost: \"" + getAttribute("image") + "\" (version " + LevelManager.getVersion() + ")")) {
                LevelLoader.failedResources.add("From signpost: \"" + getAttribute("image") + "\" (version " + LevelManager.getVersion() + ")");
            }
            image = null;
        }

    }

}
