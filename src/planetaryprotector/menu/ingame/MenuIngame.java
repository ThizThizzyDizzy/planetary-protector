package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.Controls;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuMain;
import planetaryprotector.menu.MenuSaveAs;
public class MenuIngame extends MenuComponentOverlay{
    private final Button controls;
    private final Button back;
    private final Button save;
    private final Button saveAs;
    private final Button exit;
    private final Button exitSave;
    private final Button exitNosave;
    public MenuIngame(MenuGame menu){
        super(menu);
        var layout = setLayout(new ConstrainedLayout());
        controls = add(new Button("Controls", true));
        controls.addAction(() -> open(new MenuControls(menu)));
        controls.setSize(800, 80);
        layout.constrain(controls, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 240));
        back = add(new Button("Back to game", true));
        back.addAction(() -> close());
        back.setSize(800, 80);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 80));
        saveAs = add(new Button("Save As...", true));
        saveAs.addAction(() -> new MenuSaveAs(menu.game).open());
        saveAs.setSize(800, 80);
        layout.constrain(saveAs, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -320));
        save = add(new Button("Save Game", true));
        save.addAction(() -> menu.game.save());
        save.setSize(800, 80);
        layout.constrain(save, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -240));
        exit = add(new Button("Exit to menu", true));
        exit.setSize(800, 80);
        layout.constrain(exit, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -160));
        exitSave = add(new Button("Save and Exit", false));
        exitSave.addAction(() -> {
            menu.game.save();
            new MenuMain(true).open();
        });
        exitSave.setSize(600, 80);
        layout.constrain(exitSave, new PositionAnchorConstraint(1, 0, .5f, 1, 0, -80));
        exitNosave = add(new Button("Don't save", false));
        exitNosave.addAction(() -> new MenuMain(true).open());
        exitNosave.setSize(600, 80);
        layout.constrain(exitNosave, new PositionAnchorConstraint(0, 0, .5f, 1, 0, -80));

        exit.addAction(() -> {
            exit.enabled = false;
            exitSave.enabled = true;
            exitNosave.enabled = true;
        });
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        super.onKey(id, key, scancode, action, mods);
        if(key==Controls.menu&&action==GLFW.GLFW_PRESS){
            close();
        }
    }
}
