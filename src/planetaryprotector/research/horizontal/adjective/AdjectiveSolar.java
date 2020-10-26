package planetaryprotector.research.horizontal.adjective;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdjective;
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
        Core.drawHollowCosGear(x, y, h/4, h/2, h/20, 16, 0, 0, 3);//gear
        Core.drawOval(x, y, w/6, h/2, h/32, 100, 0);//small oval
        Core.drawOval(x, y, w/4, h/2, h/26, 100, 0);//mid oval
        Core.drawOval(x, y, w/2.75, h/2, h/20, 100, 0);//large oval
    }
}