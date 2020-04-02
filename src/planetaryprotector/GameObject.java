package planetaryprotector;
import planetaryprotector.game.Game;
import simplelibrary.opengl.Renderer2D;
public abstract class GameObject extends Renderer2D{
    public Game game;
    public double x,y,width,height;
    public boolean dead = false;
    public GameObject(Game game, double x, double y, double width, double height){
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public abstract void render();
}