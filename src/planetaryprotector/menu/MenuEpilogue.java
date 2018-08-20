package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentWreck;
import planetaryprotector.building.MenuComponentPlot;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentBase;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector2.menu.MenuPrologue;
import java.awt.Point;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.Queue;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
public class MenuEpilogue extends MenuGame{
    private int timer = 195;
    private int i;
    private String[] texts = new String[]{"All of the other cities on the planet are still in ruins.", "Let the people of the city set out and rebuild.", "Thanks for playing!"};
    private Queue<Point> w = new Queue<>();
    private static double offset = 0;
    private ArrayList<MenuComponentParticle> clouds = new ArrayList<>();
    public MenuEpilogue(GUI gui){
        super(gui, null, new MenuComponentBase(Display.getWidth(), Display.getHeight()), new ArrayList<>());
        int buildingCount = (Display.getWidth()/100)*(Display.getHeight()/100);
        for(int i = 0; i<buildingCount; i++){
            MenuComponentBuilding building = MenuComponentBuilding.generateRandomBuilding(base, buildings);
            if(building==null){
                continue;
            }
            buildings.add(add(new MenuComponentWreck(building.x, building.y, i)));
        }
        phase = 0;
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentBase)continue;
            replaceBuilding(building, new MenuComponentWreck(building.x, building.y, 0));
        }
        addWorker();
        doNotDisturb = true;
        offset = 0;
    }
    public MenuEpilogue(GUI gui, int i){
        this(gui);
        this.i = i;
    }
    @Override
    public void tick(){
        if(i>2){
            if(offset==0){
                timer--;
            }
        }else{
            if(i==0&&Sounds.songTimer()>=191||
               i==1&&Sounds.songTimer()>=384||
               i==2&&Sounds.songTimer()>=576){
                restart();
            }
        }
        if(timer<=0){
            restart();
        }
        if(i>2&&Sounds.songTimer()>=1726){
            offset = (Sounds.songTimer()-1726)/97.5D;
            if(offset>=2){
                gui.open(new MenuPrologue(gui));
            }
        }
        if(offset>=Display.getHeight())return;
        if((Sounds.songTimer()>576&&i>1)||i>=10){
            for(MenuComponentParticle p : clouds){
                p.tick();
            }
            if(rand.nextDouble()<.1){
                addCloud();
            }
            for(Point p : w){
                p.x = rand.nextInt(Display.getWidth());
                p.y = rand.nextInt(Display.getHeight());
            }
            for(int i = 0; i<Display.getHeight()/100; i++){
                w.enqueue(new Point(rand.nextInt(Display.getWidth()), rand.nextInt(Display.getHeight())));
            }
            for(MenuComponentBuilding building : buildings){
                if(building.type==BuildingType.WRECK&&rand.nextInt(25)==1){
                    replaceBuilding(building, new MenuComponentPlot(building.x, building.y));
                }
                if(building.type==BuildingType.EMPTY&&rand.nextInt(15)==1){
                    MenuComponentSkyscraper s = new MenuComponentSkyscraper(building.x, building.y);
                    s.floorCount = 1;
                    replaceBuilding(building, s);
                }
                if(building.type==BuildingType.SKYSCRAPER&&rand.nextInt(4)==1){
                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper) building;
                    sky.floorCount++;
                }
            }
        }
        while(!buildingsToReplace.isEmpty()){
            ArrayList<MenuComponentBuilding> list = new ArrayList<>(buildingsToReplace.keySet());
            MenuComponentBuilding start = list.get(0);
            MenuComponentBuilding end = buildingsToReplace.remove(start);
            components.remove(start);
            buildings.remove(start);
            buildings.add(add(end));
        }
        super.tick();
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        if(i>2&&Sounds.songTimer()>=1726){
            offset = (Sounds.songTimer()-1726)/97.5D;
        }
        for(Point p : w){
            drawRect(p.x-5, p.y-5, p.x+5, p.y+5, ImageStash.instance.getTexture("/textures/him.png"));
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glTranslated(0, -Display.getHeight()*offset, 0);
        super.render(millisSinceLastTick);
        drawRect(0, Display.getHeight(), Display.getWidth(), Display.getHeight()*2, ImageStash.instance.getTexture("/gui/dirtBackground.png"));
        drawRect(0, Display.getHeight()*2, Display.getWidth(), Display.getHeight()*3, ImageStash.instance.getTexture("/gui/stoneBackground.png"));
        if(blackScreenOpacity>0&&i<10){
            blackScreenOpacity-=.01;
        }
        if(i<texts.length){
            centeredTextWithBackground(0, 0, Display.getWidth(), 50, texts[i]);
        }
        GL11.glTranslated(0, Display.getHeight()*offset, 0);
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){}
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){}
    @Override
    public void save(){}
    @Override
    public boolean isDestroyed(){
        return false;
    }
    private void restart(){
        gui.open(new MenuEpilogue(gui, i+1));
    }
    public void addCloud(){
        if(!MenuOptionsGraphics.clouds)return;
        double strength = rand.nextInt(42);
        double rateOfChange = (rand.nextDouble()-.4)/80;
        double speed = Core.game.rand.nextGaussian()/10+1;
        speed*=250;
        rateOfChange*=250;
        double y = rand.nextInt(Display.getHeight());
        int wide = rand.nextInt(25)+5;
        int high = rand.nextInt(wide/5)+2;
        int height = 1;
        for(double X = 0; X<wide; X+=.5){
            double percent = X/wide;
            if(percent>.75){
                if(rand.nextBoolean()&&rand.nextBoolean()&&height>1){
                    height--;
                }
            }else if(percent>.1){
                if(rand.nextBoolean()&&rand.nextBoolean()&&height<high){
                    height++;
                }
            }
            double x = -(X*25)-50;
            if(Math.round(X)==Math.round(X*10)/10d){
                double Y = y;
                for(int i = 0; i<height; i++){
                    clouds.add(add(new MenuComponentParticle(x, y, strength, rateOfChange, speed)));
                    y-=40;
                }
                y = Y;
            }else{
                double Y = y;
                for(int i = 0; i<height; i++){
                    clouds.add(add(new MenuComponentParticle(x, y-20, strength, rateOfChange, speed)));
                    y-=40;
                }
                y = Y;
            }
        }
    }
}