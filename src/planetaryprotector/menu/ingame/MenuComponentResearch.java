package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.research.Research;
public class MenuComponentResearch extends Component{
    private final MenuResearch menu;
    public final Research research;
    public MenuComponentResearch(MenuResearch menu, Research research){
        setSize(MenuResearch.researchSize, MenuResearch.researchSize);
        this.menu = menu;
        this.research = research;
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        float borderThickness = getWidth()/12;
        float border = borderThickness/.4f;
        int quality = 36;
        Renderer.setColor(.95f, .95f, .95f, 1);
        Renderer.fillRect(x+border, y, x+getWidth()-border, y+getHeight(), 0);
        Renderer.fillRect(x, y+border, x+getWidth(), y+getHeight()-border, 0);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+border, quality, border-border, border-border, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+border, quality, border-border, border-border, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+getHeight()-border, quality, border-border, border-border, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+getHeight()-border, quality, border-border, border-border, border, border, 18, 27);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(x+borderThickness, y+borderThickness, x+getWidth()-borderThickness, y+getHeight()-borderThickness, research.getTexture());
        Renderer.setColor(.75f, .75f, .75f, 1);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+border, quality, border-borderThickness, border-borderThickness, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+border, quality, border-borderThickness, border-borderThickness, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+getHeight()-border, quality, border-borderThickness, border-borderThickness, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+getHeight()-border, quality, border-borderThickness, border-borderThickness, border, border, 18, 27);
        Renderer.fillRect(x+border, y, x+getWidth()-border, y+borderThickness, 0);
        Renderer.fillRect(x+getWidth()-borderThickness, y+border, x+getWidth(), y+getHeight()-border, 0);
        Renderer.fillRect(x+border, y+getHeight()-borderThickness, x+getWidth()-border, y+getHeight(), 0);
        Renderer.fillRect(x, y+border, x+borderThickness, y+getHeight()-border, 0);
    }
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        super.onMouseButton(id, pos, button, action, mods); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if(button==0&&action==GLFW.GLFW_PRESS)menu.selected.research = research;
    }
}
