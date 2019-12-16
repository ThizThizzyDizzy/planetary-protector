package planetaryprotector.building.task;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.item.ItemStack;
import java.util.ArrayList;
import planetaryprotector.Core;
public class TaskDemolish extends Task{
    public TaskDemolish(Building building){
        super(building, TaskType.CONSTRUCT, 200);
        important = true;
    }
    @Override
    public boolean canPerform(){
        return building.type!=BuildingType.WRECK&&building.type!=BuildingType.BASE&&building.task==null;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Demolishing "+building.type.name);
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
            strs.add("Demolishing "+building.type.name);
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
        Core.game.addParticleEffect(new Particle(building.x+building.width/2, building.y+building.height/2-1, ParticleEffectType.EXPLOSION, 5));
        if(building.type==BuildingType.SKYSCRAPER){
            Skyscraper sky = (Skyscraper) building;
            sky.falling = true;
        }else{
            if(building.type.costs.length==1){
                Core.game.replaceBuilding(building, new Wreck(building.x, building.y, building.type.costs[0][0].count));
            }else{
                Core.game.replaceBuilding(building, new Wreck(building.x, building.y, building.type.costs[0][0].count+(building.type.costs[1][0].count*building.getLevel())));
            }
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