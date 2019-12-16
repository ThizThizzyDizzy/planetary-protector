package planetaryprotector.building.task;
import planetaryprotector.GameObject;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.menu.MenuGame;
public class TaskAnimation extends GameObject{
    public final int[] images;
    public int frame = 0;
    private final MenuGame game;
    public final TaskAnimated task;
    public TaskAnimation(MenuGame game, TaskAnimated task){
        super(task.building.x,task.building.y-(task.getHeight()-100)-(task.building.type==BuildingType.SKYSCRAPER?(((Skyscraper)task.building).floorCount*Skyscraper.floorHeight):0),task.building.width,task.getHeight());
        images = task.getAnimation();
        this.game = game;
        this.task = task;
    }
    @Override
    public final void render(){
        if(!game.paused){
            if(task.progress()>=1){
                game.animationsToRemove.add(this);
            }
            if(task.cancelled||images.length==0){
                game.animationsToRemove.add(this);
                return;
            }
        }
        renderAnim();
    }
    private void renderAnim(){
        frame = (int)Math.round(task.progress()*(images.length-1));
        if(frame<0){
            frame = 0;
        }
        if(images.length==0)return;
        if(task.progress()==0)drawFrame(images[0]);
        else drawFrame(images[frame]);
    }
    private void drawFrame(int texture){
        drawRect(x, y, x+width, y+height, texture, task.getVariant()/(double)task.getVariants(), 0, (task.getVariant()+1)/(double)task.getVariants(), 1);
    }
}