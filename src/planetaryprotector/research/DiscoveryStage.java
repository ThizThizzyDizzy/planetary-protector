package planetaryprotector.research;
import java.util.ArrayList;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class DiscoveryStage{
    public final String image;
    public ArrayList<DiscoveryPrerequisite> prerequisites = new ArrayList<>();
    public double progress = 0;
    public DiscoveryStage(String image){
        this.image = image;
    }
    public DiscoveryStage addPrerequisite(DiscoveryPrerequisite prerequisite){
        prerequisites.add(prerequisite);
        return this;
    }
    public void event(ResearchEvent event){
        synchronized(prerequisites){
            for(DiscoveryPrerequisite prerequisite : prerequisites){
                prerequisite.event(event);
            }
        }
    }
    public void tick(MenuGame game){
        double prog = 0;
        synchronized(prerequisites){
            for(DiscoveryPrerequisite prerequisite : prerequisites){
                prerequisite.tick(game);
                prog+=prerequisite.progress;
            }
            progress = prog/prerequisites.size();
        }
    }
    public Config save(Config config){
        config.set("progress", progress);
        for(int i = 0; i<prerequisites.size(); i++){
            config.set("prerequisite "+i, prerequisites.get(i).save(Config.newConfig()));
        }
        return config;
    }
    public void load(Config config){
        progress = config.get("progress", progress);
        for(int i = 0; i<prerequisites.size(); i++){
            prerequisites.get(i).load(config.get("prerequisite "+i, Config.newConfig()));
        }
    }
    public String getProgressDescription(){
        String desc = "";
        for(DiscoveryPrerequisite prerequisite : prerequisites){
            desc+="\n"+prerequisite.getProgressDescription();
        }
        return desc.trim();
    }
}