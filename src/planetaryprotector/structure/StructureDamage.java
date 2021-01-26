package planetaryprotector.structure;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.Renderer2D;
public class StructureDamage extends Renderer2D{
    private final Structure structure;
    public double opacity = 1;
    public double x,y;
    public double size = 50;
    public StructureDamage(Structure structure, double x, double y){
        this.x = x;
        this.y = y;
        this.structure = structure;
    }
    public void render(){
        GL11.glColor4d(1, 1, 1, opacity);
        structure.drawDamage(this);
        GL11.glColor4d(1, 1, 1, 1);
    }
}