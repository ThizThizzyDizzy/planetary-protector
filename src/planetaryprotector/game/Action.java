package planetaryprotector.game;
import planetaryprotector.structure.task.Task;
import planetaryprotector.item.ItemStack;
public class Action{
    public int divider = 0;
    public final String label;
    public final Runnable runFunc;
    public final ActionUpdate update;
    public final ItemStack[] tooltip;
    private boolean important;
    public Action(int divider){
        this(null, null, null);
        this.divider = divider;
    }
    public Action(String label, Task task){
        this(label, () -> {
            if(!task.canPerform())return;
            task.start();
        }, () -> {
            return task.canPerform();
        }, task.getTooltip());
    }
    public Action(String label, Runnable performFunc, ActionUpdate update, ItemStack... tooltip){
        this.label = label;
        this.runFunc = performFunc;
        this.update = update;
        this.tooltip = tooltip;
    }
    public boolean isDivider(){
        return label==null;
    }
    public Action setImportant(){
        divider = 15;
        important = true;
        return this;
    }
    public boolean isImportant(){
        return important;
    }
}