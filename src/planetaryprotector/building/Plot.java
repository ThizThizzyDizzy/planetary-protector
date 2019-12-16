package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.Core;
import simplelibrary.config2.Config;
public class Plot extends Building implements BuildingDamagable{
    public Plot(double x, double y) {
        super(x, y, 100, 100, BuildingType.EMPTY);
    }
    @Override
    public void update(){
        if(damages.size()>10){
            Core.game.replaceBuilding(this, new Wreck(x, y, 0));
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
    public static Plot loadSpecific(Config cfg, double x, double y) {
        return new Plot(x, y);
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
}