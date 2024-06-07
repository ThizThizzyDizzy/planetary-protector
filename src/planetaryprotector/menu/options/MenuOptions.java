package planetaryprotector.menu.options;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.OptionButton;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.Main;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuMain;
public class MenuOptions extends Menu{
    public MenuOptions(){
        var layout = setLayout(new ConstrainedLayout());
        var graphics = add(new Button("Graphics"));
        graphics.setSize(400, 40);
        graphics.addAction(() -> new MenuOptionsGraphics().open());
        layout.constrain(graphics, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 120));
        var discord = add(new Button("Discord"));
        discord.setSize(400, 40);
        discord.addAction(() -> new MenuOptionsDiscord().open());
        layout.constrain(discord, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 200));
        var autosaveButton = add(new OptionButton("Autosave", Options.options.autosave?0:1, "On", "Off"));
        autosaveButton.setSize(400, 40);
        autosaveButton.addAction(() -> Options.options.autosave = autosaveButton.getIndex()==0);
        layout.constrain(autosaveButton, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 280));
        var back = add(new Button("Back"));
        back.setSize(400, 40);
        back.addAction(() -> {
            Main.saveOptions();
            new MenuMain(false).open();
        });
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -80));
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(1));
    }
}