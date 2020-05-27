package planetaryprotector.menu;
import planetaryprotector.game.Game;
import planetaryprotector.structure.building.Mine;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.Wreck;
import planetaryprotector.structure.building.Plot;
import planetaryprotector.structure.building.Skyscraper;
import planetaryprotector.structure.building.Base;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.structure.Structure;
import static simplelibrary.opengl.Renderer2D.drawRect;
import static simplelibrary.opengl.Renderer2D.drawText;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuLoad extends Menu{
    int tick = 0;
    public Base base;
    public ArrayList<Building> buildings = new ArrayList<>();
    public ArrayList<Building> pendingBuildings = new ArrayList<>();
    private boolean baseMoving = true;
    private int nullBuildings;
    public Game game;
    private final String name;
    public MenuLoad(GUI gui, String name, Menu parent){
        super(gui, parent);
        game = new Game(gui, name, 1);
        base = new Base(game, game.rand.nextInt(Display.getWidth()-100), game.rand.nextInt(Display.getHeight()-100));
        buildings.add(base);
        this.name = name;
    }
    @Override
    public void renderBackground(){}
    @Override
    public void render(int millisSinceLastTick) {
        Collections.sort(buildings, (GameObject o1, GameObject o2) -> {
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
        });
        Collections.sort(buildings, (Building o1, Building o2) -> {
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
        });
        super.render(millisSinceLastTick);
        for(Building s : buildings){
            s.draw();
        }
        centeredTextWithBackground(0, 0, Display.getWidth(), 75, "Place the base wherever you want");
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
        if(!baseMoving){
            while(true){
                Building building = Building.generateRandomBuilding(game, buildings);
                if(!pendingBuildings.isEmpty()){
                    building = pendingBuildings.remove(0);
                }
                if(building==null){
                    nullBuildings++;
                    if(nullBuildings>=10){
                        break;
                    }
                    continue;
                }
                buildings.add(building);
            }
            game = new Game(gui, name, 1, new ArrayList<>(buildings));
            gui.open(new MenuGame(gui, game));
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
}