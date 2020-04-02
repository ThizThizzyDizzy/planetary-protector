package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.game.Game;
import planetaryprotector.building.Building;
import java.util.ArrayList;
import planetaryprotector.building.Building.Upgrade;
import planetaryprotector.research.ResearchEvent;
public class TaskSpecialUpgrade extends TaskAnimated{
    private final Upgrade upgrade;
    public TaskSpecialUpgrade(Building building, Upgrade upgrade){
        super(building, TaskType.CONSTRUCT, 1);
        time = upgrade.time;
        this.upgrade = upgrade;
    }
    @Override
    public boolean canPerform(){
        return game.hasResources(upgrade.costs)&&building.task==null&&building.damages.isEmpty();
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Upgrading "+building.type.name+" with "+upgrade.toString());
            strs.add("- "+Math.round(progress()*100)+"% completed");
        }else{
            int ticks = time-progress;
            ticks/=getWorkers();
            int seconds = 0;
            int minutes = 0;
            while(ticks>=20){
                seconds++;
                ticks-=20;
            }
            while(seconds>=60){
                minutes++;
                seconds-=60;
            }
            strs.add("Upgrading "+building.type.name+" with "+upgrade.toString());
            if(minutes>0){
                strs.add("- "+minutes+" minute"+(minutes==1?"":"s"));
            }
            if(seconds>0){
                strs.add("- "+seconds+" second"+(seconds==1?"":"s"));
            }
            strs.add("- "+Math.round(progress()*100)+"% completed");
        }
        return strs.toArray(new String[strs.size()]);
    }
    @Override
    public void work(){
        progress++;
    }
    @Override
    public void finish(){
        for(ItemStack stack : upgrade.costs){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        if(!building.buyUpgrade(upgrade))onCancel();
    }
    @Override
    public void begin(){
        game.removeResources(upgrade.costs);
    }
    @Override
    public void onCancel() {
        for(ItemStack stack : upgrade.costs){
            for(int i = 0; i<stack.count; i++){
                double itemX = building.x+Game.rand.nextInt(79)+11;
                double itemY = building.y+Game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return upgrade.costs;
    }
    @Override
    public int[] getAnimation(){
        return upgrade.getAnimation(building.type, building.getUpgrades(upgrade)+1);
    }
    @Override
    public int getHeight(){
        return building.getBuildingHeight()+100;
    }
    @Override
    public boolean isInBackground(){
        return false;
    }
}