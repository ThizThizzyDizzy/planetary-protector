package planetaryprotector.menu.options;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.OptionButton;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
public class MenuOptionsDiscord extends Menu{
    public MenuOptionsDiscord(){
        var layout = setLayout(new ConstrainedLayout());
        var back = add(new Button("Back"));
        back.setSize(400, 40);
        back.addAction(() -> {
            new MenuOptions().open();
            //TODO initialize discord presence?
        });
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -80));
        var richPresence = add(new OptionButton("Rich Presence", Options.options.richPresence?0:1, "On", "Off"));
        richPresence.setSize(400, 40);
        richPresence.addAction(() -> Options.options.richPresence = richPresence.getIndex()==0);
        layout.constrain(richPresence, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 120));
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(1));
    }
}