package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.OptionButton;
import com.thizthizzydizzy.dizzyengine.ui.component.TextBox;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
import java.io.File;
import java.util.ArrayList;
import planetaryprotector.Main;
import planetaryprotector.Options;
import planetaryprotector.game.WorldGenerator;
import planetaryprotector.game.Story;
public class MenuNewGame extends Menu{
    private final OptionButton selectedLevel;
    public MenuNewGame(){
        var layout = setLayout(new ConstrainedLayout());
        ArrayList<String> levels = new ArrayList<>();
        for(int i = 0; i<Options.options.level; i++){
            levels.add("Level "+(i+1));
        }
        selectedLevel = new OptionButton("Level", 0, levels.toArray(String[]::new));
        selectedLevel.setSize(400, 40);
        layout.constrain(selectedLevel, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 60));
        ArrayList<String> gens = new ArrayList<>();
        for(WorldGenerator gen : WorldGenerator.generators.get(selectedLevel.getIndex()+1)){
            gens.add(gen.getName());
        }
        var worldGenerator = add(new OptionButton("World Generator", 0, gens.toArray(String[]::new)));
        worldGenerator.setSize(600, 60);
        layout.constrain(worldGenerator, new PositionAnchorConstraint(0.5f, 0, .5f, .5f, 0, 100));
        var tutorial = /*add(*/new OptionButton("Tutorial", 0, "Off", "On")/*)*/;//TODO add tutorial
        tutorial.setSize(600, 60);
        layout.constrain(tutorial, new PositionAnchorConstraint(0.5f, 0, .5f, .5f, 0, 200));
        var name = add(new TextBox("New Game"));
        var create = add(new Button("Create", !fileExists(name.getText())));
        name.runActionsOnTextChange = true;
        name.addAction(() -> {
            create.enabled = !fileExists(name.getText());
        });
        name.setSize(600, 60);
        layout.constrain(name, new PositionAnchorConstraint(.5f, 0, .5f, .5f));
        var back = add(new Button("Back"));
        back.addAction(() -> new MenuMain(false).open());
        back.setSize(500, 60);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -80));
        create.addAction(() -> {
            Main.loadGame(validateFilename(name.getText().trim()), selectedLevel.getIndex()+1, WorldGenerator.generators.get(selectedLevel.getIndex()+1).get(worldGenerator.getIndex()), Story.stories.get(selectedLevel.getIndex()+1).get(0), tutorial.getIndex()==1);//TODO story selector
        });
        create.setSize(500, 60);
        layout.constrain(create, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -160));
        if(Options.options.level>1){
            add(selectedLevel);
        }
    }
    @Override
    public void draw(double deltaTime){
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(selectedLevel.getIndex()+1));
    }
    private boolean fileExists(String name){
        if(name.isBlank())return true;
        File file = new File("saves"+File.separator+name+".json");
        return file.exists();
    }
    private String validateFilename(String name){
        name = name.replaceAll("[/\\\\:*?\\\"<>|]", "-");
        while(fileExists(name))name+="-";
        return name;
    }
}