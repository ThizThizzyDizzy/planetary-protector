package planetaryprotector.building.task;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponent;
    public abstract class MenuComponentTaskAnimation extends MenuComponent{
    public final int[] images;
    public int frame = 0;
    public double progress = 0;
    private final MenuGame game;
    public boolean paused;
    public final Task task;
    public MenuComponentTaskAnimation(double x, double y, double width, double height, int[] images, MenuGame game, Task task){
        super(x,y,width,height);
        this.images=images;
        this.game = game;
        this.task = task;
    }
    @Override
    public void render(){
        if(!paused&&!game.paused){
            if(getProgress()>=1){
                game.componentsToRemove.add(this);
            }
        }
        renderAnim();
    }
    public void renderAnim(){
        if(!paused){
            progress = getProgress();
            frame = (int)Math.round(progress*(images.length-1));
            if(frame<0){
                frame = 0;
            }
        }
        if(progress==0)drawRect(x,y,x+width,y+height,task.building.type.getTexture());
        else drawRect(x, y, x+width, y+height, images[frame]);
    }
    public abstract double getProgress();
}