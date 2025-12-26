package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.game.Action;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.ingame.MenuResearch;
import planetaryprotector.research.Research;
import planetaryprotector.research.ResearchEvent;
public class Laboratory extends Structure implements StructureDemolishable, PowerConsumer, StarlightConsumer{
    public Research targetResearch;
    public double power;
    public double starlight;
    private final double powerSpeed = 1;
    private final double starlightSpeed = .01;
    public Laboratory(Game game, int x, int y){
        super(StructureType.LABORATORY, game, x, y, 100, 100);
    }
    @Override
    public void tick(){
        super.tick();
        if(targetResearch==null)return;
        if(targetResearch.isDiscovered()&&!targetResearch.isCompleted()){
            if(targetResearch.powerCost>0&&power>0){
                double decrease = Math.min(Math.min(power, powerSpeed), targetResearch.powerCost);
                power-=decrease;
                targetResearch.powerCost-=decrease;
            }
            if(targetResearch.starlightCost>0&&starlight>0){
                double decrease = Math.min(Math.min(starlight, starlightSpeed), targetResearch.starlightCost);
                starlight-=decrease;
                targetResearch.starlightCost-=decrease;
            }
            for(ItemStack s : targetResearch.itemCosts){
                if(s.count>0){
                    if(game.hasResources(new ItemStack(s.item))){
                        s.count--;
                        game.removeResources(new ItemStack(s.item));
                        game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, s.item, 1));
                        break;
                    }
                }
            }
            double maxPercent = 1;
            if(targetResearch.totalPowerCost>0){
                maxPercent = Math.min(maxPercent, 1-(targetResearch.powerCost/(double)targetResearch.totalPowerCost));
            }
            if(targetResearch.totalStarlightCost>0){
                maxPercent = Math.min(maxPercent, 1-(targetResearch.starlightCost/(double)targetResearch.totalStarlightCost));
            }
            for (int i = 0; i < targetResearch.itemCosts.length; i++) {
                maxPercent = Math.min(maxPercent, 1-(targetResearch.itemCosts[i].count/(double)targetResearch.totalItemCosts[i].count));
            }
            double actualPercent = 1-(targetResearch.time/(double)targetResearch.totalTime);
            if(actualPercent<maxPercent&&targetResearch.time>0)targetResearch.time--;
            if(targetResearch.time<=0||maxPercent>=1){
                targetResearch.complete(game);
                setTargetResearch(null);
            }
        }else setTargetResearch(null);
    }
//    @Override
//    public void renderBackground(){
//        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
//        super.renderBackground();
//    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.laboratory = new GameState.Structure.Laboratory();
        state.laboratory.target = targetResearch==null?"null":targetResearch.name();
        return state;
    }
    public static Laboratory loadSpecific(GameState.Structure state, Game game, int x, int y){
        String target = state.laboratory.target;
        Laboratory lab = new Laboratory(game, x, y);
        if(!target.equals("null"))lab.targetResearch = Research.valueOf(target);
        return lab;
    }
    @Override
    public double getMaxPower(){
        return Math.max(power, targetResearch==null?0:targetResearch.powerCost);
    }
    @Override
    public double getDemand(){
        return targetResearch==null?0:targetResearch.powerCost-power;
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public void addPower(double power){
        this.power+=power;
    }
    @Override
    public boolean isPowerActive(){
        return targetResearch!=null&&targetResearch.powerCost>0;
    }
    @Override
    public double getMaxStarlight(){
        return Math.max(starlight, targetResearch==null?0:targetResearch.starlightCost);
    }
    @Override
    public double getStarlightDemand(){
        return targetResearch==null?0:targetResearch.starlightCost-starlight;
    }
    @Override
    public double getStarlight(){
        return starlight;
    }
    @Override
    public void addStarlight(double starlight){
        this.starlight+=starlight;
    }
    @Override
    public boolean isStarlightActive(){
        return targetResearch!=null&&targetResearch.starlightCost>0;
    }
    public void setTargetResearch(Research research){
        targetResearch = research;
        game.refreshNetworks();
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Research", () -> {
            menu.openOverlay(new MenuResearch(menu, this));
        }, () -> {
            return true;
        }));
    }
    @Override
    public double getDisplayPower(){
        return getPower();
    }
    @Override
    public double getDisplayMaxPower(){
        return getMaxPower();
    }
    @Override
    public double getDisplayStarlight(){
        return getStarlight();
    }
    @Override
    public double getDisplayMaxStarlight(){
        return getMaxStarlight();
    }
}