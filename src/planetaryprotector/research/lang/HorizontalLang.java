package planetaryprotector.research.lang;
import planetaryprotector.research.horizontal.HorizontalAdjective;
import planetaryprotector.research.horizontal.HorizontalAdverb;
import planetaryprotector.research.horizontal.HorizontalArticle;
import planetaryprotector.research.horizontal.HorizontalNoun;
import planetaryprotector.research.horizontal.HorizontalPreposition;
import planetaryprotector.research.horizontal.HorizontalPunctuation;
import planetaryprotector.research.horizontal.HorizontalTense;
import planetaryprotector.research.horizontal.HorizontalVerb;
import planetaryprotector.research.horizontal.adjective.AdjectiveSolar;
import planetaryprotector.research.horizontal.adjective.AdjectiveUnknown;
import planetaryprotector.research.horizontal.adverb.AdverbCleaner;
import planetaryprotector.research.horizontal.adverb.AdverbFaster;
import planetaryprotector.research.horizontal.adverb.AdverbLess;
import planetaryprotector.research.horizontal.adverb.AdverbMore;
import planetaryprotector.research.horizontal.adverb.AdverbUnknown;
import planetaryprotector.research.horizontal.article.ArticleA;
import planetaryprotector.research.horizontal.article.ArticleUnknown;
import planetaryprotector.research.horizontal.noun.NounCoal;
import planetaryprotector.research.horizontal.noun.NounGenerator;
import planetaryprotector.research.horizontal.noun.NounMine;
import planetaryprotector.research.horizontal.noun.NounShield;
import planetaryprotector.research.horizontal.noun.NounSpeed;
import planetaryprotector.research.horizontal.noun.NounStarlight;
import planetaryprotector.research.horizontal.noun.NounStone;
import planetaryprotector.research.horizontal.noun.NounUnknown;
import planetaryprotector.research.horizontal.noun.NounWorker;
import planetaryprotector.research.horizontal.preposition.PrepositionFor;
import planetaryprotector.research.horizontal.preposition.PrepositionFrom;
import planetaryprotector.research.horizontal.preposition.PrepositionUnknown;
import planetaryprotector.research.horizontal.punctuation.PunctuationUnknown;
import planetaryprotector.research.horizontal.tense.TenseFuture;
import planetaryprotector.research.horizontal.tense.TensePresent;
import planetaryprotector.research.horizontal.tense.TenseUnknown;
import planetaryprotector.research.horizontal.verb.VerbCarry;
import planetaryprotector.research.horizontal.verb.VerbConsumeElectricity;
import planetaryprotector.research.horizontal.verb.VerbGenerate;
import planetaryprotector.research.horizontal.verb.VerbInfuse;
import planetaryprotector.research.horizontal.verb.VerbOperate;
import planetaryprotector.research.horizontal.verb.VerbProject;
import planetaryprotector.research.horizontal.verb.VerbUnknown;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
public class HorizontalLang extends Lang{
    @Override
    public void drawTranslation(double startX, double startY, double size, String english){
        Phrase p = null;
        try{
            p = getPhrase(english);
        }catch(Exception ex){
            drawCenteredText(0, startY-size/6, DizzyEngine.screenSize.x, startY+size/6, ex.getMessage().split("\n")[0]);
            return;
        }
        x = 0;
        if(p==null)return;
        drawPhrase(startX, startY, size, p);
    }
    double x = 0;
    private void drawPhrase(double x, double y, double size, Phrase p){
        if(p.subject!=null){
            HorizontalNoun subject = getNoun(p.subject);
            subject.draw(x, y, size);
            if(p.verb!=null){
                HorizontalVerb verb = getVerb(p.verb);
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, 0);
                verb.draw(verb.getWidth(size)/2+subject.getWidth(size)/2, 0, size);
                GL11.glScaled(-1, 1, 1);
                verb.draw(verb.getWidth(size)/2+subject.getWidth(size)/2, 0, size);
                GL11.glPopMatrix();
                if(p.object!=null){
                    HorizontalNoun object = getNoun(p.object);
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, 0);
                    object.draw(verb.getWidth(size)+subject.getWidth(size)/2+object.getWidth(size)/2, 0, size);
                    GL11.glScaled(-1, 1, 1);
                    object.draw(verb.getWidth(size)+subject.getWidth(size)/2+object.getWidth(size)/2, 0, size);
                    GL11.glPopMatrix();
                    if(p.punctuation!=' '){
                        HorizontalPunctuation punctuation = getPunctuation(p.punctuation);
                        GL11.glPushMatrix();
                        GL11.glTranslated(x, y, 0);
                        punctuation.draw(verb.getWidth(size)+subject.getWidth(size)/2+object.getWidth(size)+punctuation.getWidth(size)/2, 0, size);
                        GL11.glScaled(-1, 1, 1);
                        punctuation.draw(verb.getWidth(size)+subject.getWidth(size)/2+object.getWidth(size)+punctuation.getWidth(size)/2, 0, size);
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }
    public static HorizontalNoun getNoun(Noun subject){
        if(subject==null)return null;
        HorizontalNoun noun = getNoun(subject.words[0]);
        HorizontalNoun[] otherNouns = new HorizontalNoun[subject.words.length-1];
        for(int i = 1; i<subject.words.length; i++){
            String word = subject.words[i];
            otherNouns[i-1] = getNoun(word);
        }
        noun.setNouns(otherNouns);
        noun.setPlural(subject.plural);
        if(subject.article!=null){
            noun.setArticle(getArticle(subject.article));
        }
        for(String adjective : subject.adjectives){
            noun.addAdjective(getAdjective(adjective));
        }
        return noun;
    }
    public static HorizontalArticle getArticle(String text){
        HorizontalArticle article = null;
        switch(text){
            case "a":
                return new ArticleA();
        }
        if(article==null)article = new ArticleUnknown(text);
        return article;
    }
    public static HorizontalAdjective getAdjective(String text){
        HorizontalAdjective adjective = null;
        switch(text){
            case "solar":
                return new AdjectiveSolar();
        }
        if(adjective==null)adjective = new AdjectiveUnknown(text);
        return adjective;
    }
    public static HorizontalVerb getVerb(Verb action){
        if(action==null)return null;
        HorizontalVerb verb = null;
        switch(action.word){
            case "carry":
            case "carries":
            case "carrying":
            case "carried":
                verb = new VerbCarry();
                break;
            case "operate":
                verb = new VerbOperate();
                break;
            case "infuse":
            case "infused":
                verb = new VerbInfuse();
                break;
            case "generate":
                verb = new VerbGenerate();
                break;
            case "consume":
                verb = new VerbConsumeElectricity();
                break;
            case "project":
                verb = new VerbProject();
                break;
        }
        if(verb==null)verb = new VerbUnknown(action.word);
        if(action.tense!=null){
            verb.setTense(getTense(action.tense));
        }
        if(action.preposition!=null){
            verb.setPreposition(getPreposition(action.preposition));
        }
        for(String adverb : action.adverbs){
            verb.addAdverb(getAdverb(adverb));
        }
        return verb;
    }
    public static HorizontalTense getTense(Tense tense){
        HorizontalTense ten = null;
        switch(tense){
            case PRESENT:
                return new TensePresent();
            case FUTURE:
                return new TenseFuture();
        }
        if(ten==null)ten = new TenseUnknown(tense);
        return ten;
    }
    public static HorizontalPreposition getPreposition(String text){
        HorizontalPreposition preposition = null;
        switch(text){
            case "for":
                return new PrepositionFor();
            case "from":
                return new PrepositionFrom();
        }
        if(preposition==null)preposition = new PrepositionUnknown(text);
        return preposition;
    }
    public static HorizontalAdverb getAdverb(String text){
        HorizontalAdverb adverb = null;
        switch(text){
            case "faster":
                return new AdverbFaster();
            case "cleaner":
                return new AdverbCleaner();
            case "less":
                return new AdverbLess();
            case "more":
                return new AdverbMore();
        }
        if(adverb==null)adverb = new AdverbUnknown(text);
        return adverb;
    }
    public static HorizontalPunctuation getPunctuation(char punctuation){
        HorizontalPunctuation punc = null;
        switch(punctuation){
            case ' ':
                return null;
        }
        if(punc==null)punc = new PunctuationUnknown(punctuation);
        return punc;
    }
    public static HorizontalNoun getNoun(String word){
        if(word==null)return null;
        switch(word){
            case "worker":
            case "workers":
                return new NounWorker();
            case "coal":
                return new NounCoal();
            case "generator":
                return new NounGenerator();
            case "starlight":
                return new NounStarlight();
            case "mine":
                return new NounMine();
            case "speed":
                return new NounSpeed();
            case "shield":
                return new NounShield();
            case "stone":
                return new NounStone();
        }
        return new NounUnknown(word);
    }
}