package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.particle.Particle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.Task;
import java.util.ArrayList;
import java.util.Collections;
import static planetaryprotector.menu.MenuGame.rand;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public abstract class Building extends GameObject{
    public final BuildingType type;
    public ArrayList<BuildingDamage> damages = new ArrayList<>();
    public Task task;
    public double mouseover = 0;
    //FIRE
    public double fire = 0;
    public int fireDamage = 0;
    public double fireIncreaseRate = 0;
    public final ArrayList<Particle> fires = new ArrayList<>();
    //Levels
    private int level = 0;
    protected final ArrayList<Upgrade> upgrades = new ArrayList<>();
    public Building(double x, double y, double width, double height, BuildingType type){
        this(x, y, width, height, type, 1, new ArrayList<>());
    }
    public Building(double x, double y, double width, double height, BuildingType type, int level, ArrayList<Upgrade> upgrades){
        super(x, y, width, height);
        this.type = type;
        this.level = level;
        this.upgrades.clear();
        this.upgrades.addAll(upgrades);
    }
    public void tick(){
        fire+=fireIncreaseRate;
        if(fire-fireDamage*getFireDestroyThreshold()*.1>=getFireDestroyThreshold()*.1){
            synchronized(damages){
                damages.add(new BuildingDamage(this,0,0));
            }
            synchronized(fires){
                fires.add(new Particle(getRandX(Core.game.rand), getRandY(Core.game.rand), ParticleEffectType.FIRE));
            }
            ignite();
            fireDamage++;
        }
        synchronized(fires){
            for(Particle particle : fires){
                particle.tick();
            }
        }
        update();
    }
    public void update(){
        if(damages.size()>=10){
            Core.game.addParticleEffect(new Particle(x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            int ingots = 0;
            try{
                for(int i = 0; i<getLevel(); i++){
                    ingots += type.costs[i][0].count;
                }
            }catch(ArrayIndexOutOfBoundsException ex){}
            Core.game.replaceBuilding(this, new Wreck(x, y, ingots));
        }
    }
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+BuildingType.EMPTY.texture+".png"));
        renderDamages();
    }
    public void drawMouseover(){
        GL11.glColor4d(0, 1, 1, mouseover);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void draw(){
        drawRect(x, y, x+width, y+height, getTexture());
        renderDamages();
    }
    public void renderDamages(){
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        synchronized(damages){
            for(BuildingDamage damage : damages){
                damage.render();
            }
        }
        GL11.glPopMatrix();
    }
    @Override
    public void render(){
        draw();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        synchronized(fires){
            for(Particle fire : fires){
                fire.render();
            }
        }
        GL11.glPopMatrix();
    }
    protected double getIgnitionChance(){
        return 1;
    }
    public void upgrade(){
        level++;
        Core.game.refreshNetworks();
    }
    public static Building generateRandomBuilding(Base base, ArrayList<Building> buildings){
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
            for(Building building : buildings){
                double Y = building.y;
                if(building instanceof Skyscraper){
                    Y-=((Skyscraper) building).fallen;
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
        return new Skyscraper(buildingX, buildingY);
    }
    public static Building load(Config cfg){
        Building b = null;
        double x = cfg.get("x", 0d);
        double y = cfg.get("y", 0d);
        int level = cfg.get("level", 1);
        ArrayList<Upgrade> upgrades = new ArrayList<>();
        for(int i = 0; i<cfg.get("upgrades", 0); i++){
            upgrades.add(Upgrade.valueOf(cfg.get("upgrade "+i, "N/A")));
        }
        switch(BuildingType.valueOf(cfg.get("type", "EMPTY"))){
            case WORKSHOP:
                b = Workshop.loadSpecific(cfg,x,y);
                break;
            case OBSERVATORY:
                b = Observatory.loadSpecific(cfg,x,y);
                break;
            case BASE:
                b = Base.loadSpecific(cfg,x,y);
                break;
            case BUNKER:
                b = Bunker.loadSpecific(cfg,x,y);
                break;
            case SILO:
                b = Silo.loadSpecific(cfg,x,y,level,upgrades);
                break;
            case EMPTY:
                b = Plot.loadSpecific(cfg,x,y);
                break;
            case MINE:
                b = Mine.loadSpecific(cfg,x,y,level,upgrades);
                break;
            case SHIELD_GENERATOR:
                b = ShieldGenerator.loadSpecific(cfg,x,y,level,upgrades);
                break;
            case SKYSCRAPER:
                b = Skyscraper.loadSpecific(cfg,x,y);
                break;
            case WRECK:
                b = Wreck.loadSpecific(cfg,x,y);
                break;
            case COAL_GENERATOR:
                b = CoalGenerator.loadSpecific(cfg,x,y,level,upgrades);
                break;
            case SOLAR_GENERATOR:
                b = SolarGenerator.loadSpecific(cfg,x,y,level,upgrades);
                break;
            case POWER_STORAGE:
                b = PowerStorage.loadSpecific(cfg,x,y,level,upgrades);
                break;
//            case LABORATORY:
//                b = MenuComponentLaboratory.loadSpecific(cfg,x,y);
//                break;
        }
        for(int i = 0; i<cfg.get("count", 0); i++){
            b.damages.add(new BuildingDamage(b, cfg.get(i+" x", 0d), cfg.get(i+" y", 0d)));
        }
        b.fire = cfg.get("fire", b.fire);
        b.fireDamage = cfg.get("fire damage", b.fireDamage);
        b.fireIncreaseRate = cfg.get("fire increase", b.fireIncreaseRate);
        for(int i = 0; i<cfg.get("fires", 0); i++){
            b.fires.add(new Particle(cfg.get("fire "+i+" x", 0d), cfg.get("fire "+i+" y", 0d), ParticleEffectType.FIRE));
        }
        return b;
    }
    public boolean damage(double x, double y){
        if(Core.game.rand.nextDouble()<getIgnitionChance()){
            fireIncreaseRate += 0.0002;
            ignite(x, y);
        }
        if(this instanceof BuildingDamagable){
            return onDamage(x, y);
        }
        return false;
    }
    /**
     * damage the building at a certain location
     * @param x x-value of the damage
     * @param y y-value of the damage
     * @return true if the damage was stopped, or false if the damage should hit the ground
     */
    public boolean onDamage(double x, double y){
        synchronized(damages){
            damages.add(new BuildingDamage(this, x-25, y-25));
        }
        return true;
    }
    public Config saveBuilding(Config cfg){
        save(cfg);
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        cfg.set("level", getLevel());
        cfg.set("upgrades", upgrades.size());
        for(int i = 0; i<upgrades.size(); i++){
            Upgrade upgrade = upgrades.get(i);
            cfg.set("upgrade "+i, upgrade.name());
        }
        for(int i = 0; i<damages.size(); i++){
            BuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("fire", fire);
        cfg.set("fire damage", fireDamage);
        cfg.set("fire increase", fireIncreaseRate);
        cfg.set("fires", fires.size());
        for(int i = 0; i<fires.size(); i++){
            Particle particle = fires.get(i);
            cfg.set("fire "+i+" x", particle.x);
            cfg.set("fire "+i+" y", particle.y);
        }
        if(this instanceof BuildingPowerConsumer){
            cfg.set("power", ((BuildingPowerConsumer)this).getPower());
        }
        return cfg;
    }
    protected abstract Config save(Config cfg);
    public abstract int getMaxLevel();
    protected void ignite(double x, double y){
        x-=this.x;
        y-=this.y;
        if(this instanceof Base){
            y-=25;
        }
        synchronized(fires){
            fires.add(new Particle(x-25, y-25, ParticleEffectType.FIRE));
        }
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
        synchronized(fires){
            for(Particle fire : fires){
                fire.x+=x;
                fire.y+=y;
                fire.fading = true;
                Core.game.addParticleEffect(fire);
            }
            fires.clear();
        }
    }
    public abstract String getName();
    public int getLevel(){
        return level;
    }
    public boolean canUpgrade(){
        return getLevel()<getMaxLevel();
    }

    public ArrayList<Upgrade> getBoughtUpgrades(){
        return upgrades;
    }
    public int getUpgrades(){
        return getBoughtUpgrades().size();
    }
    public int getUpgrades(Upgrade upgrade){
        int total = 0;
        for(Upgrade u : upgrades){
            if(u==upgrade)total++;
        }
        return total;
    }
    public boolean hasUpgrade(Upgrade upgrade){
        for(Upgrade u : upgrades){
            if(u==upgrade)return true;
        }
        return false;
    }
    public ArrayList<Integer> getUpgradeLevels(){
        ArrayList<Integer> lvls = new ArrayList<>();
        for(Upgrade upgrade : type.upgrades){
            for(int i : upgrade.availability){
                if(!lvls.contains(i))lvls.add(i);
            }
        }
        Collections.sort(lvls);
        return lvls;
    }
    public int getNextUpgradeLevel(){
        ArrayList<Upgrade> currentUpgrades = new ArrayList<>(upgrades);
        int I = 0;
        L:for(int l : getUpgradeLevels()){
            I++;
            if(currentUpgrades.isEmpty())return l;
            Upgrade u = currentUpgrades.remove(0);
            for(int i : u.availability)if(i==l)continue L;
            throw new IllegalStateException("Upgrade "+u.toString()+" should not be on level "+l+" in position "+I+"!");
        }
        return -1;
    }
    public ArrayList<Upgrade> getAvailableUpgrades(){
        int next = getNextUpgradeLevel();
        if(next==-1||getLevel()<next)return new ArrayList<>();
        ArrayList<Upgrade> available = new ArrayList<>();
        for(Upgrade u : type.upgrades){
            boolean avbl = false;
            for(int i : u.availability)if(i==next)avbl = true;
            if(!avbl)continue;
            if(getUpgrades(u)>=u.max)continue;
            if(u.starlight&&!Core.game.observatory)continue;
            available.add(u);
        }
        return available;
    }
    public boolean canBuyUpgrade(Upgrade upgrade){
        return getAvailableUpgrades().contains(upgrade);
    }
    public boolean buyUpgrade(Upgrade upgrade){
        if(!canBuyUpgrade(upgrade))return false;
        upgrades.add(upgrade);
        Core.game.refreshNetworks();
        return true;
    }
    public static enum Upgrade{
        SUPERCHARGE("Supercharge", BuildingType.COAL_GENERATOR, false, 1200, 5, 4,8,12,16,20),
        ECOLOGICAL("Ecological", BuildingType.COAL_GENERATOR, false, 1200, 5, 4,8,12,16,20),
        STARLIGHT_INFUSED_FUEL("Starlight Infused Fuel", BuildingType.COAL_GENERATOR, true, 3000, 1, 12, 20),
        STONE_GRINDING("Stone Grinding", BuildingType.MINE, false, 1200, 2, 12, 20),
        POWER_TOOLS("Power Tools", BuildingType.MINE, false, 600, 1, 4,8,12,16,20),
        PHOTOVOLTAIC_SENSITIVITY("Photovoltaic Sensitivity", BuildingType.SOLAR_GENERATOR, false, 600, 5, 4,8,12,16,20),
        STARLIGHT_GENERATION("Starlight Generation", BuildingType.SOLAR_GENERATOR, true, 3000, 1, 16,20);
//        SHIELD_PROJECTOR("Shield Projector", BuildingType.SHIELD_GENERATOR, true, 200, 1, 20);//TODO make functional
        static{
            SUPERCHARGE.costs = new ItemStack[]{new ItemStack(Item.coal, 10), new ItemStack(Item.ironIngot, 50)};
            ECOLOGICAL.costs = new ItemStack[]{new ItemStack(Item.coal, 20), new ItemStack(Item.ironIngot, 80)};
            STARLIGHT_INFUSED_FUEL.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 100), new ItemStack(Item.coal, 50)};
            STONE_GRINDING.costs = new ItemStack[]{new ItemStack(Item.stone, 40), new ItemStack(Item.ironIngot, 40)};
            POWER_TOOLS.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 50)};
            PHOTOVOLTAIC_SENSITIVITY.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 10)};
            STARLIGHT_GENERATION.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 30)};
//            SHIELD_PROJECTOR.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 100)};
        }
        private final boolean starlight;
        public final int time;
        private final int max;
        private final int[] availability;
        public ItemStack[] costs;
        private final String name;
        private Upgrade(String name, BuildingType type, boolean starlight, int time, int max, int... availability){
            type.upgrades.add(this);
            this.starlight = starlight;
            this.time = time;
            this.max = max;
            this.availability = availability;
            this.name = name;
        }
        @Override
        public String toString(){
            return name;
        }
    }
}