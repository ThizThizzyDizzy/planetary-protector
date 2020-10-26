package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
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
        Core.drawOval(x, y, w/2, h/2, h/13, 100, 0);//outer oval
        Core.drawHollowCosGear(x, y, h/4, h/2, h/50, 4, 0, 0, 3);//gear
        Core.drawOval(x, y, w/6, h/2, h/28, 100, 0);//small oval
        Core.drawOval(x, y, w/4, h/2, h/22, 100, 0);//mid oval
        Core.drawOval(x, y, w/2.75, h/2, h/16, 100, 0);//large oval
    }
}