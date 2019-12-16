package planetaryprotector.research.horizontal.noun;
import planetaryprotector.research.horizontal.HorizontalNoun;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class NounShield extends HorizontalNoun{
    @Override
    public double getWidth(double size){
        return size*3;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/50, 4, 0, 0, 3);//gear
        AdvancedRenderer2D.drawOval(x, y, w/6, h/2, h/28, 100, 0);//small oval
        AdvancedRenderer2D.drawOval(x, y, w/4, h/2, h/22, 100, 0);//mid oval
        AdvancedRenderer2D.drawOval(x, y, w/2.75, h/2, h/16, 100, 0);//large oval
    }
}