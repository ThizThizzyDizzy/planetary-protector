package planetaryprotector;
import simplelibrary.opengl.Renderer2D;
public abstract class GameObject extends Renderer2D{
    public double x,y,width,height;
    public boolean dead = false;
    public GameObject(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public abstract void render();
}