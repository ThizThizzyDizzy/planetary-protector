package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbOperate extends HorizontalVerb{
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/14, 8, 0, 3);//gear
        Core.drawHorizontalLine(x-w*.49, y-h/12, x-h/5, h/24, 0);//top left line
        Core.drawHorizontalLine(x+h/5, y-h/12, x+w*.49, h/24, 0);//top right line
        Core.drawHorizontalLine(x-w*.49, y+h/12, x-h/5, h/24, 0);//bottom left line
        Core.drawHorizontalLine(x+h/5, y+h/12, x+w*.49, h/24, 0);//bottom right line
    }
}
