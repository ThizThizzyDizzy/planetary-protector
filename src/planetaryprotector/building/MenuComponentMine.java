package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.MenuComponentDroppedItem;
import planetaryprotector.menu.MenuGame;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentMine extends MenuComponentBuilding{
    private int timer = 0;
    private int delay = 20;
    public MenuComponentMine(double x, double y){
        super(x,y,100,100,BuildingType.MINE);
    }
    public MenuComponentMine(double x, double y, int level){
        super(x,y,100,100,BuildingType.MINE);
        delay -= level+1;
        this.level = level;
    }
    public void deployItem(){
        int items = 0;
        for(MenuComponentDroppedItem item : Core.game.droppedItems){
            if(isClickWithinBounds(item.x+item.width/2, item.y+item.height/2, x, y, x+width, y+height)){
                items++;
            }
        }
        if(items>25*level){
            return;
        }
        double itemX = x+MenuGame.rand.nextInt(79)+11;
        double itemY = y+MenuGame.rand.nextInt(79)+11;
        itemX-=5;
        itemY-=5;
        Item item = null;
        while(item!=Item.coal&&item!=Item.stone&&item!=Item.ironOre){
            item = Item.items.get(MenuGame.rand.nextInt(Item.items.size()));
        }
        Core.game.addItem(new MenuComponentDroppedItem(itemX, itemY, item, Core.game));
    }
    @Override
    public void update(){
        if(timer<=0){
            deployItem();
            timer = delay;
        }
        timer--;
        super.update();
    }
    @Override
    public void renderBackground() {
        super.render();
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+(level+1));
    }
    public void render(){}
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        return false;
    }
    @Override
    public boolean canUpgrade(){
        return level<19;
    }
    @Override
    public MenuComponentBuilding getUpgraded(){
        return new MenuComponentMine(x, y, level+1);
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        cfg.set("level", level);
        for(int i = 0; i<damages.size(); i++){
            MenuComponentBuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("timer", timer);
        cfg.set("delay", delay);
        return cfg;
    }
    public static MenuComponentMine loadSpecific(Config cfg){
        MenuComponentMine mine = new MenuComponentMine(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            mine.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        mine.level = cfg.get("level", 1);
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
}