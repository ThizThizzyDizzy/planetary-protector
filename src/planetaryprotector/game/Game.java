package planetaryprotector.game;
import planetaryprotector.Core;
import planetaryprotector.Expedition;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.Main;
import planetaryprotector.Sounds;
import planetaryprotector.VersionManager;
import planetaryprotector.friendly.Drone;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.friendly.Worker;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.particle.Particle;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.structure.task.TaskDemolish;
import planetaryprotector.structure.task.TaskRepair;
import planetaryprotector.structure.task.TaskRepairAll;
import planetaryprotector.structure.task.TaskType;
import planetaryprotector.structure.task.Task;
import planetaryprotector.structure.task.TaskUpgrade;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.Base;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.ParticleFog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.structure.PowerNetwork;
import planetaryprotector.structure.StarlightNetwork;
import planetaryprotector.structure.task.TaskAnimated;
import planetaryprotector.structure.task.TaskAnimation;
import planetaryprotector.structure.task.TaskSpecialUpgrade;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.research.Research;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Structure;
import simplelibrary.Queue;
import simplelibrary.Sys;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
import simplelibrary.error.ErrorCategory;
import simplelibrary.error.ErrorLevel;
import simplelibrary.opengl.ImageStash;
import planetaryprotector.event.StructureChangeEventListener;
import planetaryprotector.structure.StructureType;
import planetaryprotector.structure.Laboratory;
import planetaryprotector.structure.Structure.Upgrade;
import simplelibrary.opengl.Renderer2D;
import planetaryprotector.structure.StructureDemolishable;
import simplelibrary.image.Color;
public class Game extends Renderer2D{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    public ArrayList<DroppedItem> droppedItems = new ArrayList<>();
    public Random rand = new Random();
    public boolean lost = false;
    public boolean meteorShower;
    public int meteorShowerTimer = 100;
    public ArrayList<Worker> workers = new ArrayList<>();
    public ArrayList<Structure> structures = new ArrayList<>();
    public Structure selectedStructure;
    public int actionUpdateRequired = 0;
    HashMap<Structure, Structure> structuresToReplace = new HashMap<>();
    public boolean paused;
    public int workerCooldown = 1020;
    private final int phaseShowerPadding = 600;//30 seconds
    private final int phaseTime = 200;//10 seconds
    public int phaseCounter;
    public int phase;
    public final int level;
    public int targetPopulation = 8_000_000;
    public int popPerFloor = -1;
    private final double METEOR_INTENSITY = 1/20_000d;//default meteors per tick per 100x100 pixels
    private final double METEOR_SHOWER_MULT = 50;//multiplier during meteor showers
    public double meteorPhaseIntensityMult = 1;//modified by phase
    public double meteorManualIntensityMult = 1;//modified by cheats
    public double meteorShowerDelayMultiplier = 1;//time between meteor showers
    //number of ticks until the next meteor drops
    public double meteorTimer = 0;
    @Deprecated
    public Expedition pendingExpedition;
    public ArrayList<Expedition> activeExpeditions = new ArrayList<>();
    public ArrayList<Expedition> finishedExpeditions = new ArrayList<>();
    //enemy stuff
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Enemy> nextEnemies = new ArrayList<>();
    public ArrayList<Drone> drones = new ArrayList<>();
    private int enemyTimer = 20*30;
    
    public double damageReportTimer = 0;
    public double damageReportTime = 20*10;
    public int civilianCooldown = rand.nextInt(20*60*5);
    public double civilianIncreasePerSec = 0;
    public int civilianTimer = 0;
    public boolean doNotDisturb = false;
    public int winTimer = -1;
    public boolean fading;
    public double blackScreenOpacity = 0;
    public boolean cheats = false;
    public int tick;
    public boolean won;
    private int cloudTimer = rand.nextInt(750)+500;
    //FOG
    private static final int maxFogTime = 20*60*15;
    private static final int minFogTime = 20*60*5;
    private int fogTimer = rand.nextInt(maxFogTime-minFogTime)+minFogTime;
    @Deprecated
    private static final double fogWinter = .7;//less fog in winter
    @Deprecated
    private static final double fogSpooky = 1.2;//more fog around spookytime
    double fogTime = 0; //from 0 to 1
    double fogTimeIncrease = 0;
    double fogIntensity = .75;
    double fogHeightIntensity = 0.5;
    public int losing = -1;
    public int lostTimer;
    public boolean allowArmogeddon = true;
    public int loseSongLength = 20*60*8;
    public int secretWaiting = -1;
    private static final int secrets = 1;
    private static final int maxSecretTime = 20*60*60*6;//6 hours
    private static final int minSecretTime = 20*60*30;//30 minutes
    public int secretTimer = rand.nextInt(maxSecretTime-minSecretTime)+minSecretTime;
    public boolean observatory;
    public int time = 0;
    private static final int dayNightCycle = 12000;
    private ArrayList<PowerNetwork> powerNetworks = new ArrayList<>();
    private ArrayList<StarlightNetwork> starlightNetworks = new ArrayList<>();
    public ArrayList<Notification> notifications = new ArrayList<>();
    public ArrayList<TaskAnimation> taskAnimations = new ArrayList<>();
    public ArrayList<Asteroid> asteroids = new ArrayList<>();
    public ArrayList<ShootingStar> shootingStars = new ArrayList<>();
    public Queue<Particle> particles = new Queue<>();
    public boolean hideSkyscrapers = false;
    public boolean showPowerNetworks = false;
    private int saveTimer = 0;
    private static final int saveInterval = 20*60;
    public ArrayList<TaskAnimation> animationsToRemove = new ArrayList<>();
    public static Theme theme = Theme.NORMAL;
    public ShieldGenerator setTarget;
    public ArrayList<ItemStack> resources = new ArrayList<>();
    public String name;
    public boolean updatePhaseMarker = true;
    public int addingIron, addingCoal, smeltingIron;
    public int furnaceOre, furnaceCoal, furnaceXP, furnaceLevel, furnaceTimer;
    public static final int maxFurnaceLevel = 2;
    private boolean tickingEnemies = false;
    public Queue<GameObject> thingsToAdd = new Queue<>();
    private final WorldGenerator worldGenerator;
    public final Story story;
    public boolean tutorial = false;
    public BoundingBox generatedBBox;//the portion of the world that has been generated
//</editor-fold>
    {
        resources.add(new ItemStack(Item.stone, 0));
        resources.add(new ItemStack(Item.coal, 0));
        resources.add(new ItemStack(Item.ironOre, 0));
        resources.add(new ItemStack(Item.ironIngot, 0));
    }
    public static Game generate(String name, int level, WorldGenerator worldGenerator, BoundingBox cityBBox, Story story, boolean tutorial){
        Game game = new Game(name, level, worldGenerator, story, tutorial);
        game.generateCity(cityBBox);
        return game;
    }
    public Game(String name, int level, WorldGenerator worldGenerator, Story story, boolean tutorial){
        this.name = name;
        this.worldGenerator = worldGenerator;
        this.level = level;
        phase = 1;
        this.story = story;
        this.tutorial = tutorial;
    }
    public synchronized void renderBackground(){
        Collections.sort(structures, (Structure o1, Structure o2) -> {
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
    }
    public void render(int millisSinceLastTick){}
    public synchronized void renderWorld(int millisSinceLastTick){
        //675x365
        int chunkWidth = 1350;
        int chunkHeight = 730;
        BoundingBox bbox = getWorldBoundingBox();
        int left = bbox.getLeft()/chunkWidth-1;
        int top = bbox.getTop()/chunkHeight-1;
        int right = bbox.getRight()/chunkWidth+1;
        int bottom = bbox.getBottom()/chunkHeight+1;
        for(int x = left; x<right; x++){
            for(int y = top; y<bottom; y++){
                drawRect(x*chunkWidth,y*chunkHeight, x*chunkWidth+chunkWidth,y*chunkHeight+chunkHeight, Game.theme.getBackgroundTexture(level));
            }
        }
        for(Structure structure : structures){
            structure.renderBackground();
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
        for(Enemy e : enemies){
            if(e instanceof EnemyAlien)mainLayer.add(e);
        }
        Collections.sort(mainLayer, (GameObject o1, GameObject o2) -> {
            if(o1==null||o2==null)return 0;
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
        for(TaskAnimation anim : taskAnimations){
            if(anim.task.isInBackground())continue;
            int index = mainLayer.indexOf(anim.task.structure);
            mainLayer.add(index+1, anim);
        }
        for(TaskAnimation anim : taskAnimations){
            if(anim.task.type==TaskType.SKYSCRAPER_ADD_FLOOR){
                anim.task.anim = anim;
            }
        }
        for(GameObject o : mainLayer){
            if(o!=null)o.draw();
        }
        if(showPowerNetworks){
            for(PowerNetwork n : powerNetworks){
                n.draw();
            }
            for(StarlightNetwork s : starlightNetworks){
                s.draw();
            }
        }
        for(Particle particle : particles){
            if(particle.air)particle.draw();
        }
        for(Drone drone : drones){
            drone.draw();
        }
        //<editor-fold defaultstate="collapsed" desc="Shields">
        for(Structure structure : structures){
            if(structure instanceof ShieldGenerator){
                ShieldGenerator gen = (ShieldGenerator) structure;
                gen.shield.render(millisSinceLastTick);
            }
        }
        //</editor-fold>
        for(Asteroid asteroid : asteroids){
            asteroid.draw();
        }
        for(ShootingStar star : shootingStars){
            star.draw();
        }
        for(Enemy e : enemies){
            if(!(e instanceof EnemyAlien))e.draw();
        }
        drawDayNightCycle();
    }
    public void click(int x, int y, int button){
        if(button==0){
            Structure structure = getMouseoverStructure(x, y);
            if(structure!=null){
                if(setTarget!=null&&structure.type.isShieldable()){
                    setTarget.setProjectorTarget(structure);
                    setTarget = null;
                }else{
                    selectedStructure = structure;
                    actionUpdateRequired = 2;
                }
            }else{
                if(setTarget!=null)setTarget = null;
                selectedStructure = null;
                actionUpdateRequired = 2;
            }
        }
        if(button==1){
            if(setTarget!=null)setTarget = null;
            selectedStructure = null;
            actionUpdateRequired = 2;
        }
    }
    public synchronized void tick(){
        cachedCityBoundingBox = null;
        BoundingBox worldBBox = getWorldBoundingBox().expand(worldGenerator.getGenerationBuffer());
        if(worldBBox.x<generatedBBox.x){
            int leftExpansion = generatedBBox.x-worldBBox.x;
            generate(new BoundingBox(worldBBox.getLeft(), generatedBBox.getTop(), leftExpansion, generatedBBox.height));
            generatedBBox = generatedBBox.expandLeft(leftExpansion);
        }
        if(worldBBox.y<generatedBBox.y){
            int topExpansion = generatedBBox.y-worldBBox.y;
            generate(new BoundingBox(generatedBBox.getLeft(), worldBBox.getTop(), generatedBBox.width, topExpansion));
            generatedBBox = generatedBBox.expandUp(topExpansion);
        }
        if(worldBBox.getRight()>generatedBBox.getRight()){
            int rightExpansion = worldBBox.getRight()-generatedBBox.getRight();
            generate(new BoundingBox(generatedBBox.getRight(), generatedBBox.getTop(), rightExpansion, generatedBBox.height));
            generatedBBox = generatedBBox.expandRight(rightExpansion);
        }
        if(worldBBox.getBottom()>generatedBBox.getBottom()){
            int bottomExpansion = worldBBox.getBottom()-generatedBBox.getBottom();
            generate(new BoundingBox(generatedBBox.getLeft(), generatedBBox.getBottom(), generatedBBox.width, bottomExpansion));
            generatedBBox = generatedBBox.expandDown(bottomExpansion);
        }
        saveTimer++;
        if(saveTimer>=saveInterval){
            save();
            saveTimer = 0;
        }
        tick++;
        time++;
        if(hideSkyscrapers){
            notifyOnce("Hiding skyscrapers (H)", 1);
        }
        if(showPowerNetworks){
            notifyOnce("Showing power networks (N)", 1);
        }
        if(cheats){
            notifyOnce("Cheats Enabled", 1);
            if(Core.debugMode)notifyOnce("Debug mode enabled");
        }
        if(time>=dayNightCycle){
            time -= dayNightCycle;
        }
        fogTime+=fogTimeIncrease;
        if(fogTime>1){
            stopFog();
        }
        if(tick%20==0){
            drawFog();
        }
        if(hasResources(new ItemStack(Item.star))){
            observatory = true;
        }
        if(canFastForward()){
            Core.speedMult = 12;
        }else{
            Core.speedMult = 1;
        }
        if(damageReportTimer>0){
            damageReportTimer--;
        }
        if(furnaceLevel==maxFurnaceLevel){
            if(furnaceCoal>=furnaceOre){
                addIronToFurnace(1);
            }else{
                addCoalToFurnace(1);
            }
        }
        if(furnaceCoal>0&&furnaceOre>0){
            furnaceTimer--;
            if(furnaceTimer<=0){
                furnaceOre--;
                furnaceCoal--;
                addResources(Item.ironIngot);
                researchEvent(new ResearchEvent(ResearchEvent.Type.GAIN_RESOURCE, Item.ironIngot, 1));
                furnaceXP++;
                smeltingIron++;
                furnaceTimer = (int)Math.pow(10, maxFurnaceLevel-furnaceLevel);
            }
        }
        for(Iterator<Structure> it = structures.iterator(); it.hasNext();){
            Structure structure = it.next();
            structure.tick();
            if(structure.dead)it.remove();
        }
        while(!structuresToReplace.isEmpty()){
            ArrayList<Structure> list = new ArrayList<>(structuresToReplace.keySet());
            Structure start = list.get(0);
            Structure end = structuresToReplace.remove(start);
            if(!structures.contains(start)){
                start.dead = true;
                continue;
            }
            structures.remove(start);
            start.dead = true;
            if(selectedStructure==start){
                selectedStructure = end;
                actionUpdateRequired = 2;
            }
            if(setTarget==start){
                if(end instanceof ShieldGenerator)setTarget = (ShieldGenerator)end;
                else setTarget = null;
            }
            for(Structure s : structures){
                if(s instanceof StructureChangeEventListener){
                    ((StructureChangeEventListener) s).onStructureChange(start, end);
                }
            }
            structures.add(end);
        }
        refreshNetworks();
        tickingEnemies = true;
        for(Iterator<Enemy> it = enemies.iterator(); it.hasNext();){
            Enemy enemy = it.next();
            enemy.tick();
            if(enemy.dead)it.remove();
        }
        tickingEnemies = false; 
        enemies.addAll(nextEnemies);
        nextEnemies.clear();
        for(Iterator<Drone> it = drones.iterator(); it.hasNext();){
            Drone drone = it.next();
            drone.tick();
            if(drone.dead)it.remove();
        }
        for(Iterator<Worker> it = workers.iterator(); it.hasNext();){
            Worker worker = it.next();
            worker.tick();
            if(worker.dead){
                notify("Death: Worker", 35);
                it.remove();
            }
        }
        for(Iterator<DroppedItem> it = droppedItems.iterator(); it.hasNext();){ 
            DroppedItem item = it.next();
            item.tick();
            if(item.dead)it.remove();
        }
        for(Iterator<Asteroid> it = asteroids.iterator(); it.hasNext();){ 
            Asteroid asteroid = it.next();
            asteroid.tick();
            if(asteroid.dead)it.remove();
        }
        for(Iterator<ShootingStar> it = shootingStars.iterator(); it.hasNext();){ 
            ShootingStar shootingStar = it.next();
            shootingStar.tick();
            if(shootingStar.dead)it.remove();
        }
        for(Iterator<Particle> it = particles.iterator(); it.hasNext();){ 
            Particle particle = it.next();
            particle.tick();
            if(particle.dead)it.remove();
        }
        for(Structure s : structures){
            if(s instanceof Laboratory){
                for(Research research : Research.values()){
                    research.tick(this);
                }
                break;
            }
        }
        for(GameObject o : thingsToAdd){
            if(o instanceof Worker){
                workers.add((Worker)o);
                continue;
            }
            throw new IllegalArgumentException("I don't know how to add that!");
        }
        thingsToAdd.clear();
        if(winTimer>0){
            winTimer--;
            if(winTimer==0){
                fading = true;
                Sounds.stopSound("music");
//                Sounds.playSoundOneChannel("music", "VictoryMusic1");
            }
        }
        if(phase<3){
            for(Structure structure : structures){
                if(structure instanceof Skyscraper){
                    ((Skyscraper) structure).pop = 0;//TODO you shouldn't have to do that...
                }
            }
        }
        if(phase>=3){
            civilianIncreasePerSec = calculatePopulation()/1000D;
            double civilianIncreasePerTick = civilianIncreasePerSec/20D;
            if(civilianIncreasePerTick>=1){
                addCivilians((int) civilianIncreasePerTick);
            }else{
                civilianTimer++;
                if(civilianTimer>=(1/civilianIncreasePerTick)){
                    addCivilians(1);
                    civilianTimer = 0;
                }
            }
        }
        workerCooldown--;
        if(workerCooldown<0&&safe()&&!lost){
            notify("Worker spawned", 50);
            addWorker();
            workerCooldown += Math.max(1200,6000-workers.size()*20);
        }
        //<editor-fold defaultstate="collapsed" desc="Armogeddon">
        if(lost){
            selectedStructure = null;
            actionUpdateRequired = 2;
            notifyOnce("Game Over", 1);
        }
//</editor-fold>
        if(meteorShower){
            notifyOnce("Meteor Shower");
            meteorShowerTimer++;
        }else{
            meteorShowerTimer--;
        }
        if(meteorShowerTimer==20*3.5){
            Sounds.fadeSound("music", "Music1");
        }
        if(meteorShower&&meteorShowerTimer>0){
            meteorShowerTimer = 0;
        }
        if(!meteorShower&&meteorShowerTimer<0){
            meteorShowerTimer = 0;
        }
        if(meteorShowerTimer==0){
            meteorShower = !meteorShower;
            if(!meteorShower){
                if(Sounds.nowPlaying()!=null&&Sounds.nowPlaying().equals("Music1")){
                    Sounds.fadeSound("music");
                }
            }
            meteorShowerTimer = (int)Math.round((meteorShower||(lost&&allowArmogeddon)?-(rand.nextInt(250)+750):rand.nextInt(2500)+7500)*meteorShowerDelayMultiplier);
        }
        if(secretWaiting==-1&&workers.size()>0){
            secretTimer--;
            if(secretTimer<=0){
                secretWaiting = rand.nextInt(secrets);
                secretTimer = rand.nextInt(maxSecretTime-minSecretTime)+minSecretTime;
            }
        }
        if(fogTime==0){
            fogTimer--;
            if(fogTimer<=0){
                startFog();
                fogTimer = rand.nextInt(maxFogTime-minFogTime)+minFogTime;
                if(theme==Theme.SNOWY)fogTimer/=fogWinter;
                if(theme==Theme.SPOOKY)fogTimer/=fogSpooky;
            }
        }
        if(meteorShowerTimer>3000){
            cloudTimer--;
            if(cloudTimer<=0){
                addCloud();
                cloudTimer = rand.nextInt(750)+500;
                if(lost){
                    cloudTimer*=1.5;
                }
            }
            if(rand.nextDouble()<.002){
                addCloud();
            }
        }
        //<editor-fold defaultstate="collapsed" desc="Meteors">
        BoundingBox box = getWorldBoundingBox();
        double intensity = METEOR_INTENSITY*(meteorShower?METEOR_SHOWER_MULT:1)*meteorManualIntensityMult*meteorPhaseIntensityMult*(rand.nextDouble()+1)*box.area()/100_00;
        meteorTimer-=intensity;
        while(meteorTimer<0){
            addAsteroid(new Asteroid(this, box.randX(rand), box.randY(rand), AsteroidMaterial.random(rand), 1));
            meteorTimer++;
        }
//</editor-fold>
        taskAnimations.removeAll(animationsToRemove);
        animationsToRemove.clear();
        //<editor-fold defaultstate="collapsed" desc="Expeditions">
        if(pendingExpedition!=null&&workers.size()>1){
            for(Worker worker : workers){
                if(worker.isAvailable()){
                    pendingExpedition.workers++;
                    worker.dead = true;
                    break;
                }
            }
            if(pendingExpedition.workers>=pendingExpedition.requiredWorkers){
                activeExpeditions.add(pendingExpedition);
                pendingExpedition = null;
            }
        }
        for (Iterator<Expedition> it = activeExpeditions.iterator(); it.hasNext();){
            Expedition e = it.next();
            e.tick();
            if(e.done){
                finishedExpeditions.add(e);
                it.remove();
            }
        }
//</editor-fold>
        for(Structure structure : structures){
            if(structure.task!=null&&structure.task.getPendingWorkers()==0){
                assignWorker(structure.task);
            }
        }
        //<editor-fold defaultstate="collapsed" desc="Phase 3">
        if(phase==3){
            enemyTimer--;
            if(enemyTimer<=0){
                addRandomEnemy();
                enemyTimer+=rand.nextInt((int)Math.max(20*60*4-Math.round(Enemy.strength*60*4),1))+20*60;
            }
            for(int i = 0; i<6; i++){
                civilianCooldown--;
                if(civilianCooldown<=0){
                    civilianCooldown = rand.nextInt(20*60*5);
                    double newCivilians = Math.min(50, Math.max(-100, rand.nextGaussian()));
                    if(newCivilians<0){
                        newCivilians*=-2;
                    }
                    while(newCivilians>0){
                        newCivilians--;
                        if(rand.nextInt(1000)>0){
                            addCivilians(1);
                        }else{
                            addWorker();
                        }
                    }
                }
            }
        }
//</editor-fold>
        //Power transfer
        getPowerNetworks();
        for(PowerNetwork network : powerNetworks){
            network.tick();
        }
        getStarlightNetworks();
        for(StarlightNetwork network : starlightNetworks){
            network.tick();
        }
        if(losing==-1){
            boolean base = false;
            for(Structure s : structures){
                if(s instanceof Base){
                    if(((Base) s).deathTick>=0)continue;
                    base = true;
                }
            }
            if(!base)losing = 0;
        }else if(canLose()){
            if(losing==0){
                Sounds.stopSound("music");
                Sounds.playSound("music", "EndMusic1");
            }
            losing++;
            if(losing>=450){
                lose();
            }
        }
        if(isPlayable()){
            //<editor-fold defaultstate="collapsed" desc="Phase 1 advancing">
            if(phase==1){
                int shieldArea = 0;
                for(Structure structure : structures){
                    if(structure instanceof ShieldGenerator){
                        ShieldGenerator shield = (ShieldGenerator) structure;
                        shieldArea += Math.PI*Math.pow(shield.shieldSize/2,2);
                    }
                }
                BoundingBox bbox = getCityBoundingBox();
                if(shieldArea>=bbox.area()*.7&&meteorShowerTimer>phaseShowerPadding){
                    phaseCounter++;
                    if(phaseCounter>=phaseTime){
                        phase(2);
                    }
                }else phaseCounter = 0;
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Phase 2 Rendering and advancing">
            if(phase==2){
                int pop = calculatePopulationCapacity();
                notify("Population Capacity: ", pop+"/"+targetPopulation+" ("+Math.round(pop/(double)targetPopulation*10000D)/100D+"%)");
                if(pop>=targetPopulation&&meteorShowerTimer>phaseShowerPadding){
                    phaseCounter++;
                    if(phaseCounter>=phaseTime){
                        phase(3);
                    }
                }else phaseCounter = 0;
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Phase 3 rendering and advancing">
            if(phase==3){
                int pop = calculatePopulation();
                int maxPop = calculatePopulationCapacity();
                notify("Population Capacity: ", maxPop+"/"+targetPopulation+" ("+Math.round(maxPop/(double)targetPopulation*10000D)/100D+"%)");
                notify("Population: ", pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
                if(pop>=targetPopulation&&maxPop>=targetPopulation&&meteorShowerTimer>phaseShowerPadding){
                    phaseCounter++;
                    if(phaseCounter>=phaseTime){
                        phase(4);
                        paused = false;
                    }
                }else phaseCounter = 0;
            }
            //</editor-fold>
        }
        for(Notification n : notifications)n.tick();
    }
    public void addIronToFurnace(int amount){
        if(amount<=0)return;
        if(hasResources(new ItemStack(Item.ironOre, amount))){
            removeResources(new ItemStack(Item.ironOre, amount));
            furnaceOre+=amount;
            researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, Item.ironOre, amount));
            addingIron+=amount;
        }else addIronToFurnace(getResources(Item.ironOre));
    }
    public void addCoalToFurnace(int amount){
        if(amount<=0)return;
        if(hasResources(new ItemStack(Item.coal, amount))){
            removeResources(new ItemStack(Item.coal, amount));
            furnaceCoal+=amount;
            researchEvent(new ResearchEvent(ResearchEvent.Type.USE_RESOURCE, Item.coal, amount));
            addingCoal+=amount;
        }else addCoalToFurnace(getResources(Item.coal));
    }
    public void win(){
        if(lost)return;
        Core.winLevel(1);
        meteorShower = false;
        meteorShowerTimer = Integer.MAX_VALUE;
        for(Enemy e : enemies){
            if(e instanceof EnemyMothership){
                e.dead = true;
            }
        }
        meteorPhaseIntensityMult = 0;
        won = true;
        Sounds.fadeSound("music", "WinMusic");
        winTimer = 20*30;
    }
    public void lose(){
        if(won)return;
        losing = -1;
        meteorShower = false;
        meteorShowerTimer = Integer.MAX_VALUE;
        new Thread(() -> {
            while(!isDestroyed()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        Sounds.fadeSound("music", "SadMusic7");//cryptic sorrow
        for(Enemy e : enemies){
            if(e instanceof EnemyMothership){
                ((EnemyMothership) e).leaving = true;
            }
        }
        lost = true;
    }
    public void addItem(DroppedItem item){
        droppedItems.add(item);
    }
    public void addResources(Item item) {
        if(actionUpdateRequired<1)actionUpdateRequired = 1;
        for(ItemStack stack : resources){
            if(stack.item==item){
                stack.count++;
                return;
            }
        }
        resources.add(new ItemStack(item, 1));
    }
    public boolean hasResources(ItemStack[] resources) {
        for(ItemStack s : resources){
            if(!hasResources(s)){
                return false;
            }
        }
        return true;
    }
    public boolean hasResources(ItemStack stack) {
        return resources.stream().anyMatch((s) -> (s.item==stack.item&&s.count>=stack.count));
    }
    public void removeResources(ItemStack[] resources) {
        for(ItemStack s : resources){
            removeResources(s);
        }
    }
    public void removeResources(ItemStack stack) {
        for(ItemStack s : resources){
            if(s.item==stack.item){
                s.count-=stack.count;
                return;
            }
        }
    }
    public int getResources(Item item){
        int count = 0;
        for(ItemStack s : resources){
            if(s.item==item){
                count+=s.count;
            }
        }
        return count;
    }
    public Particle addParticleEffect(Particle particle){
        particles.enqueue(particle);
        return particle;
    }
    public void addWorker(){
        for(Structure s : structures){
            if(s instanceof Base){
                addWorker(((Base)s).getWorkerX(), ((Base)s).getWorkerY());
                break;
            }
        }
    }
    public void replaceStructure(Structure start, Structure end){
        structuresToReplace.put(start,end);
    }
    @Deprecated
    private void textWithBackground(double left, double top, double right, double bottom, String str){
        textWithBackground(left, top, right, bottom, str, false);
    }
    @Deprecated
    private void textWithBackground(double left, double top, double right, double bottom, String str, boolean pulsing){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1); 
        if(pulsing){
            GL11.glColor4d(Math.sin(tick/5d)/4+.75, 0, 0, 1);
        }
        drawText(left,top,right,bottom, str);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Deprecated
    public void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    public void startAnim(TaskAnimated task){
        taskAnimations.add(new TaskAnimation(this, task));
    }
    public boolean safe(Worker worker) {
        if(superSafe(worker))return true;
        if(lost){
            return false;
        }
        return !meteorShower;
    }
    public boolean superSafe(Worker worker) {
        if(lost){
            return false;
        }
        boolean safe = false;
        for(Structure structure : structures){
            if(structure instanceof ShieldGenerator){
                ShieldGenerator gen = (ShieldGenerator) structure;
                if(gen.shieldSize/2-50>=Core.distance(gen, worker)){
                    safe = true;
                }
            }
        }
        return safe;
    }
    public boolean safe(){
        if(lost){
            return false;
        }
        boolean safe = !meteorShower;
        for(Structure structure : structures){
            if(structure instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) structure;
                if(sky.falling){
                    safe = false;
                }
            }
        }
        return safe;
    }
    public void expedition(int workers){
        pendingExpedition = new Expedition(this, workers);
    }
    public int calculatePopulationCapacity() {
        int pop = 0;
        for(Structure structure : structures){
            if(structure instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) structure;
                pop += sky.getMaxPop();
            }
        }
        return pop;
    }
    public int calculatePopulation(){
        int pop = 0;
        for(Structure structure : structures){
            if(structure instanceof Skyscraper){
                pop += ((Skyscraper)structure).pop;
            }
        }
        return pop;
    }
    public void save(){
        if(lost||name==null){
            return;
        }
        notify("Game Saved");
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+name+".dat");
        FileOutputStream stream = null;
        try {
            if(file.exists())file.delete();
            file.createNewFile();
            stream = new FileOutputStream(file);
        } catch (IOException ex) {
            Sys.error(ErrorLevel.severe, null, ex, ErrorCategory.fileIO);
        }
        Config config = Config.newConfig();
        config.set("tutorial", tutorial);
        config.set("level", 0);
        config.set("WorldGenerator", worldGenerator.id);
        config.set("Story", story.id);
        config.set("version", VersionManager.currentVersion);
        config.save(stream);
        config.set("meteor timer", meteorTimer);
        for(Enemy e : enemies){
            if(e instanceof EnemyMothership){
                config.set("mothership health", e.health);
            }
        }
        config.set("furnace level", furnaceLevel);
        config.set("furnace timer", furnaceTimer);
        config.set("furnace iron", furnaceOre);
        config.set("furnace coal", furnaceCoal);
        config.set("furnace xp", furnaceXP);
        config.set("enemy strength", Enemy.strength);
        config.set("enemy timer", enemyTimer);
        Config cfg = Config.newConfig();
        cfg.set("count", droppedItems.size());
        for(int i = 0; i<droppedItems.size(); i++){
            DroppedItem item = droppedItems.get(i);
            cfg.set(i+" item", item.item.name);
            cfg.set(i+" life", item.life);
            cfg.set(i+" x", item.x);
            cfg.set(i+" y", item.y);
        }
        config.set("Dropped Items", cfg);
        config.set("Meteor Shower", meteorShower);
        config.set("Meteor Shower Timer", meteorShowerTimer);
        config.set("workers", workers.size());
        cfg = Config.newConfig();
        cfg.set("count", structures.size());
        for(int i = 0; i<structures.size(); i++){
            Structure structure = structures.get(i);
            cfg.set(i+"", structure.save(Config.newConfig()));
        }
        config.set("Buildings", cfg);
        config.set("worker cooldown", workerCooldown);
        config.set("phase", phase);
        config.set("phaseCounter", phaseCounter);
        config.set("target pop", targetPopulation);
        config.set("pop per floor", popPerFloor);
        config.set("meteor shower delay", meteorShowerDelayMultiplier);
        config.set("meteor shower intensity", meteorPhaseIntensityMult);
        if(pendingExpedition!=null){
            config.set("Pending Expedition", pendingExpedition.save(Config.newConfig()));
        }
        cfg = Config.newConfig();
        cfg.set("count", activeExpeditions.size());
        for(int i = 0; i<activeExpeditions.size(); i++){
            Expedition e = activeExpeditions.get(i);
            cfg.set(i+"", e.save(Config.newConfig()));
        }
        config.set("Active Expeditions", cfg);
        cfg = Config.newConfig();
        cfg.set("count", finishedExpeditions.size());
        for(int i = 0; i<finishedExpeditions.size(); i++){
            Expedition e = finishedExpeditions.get(i);
            cfg.set(i+"", e.save(Config.newConfig()));
        }
        config.set("Finished Expeditions", cfg);
        config.set("fog timer", fogTimer);
        config.set("cloud timer", cloudTimer);
        config.set("fog time", fogTime);
        config.set("fog time increase", fogTimeIncrease);
        config.set("fog intensity", fogIntensity);
        config.set("fog height intensity", fogHeightIntensity);
        config.set("secret timer", secretTimer);
        config.set("secret waiting", secretWaiting);
        config.set("observatoryUnlocked", observatory);
        config.set("time", time);
        ConfigList researches = new ConfigList();
        for(Research r : Research.values()){
            Config research = Config.newConfig();
            research.set("name", r.name());
            research.set("research", r.save(Config.newConfig()));
            researches.add(research);
        }
        config.set("research", researches);
        Config c = Config.newConfig();
        c.set("count", resources.size());
        for(int i = 0; i<resources.size(); i++){
            ItemStack stack = resources.get(i);
            if(stack.item==null)continue;
            c.set(i+" item", stack.item.name);
            c.set(i+" count", stack.count);
        }
        config.set("Resources", c);
        config.set("losing", losing);
        Config bbox = Config.newConfig();
        bbox.set("x", generatedBBox.x);
        bbox.set("y", generatedBBox.y);
        bbox.set("width", generatedBBox.width);
        bbox.set("height", generatedBBox.height);
        config.set("generatedBBox", bbox);
        config.save(stream);
        try {
            stream.close();
        } catch (IOException ex) {
            Sys.error(ErrorLevel.severe, null, ex, ErrorCategory.fileIO);
        }
    }
    public static Game load(String save){
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+save+".dat");
        FileInputStream stream;
        try{
            stream = new FileInputStream(file);
        }catch(FileNotFoundException ex){
            return null;
        }
        Config config = Config.newConfig(stream);
        config.load();
        int level = config.get("level", 1);
        if(level==0)level = 1;
        config.load();
        try{
            stream.close();
        }catch(IOException ex){
            Sys.error(ErrorLevel.severe, null, ex, ErrorCategory.fileIO);
            return null;
        }
        Game game = new Game(save, level, WorldGenerator.getWorldGenerator(level, config.get("WorldGenerator")), Story.getStory(level, config.get("Story")), config.get("tutorial", false));
        game.meteorTimer = config.get("meteor timer", game.meteorTimer);
        int hp = config.get("mothership health", -1);
        if(hp!=-1){
            EnemyMothership ship = new EnemyMothership(game);
            ship.health = hp;
            game.enemies.add(ship);
        }
        game.furnaceLevel = config.get("furnace level", 0);
        game.furnaceTimer = config.get("furnace timer", 100);
        game.furnaceOre = config.get("furnace iron", 0);
        game.furnaceCoal = config.get("furnace coal", 0);
        game.furnaceXP = config.get("furnace xp", 0);
        Enemy.strength = config.get("enemy strength", 1d);
        game.enemyTimer = config.get("enemy timer", 20*30);
        Config cfg = config.get("Dropped Items", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            DroppedItem item = new DroppedItem(game, cfg.getInt(i+" x"), cfg.getInt(i+" y"), Item.getItemByName(cfg.get(i+" item","iron ingot")));
            item.life = cfg.get(i+" life", item.life);
            game.droppedItems.add(item);
        }
        game.meteorShower = config.get("Meteor Shower", game.meteorShower);
        game.meteorShowerTimer = config.get("Meteor Shower Timer", game.meteorShowerTimer);
        cfg = config.get("Buildings", Config.newConfig());
        HashMap<Structure, Config> structures = new HashMap<>();
        for(int i = 0; i<cfg.get("count", 0); i++){
            Config conf = cfg.get(i+"", Config.newConfig());
            Structure s = Structure.load(conf, game);
            game.structures.add(s);
            structures.put(s, conf);
        }
        for(Structure s : game.structures){
            s.postLoad(game, structures.get(s));
        }
        for(int i = 0; i<config.get("workers", 1); i++){
            game.addWorker();
        }
        game.workerCooldown = config.get("worker cooldown", game.workerCooldown);
        game.phase = config.get("phase", game.phase);
        game.phaseCounter = config.get("phaseCounter", game.phaseCounter);
        game.targetPopulation = config.get("target pop", game.targetPopulation);
        game.popPerFloor = config.get("pop per floor", game.popPerFloor);
        game.meteorShowerDelayMultiplier = config.get("meteor shower delay", game.meteorShowerDelayMultiplier);
        game.meteorPhaseIntensityMult = config.get("meteor shower intensity", game.meteorPhaseIntensityMult);
        game.pendingExpedition = Expedition.load(config.get("Pending Expedition"), game);
        cfg = config.get("Active Expeditions", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            game.activeExpeditions.add(Expedition.load(cfg.get(i+"", Config.newConfig()), game));
        }
        cfg = config.get("Finished Expeditions", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            game.finishedExpeditions.add(Expedition.load(cfg.get(i+"", Config.newConfig()), game));
        }
        game.fogTimer = config.get("fog timer", game.fogTimer);
        game.cloudTimer = config.get("cloud timer", game.cloudTimer);
        game.fogTime = config.get("fog time", game.fogTime);
        game.fogTimeIncrease = config.get("fog time increase", game.fogTimeIncrease);
        game.fogIntensity = config.get("fog intensity", game.fogIntensity);
        game.fogHeightIntensity = config.get("fog height intensity", game.fogHeightIntensity);
        game.secretTimer = config.get("secret timer", game.secretTimer);
        game.secretWaiting = config.get("secret waiting", game.secretWaiting);
        game.observatory = config.get("observatoryUnlocked", game.observatory);
        game.time = config.get("time", game.time);
        ConfigList researches = config.get("research", new ConfigList());
        for(int i = 0; i<researches.size(); i++){
            Config research = researches.get(i);
            String name = research.get("name");
            if(name==null)continue;
            Research.valueOf(name).load(research.get("research", Config.newConfig()));
        }
        game.resources.clear();
        cfg = config.get("Resources", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            Item item = Item.getItemByName(cfg.get(i+" item", ""));
            if(item==null)continue;
            game.resources.add(new ItemStack(item, cfg.get(i+" count", 0)));
        }
        game.losing = config.get("losing", game.losing);
        Config bbox = config.getConfig("generatedBBox", Config.newConfig());
        game.generatedBBox = new BoundingBox(bbox.getInt("x", 0), bbox.getInt("y", 0), bbox.getInt("width", 0), bbox.getInt("height", 0));
        return game;
    }
    public void addCivilians(int civilians){
        for(Structure s : structures){
            if(s instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) s;
                civilians = sky.addPop(civilians);
                if(civilians<=0) break;
            }
        }
    }
    public void addEnemy(Enemy e){
        if(tickingEnemies){
            nextEnemies.add(e);
        }else{
            enemies.add(e);
        }
    }
    public void addRandomEnemy(){
        if(rand.nextDouble()<(Enemy.strength)/100D){
            for(int i = 0; i<Math.min(10,Math.abs(rand.nextGaussian()*3)); i++){
                addEnemy(Enemy.randomEnemy(this));
            }
        }else{
            addEnemy(Enemy.randomEnemy(this));
        }
    }
    public static void drawRegularPolygon(double x, double y, double radius, int quality, int texture){
        if(quality<3){
            throw new IllegalArgumentException("A polygon must have at least 3 sides!");
        }
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_TRIANGLES);
        double angle = 0;
        for(int i = 0; i<quality; i++){
            GL11.glVertex2d(x, y);
            double X = x+Math.cos(Math.toRadians(angle-90))*radius;
            double Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
            angle+=(360D/quality);
            X = x+Math.cos(Math.toRadians(angle-90))*radius;
            Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
    }
    public static void drawTorus(double x, double y, double outerRadius, double innerRadius, int quality, int texture){
        if(quality<3){
            throw new IllegalArgumentException("A torus must have at least 3 sides!");
        }
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_QUADS);
        double angle = 0;
        for(int i = 0; i<quality; i++){
            double innerX = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            double innerY = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            double outerX = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            double outerY = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            GL11.glVertex2d(innerX, innerY);
            GL11.glVertex2d(outerX, outerY);
            angle+=(360D/quality);
            innerX = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            innerY = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            outerX = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            outerY = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            GL11.glVertex2d(outerX, outerY);
            GL11.glVertex2d(innerX, innerY);
        }
        GL11.glEnd();
    }
    public static void drawLaser(double x1, double y1, double x2, double y2, double size, double innerR, double innerG, double innerB, double outerR, double outerG, double outerB){
        double xDiff = x2-x1;
        double yDiff = y2-y1;
        double dist = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
        GL11.glColor4d(outerR, outerG, outerB, 1);
        for(int i = 0; i<dist; i++){
            double percent = i/dist;
            Game.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), size/2D,5,0);
        }
        GL11.glColor4d((innerR+outerR)/2, (innerG+outerG)/2, (innerB+outerB)/2, 1);
        for(int i = 0; i<dist; i++){
            double percent = i/dist;
            Game.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), (size*(2/3D))/2D,5,0);
        }
        GL11.glColor4d(innerR, innerG, innerB, 1);
        for(int i = 0; i<dist; i++){
            double percent = i/dist;
            Game.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), (size*(1/3D))/2D,5,0);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public static void drawConnector(double x1, double y1, double x2, double y2, double size, double innerR, double innerG, double innerB, double outerR, double outerG, double outerB){
        drawLaser(x1, y1, x2, y2, size, innerR, innerG, innerB, outerR, outerG, outerB);
        GL11.glColor4d(outerR,outerG,outerB,1);
        drawRegularPolygon(x1, y1, size*1.5, 5, 0);
        drawRegularPolygon(x2, y2, size*1.5, 5, 0);
        GL11.glColor4d((innerR+outerR)/2, (innerG+outerG)/2, (innerB+outerB)/2, 1);
        drawRegularPolygon(x1, y1, size, 5, 0);
        drawRegularPolygon(x2, y2, size, 5, 0);
    }
    public void damageReport(){
        damageReportTimer = damageReportTime;
    }
    public Drone addDrone(Drone drone){
        drones.add(drone);
        return drone;
    }
    public void assignAllWorkers(){
        ArrayList<Task> tasks = new ArrayList<>();
        for(Worker worker : workers){
            if(worker.task!=null){
                if(worker.task instanceof TaskDemolish||worker.task.type==TaskType.REPAIR) continue;
                tasks.add(worker.task);
            }
        }
        if(tasks.isEmpty()){
            return;
        }
        ArrayList<Worker> available = new ArrayList<>();
        for(Worker worker : workers){
            if(!worker.isWorking()){
                available.add(worker);
            }
        }
        while(!available.isEmpty()){
            for(Task task : tasks){
                available.remove(0).targetTask = task;
                if(available.isEmpty()) break;
            }
        }
    }
    public boolean isDestroyed(){
        return lost;
//        for(MenuComponentBuilding building : buildings){
//            if(building instanceof MenuComponentBase) continue;
//            if(!(building instanceof MenuComponentWreck))return false;
//        }
//        return true;
    }
    public void phase(int i){
        phase(i, true);
    }
    public void phase(int i, boolean normal){
        i = Math.min(4,i);
        phaseCounter = 0;
        phase = i;
        switch(i){
            case 2:
                meteorShowerDelayMultiplier *= 0.9;
                meteorPhaseIntensityMult *= 2.5;
                break;
            case 3:
                meteorShowerDelayMultiplier *= 0.6;
                meteorPhaseIntensityMult *= 4;
                break;
            case 4:
                meteorShower = false;
                meteorShowerTimer = Integer.MAX_VALUE;
                meteorPhaseIntensityMult = 0;
                boolean mothership = false;
                for(Enemy e : enemies){
                    if(e instanceof EnemyMothership){
                        mothership = true;
                        break;
                    }
                }
                if(!mothership&&!isDestroyed()&&isPlayable()){
                    enemies.add(new EnemyMothership(this));
                    Sounds.fadeSound("music");
                }
                break;
            default:
        }
        if(normal)updatePhaseMarker = true;
    }
    public void addCloud(){
        if(!MenuOptionsGraphics.clouds)return;
        BoundingBox worldBBox = getWorldBoundingBox();
        addCloud(worldBBox.getLeft(),worldBBox.randY(rand));
    }
    public void addCloud(int x, int y){
        if(!MenuOptionsGraphics.clouds)return;
        double rateOfChange = (rand.nextDouble()-.4)/80;
        double speed = rand.nextGaussian()/10+1;
        int min = 0;
        double mod = 1;
        switch(phase){
            case 2:
                min+=2;
                mod+=.05;
                break;
            case 3:
                min+=5;
                mod+=.1;
                break;
            case 4:
                int mothershipPhase = 0;
                for(Enemy e : enemies){
                    if(e instanceof EnemyMothership){
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership) e).phase);
                    }
                }
                if(mothershipPhase>0){
                    switch(mothershipPhase){
                        case 2:
                            min+=12;
                            mod+=.15;
                            break;
                        case 3:
                            min+=17;
                            mod+=.175;
                            break;
                        case 4:
                            min+=22;
                            mod+=.2;
                            break;
                        case 1:
                        default:
                            min+=10;
                            mod+=.125;
                            break;
                    }
                }
                break;
        }
        if(Game.theme==Theme.SPOOKY){
            min+=12;
            mod+=.15;
        }
        if(lost){
            min = Particle.rainThreshold;
            rateOfChange = Math.max(0, rateOfChange);
            speed*=.75;
        }
        double strength = rand.nextInt(42-min)+min;
        rateOfChange*=mod;
        if(won){
            strength = rand.nextInt(10);
        }
        int wide = rand.nextInt(25+(Game.theme==Theme.SPOOKY?8:(Game.theme==Theme.SNOWY?-3:0)))+5;
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
            int xx = (int)(-(X*25)-50);
            if(Math.round(X)==Math.round(X*10)/10d){
                int Y = y;
                for(int i = 0; i<height; i++){
                    addParticleEffect(new Particle(this, xx+x, y, strength, rateOfChange, speed));
                    y-=40;
                }
                y = Y;
            }else{
                int Y = y;
                for(int i = 0; i<height; i++){
                    addParticleEffect(new Particle(this, xx+x, y-20, strength, rateOfChange, speed));
                    y-=40;
                }
                y = Y;
            }
        }
    }
    public void startFog(){
        if(!MenuOptionsGraphics.fog||won)return;
        fogTime = 0;
        fogIntensity = rand.nextDouble()*.875;
        fogHeightIntensity = rand.nextDouble()*.4;
        fogTimeIncrease = (rand.nextInt(125)+25)*.000005;
        if(lost){
            fogIntensity*=1.25;
            fogHeightIntensity*=2;
            fogTimeIncrease/=2.5;
        }
        //.9 intensity MAX
    }
    public void drawFog(){
        double a = Math.sin(Math.PI*fogTime);
        double b = .5*Math.sin(Math.PI*(2*fogTime-.5))+.5;
        double height = b*fogIntensity*fogHeightIntensity;
        double density = a*fogIntensity;
        double o1 = density;
        double o2 = density*height;
        double size = ParticleFog.SIZE*.75;
        BoundingBox bbox = getWorldBoundingBox();
        int count = (int)(bbox.height/size+1);
        double xOffset = count/size;
        for(int i = 0; i<count; i++){
            addParticleEffect(new ParticleFog(this, (int)(bbox.getLeft()-size*2-xOffset*i), (int)(bbox.getTop()+i*size-50), false, o1));
            addParticleEffect(new ParticleFog(this, (int)(bbox.getLeft()-size*2-xOffset*i), (int)(bbox.getTop()+i*size-50), true, o2));
        }
    }
    private void stopFog(){
        fogTimeIncrease = fogTime = 0;
    }
    public void damage(int x, int y){
        damage(x,y,1);
    }
    public void damage(int x, int y, int damage){
        damage(x,y,damage,null);
    }
    public void damage(int x, int y, AsteroidMaterial material){
        damage(x,y,1,material);
    }
    public void damage(int x, int y, int damage, AsteroidMaterial material){
        DAMAGE:for(int i = 0; i<damage; i++){
            for(Structure structure : structures){
                if(structure instanceof ShieldGenerator){
                    ShieldGenerator shield = (ShieldGenerator) structure;
                    if(Core.distance(structure, x, y)<=shield.getShieldSize()/2){
                        if(shield.shieldHit()){
                            continue DAMAGE;
                        }else{
                            break;
                        }
                    }
                }
            }
            Structure hit = getStructure(x,y);
            if(hit==null||!hit.onHit(x,y)||material!=null&&material.forceDrop){
                //<editor-fold defaultstate="collapsed" desc="Hit ground">
                double dmgRad = 25;
                for(Worker worker : workers){
                    if(isClickWithinBounds(x, y, worker.x-dmgRad, worker.y-dmgRad, worker.x+worker.width+dmgRad, worker.y+worker.height+dmgRad)){
                        worker.damage(x,y);
                    }
                }
                for(DroppedItem item : droppedItems){
                    if(isClickWithinBounds(x, y, item.x-dmgRad, item.y-dmgRad, item.x+item.width+dmgRad,item.y+item.height+dmgRad)){
                        item.damage(x,y);
                    }
                }
                if(material!=null){
                    if(material.forceDrop){
                        addItem(new DroppedItem(this, x, y, getItem(material)));
                    }else{
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            addItem(new DroppedItem(this, x+15-25, y+8-25, getItem(material)));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            addItem(new DroppedItem(this, x+8-25, y+28-25, getItem(material)));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            addItem(new DroppedItem(this, x+38-25, y+24-25, getItem(material)));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            addItem(new DroppedItem(this, x+23-25, y+34-25, getItem(material)));
                        }
                    }
                }
//</editor-fold>
            }
        }
    }
    public Structure getStructure(int x, int y){
        Structure hit = null;
        for(Structure structure : structures){
            if(structure instanceof Skyscraper){
                if(isClickWithinBounds(x, y, structure.x, structure.y-structure.getStructureHeight()-((Skyscraper) structure).fallen, structure.x+structure.width, structure.y+structure.height-((Skyscraper) structure).fallen)){
                    hit = structure;
                }
            }else{
                if(isClickWithinBounds(x, y, structure.x, structure.y-structure.getStructureHeight(), structure.x+structure.width, structure.y+structure.height)){
                    hit = structure;
                }
            }
        }
        return hit;
    }
    public Item getItem(AsteroidMaterial material) {
        switch(material){
            case STONE:
                return Item.stone;
            case IRON:
                return Item.ironOre;
            case COAL:
                return Item.coal;
        }
        return null;
    }
    /**
     * Push particles away from a location.
     * @param x the X value
     * @param y the Y value
     * @param radius The radius of the push field
     * @param distance How far to push the particles
     */
    public void pushParticles(int x, int y, double radius, double distance, Particle.PushCause cause){
        pushParticles(x, y, radius, distance, 1, cause);
    }
    /**
     * Push particles away from a location.
     * @param x the X value
     * @param y the Y value
     * @param radius The radius of the push field
     * @param distance How far to push the particles
     * @param fadeFactor How much the particles should fade
     * @param cause The force that's pushing the particles
     */
    public void pushParticles(int x, int y, double radius, double distance, double fadeFactor, Particle.PushCause cause){
        for(Particle particle : particles){
            if(Core.distance(particle.getX(), particle.getY(), x, y)<=radius){
                double mult = 1-(Core.distance(particle.getX(), particle.getY(), x, y)/radius);
                double distX = particle.getX()-x;
                double distY = particle.getY()-y;
                double totalDist = Math.sqrt(distX*distX+distY*distY);
                distX/=totalDist;
                distY/=totalDist;
                if(Double.isNaN(distX)){
                    continue;
                }
                particle.push(distX*distance*mult,distY*distance*mult, cause);
                particle.fade(fadeFactor*mult);
            }
        }
    }
    public void addWorker(int x, int y){
        thingsToAdd.enqueue(new Worker(this,x,y));
    }
    public void playSecret(ArrayList<String> playableMusic){
        playSecret(secretWaiting, playableMusic);
        secretWaiting = -1;
    }
    private void playSecret(int secret, ArrayList<String> playableMusic){
        switch(secret){
            case 0://observatory
                if(getSunlight()>0){
                    return;
                }
                playableMusic.add("MysteryMusic3");
                addShootingStar();
                break;
        }
    }
    private void addShootingStar(){
        BoundingBox bbox = getCityBoundingBox();
        addShootingStar(new ShootingStar(this, bbox.randX(rand), bbox.randY(rand)));
    }
    public ShootingStar addShootingStar(ShootingStar star){
        shootingStars.add(star);
        return star;
    }
    public Asteroid addAsteroid(Asteroid asteroid){
        asteroids.add(asteroid);
        return asteroid;
    }
    protected void drawDayNightCycle(){
        Color noon = theme.day;
        Color night = theme.night;
        if(time>=dayNightCycle/8&&time<dayNightCycle/2-dayNightCycle/8){
            GL11.glColor4d(noon.getRed()/255d, noon.getGreen()/255d, noon.getBlue()/255d, noon.getAlpha()/255d);
        }
        if(time>=dayNightCycle/2-dayNightCycle/8&&time<dayNightCycle/2+dayNightCycle/8){
            double percent = (time-dayNightCycle/2+dayNightCycle/8)/(dayNightCycle/4d);
            double r = Core.getValueBetweenTwoValues(0, noon.getRed(), 1, night.getRed(), percent)/255d;
            double g = Core.getValueBetweenTwoValues(0, noon.getGreen(), 1, night.getGreen(), percent)/255d;
            double b = Core.getValueBetweenTwoValues(0, noon.getBlue(), 1, night.getBlue(), percent)/255d;
            double a = Core.getValueBetweenTwoValues(0, noon.getAlpha(), 1, night.getAlpha(), percent)/255d;
            GL11.glColor4d(r, g, b, a);
        }
        if(time>=dayNightCycle/2+dayNightCycle/9&&time<dayNightCycle-dayNightCycle/8){
            GL11.glColor4d(night.getRed()/255d, night.getGreen()/255d, night.getBlue()/255d, night.getAlpha()/255d);
        }
        if(time<dayNightCycle/8||time>=dayNightCycle-dayNightCycle/8){
            double newTime = time+dayNightCycle/8;
            if(newTime>=dayNightCycle){
                newTime-=dayNightCycle;
            }
            double percent = newTime/(dayNightCycle/4d);
            double r = Core.getValueBetweenTwoValues(0, night.getRed(), 1, noon.getRed(), percent)/255d;
            double g = Core.getValueBetweenTwoValues(0, night.getGreen(), 1, noon.getGreen(), percent)/255d;
            double b = Core.getValueBetweenTwoValues(0, night.getBlue(), 1, noon.getBlue(), percent)/255d;
            double a = Core.getValueBetweenTwoValues(0, night.getAlpha(), 1, noon.getAlpha(), percent)/255d;
            GL11.glColor4d(r, g, b, a);
        }
        BoundingBox bbox = getWorldBoundingBox();
        drawRect(bbox.getLeft(), bbox.getTop(), bbox.getRight(), bbox.getBottom(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    private Worker getAvailableWorker(int x, int y){
        Worker closest = null;
        double distance = Double.MAX_VALUE;
        for(Worker worker : workers){
            if(!worker.isWorking()){
                if(Core.distance(worker, x, y)<distance){
                    closest = worker;
                    distance = Core.distance(worker, x, y);
                }
            }
        }
        return closest;
    }
    /**
     * @return the amount of sunlight, 0 - 1
     */
    public double getSunlight(){
        if(time>=dayNightCycle/8&&time<dayNightCycle/2-dayNightCycle/8){
            return 1;
        }
        if(time>=dayNightCycle/2-dayNightCycle/8&&time<dayNightCycle/2+dayNightCycle/8){
            double percent = (time-dayNightCycle/2+dayNightCycle/8)/(dayNightCycle/4d);
            return 1-percent;
        }
        if(time>=dayNightCycle/2+dayNightCycle/9&&time<dayNightCycle-dayNightCycle/8){
            return 0;
        }
        if(time<dayNightCycle/8||time>=dayNightCycle-dayNightCycle/8){
            double newTime = time+dayNightCycle/8;
            if(newTime>=dayNightCycle){
                newTime-=dayNightCycle;
            }
            double percent = newTime/(dayNightCycle/4d);
            return percent;
        }
        return -1;
    }
    private ArrayList<PowerNetwork> getPowerNetworks(){
        if(powerNetworks.isEmpty()){
            ArrayList<Structure> possibilities = new ArrayList<>();
            possibilities.addAll(structures);
            boolean added = true;
            while(added){
                PowerNetwork network = null;
                added = false;
                for(Structure b : possibilities){
                    network = PowerNetwork.detect(structures, b);
                    if(network!=null){
                        powerNetworks.add(network);
                        added = true;
                        break;
                    }
                }
                if(network!=null){
                    possibilities.removeAll(network.supply);
                    possibilities.removeAll(network.demand);
                }
            }
        }
        return powerNetworks;
    }
    private ArrayList<StarlightNetwork> getStarlightNetworks(){
        if(starlightNetworks.isEmpty()){
            ArrayList<Structure> possibilities = new ArrayList<>();
            possibilities.addAll(structures);
            boolean added = true;
            while(added){
                StarlightNetwork network = null;
                added = false;
                for(Structure s : possibilities){
                    network = StarlightNetwork.detect(structures, s);
                    if(network!=null){
                        starlightNetworks.add(network);
                        added = true;
                        break;
                    }
                }
                if(network!=null){
                    possibilities.removeAll(network.supply);
                    possibilities.removeAll(network.demand);
                }
            }
        }
        return starlightNetworks;
    }
    public void notify(String notification, int time){
        for(Notification not : notifications){
            if(not.name.equals(notification)&&!not.isDying()){
                not.add();
                not.time = Math.max(not.time, time);
                return;
            }
        }
        notifications.add(new Notification(notification, time));
    }
    public void notify(String notification, String value, int time){
        for(Notification not : notifications){
            if(not.name.startsWith(notification)&&!not.isDying()){
                not.name = notification+value;
                not.time = Math.max(not.time, time);
                return;
            }
        }
        notifications.add(new Notification(notification+value, time));
    }
    public void notify(String notification, String value){
        notify(notification, value, 15);
    }
    public void notify(String notification){
        notify(notification, 15);
    }
    public void notifyOnce(String notification, int time){
        for(Notification not : notifications){
            if(not.name.equals(notification)&&!not.isDying()){
                not.time = Math.max(not.time, time);
                return;
            }
        }
        notifications.add(new Notification(notification, time));
    }
    public void notifyOnce(String notification){
        notifyOnce(notification, 15);
    }
    public void refreshNetworks(){
        powerNetworks.clear();
        starlightNetworks.clear();
    }
    private void assignWorker(Task task){
        for(Worker worker : workers){
            if(worker.isAvailable()){
                worker.task(task);
                return;
            }
        }
    }
    public void researchEvent(ResearchEvent event){
        for(Research research : Research.values()){
            research.event(event);
        }
    }
    public Structure getMouseoverStructure(double x, double y){
        Structure hit = null;
        for(Structure structure : structures){
            int h = structure.getStructureHeight();
            if(structure instanceof Skyscraper&&hideSkyscrapers)h = 0;
            if(structure instanceof Skyscraper){
                if(isClickWithinBounds(x, y, structure.x, structure.y-h-((Skyscraper) structure).fallen, structure.x+structure.width, structure.y+structure.height-((Skyscraper) structure).fallen)){
                    hit = structure;
                }
            }else{
                if(isClickWithinBounds(x, y, structure.x, structure.y-h, structure.x+structure.width, structure.y+structure.height)){
                    hit = structure;
                }
            }
        }
        return hit;
    }
    public boolean canLose(){
        return isPlayable();
    }
    public ArrayList<String> getDebugData() {
        ArrayList<String> debug = new ArrayList<>();
        debug.clear();
        debug.add("Level "+level);
        debug.add("Version "+VersionManager.currentVersion);
        debug.add("Furnace XP: "+furnaceXP);
        debug.add("Enemy Strength: "+Enemy.strength);
        debug.add("Enemy Timer: "+enemyTimer);
        debug.add("Dropped items: "+droppedItems.size());
        HashMap<Item, Integer> items = new HashMap<>();
        for(DroppedItem item : droppedItems){
            if(items.containsKey(item.item)){
                items.put(item.item, items.get(item.item)+1);
            }else{
                items.put(item.item, 1);
            }
        }
        for(Item item : items.keySet()){
            int amount = items.get(item);
            debug.add(" - "+amount+" "+item.name+" ("+Math.round(amount/(double)droppedItems.size()*100)+"%)");
        }
        debug.add("Meteor timer: "+meteorTimer);
        debug.add("Meteor Shower: "+meteorShower);
        debug.add("Meteor Shower Timer: "+meteorShowerTimer);
        debug.add("Workers: "+workers.size());
        for(int i = 0; i<workers.size(); i++){
            Worker w = workers.get(i);
            if(w.targetItem!=null)debug.add("Worker "+(i+1)+": Targeted item");
            if(w.grabbedItem!=null)debug.add("Worker "+(i+1)+": Grabbed item");
            if(w.targetTask!=null)debug.add("Worker "+(i+1)+": Targeted task");
            if(w.task!=null)debug.add("Worker "+(i+1)+": Working");
        }
        debug.add("Worker Cooldown: "+workerCooldown);
        debug.add("Total Structures: "+structures.size());
        HashMap<StructureType, Integer> theStructures = new HashMap<>();
        for(Structure structure : structures){
            if(theStructures.containsKey(structure.type)){
                theStructures.put(structure.type, theStructures.get(structure.type)+1);
            }else{
                theStructures.put(structure.type, 1);
            }
        }
        debug.add("Structures: "+structures.size());
        for(StructureType structure : theStructures.keySet()){
            int amount = theStructures.get(structure);
            debug.add(" - "+amount+" "+structure.getDisplayName()+" ("+Math.round(amount/(double)structures.size()*100)+"%)");
        }
        if(selectedStructure!=null){
            debug.add("Selected structure: ");
            ArrayList<String> data = new ArrayList<>();
            selectedStructure.getDebugInfo(data);
            for(String s : data){
                debug.add(" - "+s);
            }
        }
        debug.add("Phase: "+phase);
        debug.add("Target population: "+targetPopulation);
        debug.add("Population per floor: "+popPerFloor);
        debug.add("Meteor Shower Delay Multiplier: "+meteorShowerDelayMultiplier);
        debug.add("Meteor Phase Intensity Multiplier: "+meteorPhaseIntensityMult);
        debug.add("Meteor Manual Intensity Multiplier: "+meteorManualIntensityMult);
        if(pendingExpedition!=null){
            debug.add("Pending Expedition: "+pendingExpedition.toString());
        }
        debug.add("Active Expeditions: "+activeExpeditions.size());
        for(Expedition e : activeExpeditions){
            debug.add(" - "+e.toString());
        }
        debug.add("Finished Expeditions: "+finishedExpeditions.size());
        for(Expedition e : finishedExpeditions){
            debug.add(" - "+e.toString());
        }
        debug.add("Particles: "+particles.size());
        debug.add("Fog Timer: "+fogTimer);
        debug.add("Cloud Timer: "+cloudTimer);
        debug.add("Fog time: "+fogTime);
        debug.add("Fog time increase: "+fogTimeIncrease);
        debug.add("Fog intensity: "+fogIntensity);
        debug.add("Fog Height intensity: "+fogHeightIntensity);
        debug.add("Super secret timer: "+secretTimer);
        debug.add("Save timer: "+(saveInterval-saveTimer));
        if(secretWaiting!=-1)debug.add("Pending Secret: "+secretWaiting);
        debug.add("Time: "+time);
        return debug;
    }
    private boolean canFastForward(){
        if(!workers.isEmpty())return false;
        if(lost)return false;
        if(losing>-1)return false;
        if(won)return false;
        for(Enemy e : enemies){
            if(e instanceof EnemyMothership){
                return false;
            }
        }
        return true;
    }
    private void generateCity(BoundingBox bbox){
        worldGenerator.generateCity(this, bbox);
        //<editor-fold defaultstate="collapsed" desc="Calculating Population per floor">
        if(popPerFloor==-1){
            int floorCount = 0;
            for(Structure structure : structures){
                if(structure instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)structure;
                    floorCount+=sky.floorCount;
                }
            }
            double d = rand.nextDouble()/10+.7;
            targetPopulation = (int) Math.round(100*floorCount*d);
            popPerFloor = (int) Math.round(targetPopulation/(double)floorCount);
        }
        //</editor-fold>
        addWorker();
        generatedBBox = bbox;
    }
    private void generate(BoundingBox boundingBox){
        worldGenerator.generate(this, boundingBox);
    }
    public double getXGamePadding(){
        return Math.max(Core.helper.displayWidth()/MenuGame.minZoom/4, getCityBoundingBox().width/2);
    }
    public double getYGamePadding(){
        return Math.max(Core.helper.displayHeight()/MenuGame.minZoom/4, getCityBoundingBox().height/2);
    }
    public static enum Theme{
        NORMAL("normal", new Color(255, 216, 0, 32),new Color(22, 36, 114, 72), new Color(255, 255, 255, 255)),
        SNOWY("snowy", new Color(255, 244, 179, 15),new Color(20, 33, 107, 72), new Color(31, 31, 31, 255)),
        SPOOKY("normal", new Color(255, 200, 0, 32),new Color(16, 27, 86, 72), new Color(255, 252, 250, 255));
        private final String texture;
        private final Color day;
        private final Color night;
        private final Color text;
        private Theme(String texture, Color day, Color night, Color text){
            this.texture = texture;
            this.day = day;
            this.night = night;
            this.text = text;
        }
        public String tex(){
            return texture;
        }
        public int getBackgroundTexture(int level){
            return ImageStash.instance.getTexture(getBackgroundTextureS(level));
        }
        public String getBackgroundTextureS(int level){
            return "/textures/background/level "+level+"/"+texture+".png";
        }
        public void applyTextColor(){
            GL11.glColor4d(text.getRed()/255d, text.getGreen()/255d, text.getBlue()/255d, text.getAlpha()/255d);
        }
    }
    public static void refreshTheme(){
        GregorianCalendar calendar = new GregorianCalendar();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Game.theme = Game.Theme.NORMAL;
        if(MenuOptionsGraphics.theme==2){
            Game.theme = Game.Theme.SNOWY;
        }
        if(MenuOptionsGraphics.theme==0&&(month==0||month==11)){
            Game.theme = Game.Theme.SNOWY;
        }
        if(Game.theme==Game.Theme.NORMAL&&month==9&&day>=25){
            Game.theme = Game.Theme.SPOOKY;
        }
    }
    public boolean isPlayable(){
        return !(won||lost);
    }
    public ArrayList<Action> getActions(MenuGame menu){
        ArrayList<Action> actions = new ArrayList<>();
        if(selectedStructure!=null){
            if(selectedStructure.type.isDamagable()){
                actions.add(new Action("Repair", new TaskRepair(selectedStructure)));
                actions.add(new Action("Repair All", new TaskRepairAll(selectedStructure)));
            }
            if(selectedStructure.type.getMaxLevel()>1){
                if(selectedStructure.canUpgrade()){
                    actions.add(new Action("Upgrade", new TaskUpgrade(selectedStructure)));
                }else{
                    actions.add(new Action("Maxed", null, () -> {
                        return false;
                    }));
                }
            }
            ArrayList<Upgrade> upgrades = selectedStructure.getAvailableUpgrades();
            if(!upgrades.isEmpty()){
                actions.add(new Action(5));
                for(Upgrade upgrade : upgrades){
                    actions.add(new Action("Install "+upgrade.toString(), new TaskSpecialUpgrade(selectedStructure, upgrade)));
                }
                actions.add(new Action(5));
            }
            selectedStructure.getActions(menu, actions);
            if(selectedStructure instanceof StructureDemolishable){
                actions.add(new Action("Demolish", new TaskDemolish(selectedStructure)).setImportant());
            }
            if(selectedStructure.task!=null){
                actions.add(new Action("Add Worker", () -> {
                    if(selectedStructure==null)return;
                    Worker worker = getAvailableWorker(selectedStructure.x+selectedStructure.width/2, selectedStructure.y+selectedStructure.height/2);
                    if(worker==null)return;
                    worker.targetTask = selectedStructure.task;
                }, () -> {
                    return getAvailableWorker(selectedStructure.x+selectedStructure.width/2, selectedStructure.y+selectedStructure.height/2)!=null;
                }));
                actions.add(new Action("Cancel Task", () -> {
                    selectedStructure.task.cancel();
                }, () -> {
                    return true;
                }).setImportant());
            }
        }
        return actions;
    }
    private BoundingBox cachedCityBoundingBox = null;
    public BoundingBox getCityBoundingBox(){
        if(cachedCityBoundingBox!=null)return cachedCityBoundingBox;
        if(structures.isEmpty())return new BoundingBox(0, 0, 0, 0);
        ArrayList<Structure> buildings = new ArrayList<>();
        for(Structure s : structures){
            if(!s.type.isDecoration())buildings.add(s);
        }
        return cachedCityBoundingBox = BoundingBox.enclosing(buildings).expand(100);
    } 
    public BoundingBox getWorldBoundingBox(){
        BoundingBox cityBBox = getCityBoundingBox();
        return new BoundingBox(cityBBox.x-(int)getXGamePadding(), cityBBox.y-(int)getYGamePadding(), cityBBox.width+(int)getXGamePadding()*2, cityBBox.height+(int)getYGamePadding()*2);
    } 
}