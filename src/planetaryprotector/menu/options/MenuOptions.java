package planetaryprotector.menu.options;
import planetaryprotector.Core;
import org.lwjgl.opengl.Display;
import planetaryprotector.menu.MenuMain;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuOptions extends Menu{
    private final MenuComponentButton back, music, gameplay;
    public MenuOptions(GUI gui){
        super(gui, null);
        music = add(new MenuComponentButton(Display.getWidth()/2-200, 200, 400, 40, "Music", true, true));
        gameplay = add(new MenuComponentButton(Display.getWidth()/2-200, 120, 400, 40, "Graphics", true, true));
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(new MenuMain(gui, false));
            Core.saveOptions();
        }
        if(button==music){
            gui.open(new MenuOptions1(gui, this));
        }
        if(button==gameplay){
            gui.open(new MenuOptionsGraphics(gui, this));
        }
    }
}