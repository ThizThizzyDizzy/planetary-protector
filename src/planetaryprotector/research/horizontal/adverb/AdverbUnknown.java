package planetaryprotector.research.horizontal.adverb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
import simplelibrary.font.FontManager;
import static simplelibrary.opengl.Renderer2D.drawCenteredText;
public class AdverbUnknown extends HorizontalAdverb{
    private final String adverb;
    public AdverbUnknown(String adverb){
        this.adverb = adverb;
    }
    @Override
    public double getWidth(double size){
        return (FontManager.getLengthForStringWithHeight(adverb, size-20)+40)*2;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    public void render(double x, double y, double w, double h){
        Core.drawOval(x, y, w/2, h/2, h/13, 100, 0);//outer oval
        h-=20;
        double length = FontManager.getLengthForStringWithHeight(adverb, h);
        drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, adverb);
    }
}