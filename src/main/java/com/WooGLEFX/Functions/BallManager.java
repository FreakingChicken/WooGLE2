package com.WooGLEFX.Functions;

import com.WooGLEFX.EditorObjects._Ball;
import com.WooGLEFX.File.FileManager;
import com.WooGLEFX.File.LevelExporter;
import com.WooGLEFX.GUI.Alarms;
import com.WooGLEFX.GUI.BallSelector;
import com.WooGLEFX.Structures.GameVersion;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BallManager {

    private static final ArrayList<_Ball> importedBalls = new ArrayList<>();
    public static ArrayList<_Ball> getImportedBalls() {
        return importedBalls;
    }



    public static void saveBallInVersion(GameVersion oldVersion, GameVersion newVersion) {
        new BallSelector(oldVersion, newVersion).start(new Stage());
    }

    public static void saveBallInVersion(String ball, GameVersion oldVersion, GameVersion newVersion) {

        try {

            _Ball _ball = FileManager.openBall(ball, oldVersion);
            if (_ball == null) return;

            String dir = newVersion == GameVersion.OLD ? FileManager.getOldWOGdir() : FileManager.getNewWOGdir();

            LevelExporter.exportBallAsXML(_ball, dir + "\\res\\balls\\" + ball, newVersion, false);

        } catch (Exception e) {
            Alarms.errorMessage(e);
        }

    }

}
