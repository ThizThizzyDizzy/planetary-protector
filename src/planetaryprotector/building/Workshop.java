package planetaryprotector.building;
import java.util.ArrayList;
import simplelibrary.config2.Config;
public class Workshop extends Building implements BuildingDamagable, BuildingDemolishable{
    public Workshop(double x, double y){
        super(x, y, 100, 100, BuildingType.WORKSHOP);
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static Workshop loadSpecific(Config cfg, double x, double y){
        return new Workshop(x, y);
    }
    @Override
    protected double getIgnitionChance(){
        return .4;
    }
    @Override
    public String getName(){
        return "Workshop";
    }

    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){}
}