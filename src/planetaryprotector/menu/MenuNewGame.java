package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.Main;
import java.io.File;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentSlider;
public class MenuNewGame extends Menu{
    private final MenuComponentButton back;
    private final MenuComponentButton create;
    private final MenuComponentTextBox name;
    private final MenuComponentSlider selectedLevel = new MenuComponentSlider(Display.getWidth()/2-200, 60, 400, 40, 1, Core.latestLevel+1, 1, true);
    public MenuNewGame(GUI gui){
        super(gui, null);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        create = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-160, 400, 40, "Create", false));
        name = add(new MenuComponentTextBox(Display.getWidth()/2-200, 120, 400, 40, "", true));
        if(Core.latestLevel>0){
            add(selectedLevel);
        }
    }
    @Override
    public void renderBackground(){
        switch((int)selectedLevel.getValue()){
            default:
            case 1:
                drawRect(0, 0, Display.getWidth(), Display.getHeight(), MenuGame.theme.getBackgroundTexture());
                break;
            case 2:
                drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/backgroud/cave.png"));
                break;
        }
        create.enabled = !fileExists();
        back.x = Display.getWidth()/2-200;
        back.y = Display.getHeight()-80;
        create.x = back.x;
        create.y = back.y-80;
        name.x = create.x;
        name.y = create.y-240;
    }
    private boolean fileExists(){
        String text = name.text.trim();
        if(text.isEmpty())return true;
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+text+".dat");
        return file.exists();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            back();
        }
        if(button==create){
            if(fileExists()){
                return;
            }
            create(name.text);
        }
    }
    private void back(){
        gui.open(new MenuMain(gui, false));
    }
    private void create(String name){
        Core.save = name;
        switch((int)selectedLevel.getValue()){
            default:
            case 1:
                MenuGame g = MenuGame.load(gui);
                if(g==null){
                    gui.open(new MenuLoad(gui, null));
                }else{
                    Core.game = g;
                    gui.open(g);
                }
                break;
            case 2:
                throw new UnsupportedOperationException("LEVEL 2 PLEEZ");
        }
    }
}