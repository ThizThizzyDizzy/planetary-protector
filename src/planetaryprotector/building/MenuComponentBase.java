    package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoad;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import static planetaryprotector.menu.MenuGame.rand;
import java.util.Random;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentBase extends MenuComponentBuilding{
    public ArrayList<ItemStack> resources = new ArrayList<>();
    public int door = 0;
    public int deathTick = -1;
    public MenuComponentBase(double x, double y){
        super(x, y, 100, 100, BuildingType.BASE);
        enabled = true;
        resources.add(new ItemStack(Item.stone, 0));
        resources.add(new ItemStack(Item.coal, 0));
        resources.add(new ItemStack(Item.ironOre, 0));
        resources.add(new ItemStack(Item.ironIngot, 0));
    }
    @Override
    public void update(){
        if(Core.game==null)return;
        if(!Core.game.paused){
            boolean open = false;
            for(MenuComponentWorker worker : Core.game.workers){
                if(Core.game.distanceTo(worker, x+width/2, y+height-12.5)<=25){
                    open = true;
                    break;
                }
            }
            if(Core.game.workerCooldown<=16){
                open = true;
            }
            door = Math.min(16, Math.max(0, door+(open?1:-1)));
        }
        if(parent instanceof MenuLoad){
            return;
        }
        if(damages.size()>=10&&deathTick<451){
            deathTick++;
        }
        if(deathTick>-1){
            MenuGame game = (MenuGame) parent;
            game.losing = true;
            if(deathTick==0){
                Sounds.fadeSound("music");
            }
            if(deathTick%10==0&&deathTick<100){
                game.addParticleEffect(new MenuComponentParticle(x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            }
            if(deathTick==100){
                game.addParticleEffect(new MenuComponentParticle(x+50, y+51, ParticleEffectType.EXPLOSION, 10));
                for(MenuComponentWorker w : game.workers){
                    w.dead = true;
                }
            }
            if(deathTick==450){
                game.lose();
            }
        }
    }
    @Override
    public void render(){
        if(deathTick>100){
            for(MenuComponentBuildingDamage damage : damages){
                damage.x = -1000;
            }
            drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/wreck.png"));
            return;
        }
        drawRect(x, y-25, x+width, y+height, ImageStash.instance.getTexture("/textures/base/0.png"));
        if(Core.game==null){
            drawRect(x,y-25,x+width,y+height, ImageStash.instance.getTexture("/textures/base/door/0.png"));
            return;
        }
        drawRect(x,y-25,x+width,y+height, ImageStash.instance.getTexture("/textures/base/door/"+door+".png"));
        if(Core.game.finishedExpeditions.size()>0){
            drawText(x, y+height-height/8, x+width, y+height, Core.game.finishedExpeditions.size()+"");
        }
        renderDamages();
    }
    @Override
    public void render(int millisSinceLastTick){
        if(deathTick>100){
            render();
            return;
        }
        super.render(millisSinceLastTick);
    }
    @Override
    public void renderBackground(){
        if(deathTick>100)return;
        super.renderBackground();
    }
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
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
        Config c = Config.newConfig();
        c.set("count", resources.size());
        for(int i = 0; i<resources.size(); i++){
            ItemStack stack = resources.get(i);
            c.set(i+" item", stack.item.name);
            c.set(i+" count", stack.count);
        }
        cfg.set("Resources", c);
        return cfg;
    }
    public static MenuComponentBase loadSpecific(Config cfg) {
        MenuComponentBase base = new MenuComponentBase(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            base.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        base.resources.clear();
        Config c = cfg.get("Resources", Config.newConfig());
        for(int i = 0; i<c.get("count", 0); i++){
            base.resources.add(new ItemStack(Item.getItemByName(c.get(i+" item", "")), c.get(i+" count", 0)));
        }
        return base;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return 2.5;
    }
    @Override
    protected double getIgnitionChance(){
        return .1;
    }
    @Override
    protected void drawFire(){
        drawRect(x, y-25, x+width, y+height, getTexture("fire"));
    }
    @Override
    protected double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    @Override
    protected double getRandY(Random rand){
        return rand.nextDouble()*(height+25)-25;
    }
}