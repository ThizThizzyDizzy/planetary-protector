package planetaryprotector.structure.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.structure.Wreck;
import planetaryprotector.structure.Skyscraper;
import java.util.ArrayList;
import planetaryprotector.anim.Animation;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.StructureType;
public class TaskConstruct extends TaskAnimated{
    public final Structure target;
    public TaskConstruct(Structure structure, Structure target){
        super(structure, TaskType.CONSTRUCT, 1);
        this.target = target;
        time = target.type.getConstructionTime();
    }
    @Override
    public boolean canPerform(){
        return structure.type==StructureType.EMPTY_PLOT&&game.hasResources(target.type.getConstructionCosts())&&structure.task==null;
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
        if(target.type==StructureType.MINE){//TODO put in StructureType
            if(game.rand.nextDouble()<=0.05){
                int itemX = target.x+game.rand.nextInt(79)+11;
                int itemY = target.y+game.rand.nextInt(79)+11;
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
        for(ItemStack stack : target.type.getConstructionCosts()){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        if(target.type==StructureType.SKYSCRAPER){//TODO put in StructureType
            Skyscraper sky = (Skyscraper) target;
            sky.floorCount = 10;
        }
        game.replaceStructure(structure, target);
    }
    @Override
    public void begin(){
        game.removeResources(target.type.getConstructionCosts());
    }
    @Override
    public void onCancel() {
        int ingots = 0;
        for(ItemStack stack : target.type.getConstructionCosts()){
            if(stack.item==Item.ironIngot){
                ingots += stack.count;
                continue;
            }
            for(int i = 0; i<stack.count; i++){
                int itemX = structure.x+game.rand.nextInt(79)+11;
                int itemY = structure.y+game.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
            }
        }
        game.replaceStructure(structure, new Wreck(game, structure.x, structure.y, ingots));
    }
    @Override
    public ItemStack[] getTooltip(){
        return target.type.getConstructionCosts();
    }
    @Override
    public Animation getAnimation(){
        return target.type.getAnimation();
    }
    @Override
    public int getHeight(){
        return target.getStructureHeight()+100;
    }
    @Override
    public boolean isInBackground(){
        return target.isBackgroundStructure();
    }
}