package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoad;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.TaskSkyscraperAddFloor;
import planetaryprotector.building.task.TaskType;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public class MenuComponentSkyscraper extends MenuComponentBuilding{
    public int floorHeight = 10;
    public int floorCount = 0;
    public boolean falling;
    public boolean right = true;
    public int fallen;
    public static final int maxDamages = 10;
    public static final int maxHeight = 100;
    public MenuGame game;
    private boolean falled = false;
    public double pop = 0;
    public MenuComponentSkyscraper(double x, double y) {
        super(x, y, 100, 100, BuildingType.SKYSCRAPER);
        floorCount = MenuGame.rand.nextInt(40)+10;
    }
    @Override
    public void update(){
        if(parent instanceof MenuLoad) return;
        if(game==null)game = (MenuGame)parent;
        pop*=1.000001;
        if(pop>getMaxPop()){
            pop = getMaxPop();
        }
        if(damages.size()>maxDamages){
            falling = true;
        }
        if(falling){
            synchronized(fires){
                for(Iterator<MenuComponentParticle> it = fires.iterator(); it.hasNext();){
                    MenuComponentParticle fire = it.next();
                    fire.offsetSubParticles(2);
                    if(fire.y-50>-fallen){
                        components.remove(fire);
                        it.remove();
                        fire.x+=x;
                        fire.y+=y;
                        fire.fading = true;
                        Core.game.addParticleEffect(fire);
                    }
                }
            }
            for(int i = 0; i<MenuOptionsGraphics.particles*4+1; i++){
                game.addParticleEffect(new MenuComponentParticle(MenuGame.rand.nextInt((int)width)+x-25, MenuGame.rand.nextInt((int)height)+y-fallen-25,ParticleEffectType.SMOKE, 1, false));
            }
            right = !right;
            x += right?1:-1;
            y+=2;
            fallen+=2;
            for(MenuComponentWorker worker : game.workers){
                if(isClickWithinBounds(worker.x+(worker.width/2), worker.y+(worker.height/2), x, y-fallen, x+width, y+height-fallen)){
                    worker.dead = true;
                }
            }
            for(EnemyAlien alien : game.aliens){
                if(isClickWithinBounds(alien.x+(alien.width/2), alien.y+(alien.height/2), x, y-fallen, x+width, y+height-fallen)){
                    alien.dead = true;
                }
            }
        }
        if(fallen>=floorHeight*floorCount){
            components.removeAll(damages);
            damages.clear();
            y -= fallen;
            fallen = 0;
            falling = false;
            falled = true;
            if(!right){
                x++;
            }
            game.replaceBuilding(this, new MenuComponentWreck(x, y, floorCount*floorHeight));
        }
    }
    public boolean isSelectedWorkerBehind = false;
    @Override
    public void renderBackground(){
        drawRect(x, y-fallen, x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.EMPTY.texture+".png"));
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, isSelectedWorkerBehind?.05:1);
        double fallenPercent = fallen/(floorHeight*(floorCount+0D));
        if(falled){
            drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.WRECK.texture+".png"));
            return;
        }
        for(int i = 0; i<floorCount; i++){
            drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
            if(!MenuOptionsGraphics.particulateFire){
                GL11.glColor4d(1, 1, 1, fire*(isSelectedWorkerBehind?.05:1));
                drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, getTexture("fire"));
                GL11.glColor4d(1, 1, 1, isSelectedWorkerBehind?.05:1);
            }
            if(i==floorCount-1){
                GL11.glColor4d(1, 1, 1, fallenPercent*(isSelectedWorkerBehind?.05:1));
                drawRectWithBounds(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*(i+1))+height, x, y-(floorHeight*(floorCount-1)), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.WRECK.texture+".png"));
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        if(task!=null&&task.type==TaskType.SKYSCRAPER_ADD_FLOOR){
            for(int i = floorCount; i<floorCount+((TaskSkyscraperAddFloor)task).floors; i++){
                GL11.glColor4d(1, 1, 1, task.progress()*(isSelectedWorkerBehind?.05:1));
                drawRectWithBounds(x, y-(floorHeight*i), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*(floorCount+((TaskSkyscraperAddFloor)task).floors)), x+width, y+height-fallen, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
                if(i==(floorCount+((TaskSkyscraperAddFloor)task).floors)-1){
                    GL11.glColor4d(1, 1, 1, fallenPercent*task.progress*(isSelectedWorkerBehind?.05:1));
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
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25-fallen, 50, 50)));
        pop*=0.95;
        return true;
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
        cfg.set("floors", floorCount);
        cfg.set("falling", falling);
        cfg.set("fallen", fallen);
        cfg.set("falled", falled);
        cfg.set("population", pop);
        return cfg;
    }
    public static MenuComponentSkyscraper loadSpecific(Config cfg) {
        MenuComponentSkyscraper sky = new MenuComponentSkyscraper(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            sky.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
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