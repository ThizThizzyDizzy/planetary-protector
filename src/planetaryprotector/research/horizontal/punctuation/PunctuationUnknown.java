package planetaryprotector.research.horizontal.punctuation;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalPunctuation;
public class PunctuationUnknown extends HorizontalPunctuation{
    private final char punctuation;
    public PunctuationUnknown(char punctuation){
        this.punctuation = punctuation;
    }
    @Override
    public float getWidth(float size){
        return (Renderer.getStringWidth(punctuation+"", size-10)+20)*2;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    public void render(float x, float y, float size){
        size -= 10;
        float length = Renderer.getStringWidth(punctuation+"", size);
        Renderer.drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, punctuation+"");
    }
}
