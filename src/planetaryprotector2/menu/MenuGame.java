package planetaryprotector2.menu;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.Main;
import planetaryprotector.Sounds;
import planetaryprotector.VersionManager;
import java.io.File;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuGame extends Menu{
    double blackScreen = 1;
    boolean fading = false;
    public MenuGame(GUI gui){
        super(gui, null);
        if(Core.latestLevel<1){
            Core.latestLevel = 1;
        }
        Core.game = null;
        Sounds.fadeSound("music");
    }
    @Override
    public void tick(){
        super.tick();
        if(fading){
            blackScreen+=.01;
        }else{
            blackScreen-=.01;
        }
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/gui/caveBackground.png"));
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        super.keyboardEvent(character, key, pressed, repeat);
        if(pressed&&!repeat){
            if(key==Controls.menu){
                gui.open(new MenuIngame(gui, this));
            }
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        GL11.glColor4d(0, 0, 0, blackScreen);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void save(){
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+Core.save+".dat");
        Config config = Config.newConfig(file);
        config.set("level", 1);
        config.set("version", VersionManager.currentVersion);
        config.save();
    }
    public static MenuGame load(GUI gui){
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+Core.save+".dat");
        if(!file.exists()){
            return new MenuGame(gui);
        }
        MenuGame game = new MenuGame(gui);
        Config config = Config.newConfig(file);
        config.load();
        return game;
    }
}