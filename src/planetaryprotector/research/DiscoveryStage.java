package planetaryprotector.research;
import java.util.ArrayList;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
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
        for(DiscoveryPrerequisite prerequisite : prerequisites){
            prerequisite.event(event);
        }
    }
    public void tick(Game game){
        double prog = 0;
        for(DiscoveryPrerequisite prerequisite : prerequisites){
            prerequisite.tick(game);
            prog+=prerequisite.progress;
        }
        progress = prog/prerequisites.size();
    }
    public GameState.Research.DiscoveryStage save(){
        GameState.Research.DiscoveryStage state = new GameState.Research.DiscoveryStage();
        state.progress = progress;
        for(var prereq : prerequisites){
            state.prerequisites.add(prereq.save());
        }
        return state;
    }
    public void load(GameState.Research.DiscoveryStage state){
        progress = state.progress;
        for(int i = 0; i<prerequisites.size(); i++){
            prerequisites.get(i).load(state.prerequisites.get(i));
        }
    }
    public String getProgressDescription(Game game){
        String desc = "";
        for(DiscoveryPrerequisite prerequisite : prerequisites){
            desc+="\n"+prerequisite.getProgressDescription(game);
        }
        return desc.trim();
    }
}