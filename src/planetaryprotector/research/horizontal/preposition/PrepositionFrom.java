package planetaryprotector.research.horizontal.preposition;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalPreposition;
public class PrepositionFrom extends HorizontalPreposition{
    @Override
    public void draw(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygonSegment(x-w/20, y, 100, w/6-h/30, h-h/30, w/6, h, 90, 100);
        Renderer.fillHollowRegularPolygonSegment(x-w/20, y-h/2.5f, 100, w/6-h/30, h/2-h/30, w/6, h/2, 90, 100);
    }
}
