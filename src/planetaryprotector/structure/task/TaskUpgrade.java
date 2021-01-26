package planetaryprotector.structure.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.StructureType;
public class TaskUpgrade extends Task{
    public TaskUpgrade(Structure structure){
        super(structure, TaskType.CONSTRUCT, 1);
        time = structure.type.getConstructionTime();
    }
    @Override
    public boolean canPerform(){
        return game.hasResources(structure.type.getUpgradeCosts(structure.getLevel()))&&structure.task==null&&structure.damages.isEmpty()&&structure.canUpgrade();
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Upgrading "+structure.type.getDisplayName());
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
            strs.add("Upgrading "+structure.type.getDisplayName());
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
        if(structure.type==StructureType.MINE){
            if(game.rand.nextDouble()<=0.025){
                int itemX = structure.x+game.rand.nextInt(79)+11;
                int itemY = structure.y+game.rand.nextInt(79)+11;
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
        for(ItemStack stack : structure.type.getUpgradeCosts(structure.getLevel())){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        structure.upgrade();
    }
    @Override
    public void begin(){
        game.removeResources(structure.type.getUpgradeCosts(structure.getLevel()));
    }
    @Override
    public void onCancel() {
        for(ItemStack stack : structure.type.getUpgradeCosts(structure.getLevel())){
            for(int i = 0; i<stack.count; i++){
                int itemX = structure.x+game.rand.nextInt(79)+11;
                int itemY = structure.y+game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return structure.type.getUpgradeCosts(structure.getLevel());
    }
}