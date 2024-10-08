package com.woogleFX.engine.undoHandling.userActions;

import com.woogleFX.editorObjects.EditorObject;
import com.woogleFX.engine.LevelManager;
import com.woogleFX.editorObjects.ObjectManager;
import com.woogleFX.engine.SelectionManager;

public class ObjectDestructionAction extends UserAction {

    private final int position;
    public ObjectDestructionAction(EditorObject object, int position) {
        super(object);
        this.position = position;
    }


    @Override
    public UserAction getInverse() {
        return new ObjectCreationAction(getObject(), position);
    }


    @Override
    public void execute() {
        ObjectManager.deleteItem(LevelManager.getLevel(), getObject(), false);
        SelectionManager.selectionMode();
    }
    
    
    public int compareTo(ObjectDestructionAction other) {
        return position - other.position;
    }

}
