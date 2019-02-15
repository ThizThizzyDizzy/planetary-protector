package planetaryprotector.menu;
import planetaryprotector.Main;
import java.io.File;
import org.lwjgl.opengl.Display;
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
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        rename = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-160, 400, 40, "Rename", false));
        name = add(new MenuComponentTextBox(Display.getWidth()/2-200, 120, 400, 40, "", true));
        this.save=save;
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        File file = new File(Main.getAppdataRoot()+"\\"+name.text);
        if(!file.exists()){
            rename.enabled = true;
        }else{
            rename.enabled = false;
        }
        back.x = Display.getWidth()/2-200;
        back.y = Display.getHeight()-80;
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