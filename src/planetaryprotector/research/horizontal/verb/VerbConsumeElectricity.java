package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbConsumeElectricity extends HorizontalVerb{
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/20, 4, 0, 3);//gear
        Renderer.drawHorizontalLine(x-w/2, y, x-h/4, h/20, 0);//left line
        Renderer.drawHorizontalLine(x+h/4, y, x+w/2, h/20, 0);//right line
        //right ovals
        //top ovals
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/6-h/28, h/2-h/28, w/6, h/2, 27, 43);//small oval
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/4-h/22, h/2-h/22, w/4, h/2, 29, 45);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 31, 45);//large oval
        //bottom ovals
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/6-h/28, h/2-h/28, w/6, h/2, 77, 93);//small oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/4-h/22, h/2-h/22, w/4, h/2, 79, 95);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 81, 95);//large oval
        //left ovals
        //top ovals
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/6-h/28, h/2-h/28, w/6, h/2, 57, 73);//small oval
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/4-h/22, h/2-h/22, w/4, h/2, 55, 71);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x, y-h/2, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 55, 69);//large oval
        //bottom ovals
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/6-h/28, h/2-h/28, w/6, h/2, 7, 23);//small oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/4-h/22, h/2-h/22, w/4, h/2, 5, 21);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 5, 19);//large oval
    }
}
