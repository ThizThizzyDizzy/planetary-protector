package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.Task;
import java.util.ArrayList;
import static planetaryprotector.menu.MenuGame.rand;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public abstract class MenuComponentBuilding extends MenuComponentButton{
    public final BuildingType type;
    public MenuComponentWorker worker;
    public ArrayList<MenuComponentBuildingDamage> damages = new ArrayList<>();
    public Task task;
    public int level = 0;
    //FIRE
    public double fire = 0;
    public int fireDamage = 0;
    public double fireIncreaseRate = 0;
    public final ArrayList<MenuComponentParticle> fires = new ArrayList<>();
    public MenuComponentBuilding(double x, double y, double width, double height, BuildingType type){
        super(x, y, width, height, "", false);
        this.type = type;
    }
    @Override
    public void tick(){
        fire+=fireIncreaseRate;
        if(fire-fireDamage*getFireDestroyThreshold()*.1>=getFireDestroyThreshold()*.1){
            damages.add(add(new MenuComponentBuildingDamage(0, 0, 0, 0)));
            if(MenuOptionsGraphics.particulateFire){
                add(new MenuComponentParticle(getRandX(Core.game.rand), getRandY(Core.game.rand), ParticleEffectType.FIRE));
                ignite();
            }
            fireDamage++;
        }
        synchronized(fires){
            for(MenuComponentParticle particle : fires){
                particle.tick();
            }
        }
        update();
    }
    public void update(){
        if(damages.size()>=10){
            MenuGame game = (MenuGame) parent;
            game.addParticleEffect(new MenuComponentParticle(x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            int ingots = 0;
            try{
                for(int i = 0; i<level; i++){
                    ingots += type.costs[i][0].count;
                }
            }catch(ArrayIndexOutOfBoundsException ex){}
            game.replaceBuilding(this, new MenuComponentWreck(x, y, ingots));
        }
    }
    @Override
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.EMPTY.texture+".png"));
        renderDamages();
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, getTexture());
        renderDamages();
    }
    public void renderDamages(){
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        translate(x, y);
        for(MenuComponentBuildingDamage damage : damages){
            damage.render();
        }
        untranslate();
        GL11.glPopMatrix();
    }
    @Override
    public void render(int millisSinceLastTick){
        render();
        if(!MenuOptionsGraphics.particulateFire){
            GL11.glColor4d(1, 1, 1, fire);
            drawFire();
            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        translate(x, y);
        synchronized(fires){
            for(MenuComponentParticle fire : fires){
                fire.render();
            }
        }
        untranslate();
        GL11.glPopMatrix();
    }
    protected double getIgnitionChance(){
        return 1;
    }
    public abstract MenuComponentBuilding getUpgraded();
    public static MenuComponentBuilding generateRandomBuilding(MenuComponentBase base, ArrayList<MenuComponentBuilding> buildings){
        int buildingX;
        int buildingY;
        int i = 0;
        WHILE:while(true){
            i++;
            if(i>1000){
                return null;
            }
            buildingX = MenuGame.rand.nextInt(Display.getWidth()-100);
            buildingY = MenuGame.rand.nextInt(Display.getHeight()-100);
            if(isClickWithinBounds(buildingX, buildingY, base.x, base.y, base.x+base.width, base.y+base.height)||
                 isClickWithinBounds(buildingX+100, buildingY, base.x, base.y, base.x+base.width, base.y+base.height)||
                 isClickWithinBounds(buildingX, buildingY+100, base.x, base.y, base.x+base.width, base.y+base.height)||
                 isClickWithinBounds(buildingX+100, buildingY+100, base.x, base.y, base.x+base.width, base.y+base.height)){
                continue;
            }
            for(MenuComponentBuilding building : buildings){
                double Y = building.y;
                if(building instanceof MenuComponentSkyscraper){
                    Y-=((MenuComponentSkyscraper) building).fallen;
                }
                if(isClickWithinBounds(buildingX, buildingY, building.x, Y, building.x+building.width, Y+building.height)||
                     isClickWithinBounds(buildingX+100, buildingY, building.x, Y, building.x+building.width, Y+building.height)||
                     isClickWithinBounds(buildingX, buildingY+100, building.x, Y, building.x+building.width, Y+building.height)||
                     isClickWithinBounds(buildingX+100, buildingY+100, building.x, Y, building.x+building.width, Y+building.height)){
                    continue WHILE;
                }
            }
            break;
        }
        return new MenuComponentSkyscraper(buildingX, buildingY);
    }
    public static MenuComponentBuilding load(Config cfg){
        MenuComponentBuilding b = null;
        //TODO save and load fires and fire stats
        switch(BuildingType.valueOf(cfg.get("type", "EMPTY"))){
            case WORKSHOP:
                b = MenuComponentWorkshop.loadSpecific(cfg);
                break;
            case OBSERVATORY:
                b = MenuComponentObservatory.loadSpecific(cfg);
                break;
            case BASE:
                b = MenuComponentBase.loadSpecific(cfg);
                break;
            case BUNKER:
                b = MenuComponentBunker.loadSpecific(cfg);
                break;
            case SILO:
                b = MenuComponentSilo.loadSpecific(cfg);
                break;
            case EMPTY:
                b = MenuComponentPlot.loadSpecific(cfg);
                break;
            case GENERATOR:
                b = MenuComponentGenerator.loadSpecific(cfg);
                break;
            case MINE:
                b = MenuComponentMine.loadSpecific(cfg);
                break;
            case SHIELD_GENERATOR:
                b = MenuComponentShieldGenerator.loadSpecific(cfg);
                break;
            case SKYSCRAPER:
                b = MenuComponentSkyscraper.loadSpecific(cfg);
                break;
            case WRECK:
                b = MenuComponentWreck.loadSpecific(cfg);
                break;
        }
        for(MenuComponentBuildingDamage damage : b.damages){
            if(!b.components.contains(damage)){
                b.add(damage);
            }
        }
        b.fire = cfg.get("fire", b.fire);
        b.fireDamage = cfg.get("fire damage", b.fireDamage);
        b.fireIncreaseRate = cfg.get("fire increase", b.fireIncreaseRate);
        for(int i = 0; i<cfg.get("fires", 0); i++){
            b.fires.add(new MenuComponentParticle(cfg.get("fire "+i+" x", 0d), cfg.get("fire "+i+" y", 0d), ParticleEffectType.FIRE));
        }
        for(MenuComponentParticle particle : b.fires){
            if(!b.components.contains(particle)){
                b.add(particle);
            }
        }
        return b;
    }
    public boolean damage(double x, double y){
        if(MenuOptionsGraphics.fire&&Core.game.rand.nextDouble()<getIgnitionChance()){
            fireIncreaseRate += 0.0002;
            ignite(x, y);
        }
        return onDamage(x, y);
    }
    /**
     * damage the building at a certain location
     * @param x x-value of the damage
     * @param y y-value of the damage
     * @return true if the damage was stopped, or false if the damage should hit the ground
     */
    public abstract boolean onDamage(double x, double y);
    public Config saveBuilding(Config cfg){
        save(cfg);
        cfg.set("fire", fire);
        cfg.set("fire damage", fireDamage);
        cfg.set("fire increase", fireIncreaseRate);
        cfg.set("fires", fires.size());
        for(int i = 0; i<fires.size(); i++){
            MenuComponentParticle particle = fires.get(i);
            cfg.set("fire "+i+" x", particle.x);
            cfg.set("fire "+i+" y", particle.y);
        }
        return cfg;
    }
    protected abstract Config save(Config cfg);
    public abstract boolean canUpgrade();
    protected void ignite(double x, double y){
        if(!MenuOptionsGraphics.particulateFire)return;
        x-=this.x;
        y-=this.y;
        if(this instanceof MenuComponentBase){
            y-=25;
        }
        fires.add(add(new MenuComponentParticle(x-25, y-25, ParticleEffectType.FIRE)));
    }
    protected void ignite(){
        ignite(x+getRandX(Core.game.rand),y+getRandY(Core.game.rand));
    }
    protected int getTexture(){
        return ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png");
    }
    protected int getTexture(String subtype){
        return ImageStash.instance.getTexture("/textures/buildings/"+type.texture+" "+subtype+".png");
    }
    protected double getFireDestroyThreshold(){
        return 1.1;
    }
    protected void drawFire(){
        drawRect(x, y, x+width, y+height, getTexture("fire"));
    }
    protected double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    protected double getRandY(Random rand){
        return rand.nextDouble()*height;
    }
    public void clearFires(){
        for(MenuComponentParticle fire : fires){
            fire.x+=x;
            fire.y+=y;
            fire.fading = true;
            Core.game.addParticleEffect(fire);
        }
        components.removeAll(fires);
        fires.clear();
    }
    public abstract String getName();
}