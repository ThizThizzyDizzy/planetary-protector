package planetaryprotector.research.horizontal.preposition;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalPreposition;
public class PrepositionFor extends HorizontalPreposition{
    @Override
    public void draw(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygonSegment(x-w/20, y, 100, w/12-h/30, h-h/30, w/12, h, 90, 100);
        Renderer.fillHollowRegularPolygonSegment(x-w/20, y, 100, w/6-h/30, h-h/30, w/6, h, 90, 100);
    }
}
