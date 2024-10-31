package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.TextBox;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
public class MenuSaveAs extends Menu{
    private final Button cancel;
    private final Button save;
    private final TextBox name;
    private final Game game;
    public MenuSaveAs(Game game){
        var layout = setLayout(new ConstrainedLayout());
        cancel = add(new Button("Cancel", true));
        cancel.setSize(400, 40);
        save = add(new Button("Save", false));
        save.setSize(400, 40);
        name = add(new TextBox(game.name==null?"":game.name));
        name.setSize(400, 40);
        layout.constrain(cancel, new PositionAnchorConstraint(.5f, 0, .5f, 1, -200, -80));
        layout.constrain(save, new PositionAnchorConstraint(.5f, 0, .5f, 1, -200, -160));
        layout.constrain(name, new PositionAnchorConstraint(.5f, 0, .5f, 1, -200, 120));
        cancel.addAction(() -> {
            ((Menu)parent).open();
        });
        save.addAction(() -> {
            game.name = name.getText();
            game.save();
            ((Menu)parent).open();
        });
        this.game = game;
    }
    @Override
    public void draw(double deltaTime){
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, Game.theme.getBackgroundTexture(1));
        save.enabled = !name.getText().isEmpty();
        cancel.x = DizzyEngine.screenSize.x/2-200;
        cancel.y = DizzyEngine.screenSize.y-80;
        save.x = cancel.x;
        save.y = cancel.y-80;
        name.x = save.x;
        name.y = save.y-240;
    }
}
