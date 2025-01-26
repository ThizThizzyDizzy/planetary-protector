package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounStone extends HorizontalNoun{
    @Override
    public float getWidth(float size){
        return size*3;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2.7f-h/14, h/2.7f-h/14, w/2.7f, h/2.7f);//large
        Renderer.fillHollowRegularPolygon(x, y, 100, w/4-h/18, h/4-h/18, w/4, h/4);//mid
        Core.drawVerticalLine(x, y-h/2, y+h/2, h/14, 0);
//        AdvancedRenderer2D.drawOval(x, y-h/2, w/12, h, h/32, 100, 0, 26, 73);//tiny oval
//        AdvancedRenderer2D.drawOval(x, y-h/2, w/6, h, h/28, 100, 0, 26, 73);//small oval
//        AdvancedRenderer2D.drawOval(x, y-h/2, w/4, h, h/22, 100, 0, 27, 73);//mid oval
//        AdvancedRenderer2D.drawOval(x, y-h/2, w/2.75, h, h/16, 100, 0, 28, 71);//large oval
//        
//        AdvancedRenderer2D.drawOval(x, y+h/2, w/12, h, h/32, 100, 0, 76, 23);//tiny oval
//        AdvancedRenderer2D.drawOval(x, y+h/2, w/6, h, h/28, 100, 0, 76, 23);//small oval
//        AdvancedRenderer2D.drawOval(x, y+h/2, w/4, h, h/24, 100, 0, 77, 23);//mid oval
//        AdvancedRenderer2D.drawOval(x, y+h/2, w/2.75, h, h/21, 100, 0, 78, 21);//large oval
    }
}
