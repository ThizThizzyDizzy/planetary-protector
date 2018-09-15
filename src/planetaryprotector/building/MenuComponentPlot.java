package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.building.task.TaskType;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentPlot extends MenuComponentBuilding{
    public MenuComponentPlot(double x, double y) {
        super(x, y, 100, 100, BuildingType.EMPTY);
    }
    @Override
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
        if(task!=null&&task.type==TaskType.CONSTRUCT){
            TaskConstruct c = (TaskConstruct) task;
            GL11.glColor4d(1, 1, 1, task.progress());
            c.target.renderBackground();
            GL11.glColor4d(1, 1, 1, 1);
        }
        renderDamages();
    }
    @Override
    public void render(){
        removeRenderBound();
        if(task!=null&&task.type==TaskType.CONSTRUCT){
            TaskConstruct c = (TaskConstruct) task;
            GL11.glColor4d(1, 1, 1, task.progress());
            if(c.target instanceof MenuComponentGenerator){
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+c.target.type.texture+" "+((int)((x%2)+1))+".png"));
            }else{
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+c.target.type.texture+".png"));
            }
            if(c.target instanceof MenuComponentSkyscraper){
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper) c.target;
                for(int i = 1; i<sky.floorCount; i++){
                    drawRect(x, y-i*sky.floorHeight, x+width, y+height-i*sky.floorHeight, ImageStash.instance.getTexture("/textures/buildings/"+c.target.type.texture+".png"));
                }
            }
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    @Override
    public void update(){
        if(damages.size()>10){
            Core.game.replaceBuilding(this, new MenuComponentWreck(x, y, 0));
        }
    }
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
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
        return cfg;
    }
    public static MenuComponentPlot loadSpecific(Config cfg) {
        MenuComponentPlot plot = new MenuComponentPlot(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            plot.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        return plot;
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
    @Override
    public String getName(){
        return "Empty Plot";
    }
}