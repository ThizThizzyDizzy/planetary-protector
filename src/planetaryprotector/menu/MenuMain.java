package planetaryprotector.menu;
import planetaryprotector.game.Game;
import planetaryprotector.menu.options.MenuOptions;
import planetaryprotector.Core;
import planetaryprotector.Main;
import planetaryprotector.VersionManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.Sys;
import simplelibrary.config2.Config;
import simplelibrary.error.ErrorCategory;
import simplelibrary.error.ErrorLevel;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentList;
public class MenuMain extends Menu{
    private final MenuComponentButton back;
    private final MenuComponentButton newSave;
    private final MenuComponentButton play;
    private final MenuComponentButton rename;
    private final MenuComponentButton delete;
    private final MenuComponentButton credits;
    private final MenuComponentButton options;
    private final MenuComponentList saveList;
    private HashMap<MenuComponentButton, String> badVersions = new HashMap<>();
    private double blackOpacity = 1;
    public MenuMain(GUI gui, boolean blackScreen){
        super(gui, null);
        back = add(new MenuComponentButton(Display.getWidth()/4-200, Display.getHeight()-80, 300, 60, "Exit", true));
        newSave = add(new MenuComponentButton(Display.getWidth()/2-200, 540, 500, 60, "New Game", true));
        play = add(new MenuComponentButton(Display.getWidth()/4-100, Display.getHeight()-160, 300, 60, "Play", false));
        rename = add(new MenuComponentButton(Display.getWidth()/2+200, Display.getHeight()-160, 300, 60, "Rename", true));
        delete = add(new MenuComponentButton(Display.getWidth()/2+200, Display.getHeight()-80, 300, 60, "Delete", true));
        credits = add(new MenuComponentButton(Display.getWidth()/2+200, Display.getHeight()-80, 300, 60, "Credits", true));
        options = add(new MenuComponentButton(0, Display.getHeight()-80, 300, 60, "Options", true));
        saveList = add(new MenuComponentList(Display.getWidth()/2-200, 120+500, newSave.width, Display.getHeight()-360-500, 50));
        File file = new File(Main.getAppdataRoot()+"\\saves");
        if(!file.exists()){
            file.mkdirs();
        }
        String[] filepaths = file.list();
        for(int i = 0; i<filepaths.length; i++){
            MenuComponentButton b = new MenuComponentButton(0, 0, 500, 60, filepaths[i].replace(".dat", ""), true);
            Config cfg;
            try(FileInputStream s = new FileInputStream(new File(Main.getAppdataRoot()+"\\saves\\"+filepaths[i]))){
                cfg = Config.newConfig(s);
                cfg.load();
                String ver;
                if(!VersionManager.isCompatible(ver = cfg.get("version", "<1.5.3"))){
                    badVersions.put(b,ver);
                }
            }catch(IOException ex){
                Sys.error(ErrorLevel.severe, "Failed to load save version!", ex, ErrorCategory.fileIO);
                badVersions.put(b, "???");
                continue;
            }
            saveList.add(b);
        }
        blackOpacity = blackScreen?1.2:0;
    }
    @Override
    public void onGUIOpened() {
        Core.discordState = Core.discordDetails = "";
        Core.discordLargeImageKey = "city";
        Core.discordLargeImageText = Core.discordDetails = "Main Menu";
        Core.discordEndTimestamp = 0;
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        for(MenuComponent c : saveList.components){
            if(c.isMouseOver&&badVersions.containsKey(c)){
                GL11.glColor4d(0,0,0,0.5);
                drawRect(Mouse.getX(), Display.getHeight()-Mouse.getY(), Math.min(Mouse.getX()+FontManager.getLengthForStringWithHeight(badVersions.get(c), 46)+2, Display.getWidth()), Display.getHeight()-Mouse.getY()+50, 0);
                if(badVersions.get(c).contains("<")){
                    if(VersionManager.isCompatible(badVersions.get(c).replaceFirst("<", ""))){
                        GL11.glColor4d(1,1,0,1);
                    }else{
                        GL11.glColor4d(1,0,0,1);
                    }
                }else{
                    GL11.glColor4d(1,0,0,1);
                }
                drawText(Mouse.getX()+2, Display.getHeight()-Mouse.getY()+2, Display.getWidth()-2, Display.getHeight()-Mouse.getY()+48, badVersions.get(c));
                GL11.glColor4d(1,1,1,1);
            }
        }
        GL11.glColor4d(0, 0, 0, blackOpacity);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            exit();
        }
        if(button==credits){
            gui.open(new MenuCredits(gui));
        }
        if(button==options){
            gui.open(new MenuOptions(gui));
        }
        if(button==newSave){
            newGame();
        }
        if(button==delete&&saveList.components.size()>0){
            delete((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
        if(button==rename&&saveList.components.size()>0){
            rename((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
        if(button==play&&saveList.components.size()>0){
            play((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex()));
        }
    }
    @Override
    public void renderBackground(){
        if(saveList.getSelectedIndex()==-1){
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        }else{
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(getLevel(((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex())).label)));
        }
        if(saveList.getSelectedIndex()>-1){
            play.enabled=true;
            rename.enabled=true;
            delete.enabled=true;
        }else{
            play.enabled=false;
            rename.enabled=false;
            delete.enabled=false;
        }
        for(MenuComponent c : saveList.components){
            if(saveList.getSelectedIndex()<0||saveList.getSelectedIndex()>saveList.components.size()-1){
                break;
            }
            if(c instanceof MenuComponentButton){
                MenuComponentButton b = (MenuComponentButton)c;
                b.enabled = saveList.components.get(saveList.getSelectedIndex()) != b;
            }
        }
        back.x = Display.getWidth()/4-back.width/2;
        back.y = Display.getHeight()-60-back.height/2;
        delete.x = (Display.getWidth()-Display.getWidth()/4)-delete.width/2;
        delete.y = back.y;
        play.x = back.x;
        play.y = back.y-80;
        rename.x = delete.x;
        rename.y = delete.y-80;
        newSave.x = Display.getWidth()/2-newSave.width/2;
        newSave.y = 240;
        saveList.x = newSave.x;
        saveList.y = newSave.y+80;
        saveList.height = (play.y-80)-(newSave.y+80);
        credits.x = Display.getWidth()-credits.width;
        options.y = credits.y = Display.getHeight()-credits.height;
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(play.x, 40, rename.x+rename.width, newSave.y-40, ImageStash.instance.getTexture("/textures/logo.png"));
    }
    @Override
    public void tick(){
        super.tick();
        blackOpacity-=.05;
    }
    private void exit(){
        Core.helper.running = false;
    }
    private void newGame(){
        gui.open(new MenuNewGame(gui));
    }
    private void delete(MenuComponentButton save){
        String name = save.label+".dat";
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+name);
        file.delete();
        gui.open(new MenuMain(gui, false));
    }
    private void rename(MenuComponentButton save){
        gui.open(new MenuRename(gui, save));
    }
    private void play(MenuComponentButton save){
        loadGame(save.label);
    }
    private void loadGame(String name){
        Core.loadGame(name, getLevel(name), null);
    }
    private HashMap<String, Integer> levels = new HashMap<>();
    private int getLevel(String name){
        if(!levels.containsKey(name)){
            File file = new File(Main.getAppdataRoot()+"\\saves\\"+name+".dat");
            if(!file.exists()){
                return 1;
            }
            Config config = Config.newConfig(file);
            config.load();
            int level = config.get("level", 1);
            if(level==0)level = 1;
            levels.put(name, level);
        }
        return levels.get(name);
    }
}