package planetaryprotector.menu.options;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import planetaryprotector.Core;
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
    public MenuOptions(){
        super(gui, null);
        graphics = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, 120, 400, 40, "Graphics", true, true));
        discord = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, 200, 400, 40, "Discord", true, true));
        autosaveButton = add(new MenuComponentOptionButton(Core.helper.displayWidth()/2-200, 280, 400, 40, "Autosave", true, true, autosave?0:1, "On", "Off"));
        back = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-80, 400, 40, "Back", true));
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
        drawRect(0,0,Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(1));
        autosave = autosaveButton.getIndex()==0;
    }
}