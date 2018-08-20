package planetaryprotector.building.task;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.item.ItemStack;
public abstract class Task{
    public final MenuComponentBuilding building;
    public final TaskType type;
    public int time;
    public int progress = 0;
    public final MenuGame game;
    public boolean finished = false;
    public Task(MenuComponentBuilding building, TaskType type, int time){
        this.building = building;
        this.type = type;
        this.time = time;
        game = (MenuGame) building.parent;
    }
    public int getWorkers(){
        int workers = 0;
        for(MenuComponentWorker worker : game.workers){
            if(worker.task==this){
                workers++;
            }
        }
        return workers;
    }
    public double progress(){
        if(finished){
            return Double.POSITIVE_INFINITY;
        }
        return (progress+0D)/time;
    }
    public abstract boolean canPerform();
    public abstract String[] getDetails();
    public abstract void start();
    public abstract void work();
    public abstract void finish();
    public abstract void cancel();
    public abstract ItemStack[] getTooltip();
}