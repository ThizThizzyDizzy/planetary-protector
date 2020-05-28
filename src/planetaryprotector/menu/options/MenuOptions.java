package planetaryprotector.menu.options;
import planetaryprotector.Core;
import org.lwjgl.opengl.Display;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuMain;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptions extends Menu{
    public static boolean autosave = true;
    private final MenuComponentButton back, graphics, discord;
    private final MenuComponentOptionButton autosaveButton;
    public MenuOptions(GUI gui){
        super(gui, null);
        graphics = add(new MenuComponentButton(Display.getWidth()/2-200, 120, 400, 40, "Graphics", true, true));
        discord = add(new MenuComponentButton(Display.getWidth()/2-200, 200, 400, 40, "Discord", true, true));
        autosaveButton = add(new MenuComponentOptionButton(Display.getWidth()/2-200, 280, 400, 40, "Autosave", true, true, autosave?0:1, "On", "Off"));
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(new MenuMain(gui, false));
            Core.saveOptions();
        }
        if(button==graphics){
            gui.open(new MenuOptionsGraphics(gui, this));
        }
        if(button==discord){
            gui.open(new MenuOptionsDiscord(gui, this));
        }
    }
    @Override
    public void renderBackground(){
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        autosave = autosaveButton.getIndex()==0;
    }
}