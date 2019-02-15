package planetaryprotector.building.task;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.Skyscraper;
import java.util.ArrayList;
import planetaryprotector.Core;
public class TaskSkyscraperAddFloor extends Task{
    private final Skyscraper skyscraper;
    public TaskSkyscraperAddFloor(Skyscraper skyscraper){
        this(skyscraper, 1);
    }
    public final int floors;
    public TaskSkyscraperAddFloor(Skyscraper skyscraper, int floors){
        super(skyscraper, TaskType.SKYSCRAPER_ADD_FLOOR, 1000*floors);
        this.skyscraper = skyscraper;
        this.floors = floors;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Adding "+floors+" floor"+(floors==1?"":"s")+" to "+building.type.name);
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
            strs.add("Adding "+floors+" floor"+(floors==1?"":"s")+" to "+building.type.name);
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
        skyscraper.floorCount+=floors;
    }
    @Override
    public boolean canPerform(){
        if(skyscraper.floorCount+floors>Skyscraper.maxHeight)return false;
        return !skyscraper.falling&&building.task==null&&building.damages.isEmpty()&&Core.game.hasResources(new ItemStack(Item.ironIngot, 10*floors));
    }
    @Override
    public void start(){
        Core.game.removeResources(new ItemStack(Item.ironIngot, 10*floors));
    }
    @Override
    public void cancel(){
        for(ItemStack stack : building.type.repairCost){
            for(int i = 0; i<stack.count*floors; i++){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Core.game.addItem(new DroppedItem(itemX, itemY, stack.item, Core.game));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[]{new ItemStack(Item.ironIngot, 10*floors)};
    }
}