package models.mapEditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;

public abstract class MapEditorActionController {


    public abstract void reset();
    public abstract void updateGraphics();
    public abstract void updateDatabase();
    public abstract void clearSprites();
}
