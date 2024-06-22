package planetaryprotector.item;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Matrix4f;
import planetaryprotector.game.Game;
import planetaryprotector.GameObject;
public class DroppedItem extends GameObject{
    public final Item item;
    private int flashDelay = 20;
    private int flashTimer = 20;
    public int life = -1;//1200*5;
    private float opacity = 1;
    private float rot;
    public boolean dead = false;
    public DroppedItem(Game game, int x, int y, Item item){
        super(game, x, y, 10, 10);
        this.item = item;
        rot = (float)(game.rand.nextDouble()*360);
    }
    @Override
    public void draw(){
        Renderer.setColor(1, 1, 1, opacity);
        if(item==Item.star){
            rot += .75;
            Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rot, 0, 0, 1));
            Renderer.fillRect(-width, -height, width, height, item.getWorldTexture());
            Renderer.popModel();
        }else{
            Renderer.fillRect(x, y, x+width, y+height, item.getWorldTexture());
        }
        Renderer.setColor(1, 1, 1, 1);
    }
    public void tick(){
        if(life==-1){
            return;
        }
        life--;
        if(life<=0){
            dead = true;
        }
        if(life<600){
            flashTimer--;
            flashDelay = (life/600)*20;
            if(flashTimer<=0){
                flashTimer += flashDelay;
                if(opacity==1){
                    opacity = 0.5f;
                }else{
                    opacity = 1;
                }
            }
        }
    }
    public void damage(double x, double y){
        dead = true;
    }
}
