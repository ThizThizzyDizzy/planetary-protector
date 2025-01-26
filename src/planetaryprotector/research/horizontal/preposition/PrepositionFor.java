package planetaryprotector.research.horizontal.preposition;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalPreposition;
public class PrepositionFor extends HorizontalPreposition{
    @Override
    public void draw(float x, float y, float w, float h){
        Core.drawOval(x-w/20, y, w/12, h, h/30, 100, 0, 90, 100);
        Core.drawOval(x-w/20, y, w/6, h, h/30, 100, 0, 90, 100);
    }
}