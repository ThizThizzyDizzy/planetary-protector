package planetaryprotector.game;
import java.util.ArrayList;
import java.util.HashMap;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.particle.Particle;
public abstract class Story{
    public static final HashMap<Integer, ArrayList<Story>> stories = new HashMap<>();
    static{
        for(int i = 1; i<=Core.LEVELS; i++){
            stories.put(i, new ArrayList<>());
        }
        addStory(1, new Story("level1"){
            @Override
            public void tick(Game game){
                //<editor-fold defaultstate="collapsed" desc="Post-lose epilogue loading">
                if(game.lost&&game.phase>3){
                    game.lostTimer++;
                    if(game.lostTimer>game.loseSongLength/10){
                        if(Sounds.songTimer()<game.loseSongLength/20){
                            game.allowArmogeddon = false;
                            for(Particle particle : game.particles){
                                particle.strength-=.1;
                            }
                        }
                    }
                }
        //</editor-fold>
            }
            @Override
            public String getName(){
                return "Default";
            }
        });
    }
    public static Story getStory(int level, String name){
        for(Story gen : stories.get(level)){
            if(gen.id.equals(name))return gen;
        }
        return stories.get(level).get(0);
    }
    private static void addStory(int level, Story story) {
        stories.get(level).add(story);
    }
    public final String id;
    public Story(String id){
        this.id = id;
    }
    public abstract void tick(Game game);
    public String getName(){
        return id;
    }
}