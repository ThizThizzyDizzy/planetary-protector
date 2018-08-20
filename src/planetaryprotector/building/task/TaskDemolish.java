package planetaryprotector.building.task;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentWreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.item.ItemStack;
import java.util.ArrayList;
public class TaskDemolish extends Task{
    public TaskDemolish(MenuComponentBuilding building){
        super(building, TaskType.CONSTRUCT, 1);
        time = 200;
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
        game.addParticleEffect(new MenuComponentParticle(building.x+building.width/2, building.y+building.height/2-1, ParticleEffectType.EXPLOSION, 5));
        if(building.type==BuildingType.SKYSCRAPER){
            MenuComponentSkyscraper sky = (MenuComponentSkyscraper) building;
            sky.falling = true;
            game.addWorker();
        }else{
            if(building.type.costs.length==1){
                game.replaceBuilding(building, new MenuComponentWreck(building.x, building.y, building.type.costs[0][0].count));
            }else{
                game.replaceBuilding(building, new MenuComponentWreck(building.x, building.y, building.type.costs[0][0].count+(building.type.costs[1][0].count*building.level)));
            }
        }
    }
    @Override
    public void start(){}
    @Override
    public void cancel(){
        finish();
    }
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[0];
    }
}