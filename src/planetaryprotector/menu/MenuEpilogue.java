package planetaryprotector.menu;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.Plot;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.Base;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.task.TaskAnimation;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.friendly.ShootingStar;
import simplelibrary.Queue;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
public class MenuEpilogue extends MenuGame{
    private int timer = 195;
    private int i;
    private String[] texts = new String[]{"All of the other cities on the planet are still in ruins.", "Let the people of the city set out and rebuild.", "Thanks for playing!"};
    private Queue<Point> w = new Queue<>();
    private static double offset = 0;
    private ArrayList<Particle> clouds = new ArrayList<>();
    public MenuEpilogue(GUI gui){
        super(gui, new ArrayList<>());
        int buildingCount = (Display.getWidth()/100)*(Display.getHeight()/100);
        for(int i = 0; i<buildingCount; i++){
            Building building = Building.generateRandomBuilding(buildings);
            if(building==null){
                continue;
            }
            buildings.add(new Wreck(building.x, building.y, i));
        }
        phase = 0;
        for(Building building : buildings){
            if(building instanceof Base)continue;
            replaceBuilding(building, new Wreck(building.x, building.y, 0));
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
        Core.discordState = "";
        Core.discordDetails = "Epilogue";
        Core.discordLargeImageKey = "city";
        Core.discordLargeImageText = "Epilogue";
        Core.discordSmallImageKey = "";
        Core.discordEndTimestamp = (System.currentTimeMillis()+159000-Sounds.songTimer()*50)/1000;
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
                gui.open(new MenuEpilogue2(gui));
            }
        }
        if(offset>=Display.getHeight())return;
        if((Sounds.songTimer()>576&&i>1)||i>=10){
            for(Particle p : clouds){
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
            for(Building building : buildings){
                if(building.type==BuildingType.WRECK&&rand.nextInt(25)==1){
                    replaceBuilding(building, new Plot(building.x, building.y));
                }
                if(building.type==BuildingType.EMPTY&&rand.nextInt(15)==1){
                    Skyscraper s = new Skyscraper(building.x, building.y);
                    s.floorCount = 1;
                    replaceBuilding(building, s);
                }
                if(building.type==BuildingType.SKYSCRAPER&&rand.nextInt(4)==1){
                    Skyscraper sky = (Skyscraper) building;
                    sky.floorCount++;
                }
            }
        }
        while(!buildingsToReplace.isEmpty()){
            ArrayList<Building> list = new ArrayList<>(buildingsToReplace.keySet());
            Building start = list.get(0);
            Building end = buildingsToReplace.remove(start);
            buildings.remove(start);
            buildings.add(end);
        }
        super.tick();
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        if(i>2&&Sounds.songTimer()>=1726){
            offset = (Sounds.songTimer()-1726)/97.5D;
        }
    }
    @Override
    public void renderWorld(int millisSinceLastTick){
        drawRect(0,0,Display.getWidth(), Display.getHeight(), MenuGame.theme.getBackgroundTexture());
        synchronized(buildings){
            for(Building building : buildings){
                building.renderBackground();
            }
        }
        for(Point p : w){
            drawRect(p.x-5, p.y-5, p.x+5, p.y+5, ImageStash.instance.getTexture("/textures/worker.png"));
        }
        for(TaskAnimation anim : taskAnimations){
            if(anim.task.isInBackground())anim.render();
        }
        ArrayList<Particle> groundParticles = new ArrayList<>();
        synchronized(particles){
            for(Particle particle : particles){
                if(!particle.air)groundParticles.add(particle);
            }
        }
        ArrayList<GameObject> mainLayer = new ArrayList<>();
        mainLayer.addAll(groundParticles);
        synchronized(buildings){
            mainLayer.addAll(buildings);
        }
        synchronized(droppedItems){
            mainLayer.addAll(droppedItems);
        }
        synchronized(workers){
            mainLayer.addAll(workers);
        }
        for(TaskAnimation anim : taskAnimations){
            if(!anim.task.isInBackground())mainLayer.add(anim);
        }
        Collections.sort(mainLayer, (GameObject o1, GameObject o2) -> {
            int y1 = (int)o1.y;
            int height1 = (int)o1.height;
            int y2 = (int)o2.y;
            int height2 = (int)o2.height;
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
            return y1-y2;
        });
        for(GameObject o : mainLayer){
            o.render();
        }
        synchronized(particles){
            for(Particle particle : particles){
                if(particle.air)particle.render();
            }
        }
        //<editor-fold defaultstate="collapsed" desc="Shields">
        for(Building building : buildings){
            if(building instanceof ShieldGenerator){
                ShieldGenerator gen = (ShieldGenerator) building;
                gen.shield.renderOnWorld();
            }
        }
        //</editor-fold>
        synchronized(asteroids){
            for(Asteroid asteroid : asteroids){
                asteroid.render();
            }
        }
        synchronized(shootingStars){
            for(ShootingStar star : shootingStars){
                star.render();
            }
        }
        drawDayNightCycle();
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glTranslated(0, -Display.getHeight()*offset, 0);
        renderWorld(millisSinceLastTick);
        drawRect(0, Display.getHeight(), Display.getWidth(), Display.getHeight()*2, ImageStash.instance.getTexture("/textures/background/dirt "+MenuGame.theme.tex()+".png"));
        drawRect(0, Display.getHeight()*2, Display.getWidth(), Display.getHeight()*3, ImageStash.instance.getTexture("/textures/background/stone.png"));
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
    @Override
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
                    Particle p = new Particle(x, y, strength, rateOfChange, speed);
                    clouds.add(p);
                    addParticleEffect(p);
                    y-=40;
                }
                y = Y;
            }else{
                double Y = y;
                for(int i = 0; i<height; i++){
                    Particle p = new Particle(x, y-20, strength, rateOfChange, speed);
                    clouds.add(p);
                    addParticleEffect(p);
                    y-=40;
                }
                y = Y;
            }
        }
    }
    @Override
    public boolean canLose() {
        return false;
    }
}