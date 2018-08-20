package planetaryprotector.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.MenuComponentDroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentBuildingDamage;
import java.util.ArrayList;
public class TaskRepairAll extends Task{
    ArrayList<MenuComponentBuildingDamage> damages = new ArrayList<>();
    private double initialFire;
    public TaskRepairAll(MenuComponentBuilding building){
        super(building, TaskType.REPAIR, 0);
        time = getRepairTime(building.type)*building.damages.size();
        if(building.damages.size()>0){
            damages.addAll(building.damages);
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
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper) building;
                if(sky.falling){
                    return false;
                }
                break;
        }
        return building.task==null&&building.damages.size()>0&&game.hasResources(new ItemStack(building.type.repairCost[0].item, building.type.repairCost[0].count*building.damages.size()));
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Repairing "+building.type.name);
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
        for(MenuComponentBuildingDamage damage : damages){
            damage.opacity = 1-progress();
        }
        building.fire = (1-progress())*initialFire;
    }
    @Override
    public void finish(){
        building.damages.removeAll(damages);
        building.components.removeAll(damages);
        building.fire = building.fireIncreaseRate = building.fireDamage = 0;
        building.components.removeAll(building.fires);
        building.clearFires();
    }
    @Override
    public void start(){
        building.fireIncreaseRate = 0;
        initialFire = building.fire;
        progress = (int) Math.round((1-damages.get(0).opacity)*time);
        game.removeResources(new ItemStack(building.type.repairCost[0].item, building.type.repairCost[0].count*damages.size()));
    }
    @Override
    public void cancel(){
        for(ItemStack stack : building.type.repairCost){
            for(int i = 0; i<stack.count*progress()-1*damages.size(); i++){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new MenuComponentDroppedItem(itemX, itemY, stack.item, game));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[]{new ItemStack(building.type.repairCost[0].item, building.type.repairCost[0].count*damages.size())};
    }
}