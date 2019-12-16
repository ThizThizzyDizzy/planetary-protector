package planetaryprotector.research.horizontal.article;
import planetaryprotector.research.horizontal.HorizontalArticle;
import simplelibrary.font.FontManager;
public class ArticleUnknown extends HorizontalArticle{
    private final String article;
    public ArticleUnknown(String article){
        this.article = article;
    }
    @Override
    protected void render(double x, double y, double size){
        size-=20;
        double length = FontManager.getLengthForStringWithHeight(article, size);
        drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, article);
    }
}