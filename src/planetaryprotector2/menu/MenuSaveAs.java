package planetaryprotector2.menu;
import planetaryprotector.Core;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
public class MenuSaveAs extends Menu{
    private final MenuComponentButton cancel, save;
    private final MenuComponentTextBox name;
    private final MenuGame game;
    public MenuSaveAs(GUI gui, Menu parent, MenuGame game){
        super(gui, parent);
        cancel = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Cancel", true));
        save = add(new MenuComponentButton(cancel.x, Display.getHeight()-160, 400, 40, "Save", false));
        name = add(new MenuComponentTextBox(cancel.x, 120, 400, 40, Core.save, true));
        this.game = game;
    }
    @Override
    public void renderBackground(){
        save.enabled = !name.text.isEmpty();
        cancel.x = Display.getWidth()/2-200;
        cancel.y = Display.getHeight()-80;
        name.x = save.x = cancel.x;
        save.y = cancel.y-80;
        name.y = save.y-240;
    }
    @Override
    public void render(int millisSinceLastTick){
        game.render(0);
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        super.render(millisSinceLastTick);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==cancel){
            gui.open(parent);
        }
        if(button==save){
            Core.save = name.text;
            game.save();
            gui.open(parent);
        }
    }
}