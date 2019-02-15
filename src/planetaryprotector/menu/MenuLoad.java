package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.building.Mine;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.Plot;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.Base;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import static simplelibrary.opengl.Renderer2D.drawRect;
import static simplelibrary.opengl.Renderer2D.drawText;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuLoad extends Menu{
    int tick = 0;
    public Base base;
    public ArrayList<Building> buildings = new ArrayList<>();
    public ArrayList<Building> pendingBuildings = new ArrayList<>();
    private boolean baseMoving = true;
    private boolean gonnaPlay;
    private int nullBuildings;
    private static final int speedMult = 2;
    private MenuGame game;
    public MenuLoad(GUI gui, Menu parent){
        super(gui, parent);
        base = new Base(MenuGame.rand.nextInt(Display.getWidth()-100), MenuGame.rand.nextInt(Display.getHeight()-100));
        buildings.add(base);
        Core.game = new MenuGame(gui);
    }
    public MenuLoad(GUI gui, Menu parent, MenuGame game){
        super(gui, parent);
        this.game = Core.game = game;
        gonnaPlay = true;
        baseMoving = false;
        for(Building b : game.buildings){
            if(b instanceof Base){
                continue;
            }
            buildings.add(b);
        }
    }
    @Override
    public void render(int millisSinceLastTick) {
        Collections.sort(buildings, new Comparator<GameObject>(){
            @Override
            public int compare(GameObject o1, GameObject o2){
                double y1 = o1.y;
                double y2 = o2.y;
                double height1 = o1.height;
                double height2 = o2.height;
                if(o1 instanceof Mine||o1 instanceof Plot){
                    y1 -= Display.getHeight();
                }
                if(o2 instanceof Mine||o2 instanceof Plot){
                    y2 -= Display.getHeight();
                }
                if(o1 instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)o1;
                    y1 -= sky.fallen;
                }
                if(o1 instanceof Wreck){
                    y1 -= Display.getHeight();
                }
                if(o2 instanceof Wreck){
                    y2 -= Display.getHeight();
                }
                if(o2 instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)o2;
                    y2 -= sky.fallen;
                }
                y1 += height1/2;
                y2 += height2/2;
                return (int) Math.round(y1-y2);
            }
        });
        Collections.sort(buildings, new Comparator<Building>(){
            @Override
            public int compare(Building o1, Building o2){
                double y1 = o1.y;
                double y2 = o2.y;
                double height1 = o1.height;
                double height2 = o2.height;
                if(o1 instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)o1;
                    y1 -= sky.fallen;
                }
                if(o2 instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)o2;
                    y2 -= sky.fallen;
                }
                y1 += height1/2;
                y2 += height2/2;
                return (int) Math.round(y1-y2);
            }
        });
        super.render(millisSinceLastTick);
        for(Building b : buildings){
            b.draw();
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
            base.x = Math.min(Display.getWidth()-100, Math.max(0,x-base.width/2));
            base.y = Math.min(Display.getHeight()-100, Math.max(0,y-base.width/2));
        }
        if(button==0&&pressed){
            baseMoving = false;
        }
    }
    @Override
    public void tick() {
//        super.tick();
        if(!baseMoving&&!gonnaPlay){
            tick++;
            for(int i = 0; i<speedMult; i++){
                Building building = Building.generateRandomBuilding(base, buildings);
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
                buildings.add(building);
            }
        }
        if(gonnaPlay){
            tick++;
            if(tick>=20){
                if(game==null)game = new MenuGame(gui, parent, base, buildings);
                Core.game = game;
                gui.open(Core.game);
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