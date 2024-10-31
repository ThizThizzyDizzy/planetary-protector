package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
public class MenuComponentAnimation extends Button{
    public int[] images;
    public int frame = 0;
    public int delay = 3;
    public float timeWaited = 0;
    public boolean loop = false;
    public MenuComponentAnimation(float x, float y, float width, float height, int[] images){
        super("", true);
        this.x = x;
        this.y = y;
        setSize(width, height);
        this.images = images;
    }
    @Override
    public void draw(double deltaTime){
        timeWaited += deltaTime/20;
        if(timeWaited<delay){
            return;
        }
        timeWaited = 0;
        frame++;
        if(frame>=images.length){
            if(loop){
                frame = 0;
            }else{
                frame--;
            }
        }
        Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), images[frame]);
    }
}
