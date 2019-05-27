package planetaryprotector.building.task;
import planetaryprotector.Core;
import planetaryprotector.building.Building;
public abstract class TaskAnimated extends Task{
    public TaskAnimated(Building building, TaskType type, int time){
        super(building, type, time);
    }
    @Override
    public void start(){
        super.start();
        Core.game.startAnim(this);
    }
    @Override
    public void onCancel(){
        Core.game.cancelAnim(this);
    }
}