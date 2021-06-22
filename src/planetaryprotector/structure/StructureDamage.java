package planetaryprotector.structure;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
public class StructureDamage extends Renderer2D{
    private final Structure structure;
    public double opacity = 1;
    public double x,y;
    public final double size;
    public int tex1;
    public int tex2;
    public StructureDamage(Structure structure, double x, double y){
        this(structure, x, y, 50);
    }
    public StructureDamage(Structure structure, double x, double y, double size){
        this(structure, x, y, size, 1, structure.game.rand.nextInt(10), structure.game.rand.nextInt(10));
    }
    public StructureDamage(Structure structure, double x, double y, double size, double opacity, int tex1, int tex2){
        this.x = x;
        this.y = y;
        this.structure = structure;
        this.size = size;
        this.opacity = opacity;
        this.tex1 = tex1;
        this.tex2 = tex2;
    }
    public void render(){
        GL11.glColor4d(0, 0, 0, opacity);
        drawRectWithBounds(x-structure.x, y-structure.y, x+size-structure.x, y+size-structure.y, 0, -structure.getStructureHeight(), structure.width, structure.height, ImageStash.instance.getTexture("/textures/structures/damage/"+(tex1+1)+".png"));
        drawRectWithBounds(x-structure.x, y-structure.y, x+size-structure.x, y+size-structure.y, 0, -structure.getStructureHeight(), structure.width, structure.height, ImageStash.instance.getTexture("/textures/structures/damage/"+(tex2+1)+".png"));
        if(structure instanceof Skyscraper){
            Skyscraper sky = (Skyscraper) structure;
            drawRectWithBounds(x-sky.x+(sky.right?1:0), y-sky.y+sky.fallen, x+size-sky.x+(sky.right?1:0), y+size-sky.y+sky.fallen, 0, -((sky.floorCount-1)*Skyscraper.floorHeight), sky.width, sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/damage/"+(tex1+1)+".png"));
            drawRectWithBounds(x-sky.x+(sky.right?1:0), y-sky.y+sky.fallen, x+size-sky.x+(sky.right?1:0), y+size-sky.y+sky.fallen, 0, -((sky.floorCount-1)*Skyscraper.floorHeight), sky.width, sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/damage/"+(tex2+1)+".png"));
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public Config save(Config config){
        config.set("x", x);
        config.set("y", y);
        config.set("tex1", tex1);
        config.set("tex2", tex2);
        config.set("size", size);
        config.set("opacity", opacity);
        return config;
    }
    public static StructureDamage load(Structure s, Config config) {
        return new StructureDamage(s, config.getDouble("x"), config.getDouble("y"), config.getDouble("size"), config.getDouble("opacity"), config.getInt("tex1"), config.getInt("tex2"));
    }
}