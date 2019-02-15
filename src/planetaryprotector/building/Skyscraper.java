package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.TaskSkyscraperAddFloor;
import planetaryprotector.building.task.TaskType;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public class Skyscraper extends Building implements BuildingDamagable, BuildingDemolishable{
    public int floorHeight = 10;
    public int floorCount = 0;
    public boolean falling;
    public boolean right = true;
    public int fallen;
    public static final int maxDamages = 10;
    public static final int maxHeight = 100;
    private boolean falled = false;
    public double pop = 0;
    public Skyscraper(double x, double y) {
        super(x, y, 100, 100, BuildingType.SKYSCRAPER);
        floorCount = MenuGame.rand.nextInt(40)+10;
    }
    @Override
    public void update(){
        pop*=1.000001;
        if(pop>getMaxPop()){
            pop = getMaxPop();
        }
        if(damages.size()>maxDamages){
            falling = true;
        }
        if(falling){
            synchronized(fires){
                for(Iterator<Particle> it = fires.iterator(); it.hasNext();){
                    Particle fire = it.next();
                    fire.offsetSubParticles(2);
                    if(fire.y-50>-fallen){
                        it.remove();
                        fire.x+=x;
                        fire.y+=y;
                        fire.fading = true;
                        Core.game.addParticleEffect(fire);
                    }
                }
            }
            for(int i = 0; i<MenuOptionsGraphics.particles*4+1; i++){
                Core.game.addParticleEffect(new Particle(MenuGame.rand.nextInt((int)width)+x-25, MenuGame.rand.nextInt((int)height)+y-fallen-25,ParticleEffectType.SMOKE, 1, false));
            }
            right = !right;
            x += right?1:-1;
            y+=2;
            fallen+=2;
            synchronized(Core.game.workers){
                for(Worker worker : Core.game.workers){
                    if(isClickWithinBounds(worker.x+(worker.width/2), worker.y+(worker.height/2), x, y-fallen, x+width, y+height-fallen)){
                        worker.dead = true;
                    }
                }
            }
            for(EnemyAlien alien : Core.game.aliens){
                if(isClickWithinBounds(alien.x+(alien.width/2), alien.y+(alien.height/2), x, y-fallen, x+width, y+height-fallen)){
                    alien.dead = true;
                }
            }
        }
        if(fallen>=floorHeight*floorCount){
            damages.clear();
            clearFires();
            y -= fallen;
            fallen = 0;
            falling = false;
            falled = true;
            if(!right){
                x++;
            }
            Core.game.replaceBuilding(this, new Wreck(x, y, floorCount*floorHeight));
        }
    }
    public boolean isSelectedWorkerBehind = false;
    @Override
    public void renderBackground(){
        drawRect(x, y-fallen, x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.EMPTY.texture+".png"));
    }
    @Override
    public void draw(){
        boolean seeThrough = isSelectedWorkerBehind||Core.game.hideSkyscrapers;
        GL11.glColor4d(1, 1, 1, seeThrough?.05:1);
        double fallenPercent = fallen/(floorHeight*(floorCount+0D));
        if(falled){
            drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.WRECK.texture+".png"));
            return;
        }
        for(int i = 0; i<floorCount; i++){
            drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
            if(!MenuOptionsGraphics.particulateFire){
                GL11.glColor4d(1, 1, 1, fire*(seeThrough?.05:1));
                drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, getTexture("fire"));
                GL11.glColor4d(1, 1, 1, seeThrough?.05:1);
            }
            if(i==floorCount-1){
                GL11.glColor4d(1, 1, 1, fallenPercent*(seeThrough?.05:1));
                drawRectWithBounds(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*(i+1))+height, x, y-(floorHeight*(floorCount-1)), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.WRECK.texture+".png"));
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        if(task!=null&&task.type==TaskType.SKYSCRAPER_ADD_FLOOR){
            for(int i = floorCount; i<floorCount+((TaskSkyscraperAddFloor)task).floors; i++){
                GL11.glColor4d(1, 1, 1, task.progress()*(seeThrough?.05:1));
                drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*(floorCount+((TaskSkyscraperAddFloor)task).floors)), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
                if(i==(floorCount+((TaskSkyscraperAddFloor)task).floors)-1){
                    GL11.glColor4d(1, 1, 1, fallenPercent*task.progress*(seeThrough?.05:1));
                    drawRectWithBounds(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*(i+1))+height, x, y-(floorHeight*((floorCount+((TaskSkyscraperAddFloor)task).floors)-1)), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.WRECK.texture+".png"));
                }
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        GL11.glColor4d(1, 1, 1, 1);
        renderDamages();
    }
    @Override
    public boolean onDamage(double x, double y){
        pop*=0.95;
        return super.onDamage(x, y-fallen);
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("floors", floorCount);
        cfg.set("falling", falling);
        cfg.set("fallen", fallen);
        cfg.set("falled", falled);
        cfg.set("population", pop);
        return cfg;
    }
    public static Skyscraper loadSpecific(Config cfg, double x, double y) {
        Skyscraper sky = new Skyscraper(x, y);
        sky.floorCount = cfg.get("floors", 10);
        sky.falling = cfg.get("falling", false);
        sky.fallen = cfg.get("fallen", 0);
        sky.falled = cfg.get("falled", false);
        try{
            sky.pop = cfg.get("population", 0d);
        }catch(ClassCastException ex){
            sky.pop = cfg.get("population", 0);
        }
        return sky;
    }
    public int getMaxPop(){
        int pop = 0;
        pop += Core.game.popPerFloor*floorCount;
        pop -= Core.game.popPerFloor*damages.size()*floorCount/5;
        pop = Math.max(0, pop);
        return pop;
    }
    public int addPop(int amount){
        while(pop<getMaxPop()&&amount>0){
            amount--;
            pop++;
        }
        return amount;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return 1;
    }
    @Override
    protected double getIgnitionChance(){
        return 1;
    }
    @Override
    protected void drawFire(){}
    @Override
    protected double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    @Override
    protected double getRandY(Random rand){
        return rand.nextDouble()*(height+(floorCount-1)*floorHeight)-(floorCount-1)*floorHeight;
    }
    @Override
    public String getName(){
        return floorCount+" Floor Skyscraper";
    }
}