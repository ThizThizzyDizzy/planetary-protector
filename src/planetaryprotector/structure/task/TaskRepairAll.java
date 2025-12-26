package planetaryprotector.structure.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.StructureDamage;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.StructureType;
public class TaskRepairAll extends Task{
    ArrayList<StructureDamage> damages = new ArrayList<>();
    private double initialFire;
    public TaskRepairAll(Structure structure){
        super(structure, TaskType.REPAIR, 0);
        time = getRepairTime(structure.type)*structure.damages.size();
        if(structure.damages.size()>0){
            damages.addAll(structure.damages);
        }
    }
    public int getRepairTime(StructureType type){
        int repairTime = 0;
        for(ItemStack stack : type.getRepairCosts()){
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
        if(structure.type==StructureType.SKYSCRAPER){
            Skyscraper sky = (Skyscraper) structure;
            if(sky.falling){
                return false;
            }
        }
        for(ItemStack s : structure.type.getRepairCosts()){
            if(!game.hasResources(new ItemStack(s.item, s.count*structure.damages.size())))return false;
        }
        return structure.task==null&&structure.damages.size()>0;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Repairing "+structure.type.getDisplayName());
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
            strs.add("Repairing "+structure.type.getDisplayName());
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
        for(StructureDamage damage : damages){
            damage.opacity = 1-progress();
        }
        structure.fire = (1-progress())*initialFire;
    }
    @Override
    public void finish(){
        for(ItemStack stack : structure.type.getRepairCosts()){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count*damages.size()));
        }
        structure.damages.removeAll(damages);
        structure.fire = structure.fireIncreaseRate = structure.fireDamage = 0;
        structure.clearFires();
    }
    @Override
    public void begin(){
        structure.fireIncreaseRate = 0;
        initialFire = structure.fire;
        if(damages.isEmpty()){
            finish();
            return;
        }
        progress = (int) Math.round((1-damages.get(0).opacity)*time);
        for(ItemStack stack : structure.type.getRepairCosts()){
            game.removeResources(new ItemStack(stack.item, stack.count*damages.size()));
        }
    }
    @Override
    public void onCancel(){
//        for(ItemStack stack : structure.type.getRepairCosts()){
//            for(int i = 0; i<stack.count*progress()-1*damages.size(); i++){
//                int itemX = structure.x+game.rand.nextInt(79)+11;
//                int itemY = structure.y+game.rand.nextInt(79)+11;
//                itemX-=5;
//                itemY-=5;
//                game.addItem(new DroppedItem(game, itemX, itemY, stack.item));
//            }
//        }
    }
    @Override
    public ItemStack[] getTooltip(){
        ItemStack[] one = structure.type.getRepairCosts();
        ItemStack[] many = new ItemStack[one.length];
        for(int i = 0; i<one.length; i++){
            many[i] = new ItemStack(one[i].item, one[i].count*damages.size());
        }
        return many;
    }
}