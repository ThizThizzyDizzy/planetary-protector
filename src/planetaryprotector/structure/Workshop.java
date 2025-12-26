package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.structure.task.TaskTrainWorker;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
public class Workshop extends Structure implements StructureDemolishable{
    public Workshop(Game game, int x, int y){
        super(StructureType.WORKSHOP, game, x, y, 100, 100);
    }
//    @Override
//    public void renderBackground(){
//        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
//        super.renderBackground();
//    }
    public static Workshop loadSpecific(GameState.Structure state, Game game, int x, int y){
        return new Workshop(game, x, y);
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Train Worker", new TaskTrainWorker(this)));
    }
}
