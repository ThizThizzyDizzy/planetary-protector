package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.item.MenuComponentDroppedItem;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.BuildingType;
import java.util.ArrayList;
public class TaskUpgrade extends Task{
    final MenuComponentBuilding target;
    public TaskUpgrade(MenuComponentBuilding building){
        super(building, TaskType.CONSTRUCT, 1);
        target = building.getUpgraded();
        time = target.type.constructionTime[0];
    }
    @Override
    public boolean canPerform(){
        return game.hasResources(target.type.getCosts(target.level))&&building.task==null&&building.damages.isEmpty()&&building.canUpgrade();
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Upgrading "+target.type.name);
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
            strs.add("Upgrading "+target.type.name);
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
        progress++;
        if(target.type==BuildingType.MINE){
            if(MenuGame.rand.nextDouble()<=0.025){
                double itemX = target.x+MenuGame.rand.nextInt(79)+11;
                double itemY = target.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                Item item = null;
                while(item!=Item.coal&&item!=Item.stone&&item!=Item.ironOre){
                    item = Item.items.get(MenuGame.rand.nextInt(Item.items.size()));
                }
                Core.game.addItem(new MenuComponentDroppedItem(itemX, itemY, item, Core.game));
            }
        }
    }
    @Override
    public void finish(){
        game.replaceBuilding(building, building.getUpgraded());
    }
    @Override
    public void start(){
        game.removeResources(target.type.getCosts(target.level));
    }
    @Override
    public void cancel() {
        for(ItemStack stack : target.type.getCosts(target.level)){
            for(int i = 0; i<stack.count; i++){
                double itemX = building.x+MenuGame.rand.nextInt(79)+11;
                double itemY = building.y+MenuGame.rand.nextInt(79)+11;
                itemX-=5;
                itemY-=5;
                game.addItem(new MenuComponentDroppedItem(itemX, itemY, stack.item, game));
            }
        }
    }
    @Override
    public ItemStack[] getTooltip(){
        return target.type.getCosts(target.level);
    }
}