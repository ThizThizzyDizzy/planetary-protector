package planetaryprotector.structure;
import planetaryprotector.game.GameState;
public class StructureDamage{
    private final Structure structure;
    public float opacity = 1;
    public float x,y;
    public final float size;
    public int tex1;
    public int tex2;
    public StructureDamage(Structure structure, float x, float y){
        this(structure, x, y, 50);
    }
    public StructureDamage(Structure structure, float x, float y, float size){
        this(structure, x, y, size, 1, structure.game.rand.nextInt(10), structure.game.rand.nextInt(10));
    }
    public StructureDamage(Structure structure, float x, float y, float size, float opacity, int tex1, int tex2){
        this.x = x;
        this.y = y;
        this.structure = structure;
        this.size = size;
        this.opacity = opacity;
        this.tex1 = tex1;
        this.tex2 = tex2;
    }
    public void render(){
//        Renderer.setColor(0, 0, 0, opacity);
//        Renderer.bound(0, -structure.getStructureHeight(), structure.width, structure.height);
//        Renderer.fillRect(x-structure.x, y-structure.y, x+size-structure.x, y+size-structure.y, ResourceManager.getTexture("/textures/structures/damage/"+(tex1+1)+".png"));
//        Renderer.fillRect(x-structure.x, y-structure.y, x+size-structure.x, y+size-structure.y, ResourceManager.getTexture("/textures/structures/damage/"+(tex2+1)+".png"));
//        Renderer.unBound();
//        Renderer.setColor(1, 1, 1, 1);
    }
    public GameState.Structure.Damage save(){
        GameState.Structure.Damage damage = new GameState.Structure.Damage();
        damage.x = x;
        damage.y = y;
        damage.tex1 = tex1;
        damage.tex2 = tex2;
        damage.size = size;
        damage.opacity = opacity;
        return damage;
    }
    public static StructureDamage load(Structure s, GameState.Structure.Damage damage){
        return new StructureDamage(s, damage.x, damage.y, damage.size, damage.opacity, damage.tex1, damage.tex2);
    }
}