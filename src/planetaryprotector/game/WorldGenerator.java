package planetaryprotector.game;
import java.util.ArrayList;
import java.util.HashMap;
import planetaryprotector.Core;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.Tree;
import planetaryprotector.structure.Base;
import planetaryprotector.structure.Skyscraper;
public abstract class WorldGenerator{
    public static final HashMap<Integer, ArrayList<WorldGenerator>> generators = new HashMap<>();
    static{
        for(int i = 1; i<=Core.LEVELS; i++){
            generators.put(i, new ArrayList<>());
        }
        new WorldGenerator("chaotic", 1){
            private final int FAILS = 10;
            @Override
            public void generateCity(Game game, BoundingBox bbox){
                game.structures.add(new Base(game, -50, -50));
                long tries = 1000L*bbox.area()/1920/1080;
                int fails = 0;
                while(fails<FAILS){
                    //<editor-fold defaultstate="collapsed" desc="Generate building">
                    FOR:for(int i = 0; i<tries; i++){
                        Skyscraper scraper = new Skyscraper(game, bbox.randX(game.rand), bbox.randY(game.rand), game.rand.nextInt(40)+10);
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
                        scraper.generateApocolypseDecals();
                        game.structures.add(scraper);
                    }
                    //</editor-fold>
                    fails++;
                }
                GEN:for(int i = 0; i<tries; i++){
                    Tree tree = new Tree(game, bbox.randX(game.rand), bbox.randY(game.rand));
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
            public void generate(Game game, BoundingBox bbox){
                long tries = 1000L*bbox.area()/1920/1080;
                GEN:for(int i = 0; i<tries; i++){
                    Tree tree = new Tree(game, bbox.randX(game.rand), bbox.randY(game.rand));
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
            @Override
            public int getGenerationBuffer(){
                return 100;
            }
        };
        new WorldGenerator("grid", 1){
            private static final int GAP = 25;
            @Override
            public void generateCity(Game game, BoundingBox bbox){
                int width = 1;
                while(width*100+GAP*(width+1)<Core.helper.displayWidth()){
                    width+=2;
                }
                width-=2;
                int height = 1;
                while(height*100+GAP*(height+1)<Core.helper.displayHeight()){
                    height+=2;
                }
                height-=2;
                int w = width*100+(GAP*(width-1));
                int h = height*100+(GAP*(height-1));
                int top = -h/2;
                int left = -w/2;
                for(int x = 0; x<width; x++){
                    for(int y = 0; y<height; y++){
                        int X = left+(x*(GAP+100));
                        int Y = top+(y*(GAP+100));
                        Structure structure = (x==width/2&&y==height/2)?new Base(game, X, Y):new Skyscraper(game, X, Y, game.rand.nextInt(15)+25);
                        if(structure instanceof Skyscraper)((Skyscraper)structure).generateApocolypseDecals();
                        game.structures.add(structure);
                    }
                }
                long tries = 1000L*bbox.area()/1920/1080;
                GEN:for(int i = 0; i<tries; i++){
                    Tree tree = new Tree(game, bbox.randX(game.rand), bbox.randY(game.rand));
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
            public void generate(Game game, BoundingBox bbox){
                long tries = 1000L*bbox.area()/1920/1080;
                GEN:for(int i = 0; i<tries; i++){
                    Tree tree = new Tree(game, bbox.randX(game.rand), bbox.randY(game.rand));
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
            @Override
            public int getGenerationBuffer(){
                return 100;
            }
        };
    }
    public static WorldGenerator getWorldGenerator(int level, String name){
        for(WorldGenerator gen : generators.get(level)){
            if(gen.id.equals(name))return gen;
        }
        return generators.get(level).get(0);
    }
    public final String id;
    public WorldGenerator(String id, int level){
        generators.get(level).add(this);
        this.id = id;
    }
    public abstract void generateCity(Game game, BoundingBox bbox);
    public abstract void generate(Game game, BoundingBox bbox);
    public String getName(){
        return id;
    }
    public abstract int getGenerationBuffer();
}