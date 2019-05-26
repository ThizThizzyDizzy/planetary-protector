package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.building.task.TaskType;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Plot extends Building implements BuildingDamagable{
    public Plot(double x, double y) {
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
        drawMouseover();
    }
    @Override
    public void draw(){
        removeRenderBound();
        if(task!=null&&task.type==TaskType.CONSTRUCT){
            TaskConstruct c = (TaskConstruct) task;
            GL11.glColor4d(1, 1, 1, task.progress());
            if(c.target instanceof CoalGenerator){
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+c.target.type.texture+" "+((int)((x%2)+1))+".png"));
            }else{
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+c.target.type.texture+".png"));
            }
            if(c.target instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) c.target;
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
            Core.game.replaceBuilding(this, new Wreck(x, y, 0));
        }
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        return cfg;
    }
    public static Plot loadSpecific(Config cfg, double x, double y) {
        return new Plot(x, y);
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