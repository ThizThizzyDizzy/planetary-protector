package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.discord.DiscordPresence;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.logging.Logger;
import com.thizthizzydizzy.dizzyengine.ui.FlatUI;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import com.thizthizzydizzy.dizzyengine.ui.component.Scrollable;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.ListLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.BoundsConstraint;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
import planetaryprotector.menu.options.MenuOptions;
import planetaryprotector.Core;
import planetaryprotector.VersionManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.joml.Vector2d;
import planetaryprotector.game.GameState;
public class MenuMain extends Menu{
    private final Button back;
    private final Button newSave;
    private final Button play;
    private final Button rename;
    private final Button delete;
    private final Button credits;
    private final Button options;
    private final Scrollable saveList;
    private HashMap<Button, String> badVersions = new HashMap<>();
    private float blackOpacity = 1;
    private ArrayList<GameState> saves = new ArrayList<>();
    private ArrayList<String> saveNames = new ArrayList<>();
    public MenuMain(boolean blackScreen){
        var layout = setLayout(new ConstrainedLayout());
        saveList = add(new Scrollable(0));
        saveList.setLayout(new ListLayout());
        layout.constrain(saveList, new BoundsConstraint(.5f, -250, .5f, 250, 0, 320, 1, -210));
        back = add(new Button("Exit"));
        back.addAction(DizzyEngine::stop);
        back.setSize(300, 60);
        layout.constrain(back, new PositionAnchorConstraint(.5f, .5f, .25f, 1, 0, -60));
        newSave = add(new Button("New Game"));
        newSave.addAction(() -> new MenuNewGame().open());
        newSave.setSize(500, 60);
        layout.constrain(newSave, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 240));
        play = add(new Button("Play", false));
        play.addAction(() -> Core.loadGame(saveNames.get(saveList.components.indexOf(saveList.focusedComponent[0])), saves.get(saveList.components.indexOf(saveList.focusedComponent[0])).level, null, null, false));
        play.setSize(300, 60);
        layout.constrain(play, new PositionAnchorConstraint(.5f, .5f, .25f, 1, 0, -140));
        rename = add(new Button("Rename", false));
        rename.addAction(() -> new MenuRename(saveNames.get(saveList.components.indexOf(saveList.focusedComponent[0]))).open());
        rename.setSize(300, 60);
        layout.constrain(rename, new PositionAnchorConstraint(.5f, .5f, .75f, 1, 0, -140));
        delete = add(new Button("Delete", false));
        delete.addAction(() -> {
            String name = saveNames.get(saveList.components.indexOf(saveList.focusedComponent[0]))+".json";
            File file = new File("saves"+File.separator+name);
            file.delete();
            new MenuMain(false).open();
        });
        delete.setSize(300, 60);
        layout.constrain(delete, new PositionAnchorConstraint(.5f, .5f, .75f, 1, 0, -60));
        credits = add(new Button("Credits"));
        credits.addAction(() -> new MenuCredits().open());
        credits.setSize(300, 60);
        layout.constrain(credits, new PositionAnchorConstraint(1, 1, 1, 1));
        options = add(new Button("Options"));
        options.addAction(() -> new MenuOptions().open());
        options.setSize(300, 60);
        layout.constrain(options, new PositionAnchorConstraint(0, 1, 0, 1));
        File file = new File("saves");
        if(!file.exists()){
            file.mkdirs();
        }
        String[] filepaths = file.list();
        for(int i = 0; i<filepaths.length; i++){
            Button b = new Button(filepaths[i].replace(".json", ""));
            b.setSize(500, 60);
            GameState state;
            try(FileReader reader = new FileReader("saves"+File.separator+filepaths[i])){
                state = DizzyEngine.gson.fromJson(reader, GameState.class);
                String ver;
                if(!VersionManager.isCompatible(ver = state.version)){
                    badVersions.put(b, ver);
                }
            }catch(IOException ex){
                Logger.error(ex);
                badVersions.put(b, "???");
                continue;
            }
            saves.add(state);
            saveNames.add(filepaths[i].replace(".json", ""));
            saveList.add(b);
        }
        blackOpacity = blackScreen?1.2f:0;
    }
    @Override
    public void onMenuOpened(){
        DiscordPresence.setState("");
        DiscordPresence.setDetails("Main Menu");
        DiscordPresence.setLargeImage("city", "Main Menu");
        DiscordPresence.setEndTimestamp(0);
    }
    @Override
    public void render(double deltaTime){
        super.render(deltaTime);
        FlatUI ui = DizzyEngine.getLayer(FlatUI.class);//TODO generalized tooltip system
        if(ui!=null){
            for(Component c : saveList.components){
                if(badVersions.containsKey(c)){
                    for(int i = 0; i<DizzyEngine.CURSOR_LIMIT; i++){
                        if(c.isCursorFocused[i]){
                            Vector2d cursor = ui.cursorPosition[i];
                            Renderer.setColor(0, 0, 0, 0.5f);
                            Renderer.fillRect((float)cursor.x, (float)cursor.y, Math.min((float)cursor.x+Renderer.getStringWidth(badVersions.get(c), 46)+2, getWidth()), (float)cursor.y+50);
                            Renderer.setColor(1, badVersions.get(c).contains("<")&&VersionManager.isCompatible(badVersions.get(c).replaceFirst("<", ""))?1:0, 0, 1);
                            Renderer.drawText((float)cursor.x+2, (float)cursor.y+2, getWidth()-2, (float)cursor.y+48, badVersions.get(c));
                        }
                    }
                }
            }
        }
        Renderer.setColor(0, 0, 0, blackOpacity);
        Renderer.fillRect(0, 0, getWidth(), getHeight());
        blackOpacity -= deltaTime;
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        if(saveList.focusedComponent[0]==null){
            Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(1));
            play.enabled = false;
            rename.enabled = false;
            delete.enabled = false;
        }else{
            Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(Math.max(1, saves.get(saveList.components.indexOf(saveList.focusedComponent[0])).level)));
            play.enabled = true;
            rename.enabled = true;
            delete.enabled = true;
        }
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(play.x, 40, rename.x+rename.getWidth(), newSave.y-40, ResourceManager.getTexture("/textures/logo.png"));
    }
}
