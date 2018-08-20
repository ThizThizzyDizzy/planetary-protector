package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.building.MenuComponentMine;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentWreck;
import planetaryprotector.building.MenuComponentPlot;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static simplelibrary.opengl.Renderer2D.drawRect;
import static simplelibrary.opengl.Renderer2D.drawText;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuLoad extends Menu{
    int tick = 0;
    public MenuComponentBase base;
    public ArrayList<MenuComponentBuilding> buildings = new ArrayList<>();
    public ArrayList<MenuComponentBuilding> pendingBuildings = new ArrayList<>();
    private boolean baseMoving = true;
    private boolean gonnaPlay;
    private int nullBuildings;
    public MenuLoad(GUI gui, Menu parent){
        super(gui, parent);
        base = add(new MenuComponentBase(MenuGame.rand.nextInt(Display.getWidth()-100), MenuGame.rand.nextInt(Display.getHeight()-100)));
        Core.game = null;
    }
    public MenuLoad(GUI gui, Menu parent, MenuGame game){
        super(gui, parent);
        Core.game = game;
        gonnaPlay = true;
        baseMoving = false;
        for(MenuComponentBuilding b : game.buildings){
            if(b instanceof MenuComponentBase){
                continue;
            }
            buildings.add(b);
        }
    }
    @Override
    public void render(int millisSinceLastTick) {
        Collections.sort(components, new Comparator<MenuComponent>(){
            @Override
            public int compare(MenuComponent o1, MenuComponent o2){
                double y1 = o1.y;
                double y2 = o2.y;
                double height1 = o1.height;
                double height2 = o2.height;
                if(o1 instanceof MenuComponentMine||o1 instanceof MenuComponentPlot){
                    y1 -= Display.getHeight();
                }
                if(o2 instanceof MenuComponentMine||o2 instanceof MenuComponentPlot){
                    y2 -= Display.getHeight();
                }
                if(o1 instanceof MenuComponentSkyscraper){
                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)o1;
                    y1 -= sky.fallen;
                }
                if(o1 instanceof MenuComponentWreck){
                    y1 -= Display.getHeight();
                }
                if(o2 instanceof MenuComponentWreck){
                    y2 -= Display.getHeight();
                }
                if(o2 instanceof MenuComponentSkyscraper){
                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)o2;
                    y2 -= sky.fallen;
                }
                y1 += height1/2;
                y2 += height2/2;
                return (int) Math.round(y1-y2);
            }
        });
        Collections.sort(buildings, new Comparator<MenuComponentBuilding>(){
            @Override
            public int compare(MenuComponentBuilding o1, MenuComponentBuilding o2){
                double y1 = o1.y;
                double y2 = o2.y;
                double height1 = o1.height;
                double height2 = o2.height;
                y1 += height1/2;
                y2 += height2/2;
                return (int) Math.round(y1-y2);
            }
        });
        super.render(millisSinceLastTick);
        for(MenuComponentBuilding b : buildings){
            b.render();
        }
        if(baseMoving){
            centeredTextWithBackground(0, 0, Display.getWidth(), 75, "Place the base wherever you want");
        }else if(gonnaPlay){
            centeredTextWithBackground(0, 0, Display.getWidth(), 250, "PLAY!");
        }else{
            centeredTextWithBackground(0, 0, Display.getWidth(), 100, "Loading...");
            centeredTextWithBackground(0, Display.getHeight()-50, Display.getWidth(), Display.getHeight(), "Finding "+(Core.game==null?"random ":"")+"building arrangement...");
        }
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange) {
        if(baseMoving){
            base.x = Math.min(Display.getWidth()-100, Math.max(0,x-50));
            base.y = Math.min(Display.getHeight()-100, Math.max(0,y-50));
        }
        if(button==0&&pressed){
            baseMoving = false;
        }
    }
    @Override
    public void tick() {
        super.tick();
        if(!baseMoving&&!gonnaPlay){
            tick++;
            MenuComponentBuilding building = MenuComponentBuilding.generateRandomBuilding(base, buildings);
            if(!pendingBuildings.isEmpty()){
                building = pendingBuildings.remove(0);
            }
            if(building==null){
                nullBuildings++;
                if(nullBuildings>=10){
                    gonnaPlay = true;
                    tick = 0;
                }
                return;
            }
            buildings.add(add(building));
        }
        if(gonnaPlay){
            tick++;
            if(tick>=20){
                if(Core.game!=null){
                    gui.open(Core.game);
                }else{
                    Core.game = gui.open(new MenuGame(gui, parent, base, buildings));
                }
            }
        }
    }
    private void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawText(left,top,right,bottom, str);
    }
    @Override
    public void buttonClicked(MenuComponentButton button){}
}