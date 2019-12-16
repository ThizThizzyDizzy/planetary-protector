package planetaryprotector.research.horizontal.noun;
import planetaryprotector.research.horizontal.HorizontalNoun;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class NounSpeed extends HorizontalNoun{
    @Override
    public double getWidth(double size){
        return size*3.25;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 0, 3);//gear
        //left ovals
        AdvancedRenderer2D.drawOval(x+w/6, y, w/3, h/2, h/28, 100, 0, 55, 94);//small oval
        AdvancedRenderer2D.drawOval(x+w/4, y, w/2, h/2, h/22, 100, 0, 55, 94);//mid oval
        AdvancedRenderer2D.drawOval(x+w/2.75, y, w/1.375, h/2, h/16, 100, 0, 55, 94);//large oval
        //right ovals
        AdvancedRenderer2D.drawOval(x-w/6, y, w/3, h/2, h/28, 100, 0, 5, 44);//small oval
        AdvancedRenderer2D.drawOval(x-w/4, y, w/2, h/2, h/22, 100, 0, 5, 44);//mid oval
        AdvancedRenderer2D.drawOval(x-w/2.75, y, w/1.375, h/2, h/16, 100, 0, 5, 44);//large oval
    }
}