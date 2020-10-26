package planetaryprotector.research.horizontal.tense;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalTense;
public class TensePresent extends HorizontalTense{
    @Override
    protected void render(double x, double y, double size){
        Core.drawOval(x, y+size/4, size/4, size/4, size/22, 80, 0, -12, 12);//Bottom
        Core.drawOval(x, y-size/4, size/4, size/4, size/22, 80, 0, 28, 52);//Top
        Core.drawOval(x-size/4, y, size/4, size/4, size/22, 80, 0, 8, 32);//Left
        Core.drawOval(x+size/4, y, size/4, size/4, size/22, 80, 0, 48, 72);//Right
    }
}