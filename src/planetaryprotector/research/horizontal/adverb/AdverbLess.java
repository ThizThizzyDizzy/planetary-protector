package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbLess extends HorizontalAdverb{
    @Override
    public double getWidth(double size){
        return size*2.5;
    }
    @Override
    public double getHeight(double size){
        return size*.75;
    }
    @Override
    public void render(double x, double y, double w, double h){
        Core.drawHollowCosGear(x, y-h/12, h/4, h/2, h/50, 3, 0, 60, 3);//gear
    }
}