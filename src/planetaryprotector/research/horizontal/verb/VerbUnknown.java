package planetaryprotector.research.horizontal.verb;
import planetaryprotector.research.horizontal.HorizontalVerb;
import simplelibrary.font.FontManager;
public class VerbUnknown extends HorizontalVerb{
    private final String verb;
    public VerbUnknown(String verb){
        this.verb = verb;
    }
    @Override
    public double getWidth(double size){
        return (FontManager.getLengthForStringWithHeight(verb, size-10)+20)*2;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    public void render(double x, double y, double w, double h){
        h-=10;
        double length = FontManager.getLengthForStringWithHeight(verb, h);
        drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, verb);
    }
}