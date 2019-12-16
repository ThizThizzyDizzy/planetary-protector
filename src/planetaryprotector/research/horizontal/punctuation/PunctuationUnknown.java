package planetaryprotector.research.horizontal.punctuation;
import planetaryprotector.research.horizontal.HorizontalPunctuation;
import simplelibrary.font.FontManager;
public class PunctuationUnknown extends HorizontalPunctuation{
    private final char punctuation;
    public PunctuationUnknown(char punctuation){
        this.punctuation = punctuation;
    }
    @Override
    public double getWidth(double size){
        return (FontManager.getLengthForStringWithHeight(punctuation+"", size-10)+20)*2;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    public void render(double x, double y, double size){
        size-=10;
        double length = FontManager.getLengthForStringWithHeight(punctuation+"", size);
        drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, punctuation+"");
    }
}