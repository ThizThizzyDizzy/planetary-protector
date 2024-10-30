package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import planetaryprotector.game.Game;
import planetaryprotector.Main;
import java.io.File;
import planetaryprotector.Core;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
public class MenuRename extends Menu{
    private final MenuComponentButton save;
    private final MenuComponentTextBox name;
    private final MenuComponentButton back;
    private final MenuComponentButton rename;
    public MenuRename(String save){
        super(gui, null);
        back = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-200, DizzyEngine.screenSize.y-80, 400, 40, "Back", true));
        rename = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-200, DizzyEngine.screenSize.y-160, 400, 40, "Rename", false));
        name = add(new MenuComponentTextBox(DizzyEngine.screenSize.x/2-200, 120, 400, 40, "", true));
        this.save=save;
    }
    @Override
    public void renderBackground(){
        Renderer.fillRect(0,0,DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, Game.theme.getBackgroundTexture(1));
        File file = new File(Main.getAppdataRoot()+"\\"+name.text);
        if(!file.exists()){
            rename.enabled = true;
        }else{
            rename.enabled = false;
        }
        back.x = DizzyEngine.screenSize.x/2-200;
        back.y = DizzyEngine.screenSize.y-80;
        rename.x = back.x;
        rename.y = back.y-80;
        name.x = rename.x;
        name.y = rename.y-240;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            back();
        }
        if(button==rename){
            rename(save.label, name.text);
        }
    }
    private void back(){
        gui.open(new MenuMain(gui, false));
    }
    private void rename(String from, String to){
        File From = new File(Main.getAppdataRoot()+"\\saves\\"+from+".dat");
        File To = new File(Main.getAppdataRoot()+"\\saves\\"+to+".dat");
        From.renameTo(To);
        back();
    }
}