package planetaryprotector.research;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Structure;
import simplelibrary.config2.Config;
public class DiscoveryPrerequisite{
    private final Type type;
    private int phase;
    private BuildingType building;
    private int level;
    private int count;
    private int ticks;
    private DiscoveryPrerequisite[] prerequisites = new DiscoveryPrerequisite[0];
    private Item item;
    public double progress;
    private int discovery;//all values for discovery
    private DiscoveryPrerequisite(Type type){
        this.type = type;
    }
    private DiscoveryPrerequisite(Type type, int phase){
        this.type = type;
        this.phase = phase;
    }
    private DiscoveryPrerequisite(Type type, BuildingType building, int level, int count){
        this.type = type;
        this.building = building;
        this.level = level;
        this.count = count;
    }
    private DiscoveryPrerequisite(Type type, int ticks, DiscoveryPrerequisite... prerequisites){
        this.type = type;
        this.ticks = ticks;
        this.prerequisites = prerequisites;
    }
    private DiscoveryPrerequisite(Type type, Item item, int count){
        this.type = type;
        this.item = item;
        this.count = count;
    }
    public boolean isMet(Game game){
        return progress>=1;
    }
    public static DiscoveryPrerequisite starlight(){
        return new DiscoveryPrerequisite(Type.STARLIGHT);
    }
    public static DiscoveryPrerequisite phase(int phase){
        return new DiscoveryPrerequisite(Type.PHASE, phase);
    }
    public static DiscoveryPrerequisite building(BuildingType building){
        return building(building, 1);
    }
    public static DiscoveryPrerequisite building(BuildingType building, int level){
        return building(building, level, 1);
    }
    public static DiscoveryPrerequisite building(BuildingType building, int level, int count){
        return new DiscoveryPrerequisite(Type.BUILDING, building, level, count);
    }
    public static DiscoveryPrerequisite time(int ticks, DiscoveryPrerequisite... prerequisites){
        return new DiscoveryPrerequisite(Type.TIME, ticks, prerequisites);
    }
    public static DiscoveryPrerequisite resource(Item resource, int count){
        return new DiscoveryPrerequisite(Type.RESOURCE, resource, count);
    }
    public static DiscoveryPrerequisite gainResource(Item resource, int count){
        return new DiscoveryPrerequisite(Type.GAIN_RESOURCE, resource, count);
    }
    public static DiscoveryPrerequisite useResource(Item resource, int count){
        return new DiscoveryPrerequisite(Type.USE_RESOURCE, resource, count);
    }
    public void event(ResearchEvent event){
        switch(type){
            case GAIN_RESOURCE:
                if(event.type==ResearchEvent.Type.GAIN_RESOURCE&&event.item==item)discovery+=event.value;
                break;
            case USE_RESOURCE:
                if(event.type==ResearchEvent.Type.USE_RESOURCE&&event.item==item)discovery+=event.value;
                break;
            case TIME:
                for(DiscoveryPrerequisite prerequisite : prerequisites){
                    prerequisite.event(event);
                }
                break;
        }
    }
    public void tick(Game game){
        switch(type){
            case STARLIGHT:
                progress = game.observatory?1:0;
                break;
            case PHASE:
                progress = game.phase/phase;
                break;
            case BUILDING:
                double found = 0;
                for(Structure s : game.structures){
                    if(s instanceof Building){
                        Building b = (Building) s;
                        if(b.type==building){
                            found += Math.min(level, b.getLevel())/(double)level;
                        }
                    }
                }
                progress = found/count;
                break;
            case TIME:
                double maxProgress = 0;
                for(DiscoveryPrerequisite prerequisite : prerequisites){
                    prerequisite.tick(game);
                    maxProgress+=prerequisite.progress;
                }
                maxProgress/=prerequisites.length;
                if(progress<maxProgress)discovery++;
                progress = discovery/(double)ticks;
                break;
            case RESOURCE:
                progress = game.getResources(item)/(double)count;
                break;
            case GAIN_RESOURCE:
            case USE_RESOURCE:
                progress = discovery/(double)count;
                break;
        }
        progress = Math.max(0, Math.min(1, progress));
    }
    public Config save(Config config){
        for(int i = 0; i<prerequisites.length; i++){
            config.set("prerequisite "+i, prerequisites[i].save(Config.newConfig()));
        }
        config.set("progress", progress);
        config.set("discovery", discovery);
        return config;
    }
    public void load(Config config){
        for(int i = 0; i<prerequisites.length; i++){
            prerequisites[i].load(config.get("prerequisite "+i, Config.newConfig()));
        }
        progress = config.get("progress", progress);
        discovery = config.get("discovery", discovery);
    }
    public String getProgressDescription(Game game){
        switch(type){
            case BUILDING:
                return count+"x"+building.toString()+" "+level+": ("+toPercent(progress)+"%)";
            case GAIN_RESOURCE:
                return "Gain "+item.name+": "+discovery+"/"+count+" ("+toPercent(progress)+"%)";
            case PHASE:
                return "Phase "+game.phase+"/"+phase+" ("+toPercent(progress)+"%)";
            case RESOURCE:
                return item.name+": "+Math.round(progress*count)+"/"+count+" ("+toPercent(progress)+"%)";
            case STARLIGHT:
                return "Starlight: "+game.observatory+" ("+toPercent(progress)+"%)";
            case TIME:
                String time = "Time: "+discovery+"/"+ticks+" ("+toPercent(progress)+"%)";
                for(DiscoveryPrerequisite pre : prerequisites){
                    for(String s : pre.getProgressDescription(game).split("\n")){
                        time+="\n- "+s;
                    }
                }
                return time;
            case USE_RESOURCE:
                return "Use "+item.name+": "+discovery+"/"+count+" ("+toPercent(progress)+"%)";
        }
        return "oof";
    }
    private String toPercent(double progress){
        progress*=100;
        progress = Math.round(progress*100)/100d;
        if(Math.round(progress*10)==progress*10){
            return progress+"0";
        }
        return progress+"";
    }
    private static enum Type{
        STARLIGHT,PHASE,BUILDING,TIME,RESOURCE,GAIN_RESOURCE,USE_RESOURCE;
    }
}