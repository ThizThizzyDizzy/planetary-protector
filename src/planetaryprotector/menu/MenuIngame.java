package planetaryprotector.menu;
import planetaryprotector.Controls;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuIngame extends MenuComponent{
    private final MenuComponentButton restart;
    private final MenuComponentButton controls;
    private final MenuComponentButton back;
    private final MenuComponentButton save;
    private final MenuComponentButton saveAs;
    private final MenuComponentButton exit;
    private final MenuComponentButton exitSave;
    private final MenuComponentButton exitNosave;
    private final MenuGame game;
    public MenuIngame(MenuGame game){
        super(0, 0, Display.getWidth(), Display.getHeight());
        restart = add(new MenuComponentButton(Display.getWidth()/2-400, 80, 800, 80, "Restart game", true));
        controls = add(new MenuComponentButton(Display.getWidth()/2-400, 240, 800, 80, "Controls", true));
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-400, 800, 80, "Back to game", true));
        saveAs = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-320, 800, 80, "Save As...", true));
        save = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-240, 800, 80, "Save Game", true));
        exit = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Exit to menu", true));
        exitSave = add(new MenuComponentButton(Display.getWidth()/2-600, Display.getHeight()-80, 600, 80, "Save and Exit", false));
        exitNosave = add(new MenuComponentButton(Display.getWidth()/2, Display.getHeight()-80, 600, 80, "Don't save", false));
        this.game = game;
    }
    @Override
    public void render(){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(x, y, width, height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        for(MenuComponent component : components){
            component.render();
        }
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Controls.menu&&pressed&&!repeat){
            buttonClicked(back);
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==back){
            game.componentsToRemove.add(this);
            game.overlay = null;
            game.paused = false;
        }
        if(button==save){
            game.save();
        }
        if(button==saveAs){
            gui.open(new MenuSaveAs(gui, game));
        }
        if(button==controls){
            game.components.remove(this);
            game.overlay = game.add(new MenuControls(game));
        }
        if(button==exit){
            exit.enabled = false;
            exitSave.enabled = true;
            exitNosave.enabled = true;
        }
        if(button==exitSave){
            game.save();
            gui.open(new MenuMain(gui, null));
        }
        if(button==exitNosave){
            gui.open(new MenuMain(gui, null));
        }
        if(button==restart){
            gui.open(new MenuLoad(gui, parent));
        }
    }
}