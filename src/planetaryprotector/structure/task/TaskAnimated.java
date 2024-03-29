package planetaryprotector.structure.task;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.logging.Logger;
import planetaryprotector.anim.Animation;
import planetaryprotector.structure.Structure;
public abstract class TaskAnimated extends Task{
    /**
     * usually null, except in cases where the building needs to access it (skyscraper add floor tasks)
     */
    public TaskAnimation anim;
    public TaskAnimated(Structure structure, TaskType type, int time){
        super(structure, type, time);
    }
    @Override
    public void start(){
        if(!started)game.startAnim(this);
        super.start();
    }
    public abstract Animation getAnimation();
    public static final Animation getAnimation(String animFolder){
        Animation animation = new Animation();
        int f = 1;
        while(true){
            String path = animFolder+"/"+f+".png";
            if(ResourceManager.getInternalResource(path)==null)break;
            animation.addFrame(path);
            f++;
        }
        if(animation.isEmpty())Logger.error("Animation not found: "+animFolder);//TODO proper handling
        return animation;
    }
    public static void verifyAnimation(String animFolder){
        String path = animFolder+"/1.png";
        if(ResourceManager.getInternalResource(path)==null)Logger.error("Animation not found: "+animFolder);
    }
    public abstract int getHeight();
    public abstract boolean isInBackground();
    public int getVariant(){
        if(this instanceof TaskConstruct)return ((TaskConstruct)this).target.getVariant();
        return structure.getVariant();
    }
    public int getVariants(){
        if(this instanceof TaskConstruct)return ((TaskConstruct)this).target.getVariants();
        return structure.getVariants();
    }
}