package planetaryprotector.menu;
import planetaryprotector.game.Game;
import planetaryprotector.Core;
import planetaryprotector.Main;
import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
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
    private final MenuComponentSlider selectedLevel = new MenuComponentSlider(Display.getWidth()/2-200, 60, 400, 40, 1, Core.latestLevel, 1, true);
    private final MenuComponentOptionButton worldGenerator;
    public MenuNewGame(GUI gui){
        super(gui, null);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 500, 60, "Back", true));
        create = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-160, 500, 60, "Create", false));
        name = add(new MenuComponentTextBox(Display.getWidth()/2-200, 120, 600, 60, "", true));
        ArrayList<String> gens = new ArrayList<>();
        for(WorldGenerator gen : WorldGenerator.generators.get((int)selectedLevel.getValue())){
            gens.add(gen.getName());
        }
        worldGenerator = add(new MenuComponentOptionButton(Display.getWidth()/2-300, Display.getHeight()/2+120, 600, 60, "World Generator", true, 0, gens.toArray(new String[gens.size()])));
        if(Core.latestLevel>1){
            add(selectedLevel);
        }
    }
    @Override
    public void renderBackground(){
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture((int)selectedLevel.getValue()));
        create.enabled = !fileExists();
        back.x = Display.getWidth()/2-back.width/2;
        back.y = Display.getHeight()-80;
        create.x = Display.getWidth()/2-create.width/2;
        create.y = back.y-80;
        name.x = Display.getWidth()/2-name.width/2;
        name.y = Display.getHeight()/2;
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
            Core.loadGame(name.text.trim(), (int)selectedLevel.getValue(), WorldGenerator.generators.get((int)selectedLevel.getValue()).get(worldGenerator.getIndex()), Story.stories.get((int)selectedLevel.getValue()).get(0));//TODO story selector
        }
    }
}