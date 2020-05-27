package planetaryprotector.structure.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.BuildingType;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
public class TaskUpgrade extends Task{
    public TaskUpgrade(Building building){
        super(building, TaskType.CONSTRUCT, 1);
        time = building.type.constructionTime[0];
    }
    @Override
    public boolean canPerform(){
        return game.hasResources(building.type.getCosts(building.getLevel()))&&building.task==null&&building.damages.isEmpty()&&building.canUpgrade();
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
            if(game.rand.nextDouble()<=0.025){
                double itemX = building.x+game.rand.nextInt(79)+11;
                double itemY = building.y+game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Item item = null;
                while(item!=Item.coal&&item!=Item.stone&&item!=Item.ironOre){
                    item = Item.items.get(game.rand.nextInt(Item.items.size()));
                }
                game.addItem(new DroppedItem(game, itemX, itemY, item));
            }
        }
    }
    @Override
    public void finish(){
        for(ItemStack stack : building.type.getCosts(building.getLevel()+1)){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        building.upgrade();
    }
    @Override
    public void begin(){
        game.removeResources(building.type.getCosts(building.getLevel()+1));
    }
    @Override
    public void onCancel() {
        for(ItemStack stack : building.type.getCosts(building.getLevel()+1)){
            for(int i = 0; i<stack.count; i++){
                double itemX = building.x+game.rand.nextInt(79)+11;
                double itemY = building.y+game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return building.type.getCosts(building.getLevel()+1);
    }
}