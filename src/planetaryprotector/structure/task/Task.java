package planetaryprotector.structure.task;
import planetaryprotector.friendly.Worker;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Structure;
public abstract class Task{
    public final Game game;
    public final Structure structure;
    public final TaskType type;
    public int time;
    public int progress = 0;
    public boolean finished = false;
    public boolean important = false;
    public boolean started = false;
    public boolean cancelled = false;
    public Task(Structure structure, TaskType type, int time){
        this.structure = structure;
        this.type = type;
        this.time = time;
        this.game = structure.game;
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
    public float progress(){
        if(finished){
            return 1;
        }
        return (float)progress/time;
    }
    public abstract boolean canPerform();
    public abstract String[] getDetails();
    protected abstract void begin();
    public void start(){
        if(started)return;
        structure.setTask(this);
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
        structure.setTask(null);
        for(Worker worker : game.workers){
            if(worker.targetTask==this||worker.task==this){
                worker.targetTask = worker.task = null;
            }
        }
    }
    public abstract ItemStack[] getTooltip();
}
