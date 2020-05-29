package planetaryprotector.structure.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.Wreck;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.structure.building.Skyscraper;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
public class TaskConstruct extends TaskAnimated{
    public final Building target;
    public TaskConstruct(Building building, Building target){
        super(building, TaskType.CONSTRUCT, 1);
        this.target = target;
        time = target.type.constructionTime[0];
    }
    @Override
    public boolean canPerform(){
        return building.type==BuildingType.EMPTY&&game.hasResources(target.type.costs[0])&&building.task==null;
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
            if(game.rand.nextDouble()<=0.05){
                double itemX = target.x+game.rand.nextInt(79)+11;
                double itemY = target.y+game.rand.nextInt(79)+11;
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
        for(ItemStack stack : target.type.costs[0]){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        if(target.type==BuildingType.SKYSCRAPER){
            Skyscraper sky = (Skyscraper) target;
            sky.floorCount = 10;
        }
        game.replaceStructure(building, target);
    }
    @Override
    public void begin(){
        game.removeResources(target.type.costs[0]);
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
                double itemX = building.x+game.rand.nextInt(79)+11;
                double itemY = building.y+game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
            }
        }
        game.replaceStructure(building, new Wreck(game, building.x, building.y, ingots));
    }
    @Override
    public ItemStack[] getTooltip(){
        return target.type.costs[0];
    }
    @Override
    public int[] getAnimation(){
        return target.type.getAnimation();
    }
    @Override
    public int getHeight(){
        return target.getStructureHeight()+100;
    }
    @Override
    public boolean isInBackground(){
        switch(target.type){
            case MINE:
            case SILO:
                return true;
        }
        return false;
    }
}