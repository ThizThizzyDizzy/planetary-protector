package planetaryprotector;
import java.util.Random;
import org.joml.Vector2f;
import org.joml.Vector2i;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.Game;
public abstract class GameObject{
    public Game game;
    public int x, y, width, height;
    public boolean dead = false;
    public GameObject(Game game, int x, int y, int width, int height){
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public abstract void draw();
    public int getRandX(Random rand){
        return (int)Math.round(rand.nextDouble()*width);
    }
    public int getRandY(Random rand){
        return (int)Math.round(rand.nextDouble()*height);
    }
    public BoundingBox getBoundingBox(boolean includeHeight){
        return new BoundingBox(x, y, width, height);
    }
    public Vector2i getPosition(){
        return new Vector2i(x, y);
    }
    public Vector2f getCenter(){
        return new Vector2f(x+width/2f, y+height/2f);
    }
}
