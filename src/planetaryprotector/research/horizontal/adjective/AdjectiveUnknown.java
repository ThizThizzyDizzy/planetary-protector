package planetaryprotector.research.horizontal.adjective;
import planetaryprotector.research.horizontal.HorizontalAdjective;
import simplelibrary.font.FontManager;
import static simplelibrary.opengl.Renderer2D.drawCenteredText;
public class AdjectiveUnknown extends HorizontalAdjective{
    private final String adjective;
    public AdjectiveUnknown(String adjective){
        this.adjective = adjective;
    }
    @Override
    public double getWidth(double size){
        return (FontManager.getLengthForStringWithHeight(adjective, size-20)+40)*2;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    public void render(double x, double y, double w, double h){
        h-=20;
        double length = FontManager.getLengthForStringWithHeight(adjective, h);
        drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, adjective);
    }
}