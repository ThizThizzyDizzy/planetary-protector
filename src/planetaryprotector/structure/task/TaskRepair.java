package planetaryprotector.structure.task;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.StructureDamage;
import java.util.ArrayList;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.StructureType;
public class TaskRepair extends Task{
    public StructureDamage damage;
    private double initialFire;
    public TaskRepair(Structure structure){
        super(structure, TaskType.REPAIR, 0);
        time = getRepairTime(structure.type);
        if(structure.damages.size()>0){
            damage = structure.damages.get(0);
        }
    }
    public int getRepairTime(StructureType type){
        int repairTime = 0;
        for(ItemStack stack : type.getRepairCosts()){
            repairTime+=stack.item.getRepairTime();
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
        return structure.task==null&&damage!=null&&game.hasResources(structure.type.getRepairCosts());
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
        damage.opacity = 1-progress();
        structure.fire = (1-progress())*initialFire;
    }
    @Override
    public void finish(){
        for(ItemStack stack : structure.type.getRepairCosts()){
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, stack.item, stack.count));
        }
        structure.damages.remove(damage);
        structure.fire = structure.fireIncreaseRate = structure.fireDamage = 0;
        structure.clearFires();
    }
    @Override
    public void begin(){
        structure.fireIncreaseRate = 0;
        initialFire = structure.fire;
        if(damage!=null)progress = (int) Math.round((1-damage.opacity)*time);
        game.removeResources(structure.type.getRepairCosts());
    }
    @Override
    public void onCancel(){
        for(ItemStack stack : structure.type.getRepairCosts()){
            for(int i = 0; i<stack.count*progress()-1; i++){
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
        return structure.type.getRepairCosts();
    }
}