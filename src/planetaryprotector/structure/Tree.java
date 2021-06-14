package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.game.Game;
import simplelibrary.config2.Config;
public class Tree extends Structure{
    public Tree(Game game, int x, int y){
        super(StructureType.TREE, game, x, y, 10, 4);
    }
    @Override
    public String getName(){
        return "Tree";
    }
    @Override
    public boolean damage(int x, int y){
        dead = true;
        return true;
    }
    public static Tree loadSpecific(Config cfg, Game game, int x, int y) {
        Tree tree = new Tree(game, x, y);
        return tree;
    }
    @Override
    public void postLoad(Game game, Config config){}
    @Override
    public void getDebugInfo(ArrayList<String> data){}
}