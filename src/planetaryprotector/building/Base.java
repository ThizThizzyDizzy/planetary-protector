    package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import static planetaryprotector.menu.MenuGame.rand;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Base extends Building implements BuildingDamagable{
    public ArrayList<ItemStack> resources = new ArrayList<>();
    public int door = 0;
    public int deathTick = -1;
    public Base(double x, double y){
        super(x, y, 100, 100, BuildingType.BASE);
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
            for(Worker worker : Core.game.workers){
                if(Core.distance(worker, x+width/2, y+height-12.5)<=25){
                    open = true;
                    break;
                }
            }
            if(Core.game.workerCooldown<=16){
                open = true;
            }
            door = Math.min(16, Math.max(0, door+(open?1:-1)));
        }
        if(damages.size()>=10&&deathTick<451){
            deathTick++;
        }
        if(deathTick>-1){
            Core.game.losing = true;
            if(deathTick==0){
                Sounds.stopSound("music");
                Sounds.playSound("music", "EndMusic1");
            }
            if(deathTick%10==0&&deathTick<100){
                Core.game.addParticleEffect(new Particle(x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            }
            if(deathTick==100){
                Core.game.addParticleEffect(new Particle(x+50, y+51, ParticleEffectType.EXPLOSION, 10));
                for(Worker w : Core.game.workers){
                    w.dead = true;
                }
            }
            if(deathTick==450){
                Core.game.lose();
            }
        }
    }
    @Override
    public void draw(){
        if(deathTick>100){
            for(BuildingDamage damage : damages){
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
        renderDamages();
        GL11.glColor4d(0, 1, 1, mouseover);
        drawRect(x, y-25, x+width, y+height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        if(Core.game.finishedExpeditions.size()>0){
            drawText(x, y+height-height/8, x+width, y+height, Core.game.finishedExpeditions.size()+"");
        }
    }
    @Override
    public void render(){
        if(deathTick>100){
            draw();
            return;
        }
        super.render();
    }
    @Override
    public void renderBackground(){
        if(deathTick>100)return;
        super.renderBackground();
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        Config c = Config.newConfig();
        c.set("count", resources.size());
        for(int i = 0; i<resources.size(); i++){
            ItemStack stack = resources.get(i);
            if(stack.item==null)continue;
            c.set(i+" item", stack.item.name);
            c.set(i+" count", stack.count);
        }
        cfg.set("Resources", c);
        return cfg;
    }
    public static Base loadSpecific(Config cfg, double x, double y) {
        Base base = new Base(x, y);
        base.resources.clear();
        Config c = cfg.get("Resources", Config.newConfig());
        for(int i = 0; i<c.get("count", 0); i++){
            Item item = Item.getItemByName(c.get(i+" item", ""));
            if(item==null)continue;
            base.resources.add(new ItemStack(item, c.get(i+" count", 0)));
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
    @Override
    public String getName(){
        return "Base";
    }
}