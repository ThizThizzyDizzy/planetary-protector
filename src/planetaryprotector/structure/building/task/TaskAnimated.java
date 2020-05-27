package planetaryprotector.structure.building.task;
import java.util.ArrayList;
import planetaryprotector.structure.building.Building;
import simplelibrary.opengl.ImageStash;
import simplelibrary.texture.TexturePackManager;
public abstract class TaskAnimated extends Task{
    /**
     * usually null, except in cases where the building needs to access it (skyscraper add floor tasks)
     */
    public TaskAnimation anim;
    public TaskAnimated(Building building, TaskType type, int time){
        super(building, type, time);
    }
    @Override
    public void start(){
        super.start();
        game.startAnim(this);
    }
    public abstract int[] getAnimation();
    public static final int[] getAnimation(String animFolder){
        ArrayList<Integer> images = new ArrayList<>();
        int f = 1;
        while(true){
            String path = animFolder+"/"+f+".png";
            if(TexturePackManager.instance.currentTexturePack.getResourceAsStream(path)==null)break;
            images.add(ImageStash.instance.getTexture(path));
            f++;
        }
        if(images.isEmpty()){
            System.err.println("Animation not found: "+animFolder);
        }
        int[] i = new int[images.size()];
        for(int j = 0; j<i.length; j++){
            i[j] = images.get(j);
        }
        return i;
    }
    public static void verifyAnimation(String animFolder) {
        String path = animFolder+"/1.png";
        if(TexturePackManager.instance.currentTexturePack.getResourceAsStream(path)==null)System.err.println("Animation not found: "+animFolder);
    }
    public abstract int getHeight();
    public abstract boolean isInBackground();
    public int getVariant(){
        if(this instanceof TaskConstruct)return ((TaskConstruct)this).target.getVariant();
        return building.getVariant();
    }
    public int getVariants(){
        if(this instanceof TaskConstruct)return ((TaskConstruct)this).target.getVariants();
        return building.getVariants();
    }
}