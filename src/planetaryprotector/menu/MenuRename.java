package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.TextBox;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
import java.io.File;
public class MenuRename extends Menu{
    private final TextBox name;
    private final Button back;
    private final Button rename;
    public MenuRename(String save){
        ConstrainedLayout layout = setLayout(new ConstrainedLayout());
        back = add(new Button("Back", true));
        back.setSize(400, 40);
        rename = add(new Button("Rename", false));
        rename.setSize(400, 40);
        name = add(new TextBox());
        name.setSize(400, 40);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, -200, -80));
        layout.constrain(rename, new PositionAnchorConstraint(.5f, 0, .5f, 1, -200, -160));
        layout.constrain(name, new PositionAnchorConstraint(.5f, 0, .5f, 0, -200, 120));
        back.addAction(() -> {
            new MenuMain(false).open();

        });
        rename.addAction(() -> {
            File From = new File("saves/"+save+".dat");
            File To = new File("saves/"+name.getText()+".dat");
            From.renameTo(To);
            new MenuMain(false).open();
        });
    }
    @Override
    public void draw(double deltaTime){
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, Game.theme.getBackgroundTexture(1));
        File file = new File("saves/"+name.getText());
        if(!file.exists()){
            rename.enabled = true;
        }else{
            rename.enabled = false;
        }
        back.x = DizzyEngine.screenSize.x/2-200;
        back.y = DizzyEngine.screenSize.y-80;
        rename.x = back.x;
        rename.y = back.y-80;
        name.x = rename.x;
        name.y = rename.y-240;
    }
}
