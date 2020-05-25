package planetaryprotector.menu;
import planetaryprotector.game.Game;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
public class MenuSaveAs extends Menu{
    private final MenuComponentButton cancel;
    private final MenuComponentButton save;
    private final MenuComponentTextBox name;
    private final Game game;
    public MenuSaveAs(GUI gui, Game game){
        super(gui, game);
        cancel = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Cancel", true));
        save = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-160, 400, 40, "Save", false));
        name = add(new MenuComponentTextBox(Display.getWidth()/2-200, 120, 400, 40, game.name==null?"":game.name, true));
        this.game = game;
    }
    @Override
    public void renderBackground(){
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        save.enabled = !name.text.isEmpty();
        cancel.x = Display.getWidth()/2-200;
        cancel.y = Display.getHeight()-80;
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