package planetaryprotector.research.horizontal.noun;
import planetaryprotector.research.horizontal.HorizontalNoun;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class NounCoal extends HorizontalNoun{
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
        AdvancedRenderer2D.drawOval(x, y-h/2, w/12, h, h/32, 100, 0, 26, 73);//tiny oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/6, h, h/28, 100, 0, 26, 73);//small oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/4, h, h/22, 100, 0, 27, 73);//mid oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/2.75, h, h/16, 100, 0, 28, 71);//large oval
    }
}