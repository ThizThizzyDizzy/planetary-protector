package planetaryprotector.research.horizontal.noun;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounWorker extends HorizontalNoun{
    @Override
    public double getWidth(double size){
        return size*2.5;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        Core.drawOval(x, y, w/4, h/4, h/24, 100, 0);//inner oval
        Core.drawOval(x, y+h/2, w/2, h/2, h/28, 200, 0, -15, 15);//top inner piece
        Core.drawOval(x, y-h/2, w/2, h/2, h/28, 200, 0, 85, 115);//bottom inner piece
        Core.drawOval(x+w/8, y, w/4, h/2, h/40, 100, 0, 10, 40);//right outer piece
        Core.drawOval(x-w/8, y, w/4, h/2, h/40, 100, 0, 60, 90);//left outer piece
        Core.drawVerticalLine(x, y-h/4, y+h/4, h/40, 0);//inner vertical line
        Core.drawHorizontalLine(x-w/2, y, x-w/4, h/40, 0);//outer left horizontal line
        Core.drawHorizontalLine(x+w/4, y, x+w/2, h/40, 0);//outer right horizontal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, -30,-29);//outer top left diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 29,30);//outer top right diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 129,130);//outer bottom left diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 70,71);//outer bottom right diagonal line
    }
}