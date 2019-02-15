package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Mine extends Building implements BuildingPowerConsumer, BuildingDamagable, BuildingDemolishable{
    private int timer = 0;
    private int delay = 20;
    private double power;
    public Mine(double x, double y){
        super(x,y,100,100,BuildingType.MINE);
    }
    public Mine(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x,y,100,100,BuildingType.MINE, level, upgrades);
        delay = 20-level;
    }
    private boolean canDeployItem(){
        int items = 0;
        for(DroppedItem item : Core.game.droppedItems){
            if(isClickWithinBounds(item.x+item.width/2, item.y+item.height/2, x, y, x+width, y+height)){
                items++;
            }
        }
        return items<25*getLevel();
    }
    public void deployItem(){
        if(!canDeployItem())return;
        double itemX = x+MenuGame.rand.nextInt(79)+11;
        double itemY = y+MenuGame.rand.nextInt(79)+11;
        itemX-=5;
        itemY-=5;
        Core.game.addItem(new DroppedItem(itemX, itemY, randomItem(), Core.game));
    }
    @Override
    public void update(){
        if(timer<=0){
            deployItem();
            if(hasUpgrade(Upgrade.POWER_TOOLS)&&power>=5&&canDeployItem()){
                if(MenuGame.rand.nextBoolean()){
                    deployItem();//1.5x items with power tools
                }
                power-=5;
            }
            timer = delay;
        }
        timer--;
        super.update();
    }
    @Override
    public void renderBackground() {
        super.draw();
        if(power>0)drawCenteredText(x, y, x+width, y+20, (int)power+"");
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
    }
    public void draw(){}
    @Override
    public int getMaxLevel(){
        return 20;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("timer", timer);
        cfg.set("delay", delay);
        return cfg;
    }
    public static Mine loadSpecific(Config cfg, double x, double y, int level, ArrayList<Upgrade> upgrades){
        Mine mine = new Mine(x, y, level, upgrades);
        mine.timer = cfg.get("timer", mine.timer);
        mine.delay = cfg.get("delay", mine.delay);
        return mine;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return 1.5;
    }
    @Override
    protected double getIgnitionChance(){
        return 1/3d;
    }
    @Override
    public String getName(){
        return "Level "+getLevel()+" Mine";
    }
    @Override
    public void upgrade(){
        super.upgrade();
        delay = 20-getLevel();
    }
    private Item randomItem(){
        double stoneThreshold = 0.5-getUpgrades(Upgrade.STONE_GRINDING)*.2;
        if(MenuGame.rand.nextDouble()<stoneThreshold)return Item.stone;
        if(MenuGame.rand.nextDouble()>.6)return Item.ironOre;
        return Item.coal;
    }
    @Override
    public double getMaxPower(){
        return hasUpgrade(Upgrade.POWER_TOOLS)?500:0;
    }
    @Override
    public double getDemand(){
        return hasUpgrade(Upgrade.POWER_TOOLS)?10:0;
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