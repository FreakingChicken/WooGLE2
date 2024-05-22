package com.WooGLEFX.Functions;

import com.WooGLEFX.Engine.ClipboardHandler;
import com.WooGLEFX.Engine.FX.FXHierarchy;
import com.WooGLEFX.Engine.FX.FXPropertiesView;
import com.WooGLEFX.Engine.SelectionManager;
import com.WooGLEFX.EditorObjects.ObjectCreators.ObjectAdder;
import com.WooGLEFX.Functions.UndoHandling.UndoManager;
import com.WooGLEFX.EditorObjects.EditorObject;
import com.WooGLEFX.Functions.UndoHandling.UserActions.ObjectCreationAction;
import com.WooGLEFX.Structures.WorldLevel;
import com.WorldOfGoo.Level.BallInstance;
import com.WorldOfGoo.Level.Strand;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardManager {


    public static void cut() {
        if (LevelManager.getLevel().getSelected() != null) {
            copy();
            ObjectManager.delete(LevelManager.getLevel());
        }
    }

    public static void copy() {
        if (LevelManager.getLevel().getSelected() != null && FXPropertiesView.getPropertiesView().getEditingCell() == null) {
            String clipboard = ClipboardHandler.exportToClipBoardString(LevelManager.getLevel().getSelected());
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(clipboard);
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        }
    }

    public static void paste() {

        WorldLevel level = LevelManager.getLevel();

        if (FXPropertiesView.getPropertiesView().getEditingCell() == null) {
            String clipboard = Clipboard.getSystemClipboard().getString();
            if (clipboard != null) {
                EditorObject object = ClipboardHandler.importFromClipboardString(clipboard);
                if (object != null) {

                    boolean okayToBeChild = level.getSelected() != null && level.getSelected().getParent() != null;

                    if (okayToBeChild) {
                        okayToBeChild = false;
                        for (String possibleChild : level.getSelected().getParent().getPossibleChildren()) {
                            if (possibleChild.equals(object.getType())) {
                                okayToBeChild = true;
                                break;
                            }
                        }
                    }

                    if (okayToBeChild) {
                        object.setParent(level.getSelected().getParent());
                    } else {
                        switch (object.getClass().getPackage().getName()) {
                            case "com.WorldOfGoo.Scene" -> object.setParent(level.getSceneObject());
                            case "com.WorldOfGoo.Level" -> object.setParent(level.getLevelObject());
                            case "com.WorldOfGoo.Resrc" -> object.setParent(level.getResourcesObject());
                        }
                    }
                    if (object instanceof BallInstance) {
                        ObjectAdder.fixGooball(object);
                        for (EditorObject editorObject : level.getLevel()) {
                            if (editorObject instanceof Strand strand) {
                                strand.update();
                            }
                        }
                    }
                    // object.getParent().getChildren().add(0, object);
                    ObjectManager.create(level, object, 0);
                    SelectionManager.setSelected(object);
                    UndoManager.registerChange(new ObjectCreationAction(object, FXHierarchy.getHierarchy().getRow(object.getTreeItem())
                            - FXHierarchy.getHierarchy().getRow(object.getParent().getTreeItem()) - 1));
                    level.redoActions.clear();
                    FXHierarchy.getHierarchy().refresh();
                }
            }
        }
    }

}
