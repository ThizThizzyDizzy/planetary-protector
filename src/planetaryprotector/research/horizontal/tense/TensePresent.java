package planetaryprotector.research.horizontal.tense;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalTense;
public class TensePresent extends HorizontalTense{
    @Override
    protected void render(float x, float y, float size){
        Renderer.fillHollowRegularPolygonSegment(x, y+size/4, 80, size/4-size/22, size/4-size/22, size/4, size/4, -12, 12);//Bottom
        Renderer.fillHollowRegularPolygonSegment(x, y-size/4, 80, size/4-size/22, size/4-size/22, size/4, size/4, 28, 52);//Top
        Renderer.fillHollowRegularPolygonSegment(x-size/4, y, 80, size/4-size/22, size/4-size/22, size/4, size/4, 8, 32);//Left
        Renderer.fillHollowRegularPolygonSegment(x+size/4, y, 80, size/4-size/22, size/4-size/22, size/4, size/4, 48, 72);//Right
    }
}
