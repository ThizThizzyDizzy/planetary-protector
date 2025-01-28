package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounMine extends HorizontalNoun{
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 3);//gear
        //right
        Core.drawOval(x+w/3, y, w/6, h/2, h/28, 100, 0, 58, 91);//small oval
        Core.drawOval(x+w/2, y, w/4, h/2, h/22, 100, 0, 62, 87);//mid oval
        Core.drawOval(x+w/1.4375, y, w/2.75, h/2, h/16, 100, 0, 65, 84);//large oval
        //left
        Core.drawOval(x-w/3, y, w/6, h/2, h/28, 100, 0, 8, 41);//small oval
        Core.drawOval(x-w/2, y, w/4, h/2, h/22, 100, 0, 12, 37);//mid oval
        Core.drawOval(x-w/1.4375, y, w/2.75, h/2, h/16, 100, 0, 15, 34);//large oval
    }
}