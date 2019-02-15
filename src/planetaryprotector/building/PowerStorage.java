package planetaryprotector.building;
import java.util.ArrayList;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawCenteredText;
public class PowerStorage extends Building implements BuildingPowerStorage, BuildingDamagable, BuildingDemolishable{
    private double power;
    public PowerStorage(double x, double y) {
        super(x, y, 100, 100, BuildingType.POWER_STORAGE);
    }
    public PowerStorage(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.POWER_STORAGE, level, upgrades);
    }
    @Override
    public void draw(){
        super.draw();
        drawCenteredText(x, y, x+width, y+20, (int)power+"");
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
    }
    @Override
    public int getMaxLevel(){
        return 20;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("power", power);
        return cfg;
    }
    public static PowerStorage loadSpecific(Config cfg, double x, double y, int level, ArrayList<Upgrade> upgrades){
        PowerStorage battery = new PowerStorage(x, y, level, upgrades);
        battery.power = cfg.get("power", 0);
        return battery;
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
        return "Level "+(getLevel())+" Power Storage";
    }
    @Override
    public double getProduction(){
        return Math.min(50+50*getLevel(),power);
    }
    @Override
    public void producePower(double power){
        this.power-=power;
    }
    @Override
    public boolean isRenewable(){
        return false;
    }
    @Override
    public double getMaxPower(){
        return 7000+3000*getLevel();
    }
    @Override
    public double getDemand(){
        return Math.min(5+5*getLevel(),getMaxPower()-getPower());
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public void addPower(double power){
        this.power += power;
    }
}