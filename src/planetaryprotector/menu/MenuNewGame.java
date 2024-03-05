package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.OptionButton;
import com.thizthizzydizzy.dizzyengine.ui.component.Slider;
import com.thizthizzydizzy.dizzyengine.ui.component.TextBox;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
import planetaryprotector.Core;
import java.io.File;
import java.util.ArrayList;
import planetaryprotector.game.WorldGenerator;
import planetaryprotector.game.Story;
public class MenuNewGame extends Menu{
    private final Button back;
    private final Button create;
    private final TextBox name;
    private final Slider selectedLevel;
    private final OptionButton worldGenerator;
    private final OptionButton tutorial;
    public MenuNewGame(){
        var layout = setLayout(new ConstrainedLayout());
        selectedLevel = new Slider(1, /*Core.latestLevel*/1, 1);//TODO readd level selector
        selectedLevel.setSize(400, 40);
        layout.constrain(selectedLevel, new PositionAnchorConstraint(0, 0, .5f, 0, 0, 60));
        ArrayList<String> gens = new ArrayList<>();
        for(WorldGenerator gen : WorldGenerator.generators.get((int)selectedLevel.getValue())){
            gens.add(gen.getName());
        }
        worldGenerator = add(new OptionButton("World Generator", 0, gens.toArray(String[]::new)));
        worldGenerator.setSize(600, 60);
        layout.constrain(worldGenerator, new PositionAnchorConstraint(0.5f, 0, .5f, .5f, 0, 100));
        tutorial = /*add(*/new OptionButton("Tutorial", 0, "Off", "On")/*)*/;//TODO add tutorial
        tutorial.setSize(600, 60);
        layout.constrain(tutorial, new PositionAnchorConstraint(0.5f, 0, .5f, .5f, 0, 200));
        name = add(new TextBox("New Game"));
        name.addAction(() -> {});
        name.setSize(600, 60);
        layout.constrain(name, new PositionAnchorConstraint(.5f, 0, .5f, .5f));
        back = add(new Button("Back"));
        back.addAction(() -> new MenuMain(false).open());
        back.setSize(500, 60);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -80));
        create = add(new Button("Create", false));
        create.addAction(() -> {
            Core.loadGame(validateFilename(name.getText().trim()), (int)selectedLevel.getValue(), WorldGenerator.generators.get((int)selectedLevel.getValue()).get(worldGenerator.getIndex()), Story.stories.get((int)selectedLevel.getValue()).get(0), tutorial.getIndex()==1);//TODO story selector
        });
        create.setSize(500, 60);
        layout.constrain(create, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -160));
//        if(Core.latestLevel>1){
//            add(selectedLevel);
//        }
    }
    @Override
    public void draw(double deltaTime){
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture((int)selectedLevel.getValue()));
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