package com.woogleFX.gameData.animation;

import com.woogleFX.editorObjects.EditorObject;
import com.woogleFX.engine.LevelManager;
import com.woogleFX.gameData.level.WOG1Level;
import com.worldOfGoo.scene.SceneLayer;

import java.util.ArrayList;

public class AnimationManager {

    private static final ArrayList<WoGAnimation> animations = new ArrayList<>();
    public static ArrayList<WoGAnimation> getAnimations() {
        return animations;
    }


    private static final ArrayList<SimpleBinAnimation> binAnimations = new ArrayList<>();
    public static ArrayList<SimpleBinAnimation> getBinAnimations() {
        return binAnimations;
    }


    public static void updateAnimations(float timeElapsed) {
        if (LevelManager.getLevel() != null && LevelManager.getLevel() instanceof WOG1Level) {
            for (EditorObject object : ((WOG1Level)LevelManager.getLevel()).getScene()) {
                if (object instanceof SceneLayer sceneLayer) {
                    String anim = object.getAttribute("anim").stringValue();
                    if (!anim.isEmpty()) {
                        for (WoGAnimation animation : animations) {
                            if (animation.getName().equals(anim + ".anim.binuni")
                                    || animation.getName().equals(anim + ".anim.binltl")) {
                                sceneLayer.updateWithAnimation(animation, timeElapsed);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean hasAnimation(String potential) {
        for (WoGAnimation animation : animations) {
            if (animation.getName().equals(potential + ".anim.binuni")
                    || animation.getName().equals(potential + ".anim.binltl")) {
                return true;
            }
        }
        return false;
    }

}
