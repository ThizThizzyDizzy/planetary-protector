package planetaryprotector.building;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentGenerator extends MenuComponentBuilding{
    public int power = 0;
    public int maxPower = 200;
    public int transferAmount = 1;
    private int powerIncrease = 1;
    public MenuComponentGenerator(double x, double y) {
        super(x, y, 100, 100, BuildingType.GENERATOR);
    }
    public MenuComponentGenerator(double x, double y, int level) {
        super(x, y, 100, 100, BuildingType.GENERATOR);
        powerIncrease*=level+1;
        transferAmount*=level+1;
        maxPower*=level+1;
        this.level = level;
    }
    @Override
    public void update(){
        super.update();
        power+= powerIncrease;
        power = Math.max(0, Math.min(maxPower, power));
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+" "+((int)((x%2)+1))+".png"));
        renderDamages();
        drawCenteredText(x, y, x+width, y+20, "Power: "+power);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+(level+1));
    }
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        return true;
    }
    @Override
    public boolean canUpgrade(){
        return level<49;
    }
    @Override
    public MenuComponentBuilding getUpgraded() {
        return new MenuComponentGenerator(x, y, level+1);
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        cfg.set("power", power);
        cfg.set("maxPower", maxPower);
        cfg.set("level", level);
        for(int i = 0; i<damages.size(); i++){
            MenuComponentBuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        return cfg;
    }
    public static MenuComponentGenerator loadSpecific(Config cfg){
        MenuComponentGenerator generator = new MenuComponentGenerator(cfg.get("x", 0d), cfg.get("y",0d), cfg.get("level", 1));
        for(int i = 0; i<cfg.get("count", 0); i++){
            generator.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        generator.power = cfg.get("power", 0);
        generator.maxPower = cfg.get("maxPower", 200*(generator.level+1));
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
}