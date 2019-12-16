package planetaryprotector.research.lang;
import java.util.ArrayList;
import java.util.HashMap;
import simplelibrary.opengl.Renderer2D;
public abstract class Lang extends Renderer2D{
    private static final ArrayList<Character> punctuations = new ArrayList<Character>();
    static{
        punctuations.add('.');
        punctuations.add('?');
        punctuations.add('!');
    }
    public abstract void drawTranslation(double startX, double startY, double size, String english);
    protected static class Phrase{
        public final Noun subject;
        public final Verb verb;
        public final Noun object;
        public final char punctuation;
        private Phrase(Noun subject, Verb verb, Noun object, char punctuation){
            this.subject = subject;
            this.verb = verb;
            this.object = object;
            this.punctuation = punctuation;
        }
        private Phrase(Noun subject, Verb verb, Noun object){
            this(subject, verb, object, ' ');
        }
        private Phrase(Noun subject, Verb verb){
            this(subject, verb, null);
        }
        private Phrase(Noun subject){
            this(subject, null);
        }
    }
    protected static class Noun{
        public ArrayList<String> adjectives = new ArrayList<>();
        public final String article;
        public final String[] words;
        public final boolean plural;
        private Noun(String article, String... words){
            this.article = article;
            this.words = words;
            plural = words[words.length-1].endsWith("s");
        }
        public Noun addAdjective(String adjective){
            adjectives.add(adjective);
            return this;
        }
    }
    protected static class Verb{
        public ArrayList<String> adverbs = new ArrayList<>();
        public final String word;
        public final Tense tense;
        public final String preposition;
        private Verb(String word, Tense tense, String preposition){
            this.word = word;
            this.tense = tense;
            this.preposition = preposition;
        }
        private Verb(String word, Tense tense){
            this(word, tense, null);
        }
        private Verb(String word){
            this(word, null, null);
        }
        private Verb addAdverb(String adverb){
            adverbs.add(adverb);
            return this;
        }
    }
    public static enum Tense{
        PAST,PRESENT,FUTURE;
    }
    private static final HashMap<String, Type> dictionary = new HashMap<>();
    static{
        dictionary.put("worker", Type.NOUN);
        dictionary.put("workers", Type.NOUN);
        dictionary.put("coal", Type.NOUN);
        dictionary.put("generator", Type.NOUN);
        dictionary.put("will", Type.FUTURE);
        dictionary.put("operate", Type.VERB);
        dictionary.put("faster", Type.ADVERB);
        dictionary.put("cleaner", Type.ADVERB);
        dictionary.put("be", Type.VERB);
        dictionary.put("infused", Type.VERB);
        dictionary.put("infuse", Type.VERB);
        dictionary.put("with", Type.ADVERB);
        dictionary.put("starlight", Type.NOUN);
        dictionary.put("mine", Type.NOUN);
        dictionary.put("generate", Type.VERB);
        dictionary.put("less", Type.ADVERB);
        dictionary.put("stone", Type.NOUN);
        dictionary.put("use", Type.VERB);
        dictionary.put("consume", Type.VERB);//consume power
        dictionary.put("power", Type.NOUN);
        dictionary.put("to", Type.PREPOSITION);
        dictionary.put("for", Type.PREPOSITION);
        dictionary.put("go", Type.VERB);
        dictionary.put("solar", Type.ADJECTIVE);
        dictionary.put("more", Type.ADVERB);
        dictionary.put("from", Type.PREPOSITION);
        dictionary.put("shield", Type.NOUN);
        dictionary.put("project", Type.VERB);
        dictionary.put("a", Type.ARTICLE);
        dictionary.put("speed", Type.NOUN);
        dictionary.put("the", Type.ARTICLE);
        dictionary.put("and", Type.CONJUNCTION);
        dictionary.put("iron", Type.NOUN);
        dictionary.put("ore", Type.NOUN);
        dictionary.put("ores", Type.NOUN);
        dictionary.put("ingot", Type.NOUN);
        dictionary.put("ingots", Type.NOUN);
    }
    protected Phrase getPhrase(String english){
        if(english==null)return null;
        while(english.contains("  "))english = english.replaceAll("  ", " ");
        english = english.toLowerCase().trim();
        if(english.isEmpty())return null;
        String[] words = english.split(" ");
        try{
            ArrayList<String> modifiers = new ArrayList<>();
            String article = null;
            ArrayList<String> noun = new ArrayList<>();
            Noun subject = null;
            if(getType(words[0])==Type.ARTICLE){
                article = words[0];
                words = cut(words);
            }
            while(getType(words[0])==Type.ADJECTIVE||getType(words[0])==Type.CONJUNCTION){
                Type t = getType(words[0]);
                if(t==Type.ADJECTIVE){
                    modifiers.add(words[0]);
                }
                words = cut(words);
            }
            while(getType(words[0])==Type.NOUN){
                noun.add(words[0]);
                words = cut(words);
            }
            if(!noun.isEmpty()){
                subject = new Noun(article, noun.toArray(new String[noun.size()]));
                for(String adjective : modifiers){
                    subject.addAdjective(adjective);
                }
                if(words.length==0)return new Phrase(subject);
            }
            modifiers.clear();
            String action = null;
            String preposition = null;
            boolean future = false;
            if(getType(words[0])==Type.FUTURE){
                future = true;
                words = cut(words);
            }
            while(getType(words[0])==Type.ADVERB||getType(words[0])==Type.CONJUNCTION){
                Type t = getType(words[0]);
                if(t==Type.ADVERB){
                    modifiers.add(words[0]);
                }
                words = cut(words);
            }
            if(getType(words[0])==Type.FUTURE){
                future = true;
                words = cut(words);
            }
            if(getType(words[0])==Type.VERB){
                action = words[0];
                words = cut(words);
            }else{
                throw new UnsupportedOperationException("I don't know where the verb is! "+(words.length>0?words[0]:"EOF")+"<-- \nCannot read phrase: "+english);
            }
            while(words.length>0&&getType(words[0])==Type.ADVERB){
                modifiers.add(words[0]);
                words = cut(words);
            }
            if(words.length>0&&getType(words[0])==Type.PREPOSITION){
                preposition = words[0];
                words = cut(words);
            }
            Verb verb = new Verb(action, future?Tense.FUTURE:getTense(action), preposition);
            for(String adverb : modifiers){
                verb.addAdverb(adverb);
            }
            if(words.length==0)return new Phrase(subject, verb);
            //word's object
            modifiers.clear();
            article = null;
            noun.clear();
            Noun object;
            char punctuation = ' ';
            if(getType(words[0])==Type.ARTICLE){
                article = words[0];
                words = cut(words);
            }
            while(getType(words[0])==Type.ADJECTIVE||getType(words[0])==Type.CONJUNCTION){
                Type t = getType(words[0]);
                if(t==Type.ADJECTIVE){
                    modifiers.add(words[0]);
                }
                words = cut(words);
            }
            while(words.length>0&&getType(words[0])==Type.NOUN){
                for(char p : punctuations){
                    if(words[0].contains(p+"")){
                        punctuation = p;
                        words[0] = words[0].replaceAll("\\Q"+p, "");
                    }
                }
                noun.add(words[0]);
                words = cut(words);
            }
            if(noun.isEmpty())throw new UnsupportedOperationException("I don't know where the verb's noun is! "+(words.length>0?words[0]:"EOF")+"<-- ");
            object = new Noun(article, noun.toArray(new String[noun.size()]));//TODO this only supports one-word nouns right now
            for(String adjective : modifiers){
                object.addAdjective(adjective);
            }
            modifiers.clear();
            if(words.length>0){
                throw new UnsupportedOperationException("I don't know what the rest of this is! "+(words.length>0?words[0]:"EOF")+"<-- \nCannot read phrase: "+english);
            }
            return new Phrase(subject, verb, object, punctuation);
        }catch(ArrayIndexOutOfBoundsException ex){
            throw new UnsupportedOperationException("I ran out of words! "+(words.length>0?words[0]:"EOF")+"<-- \nCannot read prase: "+english);
        }
    }
    private static enum Type{
        ARTICLE,NOUN,VERB,ADJECTIVE,ADVERB,PREPOSITION,
        /**
         * "and"
         * This is ignored.
         */
        CONJUNCTION,
        /**
         * "will"
         * This denotes the verb is future tense.
         */
        FUTURE;
    }
    private String[] cut(String[] words){
        String[] newWords = new String[words.length-1];
        for(int i = 1; i<words.length; i++){
            newWords[i-1] = words[i];
        }
        return newWords;
    }
    private Tense getTense(String verb){
        verb = verb.replaceAll("\\Q.", "").replaceAll("\\Q?", "").replaceAll("!", "");
        if(verb.endsWith("ed"))return Tense.PAST;
        if(verb.endsWith("ing")||verb.endsWith("s"))return Tense.PRESENT;
        if(verb.endsWith("y")||verb.endsWith("p"))return Tense.PRESENT;
        System.err.println("I don't understand what tense this verb is!\nUnknown tense for verb: "+verb);
        return null;
    }
    private Type getType(String word){
        word = word.replaceAll("\\Q.", "").replaceAll("\\Q?", "").replaceAll("!", "");
        return dictionary.get(word);
    }
}