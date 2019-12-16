package planetaryprotector.research.horizontal.tense;
import planetaryprotector.research.horizontal.HorizontalTense;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class TensePresent extends HorizontalTense{
    @Override
    protected void render(double x, double y, double size){
        AdvancedRenderer2D.drawOval(x, y+size/4, size/4, size/4, size/22, 80, 0, -12, 12);//Bottom
        AdvancedRenderer2D.drawOval(x, y-size/4, size/4, size/4, size/22, 80, 0, 28, 52);//Top
        AdvancedRenderer2D.drawOval(x-size/4, y, size/4, size/4, size/22, 80, 0, 8, 32);//Left
        AdvancedRenderer2D.drawOval(x+size/4, y, size/4, size/4, size/22, 80, 0, 48, 72);//Right
    }
}