package planetaryprotector.structure.building;
import java.util.ArrayList;
import planetaryprotector.structure.building.task.TaskTrainWorker;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Workshop extends Building implements BuildingDamagable, BuildingDemolishable{
    public Workshop(Game game, double x, double y){
        super(game, x, y, 100, 100, BuildingType.WORKSHOP);
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static Workshop loadSpecific(Config cfg, Game game, double x, double y){
        return new Workshop(game, x, y);
    }
    @Override
    protected double getIgnitionChance(){
        return .4;
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){}
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
    @Override
    public int getStructureHeight(){
        return 20;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Train Worker", new TaskTrainWorker(this)));
    }
}