package planetaryprotector.research.horizontal.tense;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalTense;
public class TenseFuture extends HorizontalTense{
    @Override
    protected void render(float x, float y, float size){
        Core.drawOval(x, y+size/3, size/4, size/4, size/22, 80, 0, -10, 10);//Bottom
        Core.drawOval(x, y-size/3, size/4, size/4, size/22, 80, 0, 30, 50);//Top
        Core.drawOval(x-size/3, y, size/4, size/4, size/22, 80, 0, 10, 30);//Left
        Core.drawOval(x+size/3, y, size/4, size/4, size/22, 80, 0, 50, 70);//Right
    }
}