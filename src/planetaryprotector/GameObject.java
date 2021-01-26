package planetaryprotector;
import java.util.Random;
import planetaryprotector.game.Game;
import simplelibrary.opengl.Renderer2D;
public abstract class GameObject extends Renderer2D{
    public Game game;
    public int x,y,width,height;
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
}