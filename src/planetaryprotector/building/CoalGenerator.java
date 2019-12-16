package planetaryprotector.building;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class CoalGenerator extends Building implements BuildingPowerProducer, BuildingStarlightConsumer, BuildingDamagable, BuildingDemolishable{
    public int coal = 0;
    public double burning = 0;//how much time is left on current coal burning
    private static final int BURN_TIME = 500;
    public double power;
    public boolean autoFuel = false;
    private double starlight = 0;
    private static final double STARLIGHT_THRESHOLD = 0.01;
    private int frame = 0;
    static final int frames = 8;
    private int speed = 0;//0-100
    private static final int acceleration = 2;
    private double delay = 0;
    private int spinTimer = 0;
    public CoalGenerator(double x, double y) {
        super(x, y, 100, 100, BuildingType.COAL_GENERATOR);
    }
    public CoalGenerator(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.COAL_GENERATOR, level, upgrades);
    }
    @Override
    public void update(){
        super.update();
        if(burning>0){
            power+=4+Math.pow(getLevel(),1.4)+getLevel()*Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE))*(starlight>STARLIGHT_THRESHOLD?2:0);
            if(starlight>STARLIGHT_THRESHOLD)starlight-=STARLIGHT_THRESHOLD;
            burning-=Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE));
            speed+=acceleration;
        }else{
            speed-=acceleration;
        }
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
        if(coal==0&&autoFuel&&burning<=0){
            if(Core.game.hasResources(new ItemStack(Item.coal))){
                Core.game.removeResources(new ItemStack(Item.coal));
                coal++;
            }
        }
        if(power<=0&&burning<=0&&coal>0){
            coal--;
            burning = getBurnTime();
        }
    }
    @Override
    public void draw(){
        drawRect(x, y, x+width, y+height, getTexture(), getVariant()/(double)getVariants(), frame/(double)frames, (getVariant()+1)/(double)getVariants(), (frame+1)/(double)frames);
        for(Upgrade upgrade : type.upgrades){
            int count = getUpgrades(upgrade);
            if(count==0)continue;
            drawRect(x, y, x+width, y+height, upgrade.getTexture(type, count), getVariant()/(double)getVariants(), frame/(double)frames, (getVariant()+1)/(double)getVariants(), (frame+1)/(double)frames);
        }
        renderDamages();
    }
    @Override
    public void drawForeground(){
        GL11.glColor4d(1,0,0,1);
        drawRect(x,y+18,x+width*(burning/getBurnTime()), y+20, 0);
        MenuGame.theme.applyTextColor();
        drawCenteredText(x, y, x+width, y+18, "Power: "+(int)power);
        if(coal>0)drawCenteredText(x, y+18, x+width, y+36, coal+" Coal");
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public int getMaxLevel(){
        return 20;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("power", power);
        cfg.set("autofuel", autoFuel);
        return cfg;
    }
    public static CoalGenerator loadSpecific(Config cfg, double x, double y, int level, ArrayList<Upgrade> upgrades){
        CoalGenerator generator = new CoalGenerator(x, y, level, upgrades);
        generator.power = cfg.get("power", 0d);
        generator.autoFuel = cfg.get("autofuel", false);
        return generator;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return .75;
    }
    @Override
    protected double getIgnitionChance(){
        return .75;
    }
    @Override
    public void upgrade(){
        super.upgrade();
    }
    @Override
    public double getProduction(){
        return power;
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
    public double getMaxStarlight(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL)?0.1:0;
    }
    @Override
    public double getStarlightDemand(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL)?Math.min(getMaxStarlight()-starlight,STARLIGHT_THRESHOLD*1.5):0;
    }
    @Override
    public double getStarlight(){
        return starlight;
    }
    @Override
    public void addStarlight(double starlight){
        this.starlight+=starlight;
    }
    private double getBurnTime(){
        return BURN_TIME*Math.pow(1.38, getUpgrades(Upgrade.ECOLOGICAL));
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    public boolean isStarlightActive(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL);
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){
        data.add("Coal: "+coal);
        data.add("Burning: "+burning);
        data.add("Auto-fuel: "+(autoFuel?"Enabled":"Disabled"));
        data.add("Power Production: "+(burning<=0?0:4+Math.pow(getLevel(),1.4)+getLevel()*Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE))*(starlight>STARLIGHT_THRESHOLD?2:0)));
    }
    @Override
    public int getVariants(){
        return 2;
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
}