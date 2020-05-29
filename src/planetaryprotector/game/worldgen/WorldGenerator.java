package planetaryprotector.game.worldgen;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.Tree;
import planetaryprotector.structure.building.Base;
import planetaryprotector.structure.building.Skyscraper;
import simplelibrary.opengl.Renderer2D;
public abstract class WorldGenerator{
    public static final HashMap<Integer, ArrayList<WorldGenerator>> generators = new HashMap<>();
    static{
        for(int i = 1; i<=Core.LEVELS; i++){
            generators.put(i, new ArrayList<>());
        }
        new WorldGenerator("chaotic", 1){
            private static final int TRIES = 1000;
            private static final int FAILS = 10;
            @Override
            public void generateCity(Game game){
                game.structures.add(new Base(game, Display.getWidth()/2-50, Display.getHeight()/2-50));
                int fails = 0;
                while(fails<FAILS){
                    //<editor-fold defaultstate="collapsed" desc="Generate building">
                    FOR:for(int i = 0; i<TRIES; i++){
                        Skyscraper scraper = new Skyscraper(game, game.rand.nextInt(Display.getWidth()-100), game.rand.nextInt(Display.getHeight()-100), game.rand.nextInt(40)+10);
                        for(Structure structure : game.structures){
                            double Y = structure.y;
                            if(structure instanceof Skyscraper){
                                Y-=((Skyscraper) structure).fallen;
                            }
                            if(Renderer2D.isClickWithinBounds(scraper.x, scraper.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                    Renderer2D.isClickWithinBounds(scraper.x+scraper.width, scraper.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                    Renderer2D.isClickWithinBounds(scraper.x, scraper.y+scraper.height, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                    Renderer2D.isClickWithinBounds(scraper.x+scraper.width, scraper.y+scraper.height, structure.x, Y, structure.x+structure.width, Y+structure.height)){
                                continue FOR;
                            }
                        }
                        game.structures.add(scraper);
                    }
                    //</editor-fold>
                    fails++;
                }
                GEN:for(int i = 0; i<TRIES; i++){
                    Tree tree = new Tree(game, game.rand.nextInt(Display.getWidth()-10), game.rand.nextInt(Display.getHeight()-4));
                    for(Structure structure : game.structures){
                        double Y = structure.y;
                        if(structure instanceof Skyscraper){
                            Y-=((Skyscraper) structure).fallen;
                        }
                        if(Renderer2D.isClickWithinBounds(tree.x, tree.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x+tree.width, tree.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x, tree.y+tree.height, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x+tree.width, tree.y+tree.height, structure.x, Y, structure.x+structure.width, Y+structure.height)){
                            continue GEN;
                        }
                    }
                    game.structures.add(tree);
                }
            }
            @Override
            public String getName(){
                return "Chaotic";
            }
        };
        new WorldGenerator("grid", 1){
            private static final int GAP = 25;
            @Override
            public void generateCity(Game game){
                int width = 1;
                while(width*100+GAP*(width+1)<Display.getWidth()){
                    width+=2;
                }
                width-=2;
                int height = 1;
                while(height*100+GAP*(height+1)<Display.getHeight()){
                    height+=2;
                }
                height-=2;
                int w = width*100+(GAP*(width-1));
                int h = height*100+(GAP*(height-1));
                int top = Display.getHeight()/2-h/2;
                int left = Display.getWidth()/2-w/2;
                for(int x = 0; x<width; x++){
                    for(int y = 0; y<height; y++){
                        int X = left+(x*(GAP+100));
                        int Y = top+(y*(GAP+100));
                        Structure structure = (x==width/2&&y==height/2)?new Base(game, X, Y):new Skyscraper(game, X, Y, game.rand.nextInt(15)+25);
                        game.structures.add(structure);
                    }
                }
                GEN:for(int i = 0; i<1000; i++){
                    Tree tree = new Tree(game, game.rand.nextInt(Display.getWidth()-10), game.rand.nextInt(Display.getHeight()-4));
                    for(Structure structure : game.structures){
                        double Y = structure.y;
                        if(structure instanceof Skyscraper){
                            Y-=((Skyscraper) structure).fallen;
                        }
                        if(Renderer2D.isClickWithinBounds(tree.x, tree.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x+tree.width, tree.y, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x, tree.y+tree.height, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                                Renderer2D.isClickWithinBounds(tree.x+tree.width, tree.y+tree.height, structure.x, Y, structure.x+structure.width, Y+structure.height)){
                            continue GEN;
                        }
                    }
                    game.structures.add(tree);
                }
            }
            @Override
            public String getName(){
                return "Grid";
            }
        };
    }
    public static WorldGenerator getWorldGenerator(int level, String name){
        for(WorldGenerator gen : generators.get(level)){
            if(gen.id.equals(name))return gen;
        }
        return generators.get(level).get(0);
    }
    private final String id;
    public WorldGenerator(String id, int level){
        generators.get(level).add(this);
        this.id = id;
    }
    public abstract void generateCity(Game game);
    public String getName(){
        return id;
    }
}