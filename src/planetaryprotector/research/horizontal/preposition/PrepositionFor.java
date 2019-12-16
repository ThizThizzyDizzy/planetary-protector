package planetaryprotector.research.horizontal.preposition;
import planetaryprotector.research.horizontal.HorizontalPreposition;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class PrepositionFor extends HorizontalPreposition{
    @Override
    public void draw(double x, double y, double w, double h){
        AdvancedRenderer2D.drawOval(x-w/20, y, w/12, h, h/30, 100, 0, 90, 100);
        AdvancedRenderer2D.drawOval(x-w/20, y, w/6, h, h/30, 100, 0, 90, 100);
    }
}