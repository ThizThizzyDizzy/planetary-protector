package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.structure.task.TaskWreckClean;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Wreck extends Structure{
    public int ingots;
    int progress;
    public Wreck(Game game, int x, int y, int ingots){
        super(StructureType.WRECK, game, x, y, 100, 100);
        this.ingots = ingots;
    }
    @Override
    public void renderBackground(){
        if(task!=null){
            return;
        }
        drawRect(x, y, x+width, y+height, type.getTexture());
    }
    @Override
    public boolean onDamage(int x, int y){
        ingots = Math.max(0,ingots-10);
        return false;
    }
    @Override
    public Config save(Config cfg) {
        super.save(cfg);
        cfg.set("ingots", ingots);
        cfg.set("progress", progress);
        return cfg;
    }
    public static Wreck loadSpecific(Config cfg, Game game, int x, int y){
        Wreck wreck = new Wreck(game, x, y, cfg.get("ingots", 1));
        wreck.progress = cfg.get("progress", wreck.progress);
        return wreck;
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    public String getName(){
        return "Wreck ("+ingots+" ingots)";
    }
    @Override
    public void getDebugInfo(ArrayList<String> data) {
        super.getDebugInfo(data);
        data.add("Ingots: "+ingots);
        data.add("Progress: "+progress);
    }
    @Override
    public boolean isBackgroundStructure(){
        return true;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Clean up", new TaskWreckClean(this)));
    }
}