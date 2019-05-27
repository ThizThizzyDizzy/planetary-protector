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
    public boolean started = false;
    public Task(Building building, TaskType type, int time){
        this.building = building;
        this.type = type;
        this.time = time;
    }
    public int getWorkers(){
        int workers = 0;
        synchronized(Core.game.workers){
            for(Worker worker : Core.game.workers){
                if(worker.task==this){
                    workers++;
                }
            }
        }
        return workers;
    }
    public int getPendingWorkers(){
        int workers = 0;
        synchronized(Core.game.workers){
            for(Worker worker : Core.game.workers){
                if(worker.targetTask==this||worker.task==this){
                    workers++;
                }
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
    protected abstract void begin();
    public void start(){
        if(started)return;
        building.task = this;
        begin();
        started = true;
    }
    public void finishTask(){
        if(!finished){
            finish();
            finished = true;
        }
    }
    public abstract void work();
    protected abstract void finish();
    protected abstract void onCancel();
    public void cancel(){
        if(started)onCancel();
        building.task = null;
        synchronized(Core.game.workers){
            for(Worker worker : Core.game.workers){
                if(worker.targetTask==this||worker.task==this){
                    worker.targetTask = worker.task = null;
                }
            }
        }
    }
    public abstract ItemStack[] getTooltip();
}