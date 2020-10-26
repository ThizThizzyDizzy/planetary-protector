package planetaryprotector.menu;
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
    public MenuRename(GUI gui, MenuComponentButton save){
        super(gui, null);
        back = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-80, 400, 40, "Back", true));
        rename = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-160, 400, 40, "Rename", false));
        name = add(new MenuComponentTextBox(Core.helper.displayWidth()/2-200, 120, 400, 40, "", true));
        this.save=save;
    }
    @Override
    public void renderBackground(){
        drawRect(0,0,Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(1));
        File file = new File(Main.getAppdataRoot()+"\\"+name.text);
        if(!file.exists()){
            rename.enabled = true;
        }else{
            rename.enabled = false;
        }
        back.x = Core.helper.displayWidth()/2-200;
        back.y = Core.helper.displayHeight()-80;
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