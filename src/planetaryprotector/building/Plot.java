package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Plot extends Building implements BuildingDamagable{
    public Plot(Game game, double x, double y) {
        super(game, x, y, 100, 100, BuildingType.EMPTY);
    }
    @Override
    public void update(){
        if(damages.size()>10){
            game.replaceBuilding(this, new Wreck(game, x, y, 0));
        }
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static Plot loadSpecific(Config cfg, Game game, double x, double y) {
        return new Plot(game, x, y);
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){}
    @Override
    public boolean isBackgroundStructure(){
        return true;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        for(BuildingType type : BuildingType.values()){
            if(type.isConstructable(game)){
                actions.add(new Action("Build "+type.name, new TaskConstruct(this, type.createNewBuilding(game, x, y))));
            }
        }
    }
}