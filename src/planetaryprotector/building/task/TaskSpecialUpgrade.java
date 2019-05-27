package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.Building;
import java.util.ArrayList;
import planetaryprotector.building.Building.Upgrade;
public class TaskSpecialUpgrade extends Task{
    private final Upgrade upgrade;
    public TaskSpecialUpgrade(Building building, Upgrade upgrade){
        super(building, TaskType.CONSTRUCT, 1);
        time = upgrade.time;
        this.upgrade = upgrade;
    }
    @Override
    public boolean canPerform(){
        return Core.game.hasResources(upgrade.costs)&&building.task==null&&building.damages.isEmpty();
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
        if(!building.buyUpgrade(upgrade))onCancel();
    }
    @Override
    public void begin(){
        Core.game.removeResources(upgrade.costs);
    }
    @Override
    public void onCancel() {
        for(ItemStack stack : upgrade.costs){
            for(int i = 0; i<stack.count; i++){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Core.game.addItem(new DroppedItem(itemX, itemY, stack.item, Core.game));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return upgrade.costs;
    }
}