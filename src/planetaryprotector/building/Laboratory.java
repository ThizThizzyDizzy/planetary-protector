package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.Core;
import planetaryprotector.item.ItemStack;
import planetaryprotector.research.Research;
import planetaryprotector.research.ResearchEvent;
import simplelibrary.config2.Config;
public class Laboratory extends Building implements BuildingDamagable, BuildingDemolishable, BuildingPowerConsumer, BuildingStarlightConsumer{
    public Research targetResearch;
    public double power;
    public double starlight;
    private final double powerSpeed = 1;
    private final double starlightSpeed = .01;
    public Laboratory(double x, double y){
        super(x, y, 100, 100, BuildingType.LABORATORY);
    }
    @Override
    public void update(){
        super.update();
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
                    if(Core.game.hasResources(new ItemStack(s.item))){
                        s.count--;
                        Core.game.removeResources(new ItemStack(s.item));
                        Core.game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, s.item, 1));
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
                targetResearch.complete();
                setTargetResearch(null);
            }
        }else setTargetResearch(null);
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("target", targetResearch==null?"null":targetResearch.name());
        return cfg;
    }
    public static Laboratory loadSpecific(Config cfg, double x, double y){
        String target = cfg.get("target", "null");
        Laboratory lab = new Laboratory(x, y);
        if(!target.equals("null"))lab.targetResearch = Research.valueOf(target);
        return lab;
    }
    @Override
    protected double getIgnitionChance(){
        return .8;
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data){}
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
        Core.game.refreshNetworks();
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
    @Override
    public int getBuildingHeight(){
        return 10;
    }
}