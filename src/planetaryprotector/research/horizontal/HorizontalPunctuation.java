package planetaryprotector.research.horizontal;
import simplelibrary.opengl.Renderer2D;
public abstract class HorizontalPunctuation extends Renderer2D{
    public abstract double getWidth(double size);
    public abstract double getHeight(double size);
    public final void draw(double x, double y, double size){
        render(x, y, size);
    }
    protected abstract void render(double x, double y, double size);
}