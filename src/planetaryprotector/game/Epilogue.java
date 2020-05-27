package planetaryprotector.game;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.Wreck;
import planetaryprotector.structure.building.Plot;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.structure.building.Skyscraper;
import planetaryprotector.structure.building.Base;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.structure.building.ShieldGenerator;
import planetaryprotector.structure.building.task.TaskAnimation;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.menu.MenuEpilogue2;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.structure.Structure;
import simplelibrary.Queue;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
public class Epilogue extends Game{
    private int timer = 195;
    private int i;
    private String[] texts = new String[]{"All of the other cities on the planet are still in ruins.", "Let the people of the city set out and rebuild.", "Thanks for playing!"};
    private Queue<Point> w = new Queue<>();
    private static double offset = 0;
    private ArrayList<Particle> clouds = new ArrayList<>();
    public Epilogue(GUI gui){
        super(gui, null, 1);
        int buildingCount = (Display.getWidth()/100)*(Display.getHeight()/100);
        for(int i = 0; i<buildingCount; i++){
            ArrayList<Building> buildings = new ArrayList<>();
            for(Structure s : structures){
                if(s instanceof Building)buildings.add((Building) s);
            }
            Building building = Building.generateRandomBuilding(this, buildings);
            if(building==null){
                continue;
            }
            structures.add(new Wreck(this, building.x, building.y, i));
        }
        phase = 0;
        for(Structure structure : structures){
            if(structure instanceof Base)continue;
            if(structure instanceof Building)replaceStructure(structure, new Wreck(this, structure.x, structure.y, 0));
        }
        doNotDisturb = true;
        offset = 0;
    }
    public Epilogue(GUI gui, int i){
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
            for(Structure structure : structures){
                if(structure instanceof Wreck&&rand.nextInt(25)==1){
                    replaceStructure(structure, new Plot(this, structure.x, structure.y));
                }
                if(structure instanceof Plot&&rand.nextInt(15)==1){
                    Skyscraper s = new Skyscraper(this, structure.x, structure.y);
                    s.floorCount = 1;
                    replaceStructure(structure, s);
                }
                if(structure instanceof Skyscraper&&rand.nextInt(4)==1){
                    Skyscraper sky = (Skyscraper) structure;
                    sky.floorCount++;
                }
            }
        }
        while(!structuresToReplace.isEmpty()){
            ArrayList<Structure> list = new ArrayList<>(structuresToReplace.keySet());
            Structure start = list.get(0);
            Structure end = structuresToReplace.remove(start);
            structures.remove(start);
            structures.add(end);
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
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        for(Structure structure : structures){
            structure.renderBackground();
        }
        for(Point p : w){
            drawRect(p.x-5, p.y-5, p.x+5, p.y+5, ImageStash.instance.getTexture("/textures/worker.png"));
        }
        for(TaskAnimation anim : taskAnimations){
            if(anim.task.isInBackground())anim.render();
        }
        ArrayList<Particle> groundParticles = new ArrayList<>();
        for(Particle particle : particles){
            if(!particle.air)groundParticles.add(particle);
        }
        ArrayList<GameObject> mainLayer = new ArrayList<>();
        mainLayer.addAll(groundParticles);
        mainLayer.addAll(structures);
        mainLayer.addAll(droppedItems);
        mainLayer.addAll(workers);
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
        for(Particle particle : particles){
            if(particle.air)particle.render();
        }
        //<editor-fold defaultstate="collapsed" desc="Shields">
        for(Structure structure : structures){
            if(structure instanceof ShieldGenerator){
                ShieldGenerator gen = (ShieldGenerator) structure;
                gen.shield.renderOnWorld();
            }
        }
        //</editor-fold>
        for(Asteroid asteroid : asteroids){
            asteroid.render();
        }
        for(ShootingStar star : shootingStars){
            star.render();
        }
        drawDayNightCycle();
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glTranslated(0, -Display.getHeight()*offset, 0);
        renderWorld(millisSinceLastTick);
        drawRect(0, Display.getHeight(), Display.getWidth(), Display.getHeight()*2, ImageStash.instance.getTexture("/textures/background/dirt "+Game.theme.tex()+".png"));
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
        gui.open(new MenuGame(gui, new Epilogue(gui, i+1)));
    }
    @Override
    public void addCloud(){
        if(!MenuOptionsGraphics.clouds)return;
        double strength = rand.nextInt(42);
        double rateOfChange = (rand.nextDouble()-.4)/80;
        double speed = rand.nextGaussian()/10+1;
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
                    Particle p = new Particle(this, x, y, strength, rateOfChange, speed);
                    clouds.add(p);
                    addParticleEffect(p);
                    y-=40;
                }
                y = Y;
            }else{
                double Y = y;
                for(int i = 0; i<height; i++){
                    Particle p = new Particle(this, x, y-20, strength, rateOfChange, speed);
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