package planetaryprotector.game;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.structure.Wreck;
import planetaryprotector.structure.Plot;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.Base;
import planetaryprotector.particle.Particle;
import java.util.ArrayList;
import java.util.Collections;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.Options;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.structure.task.TaskAnimation;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.menu.MenuEpilogue2;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.structure.Structure;
public class Epilogue extends Game{//TODO no Display
    private int timer = 195;
    private int i;
    private String[] texts = new String[]{"All of the other cities on the planet are still in ruins.", "Let the people of the city set out and rebuild.", "Thanks for playing!"};
    private ArrayList<int[]> w = new ArrayList<>();
    private static double offset = 0;
    private ArrayList<Particle> clouds = new ArrayList<>();
    public Epilogue(){
        super(null, 1, WorldGenerator.getWorldGenerator(1, "chaotic"), Story.stories.get(1).get(0), false);
        int buildingCount = (DizzyEngine.screenSize.x/100)*(DizzyEngine.screenSize.y/100);
        for(int i = 0; i<buildingCount; i++){
            Structure structure = genBuilding(this, structures);
            if(structure==null){
                continue;
            }
            addStructure(new Wreck(this, structure.x, structure.y, i));
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
        WHILE:
        while(true){
            i++;
            if(i>1000){
                return null;
            }
            buildingX = game.rand.nextInt(DizzyEngine.screenSize.x-100);
            buildingY = game.rand.nextInt(DizzyEngine.screenSize.y-100);
            for(Structure structure : structures){
                var bbox = structure.getBoundingBox(true);
                if(bbox.contains(buildingX, buildingY))continue WHILE;
                if(bbox.contains(buildingX+100, buildingY))continue WHILE;
                if(bbox.contains(buildingX, buildingY+100))continue WHILE;
                if(bbox.contains(buildingX+100, buildingY+100))continue WHILE;
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
            if(i==0&&Sounds.songTimer()>=191
                ||i==1&&Sounds.songTimer()>=384
                ||i==2&&Sounds.songTimer()>=576){
                restart();
            }
        }
        if(timer<=0){
            restart();
        }
        if(i>2&&Sounds.songTimer()>=1726){
            offset = (Sounds.songTimer()-1726)/97.5D;
            if(offset>=2){
                new MenuEpilogue2().open();
            }
        }
        if(offset>=DizzyEngine.screenSize.y)return;
        if((Sounds.songTimer()>576&&i>1)||i>=10){
            for(Particle p : clouds){
                p.tick();
            }
            if(rand.nextDouble()<.1){
                addCloud();
            }
            for(int[] p : w){
                p[0] = rand.nextInt(DizzyEngine.screenSize.x);
                p[1] = rand.nextInt(DizzyEngine.screenSize.y);
            }
            for(int i = 0; i<DizzyEngine.screenSize.y/100; i++){
                w.add(new int[]{rand.nextInt(DizzyEngine.screenSize.x), rand.nextInt(DizzyEngine.screenSize.y)});
            }
            for(Structure structure : structures){
                if(structure instanceof Wreck&&rand.nextInt(25)==1){
                    replaceStructure(structure, new Plot(this, structure.x, structure.y));
                }
                if(structure instanceof Plot&&rand.nextInt(15)==1){
                    replaceStructure(structure, new Skyscraper(this, structure.x, structure.y, 1));
                }
                if(structure instanceof Skyscraper&&rand.nextInt(4)==1){
                    Skyscraper sky = (Skyscraper)structure;
                    sky.floorCount++;
                }
            }
        }
        while(!structuresToReplace.isEmpty()){
            ArrayList<Structure> list = new ArrayList<>(structuresToReplace.keySet());
            Structure start = list.get(0);
            Structure end = structuresToReplace.remove(start);
            removeStructure(start);
            addStructure(end);
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
    public void renderWorld(Vector3i chunk, double deltaTime){
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, Game.theme.getBackgroundTexture(1));
        for(Structure structure : structures){
            structure.renderBackground();
        }
        for(int[] p : w){
            Renderer.fillRect(p[0]-5, p[1]-5, p[0]+5, p[1]+5, ResourceManager.getTexture("/textures/worker.png"));
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
            int height1 = (int)o1.getBoundingBox(false).height;
            int y2 = (int)o2.y;
            int height2 = (int)o2.getBoundingBox(false).height;
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
                ShieldGenerator gen = (ShieldGenerator)structure;
                gen.shield.render(deltaTime);
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
    public void render(double deltaTime){
        GL11.glTranslated(0, -DizzyEngine.screenSize.y*offset, 0);
        renderWorld(null, deltaTime);
        Renderer.fillRect(0, DizzyEngine.screenSize.y, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y*2, ResourceManager.getTexture("/textures/background/dirt "+Game.theme.tex()+".png"));
        Renderer.fillRect(0, DizzyEngine.screenSize.y*2, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y*3, ResourceManager.getTexture("/textures/background/stone.png"));
        if(blackScreenOpacity>0&&i<10){
            blackScreenOpacity -= .01;
        }
        if(i<texts.length){
            centeredTextWithBackground(0, 0, DizzyEngine.screenSize.x, 50, texts[i]);
        }
        GL11.glTranslated(0, DizzyEngine.screenSize.y*offset, 0);
    }
    @Override
    public void save(){
    }
    @Override
    public boolean isDestroyed(){
        return false;
    }
    private void restart(){
        new MenuGame(new Epilogue(i+1)).open();
    }
    @Override
    public void addCloud(){
        if(!Options.options.clouds)return;
        float strength = rand.nextInt(42);
        float rateOfChange = (float)((rand.nextDouble()-.4)/80);
        float speed = (float)(rand.nextGaussian()/10+1);
        speed *= 250;
        rateOfChange *= 250;
        int y = rand.nextInt(DizzyEngine.screenSize.y);
        int wide = rand.nextInt(25)+5;
        int high = rand.nextInt(wide/5)+2;
        int height = 1;
        for(float X = 0; X<wide; X += .5){
            float percent = X/wide;
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
                    y -= 40;
                }
                y = Y;
            }else{
                int Y = y;
                for(int i = 0; i<height; i++){
                    Particle p = new Particle(this, x, y-20, strength, rateOfChange, speed);
                    clouds.add(p);
                    addParticleEffect(p);
                    y -= 40;
                }
                y = Y;
            }
        }
    }
    @Override
    public boolean canLose(){
        return false;
    }
}
