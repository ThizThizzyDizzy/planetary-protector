package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import com.thizthizzydizzy.dizzyengine.ui.component.Scrollable;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.BoundsConstraint;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.Controls;
import planetaryprotector.structure.Laboratory;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.research.Research;
public class MenuResearch extends MenuComponentOverlayStructure{
    private final Laboratory laboratory;
    private final Scrollable undiscovered;
    private final Scrollable available;
    private final Scrollable finished;
    private static final int spacing = 30;
    private static final int textSize = 40;
    public static final int researchSize = 150;
    final MenuComponentSelectedResearch selected;
    public MenuResearch(MenuGame menu, Laboratory laboratory){
        super(menu, laboratory);
        var layout = setLayout(new ConstrainedLayout());
        this.laboratory = laboratory;
        undiscovered = add(new Scrollable());
        layout.constrain(undiscovered, new BoundsConstraint(0, spacing, .5f, -spacing, 0, spacing, 1/3f, -textSize));
        available = add(new Scrollable());
        layout.constrain(available, new BoundsConstraint(0, spacing, .5f, -spacing, 1/3f, textSize, 2/3f, -textSize));
        finished = add(new Scrollable());
        layout.constrain(finished, new BoundsConstraint(0, spacing, .5f, -spacing, 2/3f, textSize, 1f, -spacing));
        selected = add(new MenuComponentSelectedResearch(laboratory, DizzyEngine.screenSize.x/2+spacing, spacing, DizzyEngine.screenSize.x/2-spacing*2, DizzyEngine.screenSize.y-spacing*2));
        layout.constrain(selected, new BoundsConstraint(.5f, spacing, 1, -spacing, 0, spacing, 1, -spacing));
        for(Research research : Research.values()){
            if(research.isCompleted())finished.add(new MenuComponentResearch(this, research));
            else if(research.isDiscovered())available.add(new MenuComponentResearch(this, research));
            else if(research.isDiscoverable())undiscovered.add(new MenuComponentResearch(this, research));
        }
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        MenuComponentResearch discover = null;
        for(Component c : undiscovered.components){
            if(c instanceof MenuComponentResearch r){
                if(r.research.isDiscovered()){
                    discover = r;
                }
            }
        }
        if(discover!=null){
            undiscovered.components.remove(discover);
            available.components.add(discover);
        }
        MenuComponentResearch complete = null;
        for(Component c : undiscovered.components){
            if(c instanceof MenuComponentResearch r){
                if(r.research.isCompleted()){
                    complete = r;
                }
            }
        }
        if(complete!=null){
            available.components.remove(complete);
            finished.components.add(complete);
        }
    }
    @Override
    public void render(double deltaTime){
        super.render(deltaTime);
        Renderer.drawText(spacing, undiscovered.y-textSize, DizzyEngine.screenSize.x/2-spacing, undiscovered.y, "Undiscovered Research");
        Renderer.drawText(spacing, available.y-textSize, DizzyEngine.screenSize.x/2-spacing, available.y, "Available Research");
        Renderer.drawText(spacing, finished.y-textSize, DizzyEngine.screenSize.x/2-spacing, finished.y, "Finished Research");
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        super.onKey(id, key, scancode, action, mods);
        if(key==Controls.menu&&action==GLFW.GLFW_PRESS){
            close();
        }
    }
}
