package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.structure.task.TaskConstruct;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Plot extends Structure implements StructureDamagable{
    public Plot(Game game, int x, int y) {
        super(StructureType.EMPTY_PLOT, game, x, y, 100, 100);
    }
    @Override
    public void destroy(){
        game.replaceStructure(this, new Wreck(game, x, y, 0));
    }
    public static Plot loadSpecific(Config cfg, Game game, int x, int y) {
        return new Plot(game, x, y);
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    public boolean isBackgroundStructure(){
        return true;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        for(StructureType type : StructureType.structureTypes){
            if(type.isConstructible(game)){
                actions.add(new Action("Build "+type.getDisplayName(), new TaskConstruct(this, type.createStructure(game, x, y))));
            }
        }
    }
}