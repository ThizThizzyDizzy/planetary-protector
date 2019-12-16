package planetaryprotector.research.horizontal.preposition;
import planetaryprotector.research.horizontal.HorizontalPreposition;
import simplelibrary.font.FontManager;
public class PrepositionUnknown extends HorizontalPreposition{
    private final String preposition;
    public PrepositionUnknown(String preposition){
        this.preposition = preposition;
    }
    @Override
    public void draw(double x, double y, double w, double h){
        h-=20;
        double length = FontManager.getLengthForStringWithHeight(preposition, h);
        drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, preposition);
    }
}