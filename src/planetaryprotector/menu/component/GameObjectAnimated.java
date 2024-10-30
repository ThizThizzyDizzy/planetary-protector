package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.GameObject;
import planetaryprotector.game.Game;
    public class GameObjectAnimated extends GameObject{
    public String[] images;
    public int frame = 0;
    public int delay = 3;
    public int timeWaited = 0;
    public boolean loop = false;
    public GameObjectAnimated(Game game, int x, int y, int width, int height, String[] images){
        super(game, x,y,width,height);
        this.images=images;
    }
    @Override
    public void draw(){
        Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(images[frame]));
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