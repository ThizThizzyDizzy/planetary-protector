package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.gui.Menu;
import planetaryprotector.game.Game;
import planetaryprotector.menu.options.MenuOptions;
import planetaryprotector.Core;
import planetaryprotector.Main;
import planetaryprotector.VersionManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
public class MenuMain extends Menu{
    private final Button back;
    private final Button newSave;
    private final Button play;
    private final Button rename;
    private final Button delete;
    private final Button credits;
    private final Button options;
    private final Button update;
    private final MenuComponentList saveList;
    private HashMap<MenuComponentButton, String> badVersions = new HashMap<>();
    private double blackOpacity = 1;
    public MenuMain(GUI gui, boolean blackScreen){
        super(gui, null);
        back = add(new MenuComponentButton(Core.helper.displayWidth()/4-200, Core.helper.displayHeight()-80, 300, 60, "Exit", true));
        newSave = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, 540, 500, 60, "New Game", true));
        play = add(new MenuComponentButton(Core.helper.displayWidth()/4-100, Core.helper.displayHeight()-160, 300, 60, "Play", false));
        rename = add(new MenuComponentButton(Core.helper.displayWidth()/2+200, Core.helper.displayHeight()-160, 300, 60, "Rename", true));
        delete = add(new MenuComponentButton(Core.helper.displayWidth()/2+200, Core.helper.displayHeight()-80, 300, 60, "Delete", true));
        credits = add(new MenuComponentButton(Core.helper.displayWidth()/2+200, Core.helper.displayHeight()-80, 300, 60, "Credits", true));
        options = add(new MenuComponentButton(0, Core.helper.displayHeight()-80, 300, 60, "Options", true));
        update = add(new MenuComponentButton(0, Core.helper.displayHeight()-80, 300, 60, "Update", true));
        saveList = add(new MenuComponentList(Core.helper.displayWidth()/2-200, 120+500, newSave.width, Core.helper.displayHeight()-360-500, 50));
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
                drawRect(gui.mouseX, gui.mouseY, Math.min(gui.mouseX+FontManager.getLengthForStringWithHeight(badVersions.get(c), 46)+2, Core.helper.displayWidth()), gui.mouseY+50, 0);
                if(badVersions.get(c).contains("<")){
                    if(VersionManager.isCompatible(badVersions.get(c).replaceFirst("<", ""))){
                        GL11.glColor4d(1,1,0,1);
                    }else{
                        GL11.glColor4d(1,0,0,1);
                    }
                }else{
                    GL11.glColor4d(1,0,0,1);
                }
                drawText(gui.mouseX+2, gui.mouseY+2, Core.helper.displayWidth()-2, gui.mouseY+48, badVersions.get(c));
                GL11.glColor4d(1,1,1,1);
            }
        }
        GL11.glColor4d(0, 0, 0, blackOpacity);
        drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
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
        if(button==update){
            try{
                Core.update();
            }catch(URISyntaxException|IOException ex){
                Sys.error(ErrorLevel.severe, "Update failed!", ex, ErrorCategory.other);
            }
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
        update.enabled = Core.updateAvailable;
        if(saveList.getSelectedIndex()==-1){
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(1));
        }else{
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(getLevel(((MenuComponentButton)saveList.components.get(saveList.getSelectedIndex())).label)));
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
        back.x = Core.helper.displayWidth()/4-back.width/2;
        back.y = Core.helper.displayHeight()-60-back.height/2;
        delete.x = (Core.helper.displayWidth()-Core.helper.displayWidth()/4)-delete.width/2;
        delete.y = back.y;
        play.x = back.x;
        play.y = back.y-80;
        rename.x = delete.x;
        rename.y = delete.y-80;
        newSave.x = Core.helper.displayWidth()/2-newSave.width/2;
        newSave.y = 240;
        saveList.x = newSave.x;
        saveList.y = newSave.y+80;
        saveList.height = (play.y-80)-(newSave.y+80);
        update.x = credits.x = Core.helper.displayWidth()-credits.width;
        options.y = credits.y = Core.helper.displayHeight()-credits.height;
        update.y = update.enabled?credits.y-credits.height:-update.height;
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
        Core.loadGame(name, getLevel(name), null, null, false);
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