package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbMore extends HorizontalAdverb{
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
        Core.drawHollowCosGear(x, y-h/20, h/4, h/2, h/50, 5, 0, 36, 3);//gear
    }
}