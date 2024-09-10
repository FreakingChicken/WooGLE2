package com.worldOfGoo2.util;

import com.woogleFX.editorObjects.EditorObject;
import com.woogleFX.editorObjects.objectComponents.ImageComponent;
import com.woogleFX.editorObjects.objectComponents.ObjectComponent;
import com.woogleFX.file.resourceManagers.ResourceManager;
import com.woogleFX.gameData.animation.SimpleBinAnimation;
import com.woogleFX.gameData.level.GameVersion;
import com.worldOfGoo2.level._2_Level_BallInstance;
import com.worldOfGoo2.level._2_Level_Item;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;

public class BinAnimationHelper {

    public static void addBinAnimationAsObjectPositions(EditorObject editorObject, SimpleBinAnimation binAnimation, String state) {

        ArrayList<ObjectComponent> objectComponents = new ArrayList<>();

        int i1 = 0;
        for (SimpleBinAnimation.SimpleBinAnimationState simpleBinAnimationState : binAnimation.states) {
            StringBuilder stringBuilder = new StringBuilder();
            int byteIndex = binAnimation.stringDefinitions[binAnimation.stateAliasStringTableIndices[i1]].stringTableIndex;
            while (binAnimation.stringTable[byteIndex] != 0x00) {
                stringBuilder.append((char)binAnimation.stringTable[byteIndex]);
                byteIndex++;
            }
            if (stringBuilder.toString().equals(state) || state.equals("")) {
                SimpleBinAnimation.SimpleBinAnimationGroup group = binAnimation.groups[simpleBinAnimationState.groupOffset];
                addBinAnimationGroupAsObjectPositions(objectComponents, editorObject, binAnimation, group, 0, 0, 1, 1, 0);
                break;
            }
            i1++;
        }

        for (int i = objectComponents.size() - 1; i >= 0; i--) {
            editorObject.addObjectComponent(objectComponents.get(i));
        }

    }


    public static void addBinAnimationGroupAsObjectPositions(ArrayList<ObjectComponent> objectComponents, EditorObject editorObject, SimpleBinAnimation binAnimation, SimpleBinAnimation.SimpleBinAnimationGroup animationGroup, double x, double y, double scaleX, double scaleY, double rotation) {

        for (int i = 0; i < animationGroup.sectionLength; i++) {
            SimpleBinAnimation.SimpleBinAnimationSection section = binAnimation.sections[i + animationGroup.sectionOffset];
            for (int j = 0; j < section.elementLength; j++) {
                SimpleBinAnimation.SimpleBinAnimationElement element = binAnimation.elements[j + section.elementOffset];
                if (element.frame != 0) continue;
                switch (element.type) {
                    case 1 -> {
                        SimpleBinAnimation.SimpleBinAnimationKeyframe keyframe = binAnimation.keyframes[element.offset];
                        SimpleBinAnimation.SimpleBinAnimationGroup group = binAnimation.groups[keyframe.groupOffset];
                        double dx = (keyframe.offsetX - keyframe.centerX) * scaleX;
                        double dy = (keyframe.centerY - keyframe.offsetY) * scaleY;
                        addBinAnimationGroupAsObjectPositions(objectComponents, editorObject, binAnimation, group, x + dx * Math.cos(rotation) - dy * Math.sin(rotation), y - dx * Math.sin(rotation) - dy * Math.cos(rotation), scaleX * keyframe.scaleX, scaleY * keyframe.scaleY, rotation - keyframe.angleBottomRight);
                    }
                    case 2 -> {
                        SimpleBinAnimation.SimpleBinAnimationPart part = binAnimation.parts[element.offset];
                        StringBuilder stringBuilder = new StringBuilder();
                        int byteIndex = binAnimation.stringDefinitions[binAnimation.imageStringTableIndices[part.imageIndex]].stringTableIndex;
                        while (binAnimation.stringTable[byteIndex] != 0x00) {
                            stringBuilder.append((char)binAnimation.stringTable[byteIndex]);
                            byteIndex++;
                        }
                        try {
                            Image image = ResourceManager.getImage((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getResources() : null), stringBuilder.toString(), GameVersion.VERSION_WOG2);




                            objectComponents.add(new ImageComponent() {
                                @Override
                                public Image getImage() {
                                    return image;
                                }

                                @Override
                                public double getX() {

                                    double itemScaleX;
                                    double itemScaleY;
                                    if (editorObject instanceof _2_Level_Item item) {
                                        itemScaleX = item.getChildren("scale").get(0).getAttribute("x").doubleValue();
                                        itemScaleY = item.getChildren("scale").get(0).getAttribute("y").doubleValue();
                                    } else {
                                        itemScaleX = 1;
                                        itemScaleY = 1;
                                    }

                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;

                                    double initialAddX = x * scaleFactor * itemScaleX;
                                    double initialAddY = y * scaleFactor * itemScaleY;

                                    double imageAddX = image.getWidth() * scaleX * itemScaleX * part.scaleX * scaleFactor / 2;
                                    double imageAddY = image.getHeight() * scaleY * itemScaleY * part.scaleY * scaleFactor / 2;

                                    double addX = (part.offsetX - part.centerX * part.scaleX) * scaleX * itemScaleX * scaleFactor + initialAddX + imageAddX;
                                    double addY = (part.offsetY - part.centerY * part.scaleY) * scaleY * itemScaleY * scaleFactor + initialAddY + imageAddY;

                                    double ballX = editorObject.getChildren("pos").get(0).getAttribute("x").doubleValue();
                                    double rotation = (editorObject instanceof _2_Level_BallInstance) ?
                                            editorObject.getAttribute("angle").doubleValue() :
                                            editorObject.getAttribute("rotation").doubleValue();
                                    return ballX + addX * Math.cos(-rotation) - addY * Math.sin(-rotation);
                                }

                                @Override
                                public void setX(double _x) {

                                    double itemScaleX;
                                    double itemScaleY;
                                    if (editorObject instanceof _2_Level_Item item) {
                                        itemScaleX = item.getChildren("scale").get(0).getAttribute("x").doubleValue();
                                        itemScaleY = item.getChildren("scale").get(0).getAttribute("y").doubleValue();
                                    } else {
                                        itemScaleX = 1;
                                        itemScaleY = 1;
                                    }

                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;

                                    double initialAddX = x * scaleFactor * itemScaleX;
                                    double initialAddY = y * scaleFactor * itemScaleY;

                                    double imageAddX = image.getWidth() * scaleX * itemScaleX * part.scaleX * scaleFactor / 2;
                                    double imageAddY = image.getHeight() * scaleY * itemScaleY * part.scaleY * scaleFactor / 2;

                                    double addX = (part.offsetX - part.centerX * part.scaleX) * scaleX * itemScaleX * scaleFactor + initialAddX + imageAddX;
                                    double addY = (part.offsetY - part.centerY * part.scaleY) * scaleY * itemScaleY * scaleFactor + initialAddY + imageAddY;

                                    double rotation = (editorObject instanceof _2_Level_BallInstance) ?
                                            editorObject.getAttribute("angle").doubleValue() :
                                            editorObject.getAttribute("rotation").doubleValue();
                                    editorObject.getChildren("pos").get(0).setAttribute("x", _x - (addX * Math.cos(-rotation) - addY * Math.sin(-rotation)));
                                }

                                @Override
                                public double getY() {

                                    double itemScaleX;
                                    double itemScaleY;
                                    if (editorObject instanceof _2_Level_Item item) {
                                        itemScaleX = item.getChildren("scale").get(0).getAttribute("x").doubleValue();
                                        itemScaleY = item.getChildren("scale").get(0).getAttribute("y").doubleValue();
                                    } else {
                                        itemScaleX = 1;
                                        itemScaleY = 1;
                                    }

                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;

                                    double initialAddX = x * scaleFactor * itemScaleX;
                                    double initialAddY = y * scaleFactor * itemScaleY;

                                    double imageAddX = image.getWidth() * scaleX * itemScaleX * part.scaleX * scaleFactor / 2;
                                    double imageAddY = image.getHeight() * scaleY * itemScaleY * part.scaleY * scaleFactor / 2;

                                    double addX = (part.offsetX - part.centerX * part.scaleX) * scaleX * itemScaleX * scaleFactor + initialAddX + imageAddX;
                                    double addY = (part.offsetY - part.centerY * part.scaleY) * scaleY * itemScaleY * scaleFactor + initialAddY + imageAddY;

                                    double ballY = -editorObject.getChildren("pos").get(0).getAttribute("y").doubleValue();
                                    double rotation = (editorObject instanceof _2_Level_BallInstance) ?
                                            editorObject.getAttribute("angle").doubleValue() :
                                            editorObject.getAttribute("rotation").doubleValue();
                                    return ballY + addX * Math.sin(-rotation) + addY * Math.cos(-rotation);
                                }

                                @Override
                                public void setY(double _y) {

                                    double itemScaleX;
                                    double itemScaleY;
                                    if (editorObject instanceof _2_Level_Item item) {
                                        itemScaleX = item.getChildren("scale").get(0).getAttribute("x").doubleValue();
                                        itemScaleY = item.getChildren("scale").get(0).getAttribute("y").doubleValue();
                                    } else {
                                        itemScaleX = 1;
                                        itemScaleY = 1;
                                    }

                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;

                                    double initialAddX = x * scaleFactor * itemScaleX;
                                    double initialAddY = y * scaleFactor * itemScaleY;

                                    double imageAddX = image.getWidth() * scaleX * itemScaleX * part.scaleX * scaleFactor / 2;
                                    double imageAddY = image.getHeight() * scaleY * itemScaleY * part.scaleY * scaleFactor / 2;

                                    double addX = (part.offsetX - part.centerX * part.scaleX) * scaleX * itemScaleX * scaleFactor + initialAddX + imageAddX;
                                    double addY = (part.offsetY - part.centerY * part.scaleY) * scaleY * itemScaleY * scaleFactor + initialAddY + imageAddY;

                                    double rotation = (editorObject instanceof _2_Level_BallInstance) ?
                                            editorObject.getAttribute("angle").doubleValue() :
                                            editorObject.getAttribute("rotation").doubleValue();
                                    editorObject.getChildren("pos").get(0).setAttribute("y", -_y + (addX * Math.sin(-rotation) + addY * Math.cos(-rotation)));
                                }

                                @Override
                                public double getScaleX() {
                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;
                                    double itemScaleX = (editorObject instanceof _2_Level_Item) ? editorObject.getChildren("scale").get(0).getAttribute("x").doubleValue() : 1;
                                    return scaleX * part.scaleX * scaleFactor * itemScaleX;
                                }

                                @Override
                                public double getScaleY() {
                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;
                                    double itemScaleY = (editorObject instanceof _2_Level_Item) ? editorObject.getChildren("scale").get(0).getAttribute("y").doubleValue() : 1;
                                    return scaleY * part.scaleY * scaleFactor * itemScaleY;
                                }

                                @Override
                                public void setScaleX(double _scaleX) {
                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;
                                    if (editorObject instanceof _2_Level_Item) editorObject.getChildren("scale").get(0).setAttribute("x", _scaleX / scaleX / part.scaleX / scaleFactor);
                                }

                                @Override
                                public void setScaleY(double _scaleY) {
                                    double scaleFactor = ((editorObject instanceof _2_Level_BallInstance ballInstance ? ballInstance.getBall().getObjects().get(0).getChildren("ballParts").get(0).getAttribute("scale").doubleValue() : 1)) / 100;
                                    if (editorObject instanceof _2_Level_Item) editorObject.getChildren("scale").get(0).setAttribute("y", _scaleY / scaleY / part.scaleY / scaleFactor);
                                }

                                @Override
                                public double getRotation() {
                                    if (editorObject instanceof _2_Level_BallInstance) return rotation - editorObject.getAttribute("angle").doubleValue();
                                    else return rotation - editorObject.getAttribute("rotation").doubleValue();
                                }

                                @Override
                                public void setRotation(double _rotation) {
                                    if (editorObject instanceof _2_Level_BallInstance) editorObject.setAttribute("angle", -_rotation - rotation);
                                    else editorObject.setAttribute("rotation", -_rotation - rotation);
                                }

                                @Override
                                public double getDepth() {
                                    return (editorObject instanceof _2_Level_BallInstance) ? 0.000001 : editorObject.getAttribute("depth").doubleValue();
                                }

                                @Override
                                public boolean isResizable() {
                                    return editorObject instanceof _2_Level_Item;
                                }

                            });

                        } catch (IOException ignored) {
                            ignored.printStackTrace();
                        }

                    }
                }
            }
        }

    }

}
