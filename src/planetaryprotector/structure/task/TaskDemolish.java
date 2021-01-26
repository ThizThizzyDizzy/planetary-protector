package planetaryprotector.structure.task;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.Wreck;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.item.ItemStack;
import java.util.ArrayList;
import planetaryprotector.item.Item;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.StructureType;
public class TaskDemolish extends Task{
    public TaskDemolish(Structure structure){
        super(structure, TaskType.CONSTRUCT, 200);
        important = true;
    }
    @Override
    public boolean canPerform(){
        return structure.type!=StructureType.WRECK&&structure.type!=StructureType.BASE&&structure.task==null;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Demolishing "+structure.type.getDisplayName());
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
            strs.add("Demolishing "+structure.type.getDisplayName());
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
    }
    @Override
    public void finish(){
        game.addParticleEffect(new Particle(game, structure.x+structure.width/2, structure.y+structure.height/2-1, ParticleEffectType.EXPLOSION, 5));
        if(structure.type==StructureType.SKYSCRAPER){
            Skyscraper sky = (Skyscraper) structure;
            sky.falling = true;
        }else{
            game.replaceStructure(structure, new Wreck(game, structure.x, structure.y, structure.type.getTotalCost(structure.getLevel(), Item.ironIngot)));
        }
    }
    @Override
    public void begin(){}
    @Override
    public void onCancel(){}
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[0];
    }
}