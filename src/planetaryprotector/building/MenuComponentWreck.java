package planetaryprotector.building;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentWreck extends MenuComponentBuilding{
    public int ingots;
    int progress;
    public MenuComponentWreck(double x, double y, int ingots){
        super(x, y, 100, 100, BuildingType.WRECK);
        this.ingots = ingots;
    }
    @Override
    public void renderBackground(){
        if(task!=null){
            return;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
    }
    @Override
    public void render(){}
    @Override
    public boolean onDamage(double x, double y){
        ingots = Math.max(0,ingots-10);
        return false;
    }
    @Override
    public boolean canUpgrade(){
        return false;
    }
    @Override
    public MenuComponentBuilding getUpgraded() {
        return null;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        for(int i = 0; i<damages.size(); i++){
            MenuComponentBuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("ingots", ingots);
        cfg.set("prrogress", progress);
        return cfg;
    }
    public static MenuComponentWreck loadSpecific(Config cfg){
        MenuComponentWreck wreck = new MenuComponentWreck(cfg.get("x", 0d), cfg.get("y",0d), cfg.get("ingots", 1));
        for(int i = 0; i<cfg.get("count", 0); i++){
            wreck.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        wreck.level = cfg.get("level", 1);
        wreck.progress = cfg.get("progress", wreck.progress);
        return wreck;
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    public String getName(){
        return "Wreck ("+ingots+" ingots)";
    }
}