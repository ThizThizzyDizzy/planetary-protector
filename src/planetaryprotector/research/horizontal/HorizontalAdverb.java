package planetaryprotector.research.horizontal;
import simplelibrary.opengl.Renderer2D;
public abstract class HorizontalAdverb extends Renderer2D{
    public final void draw(double x, double y, double size){
        double w = getWidth(size);
        double h = getHeight(size);
        render(x, y, w, h);
    }
    public abstract double getWidth(double size);
    public abstract double getHeight(double size);
    protected abstract void render(double x, double y, double w, double h);
}