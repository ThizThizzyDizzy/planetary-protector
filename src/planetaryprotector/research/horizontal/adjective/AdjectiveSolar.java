package planetaryprotector.research.horizontal.adjective;
import planetaryprotector.research.horizontal.HorizontalAdjective;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class AdjectiveSolar extends HorizontalAdjective{
    @Override
    public double getWidth(double size){
        return size*2.5;
    }
    @Override
    public double getHeight(double size){
        return size*.625;
    }
    @Override
    public void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/20, 16, 0, 0, 3);//gear
        AdvancedRenderer2D.drawOval(x, y, w/6, h/2, h/32, 100, 0);//small oval
        AdvancedRenderer2D.drawOval(x, y, w/4, h/2, h/26, 100, 0);//mid oval
        AdvancedRenderer2D.drawOval(x, y, w/2.75, h/2, h/20, 100, 0);//large oval
    }
}