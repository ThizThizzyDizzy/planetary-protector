package planetaryprotector.research;
import java.util.ArrayList;
import java.util.Random;
import planetaryprotector.Core;
import planetaryprotector.building.Building.Upgrade;
import planetaryprotector.building.BuildingType;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public enum Research{
    SUPERCHARGE(ResearchCategory.BUILDING_UPGRADES, "Coal generator will operate faster", "Supercharger", "Each Supercharge module installed on a Coal Generator will increase the power production and fuel consumption by 38% (Multiplicative)", 5000, 0, 20*60*5, new ItemStack(Item.ironIngot, 60), new ItemStack(Item.coal, 30)),
    ECOLOGICAL(ResearchCategory.BUILDING_UPGRADES, "Coal generator will operate cleaner", "Ecological", "Each Ecological module installed a Coal Generator will decrease the fuel consumption rate by 38% (Multiplicative)", 10000, 0, 20*60*7, new ItemStack(Item.ironIngot, 100), new ItemStack(Item.coal, 40)),
    STARLIGHT_INFUSED_FUEL(ResearchCategory.BUILDING_UPGRADES, "Starlight will infuse coal", "Starlight Infused Fuel", "Installing a Starlight Infused Fuel module on a Coal Generator will allow it to consume starlight for 2x power production", 25000, 10, 20*60*15, new ItemStack(Item.star), new ItemStack(Item.ironIngot, 160), new ItemStack(Item.coal, 100)),
    STONE_GRINDING(ResearchCategory.BUILDING_UPGRADES, "Mine will generate less stone", "Stone Grinding", "Each Stone Grinding module installed on a Mine will decrease the stone production rate by 40% (Additive)", 6000, 0, 20*60*6, new ItemStack(Item.ironIngot, 80), new ItemStack(Item.coal, 20), new ItemStack(Item.stone, 60)),
    POWER_TOOLS(ResearchCategory.BUILDING_UPGRADES, "Mine will consume for speed", "Power Tools", "Installing Power Tools on a Mine will allow it to consume power for 50% higher speed", 4000, 0, 20*60*3, new ItemStack(Item.ironIngot, 40), new ItemStack(Item.coal, 20)),
    PHOTOVOLTAIC_SENSITIVITY(ResearchCategory.BUILDING_UPGRADES, "Solar generator will generate more", "Photovoltaic Sensetivity", "Each Photovoltaic Sensitivity module installed on a Solar Generator will increase its sensitivity to daylight by 38% (Multiplicative), allowing it to generate power further into the night", 8000, 0, 20*60*6, new ItemStack(Item.ironIngot, 90)),
    STARLIGHT_GENERATION(ResearchCategory.BUILDING_UPGRADES, "Solar generator will generate from starlight", "Starlight Generation", "Installing Starlight Generation on a Solar Generator will allow it to generate power at night at a decreased rate (50%)", 10000, 5, 20*60*10, new ItemStack(Item.star), new ItemStack(Item.ironIngot, 80)),
    SHIELD_PROJECTOR(ResearchCategory.BUILDING_UPGRADES, "Shield generator will project a shield", "Shield Projector", "Installing a Shield Projector on a Shield Generator will allow it to project a shield onto a targeted building ", 16000, 25, 20*60*15, new ItemStack(Item.ironIngot, 250), new ItemStack(Item.star));
    static{
        SUPERCHARGE.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR, 4),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.coal, 50),
                        DiscoveryPrerequisite.useResource(Item.coal, 50)),
                        DiscoveryPrerequisite.resource(Item.coal, 50));
        ECOLOGICAL.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR, 4),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.coal, 80)),
                        DiscoveryPrerequisite.useResource(Item.coal, 80),
                        DiscoveryPrerequisite.resource(Item.coal, 80));
        STARLIGHT_INFUSED_FUEL.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR),
                        DiscoveryPrerequisite.starlight())
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR, 12),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR, 8)),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR, 4)),
                        DiscoveryPrerequisite.time(20*60*15, DiscoveryPrerequisite.building(BuildingType.COAL_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.gainResource(Item.coal, 150)),
                        DiscoveryPrerequisite.useResource(Item.coal, 150),
                        DiscoveryPrerequisite.resource(Item.coal, 150),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.starlight()));
        STONE_GRINDING.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.MINE))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.MINE, 12),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.MINE, 8)),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.building(BuildingType.MINE, 4)),
                        DiscoveryPrerequisite.time(20*60*15, DiscoveryPrerequisite.building(BuildingType.MINE)),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.gainResource(Item.stone, 250)),
                        DiscoveryPrerequisite.useResource(Item.stone, 250),
                        DiscoveryPrerequisite.resource(Item.stone, 250));
        POWER_TOOLS.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.MINE))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.MINE, 4),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.MINE)),
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.coal, 25),
                        DiscoveryPrerequisite.useResource(Item.coal, 25)),
                        DiscoveryPrerequisite.resource(Item.coal, 25));
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.ironOre, 25),
                        DiscoveryPrerequisite.useResource(Item.ironIngot, 25),
                        DiscoveryPrerequisite.resource(Item.ironIngot, 25));
        PHOTOVOLTAIC_SENSITIVITY.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR, 4),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.coal, 80)),
                        DiscoveryPrerequisite.useResource(Item.coal, 80),
                        DiscoveryPrerequisite.resource(Item.coal, 80),
                        DiscoveryPrerequisite.time(20*60, DiscoveryPrerequisite.gainResource(Item.ironIngot, 40)),
                        DiscoveryPrerequisite.useResource(Item.ironIngot, 40),
                        DiscoveryPrerequisite.resource(Item.ironIngot, 40));
        STARLIGHT_GENERATION.addDiscoveryStage("",
                        DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR),
                        DiscoveryPrerequisite.starlight())
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR, 16),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR, 12)),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR, 8)),
                        DiscoveryPrerequisite.time(20*60*15, DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR, 4)),
                        DiscoveryPrerequisite.time(20*60*20, DiscoveryPrerequisite.building(BuildingType.SOLAR_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.gainResource(Item.ironIngot, 250)),
                        DiscoveryPrerequisite.useResource(Item.ironIngot, 250),
                        DiscoveryPrerequisite.resource(Item.ironIngot, 250),
                        DiscoveryPrerequisite.time(20*60*6, DiscoveryPrerequisite.starlight()));
        SHIELD_PROJECTOR.addDiscoveryStage("", 
                        DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR),
                        DiscoveryPrerequisite.building(BuildingType.OBSERVATORY))
                .addDiscoveryStage("undiscovered",
                        DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR, 20),
                        DiscoveryPrerequisite.time(20*60*5, DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR, 16)),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR, 12)),
                        DiscoveryPrerequisite.time(20*60*15, DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR, 8)),
                        DiscoveryPrerequisite.time(20*60*20, DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR, 4)),
                        DiscoveryPrerequisite.time(20*60*25, DiscoveryPrerequisite.building(BuildingType.SHIELD_GENERATOR)),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.gainResource(Item.ironIngot, 300)),
                        DiscoveryPrerequisite.useResource(Item.ironIngot, 300),
                        DiscoveryPrerequisite.resource(Item.ironIngot, 300),
                        DiscoveryPrerequisite.time(20*60*10, DiscoveryPrerequisite.starlight()));
    }
    public final ResearchCategory category;
    public Upgrade upgrade;
    public final String fancyTitle;
    private final String title;
    private final String description;//when fully researched
    public final ArrayList<DiscoveryStage> discoveryStages = new ArrayList<>();
    private int discovery = 0;
    private final long seed;
    //Research completion
    public int powerCost;
    public double starlightCost;
    public ItemStack[] itemCosts;
    public int time;
    public final int totalPowerCost;
    public final double totalStarlightCost;
    public final ItemStack[] totalItemCosts;
    public final int totalTime;
    public boolean completed = false;
    private Research(ResearchCategory category, String fancyTitle, String title, String description, int powerCost, double starlightCost, int time, ItemStack... itemCosts){
        this.category = category;
        this.fancyTitle = fancyTitle;
        this.title = title;
        this.description = description;
        seed = new Random().nextLong();
        this.powerCost = powerCost;
        this.starlightCost = starlightCost;
        this.itemCosts = itemCosts;
        this.time = time;
        totalTime = time;
        totalPowerCost = powerCost;
        totalStarlightCost = starlightCost;
        totalItemCosts = new ItemStack[itemCosts.length];
        for(int i = 0; i<itemCosts.length; i++){
            totalItemCosts[i] = new ItemStack(itemCosts[i]);
        }
    }
    private Research addDiscoveryStage(DiscoveryStage stage){
        discoveryStages.add(stage);
        return this;
    }
    private Research addDiscoveryStage(String image, DiscoveryPrerequisite... prerequisites){
        DiscoveryStage stage = new DiscoveryStage(image);
        for(DiscoveryPrerequisite pre : prerequisites){
            stage.addPrerequisite(pre);
        }
        return addDiscoveryStage(stage);
    }
    public boolean isDiscovered(){
        return discovery>=discoveryStages.size();
    }
    public DiscoveryStage getDiscoveryStage(){
        if(isDiscovered())return null;
        return discoveryStages.get(discovery);
    }
    public boolean isCompleted(){
        if(!isDiscovered())return false;
        return completed;
    }
    public void event(ResearchEvent event){
        if(!isDiscovered()){
            discoveryStages.get(discovery).event(event);
        }
    }
    public void tick(MenuGame game){
        if(isDiscovered()){
        }else{
            discoveryStages.get(discovery).tick(game);
            if(discoveryStages.get(discovery).progress>=1){
                discovery++;
                if(isDiscovered()){
                    game.notify("New Discovery: "+title);
                }else{
                    game.notify("Discovery progressed");
                }
            }
        }
    }
    public Config save(Config config){
        config.set("discovery", discovery);
        for(int i = 0; i<discoveryStages.size(); i++){
            config.set("stage "+i, discoveryStages.get(i).save(Config.newConfig()));
        }
        config.set("power", powerCost);
        config.set("starlight", starlightCost);
        config.set("time", time);
        for(int i = 0; i<itemCosts.length; i++){
            config.set("item "+i, itemCosts[i].count);
        }
        config.set("completed", completed);
        return config;
    }
    public void load(Config config){
        discovery = config.get("discovery", discovery);
        for(int i = 0; i<discoveryStages.size(); i++){
            discoveryStages.get(i).load(config.get("stage "+i, Config.newConfig()));
        }
        powerCost = config.get("power", powerCost);
        starlightCost = config.get("starlight", starlightCost);
        time = config.get("time", time);
        for(int i = 0; i<itemCosts.length; i++){
            itemCosts[i].count = config.get("item "+i, itemCosts[i].count);
        }
        completed = config.get("completed", completed);
    }
    public boolean isDiscoverable(){
        return discovery>0;
    }
    public String getTitle(){
        return isDiscovered()?title:"Undiscovered";
    }
    public String getDescription(){
        return isDiscovered()?description:null;
    }
    public int getTexture(){
        String texture = isDiscovered()?"researched":discoveryStages.get(discovery).image;
        return ImageStash.instance.getTexture("/textures/research/"+name().toLowerCase()+"/"+texture+".png");
    }
    public long getSeed(){
        return seed;
    }
    public double getPercentDone() {
        if(isDiscovered()){
            return 1;
        }
        return getDiscoveryStage().progress;
    }
    public void complete(){
        completed = true;
        Core.game.notify("Completed Research: "+title, 20*30);
    }
    public void cheatDiscover(){
        discovery++;
    }
    public static enum ResearchCategory{
        BUILDING_UPGRADES;
    }
}