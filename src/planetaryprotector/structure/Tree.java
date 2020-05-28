package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.game.Game;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
public class Tree extends Structure{
    public Tree(Game game, double x, double y){
        super(game, x, y, 10, 4);
    }
    @Override
    public String getName(){
        return "Tree";
    }
    @Override
    public Config saveStructure(Config cfg){
        return cfg;
    }
    @Override
    public boolean damage(double x, double y){
        dead = true;
        return true;
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
    @Override
    protected int getTexture(){
        return ImageStash.instance.getTexture("/textures/environment/tree/"+Game.theme.tex()+".png");
    }
    @Override
    public void postLoad(Game game, Config config){}
    @Override
    public void getDebugInfo(ArrayList<String> data){}
    @Override
    public int getStructureHeight(){
        return 14;
    }
    @Override
    public boolean isSelectable(){
        return true;
    }
    @Override
    public boolean canBeShielded(){
        return true;
    }
}