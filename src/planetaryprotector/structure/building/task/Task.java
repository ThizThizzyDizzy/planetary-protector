package planetaryprotector.structure.building.task;
import planetaryprotector.friendly.Worker;
import planetaryprotector.structure.building.Building;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
public abstract class Task{
    public final Game game;
    public final Building building;
    public final TaskType type;
    public int time;
    public int progress = 0;
    public boolean finished = false;
    public boolean important = false;
    public boolean started = false;
    public boolean cancelled = false;
    public Task(Building building, TaskType type, int time){
        this.building = building;
        this.type = type;
        this.time = time;
        this.game = building.game;
    }
    public int getWorkers(){
        int workers = 0;
        for(Worker worker : game.workers){
            if(worker.task==this){
                workers++;
            }
        }
        return workers;
    }
    public int getPendingWorkers(){
        int workers = 0;
        for(Worker worker : game.workers){
            if(worker.targetTask==this||worker.task==this){
                workers++;
            }
        }
        return workers;
    }
    public double progress(){
        if(finished){
            return 1;
        }
        return (progress+0D)/time;
    }
    public abstract boolean canPerform();
    public abstract String[] getDetails();
    protected abstract void begin();
    public void start(){
        if(started)return;
        building.setTask(this);
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
        cancelled = true;
        if(started)onCancel();
        building.setTask(null);
        for(Worker worker : game.workers){
            if(worker.targetTask==this||worker.task==this){
                worker.targetTask = worker.task = null;
            }
        }
    }
    public abstract ItemStack[] getTooltip();
}