package planetaryprotector.menu.component;
import planetaryprotector.GameObject;
    public class GameObjectAnimated extends GameObject{
    public int[] images;
    public int frame = 0;
    public int delay = 3;
    public int timeWaited = 0;
    public boolean loop = false;
    public GameObjectAnimated(double x, double y, double width, double height, int[] images){
        super(x,y,width,height);
        this.images=images;
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, images[frame]);
    }
    public void tick(){
        timeWaited++;
        if(timeWaited<delay){
            return;
        }
        timeWaited=0;
        frame++;
        if(frame>=images.length){
            if(loop){
                frame = 0;
            }else{
                frame--;
            }
        }
    }
}