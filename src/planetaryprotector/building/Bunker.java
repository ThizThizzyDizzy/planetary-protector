package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Bunker extends Building implements BuildingDamagable, BuildingDemolishable{
    public Bunker(double x, double y){
        super(x, y, 100, 100, BuildingType.BUNKER);
    }
    @Override
    public boolean onDamage(double x, double y){
        if(MenuGame.rand.nextDouble()<.25){
            super.onDamage(x, y);
        }
        return true;
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static Bunker loadSpecific(Config cfg, double x, double y) {
        return new Bunker(x, y);
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    public String getName(){
        return "Bunker";
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data) {}
}