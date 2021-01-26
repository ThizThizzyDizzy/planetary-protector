package planetaryprotector.structure;
import java.util.ArrayList;
import java.util.function.Function;
import planetaryprotector.anim.Animation;
import planetaryprotector.game.Game;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.structure.Structure.Upgrade;
import planetaryprotector.structure.task.TaskAnimated;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public abstract class StructureType{
    public static final ArrayList<StructureType> structureTypes = new ArrayList<>();//TODO make these all external classes
    public static final StructureType TREE = registerStructureType(new StructureType("tree"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Tree(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Tree.load(cfg, game);
        }
    }.setDisplayName("Tree").setDecoration(true).setStructureHeight(14));
    public static final StructureType BASE = registerStructureType(new StructureType("base"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Base(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Base.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Base").setRepairable(new ItemStack(Item.ironIngot, 10)).setDamagable(true).setFireDestroyThreshold(2.5).setIgnitionChance(.1).setStructureHeight(25));
    public static final StructureType EMPTY_PLOT = registerStructureType(new StructureType("empty_plot"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Plot(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Plot.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Empty Plot").setRepairable(new ItemStack(Item.stone, 10)).setDamagable(true).setIgnitionChance(0).setBackgroundStructure(true));
    public static final StructureType WRECK = registerStructureType(new StructureType("wreck"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Wreck(game, x, y, 0);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Wreck.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Wreck").setIgnitionChance(0).setBackgroundStructure(true));
    public static final StructureType SKYSCRAPER = registerStructureType(new StructureType("skyscraper"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Skyscraper(game, x, y, 10);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Skyscraper.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Skyscraper").setBuildable(5750, new ItemStack(Item.ironIngot, 125)).setRepairable(new ItemStack(Item.ironIngot, 10)).setDamagable(true).setFireDestroyThreshold(1).setIgnitionChance(1));
    public static final StructureType MINE = registerStructureType(new StructureType("mine"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Mine(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Mine.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Mine").setBuildable(1200, new ItemStack(Item.ironIngot, 10)).setRepairable(new ItemStack(Item.stone, 2)).addUpgrades(19, new ItemStack(Item.ironIngot, 5)).setDamagable(true).setFireDestroyThreshold(1.5).setIgnitionChance(1/3d).setBackgroundStructure(true));
    public static final StructureType SOLAR_GENERATOR = registerStructureType(new StructureType("solar_generator"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new SolarGenerator(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return SolarGenerator.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Solar Generator").setBuildable(600, new ItemStack(Item.ironIngot, 40)).setRepairable(new ItemStack(Item.ironIngot, 10)).addUpgrades(9, new ItemStack(Item.ironIngot, 20)).addUpgrades(5, new ItemStack(Item.ironIngot, 25)).addUpgrades(3, new ItemStack(Item.ironIngot, 30)).addUpgrade(new ItemStack(Item.ironIngot, 35)).addUpgrade(new ItemStack(Item.ironIngot, 40)).setDamagable(true).setStructureHeight(10).setFireDestroyThreshold(.75).setIgnitionChance(.85));
    public static final StructureType COAL_GENERATOR = registerStructureType(new StructureType("coal_generator"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new CoalGenerator(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return CoalGenerator.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Coal Generator").setBuildable(800, new ItemStack(Item.ironIngot, 75)).setRepairable(new ItemStack(Item.ironIngot, 20)).addUpgrades(9, new ItemStack(Item.ironIngot, 35)).addUpgrades(5, new ItemStack(Item.ironIngot, 40)).addUpgrades(3, new ItemStack(Item.ironIngot, 45)).addUpgrade(new ItemStack(Item.ironIngot, 50)).addUpgrade(new ItemStack(Item.ironIngot, 55)).setDamagable(true).setFireDestroyThreshold(.75).setIgnitionChance(.75));
    public static final StructureType POWER_STORAGE = registerStructureType(new StructureType("power_storage"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new PowerStorage(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return PowerStorage.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Power Storage").setBuildable(1200, new ItemStack(Item.ironIngot, 100)).setRepairable(new ItemStack(Item.ironIngot, 40)).addUpgrades(9, new ItemStack(Item.ironIngot, 50)).addUpgrades(5, new ItemStack(Item.ironIngot, 60)).addUpgrades(3, new ItemStack(Item.ironIngot, 70)).addUpgrade(new ItemStack(Item.ironIngot, 80)).addUpgrade(new ItemStack(Item.ironIngot, 90)).setDamagable(true).setFireDestroyThreshold(.75).setIgnitionChance(.85).setStructureHeight(19));
    public static final StructureType SHIELD_GENERATOR = registerStructureType(new StructureType("shield_generator"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new ShieldGenerator(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return ShieldGenerator.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Shield Generator").setBuildable(800, new ItemStack(Item.ironIngot, 80)).setRepairable(new ItemStack(Item.ironIngot, 20)).addUpgrades(4, new ItemStack(Item.ironIngot, 40)).addUpgrades(5, new ItemStack(Item.ironIngot, 45)).addUpgrades(5, new ItemStack(Item.ironIngot, 50)).addUpgrades(4, new ItemStack(Item.ironIngot, 60)).addUpgrade(new ItemStack(Item.ironIngot, 70)).setDamagable(true).setFireDestroyThreshold(.6).setIgnitionChance(.8));
    public static final StructureType WORKSHOP = registerStructureType(new StructureType("workshop"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Workshop(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Workshop.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Workshop").setBuildable(1500, new ItemStack(Item.ironIngot, 100)).setRepairable(new ItemStack(Item.ironIngot, 10)).setDamagable(true).setStructureHeight(20).setIgnitionChance(.4));
    public static final StructureType LABORATORY = registerStructureType(new StructureType("laboratory"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Laboratory(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Laboratory.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Laboratory").setBuildable(4000, new ItemStack(Item.ironIngot, 100)).setRepairable(new ItemStack(Item.ironIngot, 100)).setDamagable(true).setIgnitionChance(.8).setStructureHeight(10));
    public static final StructureType SILO = registerStructureType(new StructureType("silo"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Silo(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Silo.loadSpecific(cfg, game, x, y, level, upgrades);
        }
    }.setDisplayName("Silo").setBuildable(20*60*15/2, new ItemStack(Item.ironIngot, 250), new ItemStack(Item.stone, 500)).setRepairable(new ItemStack(Item.ironIngot, 50), new ItemStack(Item.stone, 100)).addUpgrade(20*60*5/2, new ItemStack(Item.ironIngot, 110), new ItemStack(Item.stone, 100)).addUpgrade((int)(20*60*7.5)/2, new ItemStack(Item.ironIngot, 170), new ItemStack(Item.stone, 100)).setConstructibilityFunc((game) -> {
        return game.phase>=3;
    }).setDamagable(true).setFireDestroyThreshold(.25).setIgnitionChance(.2).setBackgroundStructure(true));
    public static final StructureType OBSERVATORY = registerStructureType(new StructureType("observatory"){
        @Override
        public Structure createStructure(Game game, int x, int y){
            return new Observatory(game, x, y);
        }
        @Override
        public Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
            return Observatory.loadSpecific(cfg, game, x, y);
        }
    }.setDisplayName("Observatory").setBuildable(2000, new ItemStack(Item.stone, 250), new ItemStack(Item.ironIngot, 100), new ItemStack(Item.star, 1)).setRepairable(new ItemStack(Item.ironIngot, 15)).setConstructibilityFunc((game) -> {
        return game.observatory;
    }).setDamagable(true).setIgnitionChance(.4).setStructureHeight(10));
    public static StructureType getByName(String name){
        for(StructureType type : structureTypes){
            if(type.name.equals(name))return type;
        }
        return null;
    }
    public final String name;
    private String displayName;
    private ItemStack[] buildCosts;
    private int buildTime;
    private ItemStack[] repairCosts;
    private ArrayList<ItemStack[]> upgradeCosts = new ArrayList<>();
    private ArrayList<Integer> upgradeTimes = new ArrayList<>();
    private Function<Game, Boolean> constructibilityFunc;
    public ArrayList<Upgrade> upgrades = new ArrayList<>();
    private boolean isDecoration = false;
    private boolean isDamagable = false;
    private boolean isSelectable = true;
    private boolean isShieldable = true;
    private int damageDestroyThreshold = 10;
    private double ignitionChance = 1;
    private double fireDestroyThreshold = 1.1;
    private int structureHeight = 0;
    private boolean isBackgroundStructure = false;
    public StructureType(String name){
        this.name = name;
    }
    public static <T extends StructureType> T registerStructureType(T type){
        structureTypes.add(type);
        return type;
    }
    @Deprecated //TODO localization
    public StructureType setDisplayName(String displayName){
        this.displayName = displayName;
        return this;
    }
    public String getDisplayName(){
        return displayName==null?name:displayName;
    }
    public StructureType setBuildable(int buildTime, ItemStack... buildCosts){
        this.buildCosts = buildCosts;
        this.buildTime = buildTime;
        return this;
    }
    @Deprecated
    public StructureType addUpgrade(ItemStack... upgradeCosts){
        return addUpgrade(buildTime, upgradeCosts);
    }
    public StructureType addUpgrade(int time, ItemStack... upgradeCosts){
        this.upgradeCosts.add(upgradeCosts);
        upgradeTimes.add(time);
        return this;
    }
    @Deprecated
    public StructureType addUpgrades(int count, ItemStack... upgradeCosts){
        return addUpgrades(count, buildTime, upgradeCosts);
    }
    public StructureType addUpgrades(int count, int time, ItemStack... upgradeCosts){
        for(int i = 0; i<count; i++){
            this.upgradeCosts.add(upgradeCosts);
            upgradeTimes.add(time);
        }
        return this;
    }
    public StructureType setRepairable(ItemStack... repairCosts){
        this.repairCosts = repairCosts;
        return this;
    }
    public StructureType setConstructibilityFunc(Function<Game, Boolean> constructibilityFunc){
        this.constructibilityFunc = constructibilityFunc;
        return this;
    }
    public ItemStack[] getConstructionCosts(){
        return buildCosts;
    }
    public ItemStack[] getRepairCosts(){
        return repairCosts;
    }
    public ItemStack[] getUpgradeCosts(int level){
        return upgradeCosts.get(level-1);
    }
    public int getTexture(){
        return ImageStash.instance.getTexture(getTextureS());
    }
    public int getDamageTexture(){
        return ImageStash.instance.getTexture(getDamageTextureS());
    }
    public int getTexture(String path){
        return ImageStash.instance.getTexture(getTextureS(path));
    }
    public String getTextureS(){
        return "/textures/structures/"+name+"/"+Game.theme.tex()+".png";
    }
    public String getDamageTextureS(){
        return "/textures/structures/"+name+"/damage.png";
    }
    public String getTextureS(String path){
        return "/textures/structures/"+name+"/"+path+".png";
    }
    public boolean isConstructible(){
        return buildCosts!=null;
    }
    public boolean isConstructible(Game game){
        if(constructibilityFunc!=null){
            return constructibilityFunc.apply(game);
        }
        return isConstructible();
    }
    public abstract Structure createStructure(Game game, int x, int y);
    public Animation getAnimation(){
        return TaskAnimated.getAnimation(getAnimationS());
    }
    public String getAnimationS(){
        return "/textures/tasks/construct/"+name+"/"+Game.theme.tex();
    }
    public abstract Structure load(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades);
    public int getConstructionTime(){
        return buildTime;
    }
    public int getTotalCost(int level, Item item){
        int count = 0;
        for(ItemStack stack : getConstructionCosts()){
            if(stack.item==item)count+=stack.count;
        }
        for(int i = 1; i<level; i++){
            for(ItemStack stack : getUpgradeCosts(i)){
                if(stack.item==item)count+=stack.count;
            }
        }
        return count;
    }
    public int getMaxLevel(){
        return upgradeCosts.size()+1;
    }
    public StructureType setDecoration(boolean b){
        isDecoration = b;
        return this;
    }
    public boolean isDecoration(){
        return isDecoration;
    }
    public StructureType setDamagable(boolean b){
        isDamagable = b;
        return this;
    }
    public boolean isDamagable(){
        return isDamagable;
    }
    public StructureType setDamageDestroyThreshold(int threshold){
        damageDestroyThreshold = threshold;
        return this;
    }
    public int getDamageDestroyThreshold(){
        return damageDestroyThreshold;
    }
    public StructureType setSelectable(boolean b){
        isSelectable = b;
        return this;
    }
    public boolean isSelectable(){
        return isSelectable;
    }
    public StructureType setShieldable(boolean b){
        isShieldable = b;
        return this;
    }
    public boolean isShieldable(){
        return isShieldable;
    }
    public StructureType setIgnitionChance(double threshold){
        ignitionChance = threshold;
        return this;
    }
    public double getIgnitionChance(){
        return ignitionChance;
    }
    public StructureType setFireDestroyThreshold(double threshold){
        fireDestroyThreshold = threshold;
        return this;
    }
    public double getFireDestroyThreshold(){
        return fireDestroyThreshold;
    }
    public StructureType setStructureHeight(int threshold){
        structureHeight = threshold;
        return this;
    }
    public int getStructureHeight(){
        return structureHeight;
    }
    public StructureType setBackgroundStructure(boolean b){
        isBackgroundStructure = b;
        return this;
    }
    public boolean isBackgroundStructure(){
        return isBackgroundStructure;
    }
}