package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.research.horizontal.HorizontalAdverb;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class AdverbCleaner extends HorizontalAdverb{
    @Override
    public double getWidth(double size){
        return size*2.5;
    }
    @Override
    public double getHeight(double size){
        return size*.8;
    }
    @Override
    public void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, h/13, 100, 0);//outer oval
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/50, 4, 0, 0, 3);//gear
        AdvancedRenderer2D.drawOval(x, y, w/6, h/2, h/28, 100, 0);//small oval
        AdvancedRenderer2D.drawOval(x, y, w/4, h/2, h/22, 100, 0);//mid oval
        AdvancedRenderer2D.drawOval(x, y, w/2.75, h/2, h/16, 100, 0);//large oval
    }
}