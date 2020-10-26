package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbFaster extends HorizontalAdverb{
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
        Core.drawHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 0, 3);//gear
        //left ovals
        Core.drawOval(x+w/6, y, w/3, h/2, h/28, 100, 0, 55, 94);//small oval
        Core.drawOval(x+w/4, y, w/2, h/2, h/22, 100, 0, 55, 94);//mid oval
        Core.drawOval(x+w/2.75, y, w/1.375, h/2, h/16, 100, 0, 55, 94);//large oval
        //right ovals
        Core.drawOval(x-w/6, y, w/3, h/2, h/28, 100, 0, 5, 44);//small oval
        Core.drawOval(x-w/4, y, w/2, h/2, h/22, 100, 0, 5, 44);//mid oval
        Core.drawOval(x-w/2.75, y, w/1.375, h/2, h/16, 100, 0, 5, 44);//large oval
    }
}