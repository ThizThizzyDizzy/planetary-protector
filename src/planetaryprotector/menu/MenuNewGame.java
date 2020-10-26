package planetaryprotector.menu;
import planetaryprotector.game.Game;
import planetaryprotector.Core;
import planetaryprotector.Main;
import java.io.File;
import java.util.ArrayList;
import planetaryprotector.game.WorldGenerator;
import planetaryprotector.game.Story;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentTextBox;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
import simplelibrary.opengl.gui.components.MenuComponentSlider;
public class MenuNewGame extends Menu{
    private final MenuComponentButton back;
    private final MenuComponentButton create;
    private final MenuComponentTextBox name;
    private final MenuComponentSlider selectedLevel = new MenuComponentSlider(Core.helper.displayWidth()/2-200, 60, 400, 40, 1, Core.latestLevel, 1, true);
    private final MenuComponentOptionButton worldGenerator;
    private final MenuComponentOptionButton tutorial;
    public MenuNewGame(GUI gui){
        super(gui, null);
        back = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-80, 500, 60, "Back", true));
        create = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-160, 500, 60, "Create", false));
        name = add(new MenuComponentTextBox(Core.helper.displayWidth()/2-200, 120, 600, 60, "", true));
        ArrayList<String> gens = new ArrayList<>();
        for(WorldGenerator gen : WorldGenerator.generators.get((int)selectedLevel.getValue())){
            gens.add(gen.getName());
        }
        worldGenerator = add(new MenuComponentOptionButton(Core.helper.displayWidth()/2-300, Core.helper.displayHeight()/2+100, 600, 60, "World Generator", true, 0, gens.toArray(new String[gens.size()])));
        tutorial = /*add(*/new MenuComponentOptionButton(Core.helper.displayWidth()/2-300, Core.helper.displayHeight()/2+200, 600, 60, "Tutorial", true, 0, "Off", "On")/*)*/;//TODO add tutorial
        if(Core.latestLevel>1){
            add(selectedLevel);
        }
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture((int)selectedLevel.getValue()));
        create.enabled = !fileExists();
        back.x = Core.helper.displayWidth()/2-back.width/2;
        back.y = Core.helper.displayHeight()-80;
        create.x = Core.helper.displayWidth()/2-create.width/2;
        create.y = back.y-80;
        name.x = Core.helper.displayWidth()/2-name.width/2;
        name.y = Core.helper.displayHeight()/2;
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
            gui.open(new MenuMain(gui, false));
        }
        if(button==create){
            if(fileExists()){
                return;
            }
            Core.loadGame(name.text.trim(), (int)selectedLevel.getValue(), WorldGenerator.generators.get((int)selectedLevel.getValue()).get(worldGenerator.getIndex()), Story.stories.get((int)selectedLevel.getValue()).get(0), tutorial.getIndex()==1);//TODO story selector
        }
    }
}