package planetaryprotector.research.horizontal.verb;
import planetaryprotector.research.horizontal.HorizontalVerb;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class VerbConsumeElectricity extends HorizontalVerb{
    @Override
    public double getWidth(double size){
        return size*3.5;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/20, 4, 0, 0, 3);//gear
        AdvancedRenderer2D.drawHorizontalLine(x-w/2, y, x-h/4, h/20, 0);//left line
        AdvancedRenderer2D.drawHorizontalLine(x+h/4, y, x+w/2, h/20, 0);//right line
        //right ovals
        //top ovals
        AdvancedRenderer2D.drawOval(x, y-h/2, w/6, h/2, h/28, 100, 0, 27, 43);//small oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/4, h/2, h/22, 100, 0, 29, 45);//mid oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/2.75, h/2, h/16, 100, 0, 31, 45);//large oval
        //bottom ovals
        AdvancedRenderer2D.drawOval(x, y+h/2, w/6, h/2, h/28, 100, 0, 77, 93);//small oval
        AdvancedRenderer2D.drawOval(x, y+h/2, w/4, h/2, h/22, 100, 0, 79, 95);//mid oval
        AdvancedRenderer2D.drawOval(x, y+h/2, w/2.75, h/2, h/16, 100, 0, 81, 95);//large oval
        //left ovals
        //top ovals
        AdvancedRenderer2D.drawOval(x, y-h/2, w/6, h/2, h/28, 100, 0, 57, 73);//small oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/4, h/2, h/22, 100, 0, 55, 71);//mid oval
        AdvancedRenderer2D.drawOval(x, y-h/2, w/2.75, h/2, h/16, 100, 0, 55, 69);//large oval
        //bottom ovals
        AdvancedRenderer2D.drawOval(x, y+h/2, w/6, h/2, h/28, 100, 0, 7, 23);//small oval
        AdvancedRenderer2D.drawOval(x, y+h/2, w/4, h/2, h/22, 100, 0, 5, 21);//mid oval
        AdvancedRenderer2D.drawOval(x, y+h/2, w/2.75, h/2, h/16, 100, 0, 5, 19);//large oval
    }
}