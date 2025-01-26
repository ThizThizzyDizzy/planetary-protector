package planetaryprotector.research.horizontal.adjective;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalAdjective;
public class AdjectiveUnknown extends HorizontalAdjective{
    private final String adjective;
    public AdjectiveUnknown(String adjective){
        this.adjective = adjective;
    }
    @Override
    public float getWidth(float size){
        return (Renderer.getStringWidth(adjective, size-20)+40)*2;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    public void render(float x, float y, float w, float h){
        h -= 20;
        float length = Renderer.getStringWidth(adjective, h);
        Renderer.drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, adjective);
    }
}
