package com.woogleFX.engine.gui;

import com.woogleFX.editorObjects.EditorObject;
import com.woogleFX.engine.renderer.Renderer;
import com.woogleFX.gameData.level.WOG1Level;
import com.woogleFX.gameData.particle.ParticleUtility;
import com.woogleFX.gameData.animation.AnimationManager;
import com.woogleFX.engine.LevelManager;
import com.worldOfGoo.level.Fire;
import com.worldOfGoo.scene.Particles;
import javafx.animation.AnimationTimer;

public class EditorWindow extends AnimationTimer {

    private static float timeElapsed = 0;
    public static float getTimeElapsed() {
        return timeElapsed;
    }


    private static long timeStarted = -1;


    private static long currentTime = -1;


    @Override
    public void handle(long now) {

        double timeStep = (now - currentTime) / 1000000000d;
        currentTime = now;

        if (timeStarted == -1) timeStarted = now;
        timeElapsed = (now - timeStarted) / 1000000000f;

        AnimationManager.updateAnimations(timeElapsed);

        if (LevelManager.getLevel() != null) {
            if (LevelManager.getLevel() instanceof WOG1Level level) {
                for (EditorObject EditorObject : level.getScene()) {
                    if (EditorObject instanceof Particles particles) {
                        ParticleUtility.frameUpdate(particles, particles.getCounts(), particles.getDrawing(), timeStep);
                    }
                }
                for (EditorObject EditorObject : level.getLevel()) {
                    if (EditorObject instanceof Fire fire) {
                        ParticleUtility.frameUpdate(fire, fire.getCounts(), fire.getDrawing(), timeStep);
                    }
                }
            }
        }

        Renderer.draw();

    }

}