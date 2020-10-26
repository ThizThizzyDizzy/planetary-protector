package planetaryprotector.menu.ingame;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.structure.building.Laboratory;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.research.Research;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public class MenuResearch extends MenuComponentOverlayBuilding{
    private final Laboratory laboratory;
    private final MenuComponentMulticolumnList undiscovered;
    private final MenuComponentMulticolumnList available;
    private final MenuComponentMulticolumnList finished;
    private static final int spacing = 30;
    private static final int textSize = 40;
    public static final int researchSize = 150;
    final MenuComponentSelectedResearch selected;
    public MenuResearch(MenuGame menu, Laboratory laboratory){
        super(menu, laboratory);
        this.laboratory = laboratory;
        undiscovered = add(new MenuComponentMulticolumnList(spacing, spacing+textSize, Core.helper.displayWidth()/2-spacing*2, (Core.helper.displayHeight()-spacing*2)/3-textSize, researchSize, researchSize, 20, false));
        available = add(new MenuComponentMulticolumnList(spacing, undiscovered.y+undiscovered.height+textSize, Core.helper.displayWidth()/2-spacing*2, (Core.helper.displayHeight()-spacing*2)/3-textSize, researchSize, researchSize, 20, false));
        finished = add(new MenuComponentMulticolumnList(spacing, available.y+available.height+textSize, Core.helper.displayWidth()/2-spacing*2, (Core.helper.displayHeight()-spacing*2)/3-textSize, researchSize, researchSize, researchSize/10, false));
        selected = add(new MenuComponentSelectedResearch(laboratory, Core.helper.displayWidth()/2+spacing, spacing, Core.helper.displayWidth()/2-spacing*2, Core.helper.displayHeight()-spacing*2));
        for(Research research : Research.values()){
            if(research.isCompleted())finished.add(new MenuComponentResearch(this, research));
            else if(research.isDiscovered())available.add(new MenuComponentResearch(this, research));
            else if(research.isDiscoverable())undiscovered.add(new MenuComponentResearch(this, research));
        }
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        MenuComponent discover = null;
        for(MenuComponent c : undiscovered.components){
            if(c instanceof MenuComponentResearch){
                if(((MenuComponentResearch)c).research.isDiscovered()){
                    discover = c;
                }
            }
        }
        if(discover!=null){
            undiscovered.components.remove(discover);
            available.components.add(discover);
        }
        MenuComponent complete = null;
        for(MenuComponent c : undiscovered.components){
            if(c instanceof MenuComponentResearch){
                if(((MenuComponentResearch)c).research.isCompleted()){
                    complete = c;
                }
            }
        }
        if(complete!=null){
            available.components.remove(complete);
            finished.components.add(complete);
        }
    }
    @Override
    public void render(){
        GL11.glColor4d(1, 1, 1, 1);
        drawText(spacing, undiscovered.y-textSize, Core.helper.displayWidth()/2-spacing, undiscovered.y, "Undiscovered Research");
        drawText(spacing, available.y-textSize, Core.helper.displayWidth()/2-spacing, available.y, "Available Research");
        drawText(spacing, finished.y-textSize, Core.helper.displayWidth()/2-spacing, finished.y, "Finished Research");
    }
    @Override
    public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
        if(key==Controls.menu&&isPress&&!isRepeat){
            close();
        }
    }
}