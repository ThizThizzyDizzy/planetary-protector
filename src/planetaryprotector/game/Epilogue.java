package planetaryprotector.game;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.structure.Wreck;
import planetaryprotector.structure.Plot;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.Base;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.structure.task.TaskAnimation;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.menu.MenuEpilogue2;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.structure.Structure;
import simplelibrary.Queue;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Epilogue extends Game{//TODO no Display
    private int timer = 195;
    private int i;
    private String[] texts = new String[]{"All of the other cities on the planet are still in ruins.", "Let the people of the city set out and rebuild.", "Thanks for playing!"};
    private Queue<int[]> w = new Queue<>();
    private static double offset = 0;
    private ArrayList<Particle> clouds = new ArrayList<>();
    public Epilogue(){
        super(null, 1, WorldGenerator.getWorldGenerator(1, "chaotic"), Story.stories.get(1).get(0), false);
        int buildingCount = (Core.helper.displayWidth()/100)*(Core.helper.displayHeight()/100);
        for(int i = 0; i<buildingCount; i++){
            Structure structure = genBuilding(this, structures);
            if(structure==null){
                continue;
            }
            structures.add(new Wreck(this, structure.x, structure.y, i));
        }
        phase = 0;
        for(Structure structure : structures){
            if(structure instanceof Base)continue;
            if(structure.type.isBackgroundStructure())continue;
            replaceStructure(structure, new Wreck(this, structure.x, structure.y, 0));
        }
        doNotDisturb = true;
        offset = 0;
        meteorPhaseIntensityMult = 0;
        meteorShowerTimer = Integer.MAX_VALUE;
        paused = false;
        updatePhaseMarker = false;
        generatedBBox = getWorldBoundingBox();
    }
    private static Structure genBuilding(Game game, ArrayList<Structure> structures){
        int buildingX;
        int buildingY;
        int i = 0;
        WHILE:while(true){
            i++;
            if(i>1000){
                return null;
            }
            buildingX = game.rand.nextInt(Core.helper.displayWidth()-100);
            buildingY = game.rand.nextInt(Core.helper.displayHeight()-100);
            for(Structure structure : structures){
                double Y = structure.y;
                if(structure instanceof Skyscraper){
                    Y-=((Skyscraper) structure).fallen;
                }
                if(isClickWithinBounds(buildingX, buildingY, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                     isClickWithinBounds(buildingX+100, buildingY, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                     isClickWithinBounds(buildingX, buildingY+100, structure.x, Y, structure.x+structure.width, Y+structure.height)||
                     isClickWithinBounds(buildingX+100, buildingY+100, structure.x, Y, structure.x+structure.width, Y+structure.height)){
                    continue WHILE;
                }
            }
            break;
        }
        return new Skyscraper(game, buildingX, buildingY, game.rand.nextInt(40)+10);
    }
    public Epilogue(int i){
        this();
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
                Core.gui.open(new MenuEpilogue2(Core.gui));
            }
        }
        if(offset>=Core.helper.displayHeight())return;
        if((Sounds.songTimer()>576&&i>1)||i>=10){
            for(Particle p : clouds){
                p.tick();
            }
            if(rand.nextDouble()<.1){
                addCloud();
            }
            for(int[] p : w){
                p[0] = rand.nextInt(Core.helper.displayWidth());
                p[1] = rand.nextInt(Core.helper.displayHeight());
            }
            for(int i = 0; i<Core.helper.displayHeight()/100; i++){
                w.enqueue(new int[]{rand.nextInt(Core.helper.displayWidth()), rand.nextInt(Core.helper.displayHeight())});
            }
            for(Structure structure : structures){
                if(structure instanceof Wreck&&rand.nextInt(25)==1){
                    replaceStructure(structure, new Plot(this, structure.x, structure.y));
                }
                if(structure instanceof Plot&&rand.nextInt(15)==1){
                    replaceStructure(structure, new Skyscraper(this, structure.x, structure.y, 1));
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
        drawRect(0,0,Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(1));
        for(Structure structure : structures){
            structure.renderBackground();
        }
        for(int[] p : w){
            drawRect(p[0]-5, p[1]-5, p[0]+5, p[1]+5, ImageStash.instance.getTexture("/textures/worker.png"));
        }
        for(TaskAnimation anim : taskAnimations){
            if(anim.task.isInBackground())anim.draw();
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
            o.draw();
        }
        for(Particle particle : particles){
            if(particle.air)particle.draw();
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
            asteroid.draw();
        }
        for(ShootingStar star : shootingStars){
            star.draw();
        }
        drawDayNightCycle();
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glTranslated(0, -Core.helper.displayHeight()*offset, 0);
        renderWorld(millisSinceLastTick);
        drawRect(0, Core.helper.displayHeight(), Core.helper.displayWidth(), Core.helper.displayHeight()*2, ImageStash.instance.getTexture("/textures/background/dirt "+Game.theme.tex()+".png"));
        drawRect(0, Core.helper.displayHeight()*2, Core.helper.displayWidth(), Core.helper.displayHeight()*3, ImageStash.instance.getTexture("/textures/background/stone.png"));
        if(blackScreenOpacity>0&&i<10){
            blackScreenOpacity-=.01;
        }
        if(i<texts.length){
            centeredTextWithBackground(0, 0, Core.helper.displayWidth(), 50, texts[i]);
        }
        GL11.glTranslated(0, Core.helper.displayHeight()*offset, 0);
    }
    @Override
    public void save(){}
    @Override
    public boolean isDestroyed(){
        return false;
    }
    private void restart(){
        Core.gui.open(new MenuGame(Core.gui, new Epilogue(i+1)));
    }
    @Override
    public void addCloud(){
        if(!MenuOptionsGraphics.clouds)return;
        double strength = rand.nextInt(42);
        double rateOfChange = (rand.nextDouble()-.4)/80;
        double speed = rand.nextGaussian()/10+1;
        speed*=250;
        rateOfChange*=250;
        int y = rand.nextInt(Core.helper.displayHeight());
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
            int x = (int)(-(X*25)-50);
            if(Math.round(X)==Math.round(X*10)/10d){
                int Y = y;
                for(int i = 0; i<height; i++){
                    Particle p = new Particle(this, x, y, strength, rateOfChange, speed);
                    clouds.add(p);
                    addParticleEffect(p);
                    y-=40;
                }
                y = Y;
            }else{
                int Y = y;
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