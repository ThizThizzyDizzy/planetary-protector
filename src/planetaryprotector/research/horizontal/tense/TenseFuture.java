package planetaryprotector.research.horizontal.tense;
import planetaryprotector.research.horizontal.HorizontalTense;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class TenseFuture extends HorizontalTense{
    @Override
    protected void render(double x, double y, double size){
        AdvancedRenderer2D.drawOval(x, y+size/3, size/4, size/4, size/22, 80, 0, -10, 10);//Bottom
        AdvancedRenderer2D.drawOval(x, y-size/3, size/4, size/4, size/22, 80, 0, 30, 50);//Top
        AdvancedRenderer2D.drawOval(x-size/3, y, size/4, size/4, size/22, 80, 0, 10, 30);//Left
        AdvancedRenderer2D.drawOval(x+size/3, y, size/4, size/4, size/22, 80, 0, 50, 70);//Right
    }
}