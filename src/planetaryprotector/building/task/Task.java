package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.friendly.Worker;
import planetaryprotector.building.Building;
import planetaryprotector.item.ItemStack;
public abstract class Task{
    public final Building building;
    public final TaskType type;
    public int time;
    public int progress = 0;
    public boolean finished = false;
    public boolean important = false;
    public Task(Building building, TaskType type, int time){
        this.building = building;
        this.type = type;
        this.time = time;
    }
    public int getWorkers(){
        int workers = 0;
        for(Worker worker : Core.game.workers){
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
    public void finishTask(){
        if(!finished){
            finish();
            finished = true;
        }
    }
    public abstract void work();
    protected abstract void finish();
    public abstract void cancel();
    public abstract ItemStack[] getTooltip();
}