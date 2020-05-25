package planetaryprotector.menu.ingame;
import planetaryprotector.Controls;
import org.lwjgl.opengl.Display;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuMain;
import planetaryprotector.menu.MenuSaveAs;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuIngame extends MenuComponentOverlay{
    private final MenuComponentButton controls;
    private final MenuComponentButton back;
    private final MenuComponentButton save;
    private final MenuComponentButton saveAs;
    private final MenuComponentButton exit;
    private final MenuComponentButton exitSave;
    private final MenuComponentButton exitNosave;
    public MenuIngame(MenuGame menu){
        super(menu);
        controls = add(new MenuComponentButton(Display.getWidth()/2-400, 240, 800, 80, "Controls", true));
        back = add(new MenuComponentButton(Display.getWidth()/2-400, 80, 800, 80, "Back to game", true));
        saveAs = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-320, 800, 80, "Save As...", true));
        save = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-240, 800, 80, "Save Game", true));
        exit = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Exit to menu", true));
        exitSave = add(new MenuComponentButton(Display.getWidth()/2-600, Display.getHeight()-80, 600, 80, "Save and Exit", false));
        exitNosave = add(new MenuComponentButton(Display.getWidth()/2, Display.getHeight()-80, 600, 80, "Don't save", false));
    }
    @Override
    public void render(){}
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Controls.menu&&pressed&&!repeat){
            close();
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==back){
            close();
        }
        if(button==save){
            menu.game.save();
        }
        if(button==saveAs){
            gui.open(new MenuSaveAs(gui, menu.game));
        }
        if(button==controls){
            open(new MenuControls(menu));
        }
        if(button==exit){
            exit.enabled = false;
            exitSave.enabled = true;
            exitNosave.enabled = true;
        }
        if(button==exitSave){
            menu.game.save();
            gui.open(new MenuMain(gui, true));
        }
        if(button==exitNosave){
            gui.open(new MenuMain(gui, true));
        }
    }
}