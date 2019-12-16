package planetaryprotector.research.horizontal;
import simplelibrary.opengl.Renderer2D;
public abstract class HorizontalTense extends Renderer2D{
    public final void draw(double x, double y, double size){
        render(x, y, size);
    }
    protected abstract void render(double x, double y, double size);
}