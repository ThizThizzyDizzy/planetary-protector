package planetaryprotector.building;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.Core;
import planetaryprotector.enemy.MenuComponentEnemy;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoad;
import org.lwjgl.opengl.Display;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentShieldGenerator extends BuildingPowerConsumer{
    public double shieldSize = 0;
    public double maxShieldSize = 500;
    public double shieldStrength = 1;
    public double oldPower;
    public double blastRecharge = 0;
    public boolean canBlast = false;
    public final MenuComponentShield shield;
    public boolean shieldOutline = false;
    public boolean powerOutline = false;
    public MenuComponentShieldGenerator(double x, double y){
        super(x, y, 100, 100, BuildingType.SHIELD_GENERATOR);
        shield = add(new MenuComponentShield(this));
        maxPower = Integer.MAX_VALUE;
    }
    public MenuComponentShieldGenerator(double x, double y, int level){
        super(x, y, 100, 100, BuildingType.SHIELD_GENERATOR);
        this.level = level;
        canBlast = level>=10;
        shieldStrength = getStats(level);
        maxShieldSize += (level)*250;
        shield = add(new MenuComponentShield(this));
    }
    public void blast(){
        if(!canBlast||blastRecharge!=0) return;
        blastRecharge = -50;
    }
    @Override
    public void update(){
        super.update();
        if(parent instanceof MenuLoad) return;
        MenuGame game = Core.game;
        if(blastRecharge>0) blastRecharge--;
        if(blastRecharge<0){//<editor-fold defaultstate="collapsed" desc="Shield blast">
            blastRecharge++;
            double size = (Display.getWidth()*1.8)-(((-blastRecharge)%10)*(Display.getWidth()/5));
            shieldSize = size;
            game.pushParticles(x+width/2, y+height/2, size, size/50);
            if(size==0){
                for(MenuComponentEnemy enemy : game.enemies){
                    enemy.dead = true;
                }
                game.meteorShower = false;
                game.shower.opacitizing = -1;
                for(AsteroidMaterial mat : AsteroidMaterial.values()){
                    mat.timer+=100;
                }
                if(game.mothership!=null){
                    game.mothership.shieldBlast();
                }
            }
            if(blastRecharge==0){
                blastRecharge = 20*60*10;
                shieldSize = 0;
                power = 0;
            }
        }//</editor-fold>
        double surface = 2*Math.PI*Math.pow(shieldSize/2,2);
        double powerDrawFactor = 50_000D;
        power -= surface/powerDrawFactor;
        double maxSurface = power/100*powerDrawFactor;
        double maxSize = Math.sqrt(maxSurface/2/Math.PI)*2;
        if(shieldSize>maxSize){
            shieldSize--;
        }
        shieldSize+=(maxSize-shieldSize)/100;
        oldPower = power;
        if(power<=0){
            shieldSize = 0;
            power = 0;
        }
        shieldSize = Math.max(0, Math.min(maxShieldSize, shieldSize));
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
        renderDamages();
        if(powerOutline){
            drawRect(x+width/2-250, y+height/2-250, x+width/2+250, y+height/2+250, ImageStash.instance.getTexture("/textures/buildings/power outline.png"));
        }
        drawCenteredText(x, y, x+width, y+20, (int)power+":"+(int)shieldSize);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+(level+1));
    }
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        return true;
    }
    @Override
    public boolean canUpgrade(){
        return level<19;
    }
    @Override
    public MenuComponentBuilding getUpgraded(){
        MenuComponentShieldGenerator s = new MenuComponentShieldGenerator(x, y, level+1);
        s.shieldOutline = shieldOutline;
        s.powerOutline = powerOutline;
        return s;
    }
    /**
     * @return the shieldSize
     */
    public double getShieldSize(){
        return shieldSize;
    }
    public double getShieldSize(double damage){
        double difference = -damage;
        double size = shieldSize;
        size += difference/shieldStrength;
        if(size<0){
            return 0;
        }
        return size;
    }
    /**
     * @param shieldSize the shieldSize to set
     */
    public void setShieldSize(double shieldSize){
        double difference = shieldSize-this.shieldSize;
        this.shieldSize += difference/shieldStrength;
        if(this.shieldSize<0){
            this.shieldSize = 0;
        }
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
        cfg.set("power", power);
        cfg.set("blastRecharge", blastRecharge);
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("level", level);
        cfg.set("size", shieldSize);
        cfg.set("maxSize", maxShieldSize);
        cfg.set("strength", shieldStrength);
        cfg.set("oldPower", oldPower);
        cfg.set("shieldOutline", shieldOutline);
        return cfg;
    }
    public static MenuComponentShieldGenerator loadSpecific(Config cfg){
        MenuComponentShieldGenerator generator = new MenuComponentShieldGenerator(cfg.get("x", 0d), cfg.get("y",0d), cfg.get("level", 1));
        for(int i = 0; i<cfg.get("count", 0); i++){
            generator.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        if(cfg.hasProperty("power")&&cfg.get("power") instanceof Integer){
            generator.power = cfg.get("power", 0);
        }else{
            generator.power = cfg.get("power", 0d);
        }
        generator.blastRecharge = cfg.get("blastRecharge", 0d);
        generator.shieldSize = cfg.get("size", 0d);
        generator.maxShieldSize = cfg.get("maxSize", 500d);
        generator.shieldStrength = cfg.get("strength", 1d);
        generator.oldPower = cfg.get("oldPower", 0d);
        generator.shieldOutline = cfg.get("shieldOutline", false);
        return generator;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return .6;
    }
    @Override
    protected double getIgnitionChance(){
        return .8;
    }
    @Override
    public String getName(){
        return "Level "+(level+1)+" Shield Generator";
    }
    private double getStats(int level){
        level++;
        return Math.max(level, (49/400d)*Math.pow(level, 2)+1);
    }
}