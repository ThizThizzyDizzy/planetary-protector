package planetaryprotector2.menu;
import planetaryprotector.Controls;
import planetaryprotector.menu.MenuMain;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.Menu;
public class MenuIngame extends Menu{
    private final MenuComponentButton restart;
    private final MenuComponentButton controls;
    private final MenuComponentButton back;
    private final MenuComponentButton save;
    private final MenuComponentButton saveAs;
    private final MenuComponentButton exit;
    private final MenuComponentButton exitSave;
    private final MenuComponentButton exitNosave;
    private final MenuGame game;
    public MenuIngame(GUI gui, MenuGame game){
        super(gui, game);
        restart = add(new MenuComponentButton(Display.getWidth()/2-400, 80, 800, 80, "Restart game", true));
        controls = add(new MenuComponentButton(Display.getWidth()/2-400, 240, 800, 80, "Controls", true));
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-420, 800, 80, "Back to game", true));
        saveAs = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-320, 800, 80, "Save As...", true));
        save = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-240, 800, 80, "Save Game", true));
        exit = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Exit to menu", true));
        exitSave = add(new MenuComponentButton(Display.getWidth()/2-600, Display.getHeight()-80, 600, 80, "Save and Exit", false));
        exitNosave = add(new MenuComponentButton(Display.getWidth()/2, Display.getHeight()-80, 600, 80, "Don't save", false));
        this.game = game;
    }
    @Override
    public void renderBackground(){}
    @Override
    public void render(int millisSinceLastTick){
        game.render(0);
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        super.render(millisSinceLastTick);
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
            gui.open(game);
        }
        if(button==save){
            game.save();
        }
        if(button==saveAs){
            gui.open(new MenuSaveAs(gui, this, game));
        }
        if(button==controls){
            gui.open(new MenuControls(gui, this, game));
        }
        if(button==exit){
            exit.enabled = false;
            exitSave.enabled = true;
            exitNosave.enabled = true;
        }
        if(button==exitSave){
            game.save();
            gui.open(new MenuMain(gui));
        }
        if(button==exitNosave){
            gui.open(new MenuMain(gui));
        }
        if(button==restart){
            gui.open(new MenuGame(gui));
        }
    }
}