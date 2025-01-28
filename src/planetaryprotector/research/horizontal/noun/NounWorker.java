package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounWorker extends HorizontalNoun{
    @Override
    public float getWidth(float size){
        return size*2.5f;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygon(x, y, 100, w/4-h/24, h/4-h/24, w/4, h/4);//inner oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 200, w/2-h/28, h/2-h/28, w/2, h/2, -15, 15);//top inner piece
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 200, w/2-h/28, h/2-h/28, w/2, h/2, 85, 115);//bottom inner piece
        Renderer.fillHollowRegularPolygonSegment(x+w/8, y, 100, w/4-h/40, h/2-h/40, w/4, h/2, 10, 40);//right outer piece
        Renderer.fillHollowRegularPolygonSegment(x-w/8, y, 100, w/4-h/40, h/2-h/40, w/4, h/2, 60, 90);//left outer piece
        Renderer.drawVerticalLine(x, y-h/4, y+h/4, h/40, 0);//inner vertical line
        Renderer.drawHorizontalLine(x-w/2, y, x-w/4, h/40, 0);//outer left horizontal line
        Renderer.drawHorizontalLine(x+w/4, y, x+w/2, h/40, 0);//outer right horizontal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, -30, -29);//outer top left diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 29, 30);//outer top right diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 129, 130);//outer bottom left diagonal line
        Renderer.fillHollowRegularPolygonSegment(x, y, 200, w/2-w/4, h/2-h/4, w/2, h/2, 70, 71);//outer bottom right diagonal line
    }
}
