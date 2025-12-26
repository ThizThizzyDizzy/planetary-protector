package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.structure.task.TaskWreckClean;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
public class Wreck extends Structure{
    public int ingots;
    int progress;
    public Wreck(Game game, int x, int y, int ingots){
        super(StructureType.WRECK, game, x, y, 100, 100);
        this.ingots = ingots;
    }
//    @Override
//    public void renderBackground(){
//        if(task!=null){
//            return;
//        }
//        Renderer.fillRect(x, y, x+width, y+height, type.getTexture());
//    }
    @Override
    public boolean onDamage(int x, int y){
        ingots = Math.max(0,ingots-10);
        return false;
    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.wreck = new GameState.Structure.Wreck();
        state.wreck.ingots = ingots;
        state.wreck.progress = progress;
        return state;
    }
    public static Wreck loadSpecific(GameState.Structure state, Game game, int x, int y){
        Wreck wreck = new Wreck(game, x, y, state.wreck.ingots);
        wreck.progress = state.wreck.progress;
        return wreck;
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
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Clean up", new TaskWreckClean(this)));
    }
}