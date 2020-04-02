package planetaryprotector.game;
import java.awt.event.ActionListener;
import planetaryprotector.building.task.Task;
import planetaryprotector.item.ItemStack;
public class Action{
    public int divider = 0;
    public final String label;
    public final ActionListener listener;
    public final ActionUpdate update;
    public final ItemStack[] tooltip;
    private boolean important;
    public Action(int divider){
        this(null, null, null);
        this.divider = divider;
    }
    public Action(String label, Task task){
        this(label, (e) -> {
            if(!task.canPerform())return;
            task.start();
        }, () -> {
            return task.canPerform();
        }, task.getTooltip());
    }
    public Action(String label, ActionListener listener, ActionUpdate update, ItemStack... tooltip){
        this.label = label;
        this.listener = listener;
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