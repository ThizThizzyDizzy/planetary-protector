package planetaryprotector.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.Plot;
import java.util.ArrayList;
import planetaryprotector.Core;
import planetaryprotector.item.ItemStack;
public class TaskWreckClean extends Task{
    private final Wreck wreck;
    public TaskWreckClean(Wreck wreck){
        super(wreck, TaskType.WRECK_CLEAN, Math.max(100,wreck.ingots));
        this.wreck = wreck;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Cleaning "+building.type.name);
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
            strs.add("Cleaning "+building.type.name);
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
        if(wreck.ingots>0){
            double itemX = building.x+MenuGame.rand.nextInt(79)+11;
            double itemY = building.y+MenuGame.rand.nextInt(79)+11;
            itemX-=5;
            itemY-=5;
            Core.game.addItem(new DroppedItem(itemX, itemY, Item.ironIngot, Core.game));
            wreck.ingots--;
        }
    }
    @Override
    public void finish(){
        Core.game.replaceBuilding(building, new Plot(building.x, building.y));
        while(wreck.ingots>0){
            double itemX = building.x+MenuGame.rand.nextInt(79)+11;
            double itemY = building.y+MenuGame.rand.nextInt(79)+11;
            itemX-=5;
            itemY-=5;
            Core.game.addItem(new DroppedItem(itemX, itemY, Item.ironIngot, Core.game));
            wreck.ingots--;
        }
    }
    @Override
    public boolean canPerform(){
        return building.task==null;
    }
    @Override
    public void start(){
        Core.game.startAnim(this);
    }
    @Override
    public void cancel(){
        Core.game.cancelAnim(this);
    }
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[0];
    }
}