package planetaryprotector;
import java.util.Random;
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
}
