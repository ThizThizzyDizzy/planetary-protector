package planetaryprotector.building;
import java.util.ArrayList;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Wreck extends Building{
    public int ingots;
    int progress;
    public Wreck(double x, double y, int ingots){
        super(x, y, 100, 100, BuildingType.WRECK);
        this.ingots = ingots;
    }
    @Override
    public void renderBackground(){
        if(task!=null){
            return;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
        drawMouseover();
    }
    @Override
    public void draw(){}
    @Override
    public boolean onDamage(double x, double y){
        ingots = Math.max(0,ingots-10);
        return false;
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        for(int i = 0; i<damages.size(); i++){
            BuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("ingots", ingots);
        cfg.set("prrogress", progress);
        return cfg;
    }
    public static Wreck loadSpecific(Config cfg, double x, double y){
        Wreck wreck = new Wreck(x, y, cfg.get("ingots", 1));
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
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data) {
        data.add("Ingots: "+ingots);
        data.add("Progress: "+progress);
    }
}