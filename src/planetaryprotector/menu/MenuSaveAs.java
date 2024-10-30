package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
public class MenuSaveAs extends Menu{
    private final MenuComponentButton cancel;
    private final MenuComponentButton save;
    private final MenuComponentTextBox name;
    private final Game game;
    public MenuSaveAs(GUI gui, MenuGame game){
        super(gui, game);
        cancel = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-200, DizzyEngine.screenSize.y-80, 400, 40, "Cancel", true));
        save = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-200, DizzyEngine.screenSize.y-160, 400, 40, "Save", false));
        name = add(new MenuComponentTextBox(DizzyEngine.screenSize.x/2-200, 120, 400, 40, game.game.name==null?"":game.game.name, true));
        this.game = game.game;
    }
    @Override
    public void renderBackground(){
        Renderer.fillRect(0,0,DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, Game.theme.getBackgroundTexture(1));
        save.enabled = !name.text.isEmpty();
        cancel.x = DizzyEngine.screenSize.x/2-200;
        cancel.y = DizzyEngine.screenSize.y-80;
        save.x = cancel.x;
        save.y = cancel.y-80;
        name.x = save.x;
        name.y = save.y-240;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==cancel){
            gui.open(parent);
        }
        if(button==save){
            game.name = name.text;
            game.save();
            gui.open(parent);
        }
    }
}