package planetaryprotector.structure.task;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.GameObject;
import planetaryprotector.anim.Animation;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.game.Game;
import planetaryprotector.structure.StructureType;
public class TaskAnimation extends GameObject{
    public final Animation anim;
    public int frame = 0;
    public final TaskAnimated task;
    public TaskAnimation(Game game, TaskAnimated task){
        super(game, task.structure.x, task.structure.y-(task.getHeight()-100)-(task.structure.type==StructureType.SKYSCRAPER?(((Skyscraper)task.structure).floorCount*Skyscraper.floorHeight):0), task.structure.width, task.getHeight());
        anim = task.getAnimation();
        this.task = task;
        task.anim = this;
    }
    @Override
    public final void draw(){
        if(!game.paused){
            if(task.progress()>=1){
                game.animationsToRemove.add(this);
            }
            if(task.cancelled||anim.isEmpty()){
                game.animationsToRemove.add(this);
                return;
            }
        }
        renderAnim();
    }
    private void renderAnim(){
        frame = (int)Math.round(task.progress()*(anim.length()-1));
        if(frame<0){
            frame = 0;
        }
        if(anim.isEmpty())return;
        if(task.progress()==0)drawFrame(anim.getTexture(0));
        else
            drawFrame(anim.getTexture(frame));
    }
    private void drawFrame(int texture){
        Renderer.fillRect(x, y, x+width, y+height, texture, task.getVariant()/(float)task.getVariants(), 0, (task.getVariant()+1)/(float)task.getVariants(), 1);
    }
}
