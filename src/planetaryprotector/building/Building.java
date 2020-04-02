package planetaryprotector.building;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.Task;
import java.util.ArrayList;
import java.util.Collections;
import static planetaryprotector.game.Game.rand;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.building.task.TaskAnimated;
import planetaryprotector.game.Action;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.research.Research;
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
    public ShieldGenerator shield = null;//the shield generator that is currently projecting a shield on this building
    private static final int barHeight = 5;
    private double barOffset = 0;
    public Building(Game game, double x, double y, double width, double height, BuildingType type){
        this(game, x, y, width, height, type, 1, new ArrayList<>());
    }
    public Building(Game game, double x, double y, double width, double height, BuildingType type, int level, ArrayList<Upgrade> upgrades){
        super(game, x, y, width, height);
        this.type = type;
        this.level = level;
        this.upgrades.clear();
        this.upgrades.addAll(upgrades);
    }
    public final void tick(){
        fire+=fireIncreaseRate;
        if(fire-fireDamage*getFireDestroyThreshold()*.1>=getFireDestroyThreshold()*.1){
            damages.add(new BuildingDamage(this,0,0));
            fires.add(new Particle(game, getRandX(game.rand), getRandY(game.rand), ParticleEffectType.FIRE));
            ignite();
            fireDamage++;
        }
        for(Particle particle : fires){
            particle.tick();
        }
        update();
    }
    public void update(){
        if(damages.size()>=10){
            game.addParticleEffect(new Particle(game, x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            int ingots = 0;
            try{
                for(int i = 0; i<getLevel(); i++){
                    ingots += type.costs[i][0].count;
                }
            }catch(ArrayIndexOutOfBoundsException ex){}
            game.replaceBuilding(this, new Wreck(game, x, y, ingots));
        }
    }
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, isBackgroundStructure()?getTexture():BuildingType.EMPTY.getTexture());
        if(isBackgroundStructure()){
            for(Upgrade upgrade : type.upgrades){
                int count = getUpgrades(upgrade);
                if(count==0)continue;
                drawRect(x, y-getBuildingHeight(), x+width, y+height, upgrade.getTexture(type, count));
            }
        }
        renderDamages();
    }
    public void drawForeground(){
        barOffset = barHeight-getBuildingHeight();
        if((this instanceof BuildingPowerUser)&&((BuildingPowerUser)this).isPowerActive()){
            drawBar(((BuildingPowerUser)this).getDisplayPower()/((BuildingPowerUser)this).getDisplayMaxPower(), 0, .25, 1);
        }
        if(game.observatory&&(this instanceof BuildingStarlightUser)&&((BuildingStarlightUser)this).isStarlightActive()){
            drawBar(((BuildingStarlightUser)this).getDisplayStarlight()/((BuildingStarlightUser)this).getDisplayMaxStarlight(), .625, .875, 1);
        }
        barOffset = height;
        if(this instanceof BuildingDamagable){
            switch(MenuOptionsGraphics.health){
                case 0://none
                    break;
                case 1://when damaged
                    if(damages.isEmpty()&&fires.isEmpty())break;
                case 2:
                    double percent = getHealthPercent();
                    drawBar(percent, 1-percent, percent, 0);
            }
        }
    }
    protected void drawBar(double percent, double r, double g, double b){
        if(Double.isNaN(percent))return;
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(x,y+barOffset-barHeight,x+width, y+barOffset, 0);
        if(percent>0){
            GL11.glColor4d(r, g, b, 1);
            drawRect(x+1,y+barOffset-barHeight+1,x+width*percent-1, y+barOffset-1, 0);
        }
        GL11.glColor4d(1, 1, 1, 1);
        barOffset += barHeight;
    }
    public double getHealthPercent(){
        return 1-(damages.size()/10d);
    }
    public void drawOverlay(){
        drawRect(x, y-getBuildingHeight(), x+width, y+height, 0);
    }
    public void draw(){
        if(!isBackgroundStructure()){
            drawRect(x, y-getBuildingHeight(), x+width, y+height, getTexture());
            for(Upgrade upgrade : type.upgrades){
                int count = getUpgrades(upgrade);
                if(count==0)continue;
                drawRect(x, y-getBuildingHeight(), x+width, y+height, upgrade.getTexture(type, count));
            }
            renderDamages();
        }
    }
    public void renderDamages(){
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        for(BuildingDamage damage : damages){
            damage.render();
        }
        GL11.glPopMatrix();
    }
    @Override
    public void render(){
        draw();
        GL11.glColor4d(1, 0, 0, (game.damageReportTimer/game.damageReportTime)*damages.size()/10d);
        drawOverlay();
        GL11.glColor4d(0, 1, 1, mouseover);
        drawOverlay();
        GL11.glColor4d(1, 1, 1, 1);
        if(shield!=null){
            GL11.glColor4d(.75, .875, 1, shield.getProjectedShieldStrength()/10d);
            drawOverlay();
            GL11.glColor4d(1, 1, 1, 1);
        }
        drawForeground();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        for(Particle fire : fires){
            fire.render();
        }
        GL11.glPopMatrix();
    }
    protected double getIgnitionChance(){
        return 1;
    }
    public void upgrade(){
        level++;
        game.refreshNetworks();
    }
    public static Building generateRandomBuilding(Game game, ArrayList<Building> buildings){
        int buildingX;
        int buildingY;
        int i = 0;
        WHILE:while(true){
            i++;
            if(i>1000){
                return null;
            }
            buildingX = Game.rand.nextInt(Display.getWidth()-100);
            buildingY = Game.rand.nextInt(Display.getHeight()-100);
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
        return new Skyscraper(game, buildingX, buildingY);
    }
    public static Building load(Config cfg, Game game){
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
                b = Workshop.loadSpecific(cfg,game,x,y);
                break;
            case OBSERVATORY:
                b = Observatory.loadSpecific(cfg,game,x,y);
                break;
            case BASE:
                b = Base.loadSpecific(cfg,game,x,y);
                break;
            case SILO:
                b = Silo.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case EMPTY:
                b = Plot.loadSpecific(cfg,game,x,y);
                break;
            case MINE:
                b = Mine.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case SHIELD_GENERATOR:
                b = ShieldGenerator.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case SKYSCRAPER:
                b = Skyscraper.loadSpecific(cfg,game,x,y);
                break;
            case WRECK:
                b = Wreck.loadSpecific(cfg,game,x,y);
                break;
            case COAL_GENERATOR:
                b = CoalGenerator.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case SOLAR_GENERATOR:
                b = SolarGenerator.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case POWER_STORAGE:
                b = PowerStorage.loadSpecific(cfg,game,x,y,level,upgrades);
                break;
            case LABORATORY:
                b = Laboratory.loadSpecific(cfg,game,x,y);
                break;
        }
        for(int i = 0; i<cfg.get("count", 0); i++){
            b.damages.add(new BuildingDamage(b, cfg.get(i+" x", 0d), cfg.get(i+" y", 0d)));
        }
        b.fire = cfg.get("fire", b.fire);
        b.fireDamage = cfg.get("fire damage", b.fireDamage);
        b.fireIncreaseRate = cfg.get("fire increase", b.fireIncreaseRate);
        for(int i = 0; i<cfg.get("fires", 0); i++){
            b.fires.add(new Particle(game, cfg.get("fire "+i+" x", 0d), cfg.get("fire "+i+" y", 0d), ParticleEffectType.FIRE));
        }
        return b;
    }
    public boolean damage(double x, double y){
        if(this instanceof BuildingDamagable){
            if(shield!=null){
                if(shield.shieldHit())return true;
            }
            if(game.rand.nextDouble()<getIgnitionChance()){
                fireIncreaseRate += 0.0002;
                ignite(x, y);
            }
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
        damages.add(new BuildingDamage(this, x-25, y-25));
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
        if(this instanceof BuildingStarlightConsumer){
            cfg.set("starlight", ((BuildingStarlightConsumer)this).getStarlight());
        }
        cfg.set("shield", shield==null?-1:shield.getIndex());
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
        fires.add(new Particle(game, x-25, y-25, ParticleEffectType.FIRE));
    }
    protected void ignite(){
        ignite(x+getRandX(game.rand),y+getRandY(game.rand));
    }
    protected int getTexture(){
        return type.getTexture();
    }
    protected int getDamageTexture(){
        return type.getDamageTexture();
    }
    protected int getTexture(String path){
        return type.getTexture(path);
    }
    protected double getFireDestroyThreshold(){
        return 1.1;
    }
    protected double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    protected double getRandY(Random rand){
        return rand.nextDouble()*height;
    }
    public void clearFires(){
        for(Particle fire : fires){
            fire.x+=x;
            fire.y+=y;
            fire.fading = true;
            game.addParticleEffect(fire);
        }
        fires.clear();
    }
    public String getName(){
        if(getMaxLevel()>0)return "Level "+getLevel()+" "+type.name;
        return type.name;
    }
    public int getLevel(){
        return level;
    }
    public boolean canUpgrade(){
        return getLevel()<getMaxLevel();
    }
    public void cancelTask(){
        task.cancel();
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
            if(!u.research.isCompleted())continue;
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
        game.refreshNetworks();
        return true;
    }
    public void getDebugInfo(ArrayList<String> data){
        data.add(type.name);
        data.add("Damage: "+damages.size());
        data.add("Level "+getLevel());
        data.add("Upgrades: "+upgrades.size());
        for(int i = 0; i<upgrades.size(); i++){
            data.add(" "+upgrades.get(i).name());
        }
        data.add("Fire: "+fire);
        data.add("Fire damage: "+fireDamage);
        data.add("Fire increase: "+fireIncreaseRate);
        data.add("Fires: "+fires.size());
        if(this instanceof BuildingPowerConsumer){
            data.add("Power Consumer");
            data.add(" Demand: "+((BuildingPowerConsumer)this).getDemand());
            data.add(" Power: "+((BuildingPowerConsumer)this).getPower()+"/"+((BuildingPowerConsumer)this).getMaxPower());
        }
        if(this instanceof BuildingPowerProducer){
            data.add("Power Producer");
            data.add(" Supply: "+((BuildingPowerProducer)this).getProduction());
            data.add(" Renewable: "+(((BuildingPowerProducer)this).isRenewable()?"TRUE":"FALSE"));
        }
        if(this instanceof BuildingStarlightConsumer){
            data.add("Starlight Consumer");
            data.add(" Demand: "+((BuildingStarlightConsumer)this).getStarlightDemand());
            data.add(" Starlight: "+((BuildingStarlightConsumer)this).getStarlight()+"/"+((BuildingStarlightConsumer)this).getMaxStarlight());
        }
        if(this instanceof BuildingStarlightProducer){
            data.add("Starlight Producer");
            data.add(" Supply: "+((BuildingStarlightProducer)this).getStarlightProduction());
            data.add(" Renewable: "+(((BuildingStarlightProducer)this).isStarlightRenewable()?"TRUE":"FALSE"));
        }
        getBuildingDebugInfo(data);
    }
    protected abstract void getBuildingDebugInfo(ArrayList<String> data);
    public int getVariants(){
        return 1;
    }
    public int getVariant(){
        return ((int)x)%getVariants();
    }
    /**
     * Called after all buildings are loaded- this is in case any building needs to load data regarding another building (For example, shield projector target)
     * @param game the game that is being loaded
     * @param config the config to load from
     */
    public void postLoad(Game game, Config config){
        int index = config.get("shield", -1);
        if(index!=-1){
            shield = (ShieldGenerator) game.buildings.get(index);
        }
    }
    public void getActions(MenuGame menu, ArrayList<Action> actions){}
    public static enum Upgrade{
        SUPERCHARGE(Research.SUPERCHARGE, "Supercharge", BuildingType.COAL_GENERATOR, 1200, 5, 4,8,12,16,20),
        ECOLOGICAL(Research.ECOLOGICAL, "Ecological", BuildingType.COAL_GENERATOR, 1200, 5, 4,8,12,16,20),
        STARLIGHT_INFUSED_FUEL(Research.STARLIGHT_INFUSED_FUEL, "Starlight Infused Fuel", BuildingType.COAL_GENERATOR, 3000, 1, 12, 20),
        STONE_GRINDING(Research.STONE_GRINDING, "Stone Grinding", BuildingType.MINE, 1200, 2, 8, 12),//12,20
        POWER_TOOLS(Research.POWER_TOOLS, "Power Tools", BuildingType.MINE, 600, 1, 4,8,12,16,20),
        PHOTOVOLTAIC_SENSITIVITY(Research.PHOTOVOLTAIC_SENSITIVITY, "Photovoltaic Sensitivity", BuildingType.SOLAR_GENERATOR, 600, 5, 4,8,12,16,20),
        STARLIGHT_GENERATION(Research.STARLIGHT_GENERATION, "Starlight Generation", BuildingType.SOLAR_GENERATOR, 3000, 1, 16,20),
        SHIELD_PROJECTOR(Research.SHIELD_PROJECTOR, "Shield Projector", BuildingType.SHIELD_GENERATOR, 200, 1, 4);//20
        static{
            SUPERCHARGE.costs = new ItemStack[]{new ItemStack(Item.coal, 10), new ItemStack(Item.ironIngot, 50)};
            ECOLOGICAL.costs = new ItemStack[]{new ItemStack(Item.coal, 20), new ItemStack(Item.ironIngot, 80)};
            STARLIGHT_INFUSED_FUEL.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 100), new ItemStack(Item.coal, 50)};
            STONE_GRINDING.costs = new ItemStack[]{new ItemStack(Item.stone, 40), new ItemStack(Item.ironIngot, 40)};
            POWER_TOOLS.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 50)};
            PHOTOVOLTAIC_SENSITIVITY.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 10)};
            STARLIGHT_GENERATION.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 30)};
            SHIELD_PROJECTOR.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 120)};
        }
        public final int time;
        public final int max;
        private final int[] availability;
        public ItemStack[] costs;
        private final Research research;
        private final String name;
        private Upgrade(Research research, String name, BuildingType type, int time, int max, int... availability){
            type.upgrades.add(this);
            this.time = time;
            this.max = max;
            this.availability = availability;
            this.research = research;
            this.name = name;
            if(research!=null){
                research.upgrade = this;
            }
        }
        @Override
        public String toString(){
            return name;
        }
        public int getTexture(BuildingType type, int count){
            return ImageStash.instance.getTexture(getTextureS(type, count));
        }
        public String getTextureS(BuildingType type, int count){
            return type.getTextureS(name().toLowerCase().replace("_", " ")+"/"+Game.theme.tex()+"/"+count);
        }
        public int[] getAnimation(BuildingType type, int count){
            return TaskAnimated.getAnimation(getAnimationS(type, count));
        }
        public String getAnimationS(BuildingType type, int count){
            return "/textures/tasks/upgrade/"+type.texture+"/"+name().toLowerCase().replace("_", " ")+"/"+Game.theme.tex()+"/"+count;
        }
        /**
         * @return A String interpretation of the upgrade slots
         */
        public String getConfiguration(){
            char a = 'X';
            char b = 'X';
            char c = 'X';
            char d = 'X';
            char e = 'X';
            for(int i : availability){
                if(i==4)a = 'O';
                if(i==8)b = 'O';
                if(i==12)c = 'O';
                if(i==16)d = 'O';
                if(i==20)e = 'O';
            }
            return max+" ~ |"+a+"|"+b+"|"+c+"|"+d+"|"+e+"|";
        }
    }
    public int getBuildingHeight(){
        return 0;
    }
    public int getIndex(){
        return game.buildings.indexOf(this);
    }
    public abstract boolean isBackgroundStructure();
    public void setTask(Task task){
        if(game.selectedBuilding==this)game.actionUpdateRequired = 2;
        this.task = task;
    }
}