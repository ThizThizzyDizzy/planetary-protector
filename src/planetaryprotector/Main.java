package planetaryprotector;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.discord.DiscordPresence;
import com.thizthizzydizzy.dizzyengine.flat.FlatGame;
import com.thizthizzydizzy.dizzyengine.graphics.Font;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.FlatUI;
import com.thizthizzydizzy.dizzyengine.logging.Logger;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.Slider;
import com.thizthizzydizzy.dizzyengine.ui.component.TextBox;
import com.thizthizzydizzy.dizzyengine.ui.component.layer.TextLabelLayer;
import com.thizthizzydizzy.dizzyengine.ui.component.layer.TexturedBackgroundLayer;
import com.thizthizzydizzy.dizzyengine.ui.component.layer.TexturedLinearHandle;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.Game;
import planetaryprotector.game.Story;
import planetaryprotector.game.WorldGenerator;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoadTextures;
import planetaryprotector.structure.Structure.Upgrade;
import planetaryprotector.structure.StructureType;
import planetaryprotector.ui.component.layer.ButtonBackgroundLayer;
public class Main{
    public static void main(String[] args){
        DizzyEngine.init("Planetary Protector");
        DizzyEngine.addFixedUpdateThread("Tick Thread", Main::tick, () -> {
            saveOptions();
        }, 20);
        DiscordPresence.init("592509210277838848");
        DiscordPresence.setLargeImage("city", "Starting up...");
        DiscordPresence.setDetails("Starting up...");
        DizzyEngine.addLayer(new FlatGame());//TODO FPS tracker (also tick tracking in each fixed update thread)
        FlatUI ui = DizzyEngine.addLayer(new FlatUI());
        ui.setDefaultComponentBackground(Button.class, ButtonBackgroundLayer::new);
        ui.setDefaultComponentBackground(TextBox.class, ()->new TexturedBackgroundLayer(ResourceManager.getTexture("/assets/textures/ui/text_box.png")));
        ui.setDefaultComponentBackground(Slider.class, ()->new TexturedBackgroundLayer(ResourceManager.getTexture("/assets/textures/ui/slider/background.png")));
        ui.setDefaultComponentLabel(() -> new TextLabelLayer("", Color.BLACK));
        ui.setDefaultComponentHandle(Slider.class, ()->new TexturedLinearHandle(ResourceManager.getTexture("/assets/textures/ui/slider/handle.png")));
        ui.open(new MenuLoadTextures());
        Logger.info("Loading fonts");
        Renderer.setDefaultFont(Font.loadFont(ResourceManager.loadData(ResourceManager.getInternalResource("/assets/fonts/high_resolution.ttf"))));
        Sounds.init();
//        Game.refreshTheme();
        loadOptions();
        DizzyEngine.start();
    }
    private static void tick(long tickCounter){
        if(tickCounter==0){
            //TICK INIT
            Logger.push("TICK INIT");
            for(Upgrade upgrade : Upgrade.values());//initialize upgrades to add them to building upgrade lists
            for(StructureType type : StructureType.structureTypes){
                Logger.info("Loaded structure "+type.name+". Max level: "+type.getMaxLevel());
            }
            Logger.pop();
        }
        Sounds.tick(tickCounter);
//        DizzyEngine.getLayer(FlatGame.class).tick();
    }
    public static void saveOptions(){
        try(FileWriter writer = new FileWriter("options.json")){
            DizzyEngine.gson.toJson(Options.options, writer);
        }catch(IOException ex){
            Logger.error("Failed to save options!", ex);
        }
    }
    public static void loadOptions(){
        try(FileReader reader = new FileReader("options.json")){
            var options = DizzyEngine.gson.fromJson(reader, Options.class);
            if(options!=null)Options.options = options;
        }catch(IOException ex){
            Logger.error("Failed to load options!", ex);
        }
    }
    public static void loadGame(String name, int level, WorldGenerator gen, Story story, boolean tutorial){
        Game g = Game.load(name);
        if(g==null){
            if(gen==null)throw new IllegalArgumentException("Tried to load invalid game!");
            new MenuGame(Game.generate(name, level, gen, new BoundingBox(-960, -540, 1920, 1080), story, tutorial)).open();
        }else{
            new MenuGame(g).open();
        }
    }
}
