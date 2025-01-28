package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbInfuse extends HorizontalVerb{
    @Override
    public float getWidth(float size){
        return size*3.5f;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/20, 12, 0, 3);//gear
        Renderer.drawHorizontalLine(x-w/2, y, x-w/4, h/40, 0);//outer left horizontal line
        Renderer.drawHorizontalLine(x+w/4, y, x+w/2, h/40, 0);//outer right horizontal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, -30, -29);//outer top left diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 29, 30);//outer top right diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 129, 130);//outer bottom left diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 70, 71);//outer bottom right diagonal line
    }
}
