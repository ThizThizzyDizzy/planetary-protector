package planetaryprotector.research.horizontal.noun;
import planetaryprotector.research.lang.HorizontalLang;
import planetaryprotector.research.horizontal.HorizontalNoun;
import simplelibrary.font.FontManager;
public class NounUnknown extends HorizontalNoun{
    private final String word;
    public NounUnknown(String... words){
        if(words.length==0){
            word = "";
        }else{
            word = words[0];
            if(words.length>1){
                HorizontalNoun[] newWords = new HorizontalNoun[words.length-1];
                for(int i = 0; i<newWords.length; i++){
                    newWords[i] = HorizontalLang.getNoun(words[i+1]);
                }
                setNouns(newWords);
            }
        }
    }
    @Override
    public double getWidth(double size){
        return (FontManager.getLengthForStringWithHeight(word, size-10)+20)*2;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    public void render(double x, double y, double w, double h){
        h-=10;
        double length = FontManager.getLengthForStringWithHeight(word, h);
        drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, word);
    }
}