package planetaryprotector.research.horizontal.tense;
import planetaryprotector.research.horizontal.HorizontalTense;
import planetaryprotector.research.lang.Lang.Tense;
import simplelibrary.font.FontManager;
public class TenseUnknown extends HorizontalTense{
    private final Tense tense;
    public TenseUnknown(Tense tense){
        this.tense = tense;
    }
    @Override
    protected void render(double x, double y, double size){
        size-=20;
        double length = FontManager.getLengthForStringWithHeight(tense.toString(), size);
        drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, tense.toString());
    }
}