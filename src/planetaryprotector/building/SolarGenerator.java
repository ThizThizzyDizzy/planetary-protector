    package planetaryprotector.building;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class SolarGenerator extends Building implements BuildingPowerProducer, BuildingDamagable, BuildingDemolishable{
    private int frame = 0;
    static final int frames = 4;
    private int speed = 0;//0-100
    private double delay = 0;
    private int spinTimer = 0;
    public SolarGenerator(double x, double y) {
        super(x, y, 100, 100, BuildingType.SOLAR_GENERATOR);
    }
    public SolarGenerator(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.SOLAR_GENERATOR, level, upgrades);
    }
    @Override
    public void update(){
        super.update();
        speed = (int) ((getProduction()/getMaxProduction())*100);
        speed = Math.max(0, Math.min(100, speed));
        if(speed>0){
            delay = 100d/frames/speed;
            spinTimer++;
            while(spinTimer>=delay){
                spinTimer-=delay;
                frame++;
                if(frame>=frames)frame = 0;
            }
        }
    }
    
    @Override
    public void draw(){
        drawRect(x, y-10, x+width, y+height, getTexture(), 0, frame/(double)frames, 1, (frame+1)/(double)frames);
        for(Upgrade upgrade : type.upgrades){
            int count = getUpgrades(upgrade);
            if(count==0)continue;
            drawRect(x, y-10, x+width, y+height, upgrade.getTexture(type, count), 0, frame/(double)frames, 1, (frame+1)/(double)frames);
        }
        renderDamages();
        drawMouseover();
        MenuGame.theme.applyTextColor();
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
        GL11.glColor4d(1, 1, 1, 1);
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
    public double getProduction(){
        double sunlight = Math.min(1,Core.game.getSunlight()*Math.pow(1.38,getUpgrades(Upgrade.PHOTOVOLTAIC_SENSITIVITY)));
        if(hasUpgrade(Upgrade.STARLIGHT_GENERATION))sunlight = (1+sunlight)/2;
        return getMaxProduction()*sunlight;
    }
    public double getMaxProduction(){
        return Math.max(getLevel(), (49/400d)*Math.pow(getLevel(), 2)+1)/2;
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
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){}
    @Override
    public int getBuildingHeight(){
        return 10;
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
}