package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.Core;
import simplelibrary.config2.Config;
public class SolarGenerator extends Building implements BuildingPowerProducer, BuildingDamagable, BuildingDemolishable{
    public SolarGenerator(double x, double y) {
        super(x, y, 100, 100, BuildingType.SOLAR_GENERATOR);
    }
    public SolarGenerator(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.SOLAR_GENERATOR, level, upgrades);
    }
    @Override
    public void draw(){
        super.draw();
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
    }
    @Override
    public int getMaxLevel(){
        return 20;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static SolarGenerator loadSpecific(Config cfg, double x, double y, int level, ArrayList<Upgrade> upgrades){
        return new SolarGenerator(x, y, level, upgrades);
    }
    @Override
    protected double getFireDestroyThreshold(){
        return .75;
    }
    @Override
    protected double getIgnitionChance(){
        return .85;
    }
    @Override
    public String getName(){
        return "Level "+(getLevel())+" Solar Generator";
    }
    @Override
    public double getProduction(){
        double sunlight = Math.min(1,Core.game.getSunlight()*Math.pow(1.38,getUpgrades(Upgrade.PHOTOVOLTAIC_SENSITIVITY)));
        if(hasUpgrade(Upgrade.STARLIGHT_GENERATION))sunlight = (1+sunlight)/2;
        return Math.max(getLevel(), (49/400d)*Math.pow(getLevel(), 2)+1)/2*sunlight;
    }
    @Override
    public void producePower(double power){}
    @Override
    public boolean isRenewable(){
        return true;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
}