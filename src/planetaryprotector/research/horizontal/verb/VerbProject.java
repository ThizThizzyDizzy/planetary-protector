package planetaryprotector.research.horizontal.verb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbProject extends HorizontalVerb{
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
        Core.drawHollowCosGear(x, y, h/4, h/2, h/20, 4, 0, 0, 3);//gear
        Core.drawOval(x, y, w/6, h/2, h/28, 100, 0);//small oval
        Core.drawOval(x, y, w/4, h/2, h/22, 100, 0);//mid oval
        Core.drawOval(x, y, w/2.75, h/2, h/16, 100, 0);//large oval
        Core.drawHorizontalLine(x-w/2, y, x-h/4, h/20, 0);//left line
        Core.drawHorizontalLine(x+h/4, y, x+w/2, h/20, 0);//right line
    }
}