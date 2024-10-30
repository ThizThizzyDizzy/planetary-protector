package planetaryprotector.menu.component;
import simplelibrary.opengl.gui.components.MenuComponentButton;
    public class MenuComponentAnimation extends MenuComponentButton{
    public int[] images;
    public int frame = 0;
    public int delay = 3;
    public int timeWaited = 0;
    public boolean loop = false;
    public MenuComponentAnimation(double x, double y, double width, double height, int[] images){
        super(x,y,width,height, "", true);
        this.images=images;
    }
    @Override
    public void render(){
        Renderer.fillRect(x, y, x+width, y+height, images[frame]);
    }
    @Override
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