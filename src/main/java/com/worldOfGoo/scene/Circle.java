package com.worldOfGoo.scene;

import com.woogleFX.editorObjects.EditorObject;
import com.woogleFX.editorObjects.attributes.EditorAttribute;
import com.woogleFX.editorObjects.attributes.InputField;
import com.woogleFX.editorObjects.objectComponents.CircleComponent;
import com.woogleFX.editorObjects.objectComponents.ImageComponent;
import com.woogleFX.editorObjects.ObjectUtil;
import com.woogleFX.engine.renderer.Depth;
import com.woogleFX.engine.LevelManager;
import com.woogleFX.gameData.level.GameVersion;
import com.woogleFX.editorObjects.attributes.MetaEditorAttribute;
import com.woogleFX.editorObjects.attributes.dataTypes.Position;
import com.woogleFX.gameData.level.WOG1Level;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;

public class Circle extends EditorObject {

    public Circle(EditorObject _parent, GameVersion version) {
        super(_parent, "circle", version);

        addAttribute("id",               InputField._1_STRING)                                   .assertRequired();
        addAttribute("mass",             InputField._1_NUMBER)     .setDefaultValue("0");
        addAttribute("static",           InputField._1_FLAG);
        addAttribute("tag",              InputField._1_TAG);
        addAttribute("material",         InputField._1_MATERIAL);
        addAttribute("contacts",         InputField._1_FLAG)       .setDefaultValue("true");
        addAttribute("x",                InputField._1_NUMBER)     .setDefaultValue("0")      .assertRequired();
        addAttribute("y",                InputField._1_NUMBER)     .setDefaultValue("0")      .assertRequired();
        addAttribute("radius",           InputField._1_NUMBER)     .setDefaultValue("75")     .assertRequired();
        addAttribute("break",            InputField._1_NUMBER);
        addAttribute("image",            InputField._1_IMAGE);
        addAttribute("imagepos",         InputField._1_POSITION);
        addAttribute("imagerot",         InputField._1_NUMBER);
        addAttribute("imagescale",       InputField._1_POSITION)   .setDefaultValue("1,1");
        addAttribute("rotspeed",         InputField._1_NUMBER);
        addAttribute("nogeomcollisions", InputField._1_FLAG);

        addObjectComponent(new CircleComponent() {
            public double getX() {

                if (getParent() instanceof Compositegeom compositegeom) {

                    double compGeomX = compositegeom.getAttribute("x").doubleValue();
                    double compGeomY = -compositegeom.getAttribute("y").doubleValue();
                    double compGeomRotation = -compositegeom.getAttribute("rotation").doubleValue();

                    double x = getAttribute("x").doubleValue();
                    double y = -getAttribute("y").doubleValue();

                    Point2D position = new Point2D(x, y);
                    position = ObjectUtil.rotate(position, compGeomRotation, new Point2D(0, 0));
                    position = position.add(compGeomX, compGeomY);

                    return position.getX();

                } else {

                    return getAttribute("x").doubleValue();

                }

            }
            public void setX(double x) {

                if (getParent() instanceof Compositegeom compositegeom) {

                    double compGeomX = compositegeom.getAttribute("x").doubleValue();
                    double compGeomY = -compositegeom.getAttribute("y").doubleValue();
                    double compGeomRotation = -compositegeom.getAttribute("rotation").doubleValue();

                    Point2D position = new Point2D(x, getY());
                    position = position.subtract(compGeomX, compGeomY);
                    position = ObjectUtil.rotate(position, -compGeomRotation, new Point2D(0, 0));

                    setAttribute("x", position.getX());
                    setAttribute("y", -position.getY());

                } else {

                    setAttribute("x", x);

                }

            }
            public double getY() {

                if (getParent() instanceof Compositegeom compositegeom) {

                    double compGeomX = compositegeom.getAttribute("x").doubleValue();
                    double compGeomY = -compositegeom.getAttribute("y").doubleValue();
                    double compGeomRotation = -compositegeom.getAttribute("rotation").doubleValue();

                    double x = getAttribute("x").doubleValue();
                    double y = -getAttribute("y").doubleValue();

                    Point2D position = new Point2D(x, y);
                    position = ObjectUtil.rotate(position, compGeomRotation, new Point2D(0, 0));
                    position = position.add(compGeomX, compGeomY);

                    return position.getY();

                } else {

                    return -getAttribute("y").doubleValue();

                }

            }
            public void setY(double y) {

                if (getParent() instanceof Compositegeom compositegeom) {

                    double compGeomX = compositegeom.getAttribute("x").doubleValue();
                    double compGeomY = -compositegeom.getAttribute("y").doubleValue();
                    double compGeomRotation = -compositegeom.getAttribute("rotation").doubleValue();

                    Point2D position = new Point2D(getX(), y);
                    position = position.subtract(compGeomX, compGeomY);
                    position = ObjectUtil.rotate(position, -compGeomRotation, new Point2D(0, 0));

                    setAttribute("x", position.getX());
                    setAttribute("y", -position.getY());

                } else {

                    setAttribute("y", -y);

                }

            }
            public double getRadius() {
                return getAttribute("radius").doubleValue();
            }
            public void setRadius(double radius) {
                setAttribute("radius", radius);
            }
            public double getEdgeSize() {
                boolean contacts = getAttribute("contacts").booleanValue();
                return (contacts || LevelManager.getLevel().getVisibilitySettings().getShowGeometry() != 2) ? 4 : 0;
            }
            public boolean isEdgeOnly() {
                return false;
            }
            public double getDepth() {
                return Depth.GEOMETRY;
            }
            public Paint getBorderColor() {
                return Rectangle.geometryColor(getAttribute("tag").listValue(), getParent());
            }
            public Paint getColor() {
                Color color = Rectangle.geometryColor(getAttribute("tag").listValue(), getParent());
                return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.25);
            }
            public boolean isVisible() {
                return LevelManager.getLevel().getVisibilitySettings().getShowGeometry() != 0;
            }
        });

        addObjectComponent(new ImageComponent() {
            public double getX() {
                EditorAttribute imagepos = getAttribute("imagepos");
                if (imagepos.stringValue().isEmpty()) return getAttribute("x").doubleValue();
                else return imagepos.positionValue().getX();
            }
            public void setX(double x) {
                setAttribute("imagepos", x + "," + -getY());
            }
            public double getY() {
                EditorAttribute imagepos = getAttribute("imagepos");
                if (imagepos.stringValue().isEmpty()) return -getAttribute("y").doubleValue();
                else return -imagepos.positionValue().getY();
            }
            public void setY(double y) {
                setAttribute("imagepos", getX() + "," + -y);
            }
            public double getRotation() {
                return -getAttribute("imagerot").doubleValue();
            }
            public void setRotation(double rotation) {
                setAttribute("imagerot", -rotation);
            }
            public double getScaleX() {
                return getAttribute("imagescale").positionValue().getX();
            }
            public void setScaleX(double scaleX) {
                Position scale = getAttribute("imagescale").positionValue();
                setAttribute("imagescale", scaleX + "," + scale.getY());
            }
            public double getScaleY() {
                return getAttribute("imagescale").positionValue().getY();
            }
            public void setScaleY(double scaleY) {
                Position scale = getAttribute("imagescale").positionValue();
                setAttribute("imagescale", scale.getX() + "," + scaleY);
            }
            public Image getImage() {
                try {
                    return getAttribute("image").imageValue(((WOG1Level)LevelManager.getLevel()).getResrc(), getVersion());
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
            public boolean isGeometryImage() {
                return true;
            }
            public double getDepth() {
                return 0;
            }
            public boolean isVisible() {
                return LevelManager.getLevel().getVisibilitySettings().isShowGraphics();
            }
        });

        String geometry = "Geometry<static,mass,material,tag,break,rotspeed,contacts,nogeomcollisions>";
        String image = "?Image<image,imagepos,imagerot,imagescale>";
        setMetaAttributes(MetaEditorAttribute.parse("id,x,y,radius," + geometry + image));

    }


    @Override
    public String getName() {
        return getAttribute("id").stringValue();
    }

}
