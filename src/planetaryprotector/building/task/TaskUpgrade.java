package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import java.util.ArrayList;
public class TaskUpgrade extends Task{
    public TaskUpgrade(Building building){
        super(building, TaskType.CONSTRUCT, 1);
        time = building.type.constructionTime[0];
    }
    @Override
    public boolean canPerform(){
        return Core.game.hasResources(building.type.getCosts(building.getLevel()))&&building.task==null&&building.damages.isEmpty()&&building.canUpgrade();
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Upgrading "+building.type.name);
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
            strs.add("Upgrading "+building.type.name);
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
        if(building.type==BuildingType.MINE){
            if(MenuGame.rand.nextDouble()<=0.025){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Item item = null;
                while(item!=Item.coal&&item!=Item.stone&&item!=Item.ironOre){
                    item = Item.items.get(MenuGame.rand.nextInt(Item.items.size()));
                }
                Core.game.addItem(new DroppedItem(itemX, itemY, item, Core.game));
            }
        }
    }
    @Override
    public void finish(){
        building.upgrade();
    }
    @Override
    public void begin(){
        Core.game.removeResources(building.type.getCosts(building.getLevel()+1));
    }
    @Override
    public void onCancel() {
        for(ItemStack stack : building.type.getCosts(building.getLevel()+1)){
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
        return building.type.getCosts(building.getLevel()+1);
    }
}