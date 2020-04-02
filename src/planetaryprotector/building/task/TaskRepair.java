package planetaryprotector.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.game.Game;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.BuildingDamage;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
public class TaskRepair extends Task{
    public BuildingDamage damage;
    private double initialFire;
    public TaskRepair(Building building){
        super(building, TaskType.REPAIR, 0);
        time = getRepairTime(building.type);
        if(building.damages.size()>0){
            damage = building.damages.get(0);
        }
    }
    public int getRepairTime(BuildingType type){
        int repairTime = 0;
        for(ItemStack stack : type.repairCost){
            if(stack.item==Item.stone){
                repairTime+=20;
            }
            if(stack.item==Item.ironIngot){
                repairTime+=50;
            }
        }
        return repairTime;
    }
    @Override
    public boolean canPerform(){
        switch(building.type){
            case SKYSCRAPER:
                Skyscraper sky = (Skyscraper) building;
                if(sky.falling){
                    return false;
                }
                break;
        }
        return building.task==null&&damage!=null&&game.hasResources(building.type.repairCost);
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            
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
            strs.add("Repairing "+building.type.name);
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
        damage.opacity = 1-progress();
        building.fire = (1-progress())*initialFire;
    }
    @Override
    public void finish(){
        for(ItemStack stack : building.type.repairCost){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        building.damages.remove(damage);
        building.fire = building.fireIncreaseRate = building.fireDamage = 0;
        building.clearFires();
    }
    @Override
    public void begin(){
        building.fireIncreaseRate = 0;
        initialFire = building.fire;
        if(damage!=null)progress = (int) Math.round((1-damage.opacity)*time);
        game.removeResources(building.type.repairCost);
    }
    @Override
    public void onCancel(){
        for(ItemStack stack : building.type.repairCost){
            for(int i = 0; i<stack.count*progress()-1; i++){
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
        return new ItemStack[]{building.type.repairCost[0]};
    }
}