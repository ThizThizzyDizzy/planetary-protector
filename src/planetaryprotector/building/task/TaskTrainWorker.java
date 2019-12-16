package planetaryprotector.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import java.util.ArrayList;
import planetaryprotector.Core;
import planetaryprotector.building.Workshop;
import planetaryprotector.research.ResearchEvent;
public class TaskTrainWorker extends Task{
    private final Workshop workshop;
    public TaskTrainWorker(Workshop workshop){
        super(workshop, TaskType.TRAIN_WORKER, 60*20);
        this.workshop = workshop;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        strs.add("Training Worker...");
        if(getWorkers()>0){
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
            if(minutes>0){
                strs.add("- "+minutes+" minute"+(minutes==1?"":"s"));
            }
            if(seconds>0){
                strs.add("- "+seconds+" second"+(seconds==1?"":"s"));
            }
        }
        strs.add("- "+Math.round(progress()*100)+"% completed");
        return strs.toArray(new String[strs.size()]);
    }
    @Override
    public void work(){
        progress++;
    }
    @Override
    public void finish(){
        Core.game.researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, Item.ironIngot, 30));
        Core.game.addWorker(workshop.x+workshop.width/2, workshop.y+workshop.height/2);
    }
    @Override
    public boolean canPerform(){
        return Core.game.hasResources(new ItemStack(Item.ironIngot, 30))&&building.task==null&&building.damages.isEmpty();
    }
    @Override
    public void begin(){
        Core.game.removeResources(new ItemStack(Item.ironIngot, 30));
    }
    @Override
    public void onCancel(){
        for(int i = 0; i<30; i++){
            double itemX = building.x+MenuGame.rand.nextInt(79)+11;
            double itemY = building.y+MenuGame.rand.nextInt(79)+11;
            itemX-=5;
            itemY-=5;
            Core.game.addItem(new DroppedItem(itemX, itemY, Item.ironIngot));
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[]{new ItemStack(Item.ironIngot, 30)};
    }
}