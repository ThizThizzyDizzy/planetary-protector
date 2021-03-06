package planetaryprotector.research.horizontal.noun;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounStarlight extends HorizontalNoun{
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
        Core.drawOval(x, y+h/2, w/12, h, h/32, 100, 0, 76, 23);//tiny oval
        Core.drawOval(x, y+h/2, w/6, h, h/28, 100, 0, 76, 23);//small oval
        Core.drawOval(x, y+h/2, w/4, h, h/24, 100, 0, 77, 23);//mid oval
        Core.drawOval(x, y+h/2, w/2.75, h, h/21, 100, 0, 78, 21);//large oval
    }
}