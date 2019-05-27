package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import java.util.ArrayList;
public class TaskConstruct extends Task{
    public final Building target;
    public TaskConstruct(Building building, Building target){
        super(building, TaskType.CONSTRUCT, 1);
        if(target instanceof Skyscraper){
            ((Skyscraper) target).floorCount = 10;
        }
        this.target = target;
        time = target.type.constructionTime[0];
    }
    @Override
    public boolean canPerform(){
        return building.type==BuildingType.EMPTY&&Core.game.hasResources(target.type.costs[0])&&building.task==null;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Constructing "+target.type.name);
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
            strs.add("Constructing "+target.type.name);
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
        if(target.type==BuildingType.MINE){
            if(MenuGame.rand.nextDouble()<=0.05){
                double itemX = target.x+MenuGame.rand.nextInt(79)+11;
                double itemY = target.y+MenuGame.rand.nextInt(79)+11;
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
        if(target.type==BuildingType.SKYSCRAPER){
            Skyscraper sky = (Skyscraper) target;
            sky.floorCount = 10;
        }
        Core.game.replaceBuilding(building, target);
    }
    @Override
    public void begin(){
        Core.game.removeResources(target.type.costs[0]);
    }
    @Override
    public void onCancel() {
        int ingots = 0;
        for(ItemStack stack : target.type.costs[0]){
            if(stack.item==Item.ironIngot){
                ingots += stack.count;
                continue;
            }
            for(int i = 0; i<stack.count; i++){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Core.game.addItem(new DroppedItem(itemX, itemY, stack.item, Core.game));
            }
        }
        Core.game.replaceBuilding(building, new Wreck(building.x, building.y, ingots));
    }
    @Override
    public ItemStack[] getTooltip(){
        return target.type.costs[0];
    }
}