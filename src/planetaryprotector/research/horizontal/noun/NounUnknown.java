package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.lang.HorizontalLang;
import planetaryprotector.research.horizontal.HorizontalNoun;
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
    public float getWidth(float size){
        return (Renderer.getStringWidth(word, size-10)+20)*2;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    public void render(float x, float y, float w, float h){
        h -= 10;
        float length = Renderer.getStringWidth(word, h);
        Renderer.drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, word);
    }
}
