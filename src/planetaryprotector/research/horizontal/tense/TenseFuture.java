package planetaryprotector.research.horizontal.tense;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalTense;
public class TenseFuture extends HorizontalTense{
    @Override
    protected void render(float x, float y, float size){
        Renderer.fillHollowRegularPolygonSegment(x, y+size/3, 80, size/4-size/22, size/4-size/22, size/4, size/4, -10, 10);//Bottom
        Renderer.fillHollowRegularPolygonSegment(x, y-size/3, 80, size/4-size/22, size/4-size/22, size/4, size/4, 30, 50);//Top
        Renderer.fillHollowRegularPolygonSegment(x-size/3, y, 80, size/4-size/22, size/4-size/22, size/4, size/4, 10, 30);//Left
        Renderer.fillHollowRegularPolygonSegment(x+size/3, y, 80, size/4-size/22, size/4-size/22, size/4, size/4, 50, 70);//Right
    }
}