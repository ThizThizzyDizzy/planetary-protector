package planetaryprotector.menu;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.Expedition;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.Main;
import planetaryprotector.Sounds;
import planetaryprotector.VersionManager;
import planetaryprotector.menu.component.MenuComponentActionButton;
import planetaryprotector.menu.component.MenuComponentClickable;
import planetaryprotector.friendly.Drone;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.menu.component.MenuComponentFalling;
import planetaryprotector.menu.component.MenuComponentFurnace;
import planetaryprotector.menu.component.MenuComponentRising;
import planetaryprotector.friendly.Worker;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.particle.Particle;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.MenuComponentEnemy;
import planetaryprotector.building.task.TaskSkyscraperAddFloor;
import planetaryprotector.building.task.MenuComponentTaskAnimation;
import planetaryprotector.building.task.TaskDemolish;
import planetaryprotector.building.task.TaskWreckClean;
import planetaryprotector.building.task.TaskRepair;
import planetaryprotector.building.task.TaskRepairAll;
import planetaryprotector.building.task.TaskType;
import planetaryprotector.building.task.Task;
import planetaryprotector.building.task.TaskUpgrade;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.building.Mine;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.Building;
import planetaryprotector.building.Building.Upgrade;
import planetaryprotector.building.CoalGenerator;
import planetaryprotector.building.Bunker;
import planetaryprotector.building.IllegalBuildingException;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.Silo;
import planetaryprotector.building.Base;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.ParticleFog;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.building.BuildingDamagable;
import planetaryprotector.building.BuildingDemolishable;
import planetaryprotector.building.Observatory;
import planetaryprotector.building.PowerStorage;
import planetaryprotector.building.SolarGenerator;
import planetaryprotector.building.Workshop;
import planetaryprotector.building.PowerNetwork;
import planetaryprotector.building.StarlightNetwork;
import planetaryprotector.building.task.TaskSpecialUpgrade;
import planetaryprotector.building.task.TaskTrainWorker;
import planetaryprotector.particle.ParticleEffectType;
import simplelibrary.config2.Config;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuGame extends Menu{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    public static final int actionButtonWidth = 350;
    public ArrayList<DroppedItem> droppedItems = new ArrayList<>();
    public Base base;
    public static Random rand = new Random();
    public ArrayList<DroppedItem> itemsToDrop = new ArrayList<>();
    public ArrayList<MenuComponent> componentsToRemove = new ArrayList<>();
    public boolean baseGUI = true;
    private MenuComponentFurnace furnace;
    public ArrayList<MenuComponent> componentsToAdd = new ArrayList<>();
    public boolean lost = false;
    public boolean meteorShower;
    public MenuComponentClickable ironChunks;
    public MenuComponentClickable coal;
    public int meteorShowerTimer = 100;
    public int actionButtonOffset = 0;
    public ArrayList<Worker> workers = new ArrayList<>();
    public ArrayList<EnemyAlien> aliens = new ArrayList<>();
    public ArrayList<Building> buildings = new ArrayList<>();
    public ArrayList<MenuComponentActionButton> actionButtons = new ArrayList<>();
    public Worker selectedWorker;
    public Building selectedBuilding;
    private boolean actionUpdateRequired = false;
    HashMap<Building, Building> buildingsToReplace = new HashMap<>();
    private Building oldSelectedBuilding;
    private Task oldSelectedTask;
    private Task selectedTask;
    public boolean paused;
    public int workerCooldown = 1020;
    public MenuComponent overlay;
    private MenuComponent phaseMarker;
    public int phase;
    int targetPopulation = 8_000_000;
    public int popPerFloor = -1;
    private double meteorShowerDelayMultiplier = 1;
    private double meteorShowerIntensityMultiplier = 1;
    Expedition pendingExpedition;
    ArrayList<Expedition> activeExpeditions = new ArrayList<>();
    public ArrayList<Expedition> finishedExpeditions = new ArrayList<>();
    public ArrayList<MenuComponentEnemy> enemies = new ArrayList<>();
    public ArrayList<Drone> drones = new ArrayList<>();
    private int enemyTimer = 20*30;
    private double damageReportTimer = 0;
    private double damageReportTime = 20*10;
    public EnemyMothership mothership;
    public int civilianCooldown = MenuGame.rand.nextInt(20*60*5);
    public double civilianIncreasePerSec = 0;
    public int civilianTimer = 0;
    private boolean autoTask = true;
    public boolean doNotDisturb = false;
    private int winTimer = -1;
    boolean fading;
    double blackScreenOpacity = 0;
    public boolean cheats = false;
    public static int tick;
    public boolean won;
    private int cloudTimer = rand.nextInt(750)+500;
    //FOG
    private static final int maxFogTime = 20*60*15;
    private static final int minFogTime = 20*60*5;
    private int fogTimer = rand.nextInt(maxFogTime-minFogTime)+minFogTime;
    double fogTime = 0; //from 0 to 1
    double fogTimeIncrease = 0;
    double fogIntensity = .75;
    double fogHeightIntensity = 0.5;
    public boolean losing = false;
    private ArrayList<String> debugData = new ArrayList<>();
    private int lostTimer;
    private boolean allowArmogeddon = true;
    private int loseSongLength = 20*60*8;
    public int secretWaiting = -1;
    private int secrets = 1;
    private static final int maxSecretTime = 20*60*60*12;
    private static final int minSecretTime = 20*60*60;
    public int secretTimer = rand.nextInt(maxSecretTime-minSecretTime)+minSecretTime;
    public boolean observatory;
    public int time = 0;
    private static final int dayNightCycle = 12000;
    private ArrayList<PowerNetwork> powerNetworks = new ArrayList<>();
    private ArrayList<StarlightNetwork> starlightNetworks = new ArrayList<>();
    public ArrayList<Notification> notifications = new ArrayList<>();
    public ArrayList<MenuComponentTaskAnimation> taskAnimations = new ArrayList<>();
    public ArrayList<Asteroid> asteroids = new ArrayList<>();
    public ArrayList<Particle> particles = new ArrayList<>();
    public boolean hideSkyscrapers = false;
    public boolean showPowerNetworks = false;
//</editor-fold>
    public MenuGame(GUI gui){
        super(gui, null);
        AsteroidMaterial.resetTimers();
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        phase = 1;
    }
    public MenuGame(GUI gui, Menu parent){
        this(gui,parent,new Base(rand.nextInt(Display.getWidth()-100), rand.nextInt(Display.getHeight()-100)));
    }
    public MenuGame(GUI gui, Menu parent, Base base){
        super(gui,parent);
        AsteroidMaterial.resetTimers();
        int buildingCount = (Display.getWidth()/100)*(Display.getHeight()/100);
        this.base = base;
        buildings.add(base);
        for(int i = 0; i<buildingCount; i++){
            Building building = Building.generateRandomBuilding(base, buildings);
            if(building==null){
                continue;
            }
            buildings.add(building);
        }
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        addWorker();
        phase = 1;
    }
    public MenuGame(GUI gui, MenuGame game, int phase){
        this(gui, game, game.base, game.buildings, phase);
    }
    public MenuGame(GUI gui, Menu parent, Base base, ArrayList<Building> buildings) {
        this(gui, parent, base, buildings, 1);
    }
    public MenuGame(GUI gui, Menu parent, Base base, ArrayList<Building> buildings, int phase) {
        super(gui,parent);
        AsteroidMaterial.resetTimers();
        this.base = base;
        this.buildings = new ArrayList<>(buildings);
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        addWorker();
        this.phase = phase;
    }
    @Override
    public void onGUIOpened(){
        paused = true;
        phaseMarker = add(new MenuComponent(0, 0, Display.getWidth(), Display.getHeight()) {
            private double opacity = 0;
            private boolean opacitizing = true;
            @Override
            public void mouseEvent(double x, double y, int button, boolean isDown) {
                opacitizing = false;
                paused = false;
            }
            @Override
            public void render(){
                if(doNotDisturb||!paused){
                    opacitizing = false;
                }
                if(opacitizing){
                    opacity+=0.01;
                    if(opacity>1){
                        opacity = 1;
                    }
                }else{
                    opacity-=0.01;
                    if(opacity<=0){
                        x = Display.getWidth();
                        return;
                    }
                }
                if(phase>3)return;
                GL11.glColor4d(1, 1, 1, opacity);
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/phase/"+phase+".png"));
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        //<editor-fold defaultstate="collapsed" desc="Calculating Population per floor">
        if(popPerFloor==-1){
            int floorCount = 0;
            for(Building building : buildings){
                if(building instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)building;
                    floorCount+=sky.floorCount;
                }
            }
            double d = rand.nextDouble()/10+.7;
            targetPopulation = (int) Math.round(100*floorCount*d);
            popPerFloor = (int) Math.round(targetPopulation/(double)floorCount);
            meteorShowerIntensityMultiplier = Display.getWidth()*Display.getHeight()/1024000d;
        }
        //</editor-fold>
    }
    @Override
    public void renderBackground(){
        //<editor-fold defaultstate="collapsed" desc="BAD - Sorting all components">
        synchronized(components){
            Collections.sort(components, new Comparator<MenuComponent>(){
                @Override
                public int compare(MenuComponent o1, MenuComponent o2){
                    double y1 = o1.y;
                    double y2 = o2.y;
                    double height1 = o1.height;
                    double height2 = o2.height;
//                    if(o1 instanceof ZComponent){
//                        y1 += Display.getHeight()*((ZComponent)o1).getZ();
//                    }else{
                        if(o1==overlay){
                            y1 += Display.getHeight()*50;
                        }
                        if(o1 instanceof MenuComponentActionButton){
                            y1 += Display.getHeight()*10;
                        }
                        for(Worker worker : workers){
                            if(o1==worker.button){
                                y1 += Display.getHeight()*10;
                            }
                        }
                        if(o1 instanceof MenuComponentFalling||o1 instanceof MenuComponentRising){
                            y1 += Display.getHeight()*9;
                        }
                        if(o1==mothership){
                            y1 += Display.getHeight()*7;
                        }
                        if(o1 instanceof MenuComponentEnemy){
                            y1 += Display.getHeight()*4;
                        }
                        if(o1==furnace){
                            y1 += Display.getHeight()*2;
                        }
//                    }
//                    if(o2 instanceof ZComponent){
//                        y2 += Display.getHeight()*((ZComponent)o2).getZ();
//                    }else{
                        if(o2==overlay){
                            y2 += Display.getHeight()*50;
                        }
                        for(Worker worker : workers){
                            if(o2==worker.button){
                                y2 += Display.getHeight()*10;
                            }
                        }
                        if(o2 instanceof MenuComponentActionButton){
                            y2 += Display.getHeight()*10;
                        }
                        if(o2 instanceof MenuComponentFalling||o2 instanceof MenuComponentRising){
                            y2 += Display.getHeight()*9;
                        }
                        if(o2==mothership){
                            y2 += Display.getHeight()*7;
                        }
                        if(o2 instanceof MenuComponentEnemy){
                            y2 += Display.getHeight()*4;
                        }
                        if(o2==furnace){
                            y2 += Display.getHeight()*2;
                        }
//                    }
                    y1 += height1/2;
                    y2 += height2/2;
                    return (int) Math.round(y1-y2);
                }
            });
        }
//</editor-fold>
        synchronized(buildings){
            for(Building building : buildings){
                building.mouseover = 0;
            }
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
        }
        //<editor-fold defaultstate="collapsed" desc="BaseGUI location">
        if(baseGUI){
            coal.x = ironChunks.x = furnace.x = Display.getWidth()-100;
            for(Worker worker : workers){
                worker.button.x = 0;
                worker.button.color = (!(worker.dead||worker.isWorking()))?Color.WHITE:Color.RED;
                worker.button.enabled = !(worker.dead||worker.isWorking());
            }
            for(MenuComponentActionButton button : actionButtons){
                button.x = 50;
            }
        }else{
            coal.x = ironChunks.x = furnace.x = -100;
            for(Worker worker : workers){
                worker.button.x = -50;
            }
            for(MenuComponentActionButton button : actionButtons){
                button.x = 0;
            }
        }
//</editor-fold>
        Building building = getBuilding(Mouse.getX(), Display.getHeight()-Mouse.getY());
        if(building!=null){
            building.mouseover = .1;
        }
        if(selectedBuilding!=null){
            selectedBuilding.mouseover+=.2;
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        if(baseGUI){
            drawRect(Display.getWidth()-100, Display.getHeight()-200, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/sidebar.png"));
        }
        super.render(millisSinceLastTick);
        if(Mouse.isButtonDown(1)&&selectedWorker!=null){
            selectedWorker.selectedTarget = new double[]{Mouse.getX(),Display.getHeight()-Mouse.getY()};
        }
        //<editor-fold defaultstate="collapsed" desc="Damage Report">
        if(damageReportTimer>=0){
            for(Building b : buildings){
                if(b.damages.size()>10) continue;
                GL11.glColor4d(1, 0, 0, (damageReportTimer/damageReportTime)*(b.damages.size()/10D));
                if(b.type==BuildingType.WRECK){
                    GL11.glColor4d(1, 0, 0, 1*(damageReportTimer/damageReportTime));
                }
                drawRect(b.x, b.y, b.x+b.width, b.y+b.height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
//</editor-fold>
        if(!won&&!lost){
            //<editor-fold defaultstate="collapsed" desc="Phase 1 advancing">
            if(phase==1){
                int shieldArea = 0;
                double maxSize = 0;
                for(Building building : buildings){
                    if(building instanceof ShieldGenerator){
                        ShieldGenerator shield = (ShieldGenerator) building;
                        shieldArea += Math.PI*Math.pow(shield.shieldSize*.5,2);
                        maxSize = Math.max(maxSize, shield.shieldSize);
                    }
                }
                if(shieldArea>=Display.getWidth()*Display.getHeight()*.7||maxSize>Display.getWidth()){
                    phase(2);
                }
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Phase 2 Rendering and advancing">
            if(phase==2){
                int pop = calculatePopulationCapacity();
                notify("Population Capacity: ", pop+"/"+targetPopulation+" ("+Math.round(pop/(double)targetPopulation*10000D)/100D+"%)");
//                centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population Capacity: "+pop+"/"+targetPopulation+" ("+Math.round(pop/(double)targetPopulation*10000D)/100D+"%)");
                if(pop>=targetPopulation){
                    phase(3);
                }
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Phase 3 rendering and advancing">
            if(phase==3){
                int pop = calculatePopulation();
                int maxPop = calculatePopulationCapacity();
                notify("Population Capacity: ", maxPop+"/"+targetPopulation+" ("+Math.round(maxPop/(double)targetPopulation*10000D)/100D+"%)");
                notify("Population: ", pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
//                if(maxPop<targetPopulation){
//                    centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population Capacity: "+maxPop+"/"+targetPopulation+" ("+Math.round(maxPop/(double)targetPopulation*10000D)/100D+"%)");
//                    centeredTextWithBackground(0, 50, Display.getWidth(), 100, "Population: "+pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
//                }else{
//                    centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population: "+pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
//                }
                if(pop>=targetPopulation&&maxPop>=targetPopulation){
                    phase(4);
                    paused = false;
                }
            }
            //</editor-fold>
            if(selectedBuilding!=null){
                String upgrades = "";
                for(Upgrade u : selectedBuilding.getBoughtUpgrades())upgrades+="*";
                textWithBackground(baseGUI?50:0, 0, (baseGUI?50:0)+actionButtonWidth, 20, upgrades+" "+selectedBuilding.getName());
            }
            drawText(furnace.x+10, Display.getHeight()-60, Display.getWidth()-10, Display.getHeight()-40, furnace.ironOre+" Iron");
            drawText(furnace.x+10, Display.getHeight()-40, Display.getWidth()-10, Display.getHeight()-20, furnace.coal+" Coal");
            drawText(furnace.x+10, Display.getHeight()-20, Display.getWidth()-10, Display.getHeight(), furnace.level>=MenuComponentFurnace.maxLevel?"Maxed":"Level "+(furnace.level+1));
            if(baseGUI){
                for(int i = 0; i<base.resources.size(); i++){
                    int I = 1;
                    if(i==0)I = 0;
                    if(i==base.resources.size()-1)I = 2;
                    drawRect(Display.getWidth()-100, i*20, Display.getWidth(), (i+1)*20+(I==2?5:0), ImageStash.instance.getTexture("/textures/gui/sidebar "+I+".png"));
                    drawText(Display.getWidth()-80, i*20, Display.getWidth(), (i+1)*20, base.resources.get(i).count+"");
                    drawRect(Display.getWidth()-100, i*20, Display.getWidth()-80, (i+1)*20, ImageStash.instance.getTexture("/textures/items/"+base.resources.get(i).item.texture+".png"));
                }
            }
        }else if(won){
            if(phase>0){
                notifications.clear();
                centeredTextWithBackground(0, 0, Display.getWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                if(winTimer<20&&Sounds.nowPlaying().equals("VictoryMusic1")){
                    centeredTextWithBackground(0, 35, Display.getWidth(), 85, "Only one problem remains...");
                }
            }
        }
        if(selectedBuilding==null&&actionButtons.size()>0){
            componentsToRemove.addAll(actionButtons);
            actionButtons.clear();
        }
        if(selectedBuilding==null){
            selectedTask = null;
        }else{
            selectedTask = selectedBuilding.task;
        }
        //<editor-fold defaultstate="collapsed" desc="Replacing Action Buttons">
        if(oldSelectedBuilding!=selectedBuilding||oldSelectedTask!=selectedTask||actionUpdateRequired){
            actionUpdateRequired = false;
            componentsToRemove.addAll(actionButtons);
            actionButtons.clear();
            actionButtonOffset = 20;
            if(selectedBuilding!=null){
                if(selectedBuilding instanceof BuildingDamagable){
                    taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                    taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                }
                if(selectedBuilding.getMaxLevel()>-1){
                    if(selectedBuilding.canUpgrade()){
                        taskButton("Upgrade", selectedWorker, new TaskUpgrade(selectedBuilding));
                    }else{
                        action("Maxed", false, null);
                    }
                }
                ArrayList<Upgrade> upgrades = selectedBuilding.getAvailableUpgrades();
                if(!upgrades.isEmpty()){
                    actionButtonOffset+=5;
                    for(Upgrade upgrade : upgrades){
                        taskButton("Install "+upgrade.toString(), selectedWorker, new TaskSpecialUpgrade(selectedBuilding, upgrade));
                    }
                    actionButtonOffset+=5;
                }
                switch(selectedBuilding.type){
                    case SKYSCRAPER:
                        taskButton("Add Floor", selectedWorker, new TaskSkyscraperAddFloor((Skyscraper)selectedBuilding));
                        taskButton("Add 10 Floors", selectedWorker, new TaskSkyscraperAddFloor((Skyscraper)selectedBuilding,10));
                        break;
                    case WORKSHOP:
                        taskButton("Train Worker", selectedWorker, new TaskTrainWorker((Workshop)selectedBuilding));
                        break;
                    case OBSERVATORY:
                        action("Add Star", hasResources(new ItemStack(Item.star))&&((Observatory)selectedBuilding).canAddStar(), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(hasResources(new ItemStack(Item.star))){
                            if(((Observatory)selectedBuilding).addStar()){
                                removeResources(new ItemStack(Item.star));
                            }
                        }
                    }
                });
                        action("Toggle Scan", true, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((Observatory)selectedBuilding).toggleScan();
                    }
                });
                        break;
                    case BUNKER:
                        break;
                    case SILO:
                        action("Toggle Power Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((Silo)selectedBuilding).outline = !((Silo)selectedBuilding).outline;
                            }
                        });
                        action("Build Missile", ((Silo)selectedBuilding).canBuildMissile(), new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent ae){
                                ((Silo)selectedBuilding).buildMissile();
                            }
                        }, ((Silo)selectedBuilding).missileCost());
                        if(selectedBuilding.getLevel()>1){
                            action("Build Drone", ((Silo)selectedBuilding).canBuildDrone(), new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){
                                    ((Silo)selectedBuilding).buildDrone();
                                }
                            }, ((Silo)selectedBuilding).droneCost());
                        }
                        action("Fire Missiles", ((Silo)selectedBuilding).canLaunchMissile(), new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent ae){
                                ((Silo)selectedBuilding).launchMissile();
                            }
                        });
                        break;
                    case SOLAR_GENERATOR:
                        break;
                    case COAL_GENERATOR:
                        action("Add Coal", hasResources(new ItemStack(Item.coal)), new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                removeResources(new ItemStack(Item.coal));
                                ((CoalGenerator)selectedBuilding).coal++;
                            }
                        });
                        action((((CoalGenerator)selectedBuilding).autoFuel?"Disable":"Enable")+" Auto-fueling", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((CoalGenerator)selectedBuilding).autoFuel = !((CoalGenerator)selectedBuilding).autoFuel;
                            }
                        });
                        break;
                    case POWER_STORAGE:
                        PowerStorage s = (PowerStorage)selectedBuilding;
                        action((s.charge?"Disable":"Enable")+" Charging", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                s.charge = !s.charge;
                            }
                        });
                        action((s.discharge?"Disable":"Enable")+" Discharging", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                s.discharge = !s.discharge;
                            }
                        });
                        break;
                    case MINE:
                        break;
                    case SHIELD_GENERATOR:
                        action("Toggle Shield Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((ShieldGenerator)selectedBuilding).shieldOutline = !((ShieldGenerator)selectedBuilding).shieldOutline;
                            }
                        });
                        action("Toggle Power Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((ShieldGenerator)selectedBuilding).powerOutline = !((ShieldGenerator)selectedBuilding).powerOutline;
                            }
                        });
                        if(phase>=3&&((ShieldGenerator)selectedBuilding).canBlast){
                            action("Blast", ((ShieldGenerator)selectedBuilding).blastRecharge==0, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ((ShieldGenerator)selectedBuilding).blast();
                                }
                            });
                        }
                        break;
                    case WRECK:
                        taskButton("Clean up", selectedWorker, new TaskWreckClean((Wreck) selectedBuilding));
                        break;
                    case BASE:
                        action("Assign All Workers", true, (ActionEvent e) -> {
                            assignAllWorkers();
                        });
                        action((autoTask?"Disable":"Enable")+" Autotasking", true, (ActionEvent e) -> {
                            autoTask = !autoTask;
                        });
                        if(phase>=2){
                            action("Damage Report", true, (ActionEvent e) -> {
                                damageReport();
                            });
                        }
                        if(phase>=3){
                            action("Expeditions", workers.size()>1, (ActionEvent ae) -> {
                                if(overlay!=null)return;
                                overlay = add(new MenuExpedition(this));
                            });
                        }
                        break;
//                    case LABORATORY://TODO research
//                            action("Research", true, (ActionEvent ae) -> {
//                                if(overlay!=null)return;
//                                overlay = add(new MenuResearch(this));
//                            });
//                        break;
                    case EMPTY:
                        taskButton("Build Bunker", selectedWorker, new TaskConstruct(selectedBuilding, new Bunker(selectedBuilding.x, selectedBuilding.y)));
                        if(phase>=3){
                            taskButton("Build Silo", selectedWorker, new TaskConstruct(selectedBuilding, new Silo(selectedBuilding.x, selectedBuilding.y)));
                        }
                        taskButton("Build Skyscraper", selectedWorker, new TaskConstruct(selectedBuilding, new Skyscraper(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Mine", selectedWorker, new TaskConstruct(selectedBuilding, new Mine(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Solar Generator", selectedWorker, new TaskConstruct(selectedBuilding, new SolarGenerator(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Coal Generator", selectedWorker, new TaskConstruct(selectedBuilding, new CoalGenerator(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Power Storage", selectedWorker, new TaskConstruct(selectedBuilding, new PowerStorage(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Shield Generator", selectedWorker, new TaskConstruct(selectedBuilding, new ShieldGenerator(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Workshop", selectedWorker, new TaskConstruct(selectedBuilding, new Workshop(selectedBuilding.x, selectedBuilding.y)));
//                        taskButton("Build Laboratory", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentLaboratory(selectedBuilding.x, selectedBuilding.y)));
                        if(observatory){
                            taskButton("Build Observatory", selectedWorker, new TaskConstruct(selectedBuilding, new Observatory(selectedBuilding.x, selectedBuilding.y)));
                        }
                        break;
                    default:
                        throw new IllegalBuildingException(selectedBuilding.type);
                }
                if(selectedBuilding instanceof BuildingDemolishable){
                    taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                }
                if(selectedBuilding.task!=null&&selectedWorker!=null&&selectedWorker.task==null){
                    action("Continue Task", true, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker.task = selectedBuilding.task;
                        }
                    });
                }
                if(selectedBuilding.task!=null){
                    action("Add Worker", getAvailableWorker(selectedBuilding.x+selectedBuilding.width/2, selectedBuilding.y+selectedBuilding.height/2)!=null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(selectedBuilding==null)return;
                            Worker worker = getAvailableWorker(selectedBuilding.x+selectedBuilding.width/2, selectedBuilding.y+selectedBuilding.height/2);
                            if(worker==null)return;
                            worker.targetTask = selectedBuilding.task;
                        }
                    });
                    action("Cancel Task", true, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e){
                            selectedWorker.cancelTask();
                        }
                    });
                }
            }
        }
//</editor-fold>
        oldSelectedBuilding = selectedBuilding;
        oldSelectedTask = selectedTask;
        if(selectedBuilding!=null&&selectedBuilding.task!=null){
            for(int i = 0; i<selectedBuilding.task.getDetails().length; i++){
                textWithBackground((baseGUI?50:0)+actionButtonWidth, 30*i, Display.getWidth(), 30*(i+1), selectedBuilding.task.getDetails()[i], selectedBuilding.task.important);
            }
        }
        if(Core.debugMode&&cheats){
            debugYOffset = 0;
            double textHeight = Display.getHeight()/debugData.size();
            for(String str : debugData){
                debugText(textHeight, str);
            }
        }
        int offset = 0;
        for(Iterator<Notification> it = notifications.iterator(); it.hasNext();){
            Notification n = it.next();
            double wide = FontManager.getLengthForStringWithHeight(n.toString(), 20);
            double left = Display.getWidth()/2-(wide/2*n.width);
            double right = Display.getWidth()/2+(wide/2*n.width);
            int y = 20-n.height;
            GL11.glColor4d(0,0,0,.5);
            drawRectWithBounds(Display.getWidth()/2-wide/2, offset-y/2, Display.getWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, 0);
            GL11.glColor4d(1,1,1,1);
            drawCenteredTextWithBounds(Display.getWidth()/2-wide/2, offset-y/2, Display.getWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, n.toString());
            offset+=n.height;
            if(n.isDead())it.remove();
        }
        if(paused){
            drawCenteredText(0, Display.getHeight()/2-50, Display.getWidth(), Display.getHeight()/2+50, "Paused");
        }
        //<editor-fold defaultstate="collapsed" desc="BAD - Phase marker Re-rendering">
        if(phaseMarker!=null){
            phaseMarker.render();
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BAD - Overlay Re-rendering">
        if(overlay!=null){
            overlay.render();
        }
        //</editor-fold>
        renderForeground();
    }
    public void renderWorld(int millisSinceLastTick){
        drawRect(0,0,Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/gui/menuBackground.png"));
        synchronized(buildings){
            for(Building building : buildings){
                building.renderBackground();
            }
        }
        for(MenuComponentTaskAnimation anim : taskAnimations){
            anim.render();
        }
        ArrayList<Particle> groundParticles = new ArrayList<>();
        synchronized(particles){
            for(Particle particle : particles){
                if(!particle.air)groundParticles.add(particle);
            }
        }
        ArrayList<GameObject> mainLayer = new ArrayList<>();
        mainLayer.addAll(groundParticles);
        mainLayer.addAll(buildings);
        mainLayer.addAll(droppedItems);
        mainLayer.addAll(workers);
        Collections.sort(mainLayer, new Comparator<GameObject>(){
            @Override
            public int compare(GameObject o1, GameObject o2){
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
            }
        });
        for(GameObject o : mainLayer){
            if(o!=null)o.render();
        }
        if(showPowerNetworks){
            synchronized(powerNetworks){
                for(PowerNetwork n : powerNetworks){
                    n.draw();
                }
            }
            synchronized(starlightNetworks){
                for(StarlightNetwork s : starlightNetworks){
                    s.draw();
                }
            }
        }
        synchronized(particles){
            for(Particle particle : particles){
                if(particle.air)particle.render();
            }
        }
        synchronized(drones){
            for(Drone drone : drones){
                drone.render();
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
        drawDayNightCycle();
    }
    @Override
    public void renderForeground(){
        GL11.glColor4d(0, 0, 0, blackScreenOpacity);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Controls.deselect&&pressed&&!repeat&&selectedWorker!=null){
            selectedWorker = null;
            selectedBuilding = null;
        }
        if(key==Controls.cancel&&pressed&&!repeat){
            if(selectedWorker!=null&&selectedWorker.task!=null){
                selectedWorker.cancelTask();
            }
        }
        if(key==Controls.hideSkyscrapers&&pressed&&!repeat){
            hideSkyscrapers = !hideSkyscrapers;
        }
        if(key==Controls.showPowerNetworks&&pressed&&!repeat){
            showPowerNetworks = !showPowerNetworks;
        }
        if(key==Controls.menu&&pressed&&!repeat){
            if(overlay!=null)return;
            overlay = add(new MenuIngame(this));
            paused = true;
        }
        if(key==Controls.pause&&pressed&&!repeat){
            paused = !paused;
        }
        if(key==Controls.mute&&pressed&&!repeat){
            Sounds.vol = 1-Sounds.vol;
            if(Sounds.vol<.01){
                notify("Sound ", "Off", 50);
            }else{
                notify("Sound ", "On", 50);
            }
        }
        if(key==Controls.cheat&&pressed&&!repeat){
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                cheats = !cheats;
            }
        }
        if(cheats&&pressed){
            if(key==Controls.CHEAT_LOSE&&!repeat){
                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                    notify("Cheat: Losing Epilogue");
                    if(Core.gui.menu==this)gui.open(new MenuLost(gui, this));
                }
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_LMENU)&&Keyboard.isKeyDown(Controls.CHEAT_SECRET)){
                if(key==Keyboard.KEY_1){
                    notify("Cheat: Secret #1");
                    secretWaiting = 0;
                }
                if(key==Keyboard.KEY_2){
                    notify("Cheat: Secret #2");
                    secretWaiting = 1;
                }
                if(key==Keyboard.KEY_3){
                    notify("Cheat: Secret #3");
                    secretWaiting = 2;
                }
                if(key==Keyboard.KEY_4){
                    notify("Cheat: Secret #4");
                    secretWaiting = 3;
                }
                if(key==Keyboard.KEY_5){
                    notify("Cheat: Secret #5");
                    secretWaiting = 4;
                }
                if(key==Keyboard.KEY_6){
                    notify("Cheat: Secret #6");
                    secretWaiting = 5;
                }
                if(key==Keyboard.KEY_7){
                    notify("Cheat: Secret #7");
                    secretWaiting = 6;
                }
                if(key==Keyboard.KEY_8){
                    notify("Cheat: Secret #8");
                    secretWaiting = 7;
                }
                if(key==Keyboard.KEY_9){
                    notify("Cheat: Secret #9");
                    secretWaiting = 8;
                }
                if(key==Keyboard.KEY_0){
                    notify("Cheat: Secret #10");
                    secretWaiting = 9;
                }
            }
            if(key==Controls.CHEAT_PHASE&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                int oldPhase = phase;
                phase(phase+1);
                if(phase!=oldPhase){
                    notify("Cheat: Advance to phase "+phase);
                }
                paused = false;
            }
            if(key==Controls.CHEAT_RESOURCES){
                notify("Cheat: Resources");
                for(ItemStack resource : base.resources){
                    resource.count+=100;
                }
            }
            if(key==Controls.CHEAT_CLOUD){
                notify("Cheat: Cloud");
                addCloud(Mouse.getX(), Display.getHeight()-Mouse.getY());
            }
            if(key==Controls.CHEAT_FOG){
                notify("Cheat: Fog");
                startFog();
            }
            if(!actionButtons.isEmpty()&&selectedBuilding!=null&&selectedBuilding.task==null){
                if(key==Keyboard.KEY_1){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(0).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_2&&actionButtons.size()>1){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(1).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_3&&actionButtons.size()>2){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(2).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_4&&actionButtons.size()>3){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(3).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_5&&actionButtons.size()>4){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(4).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_6&&actionButtons.size()>5){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(5).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_7&&actionButtons.size()>6){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(6).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_8&&actionButtons.size()>7){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(7).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_9&&actionButtons.size()>8){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(8).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_0&&actionButtons.size()>9){
                    Building selectedBuilding = this.selectedBuilding;
                    actionButtons.get(9).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        notify("Cheat: Instant Completion");
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
            }
            if(key==Controls.CHEAT_WORKER){
                notify("Cheat: Add Worker");
                addWorker();
            }
            if(key==Controls.CHEAT_ENEMY){
                if(Keyboard.isKeyDown(Controls.CHEAT_SECRET)&&Keyboard.isKeyDown(Keyboard.KEY_1)){
                    notify("Cheat: Shooting Star");
                    int X = Mouse.getX()-25;
                    int Y = Display.getHeight()-Mouse.getY()-25;
                    addAsteroid(new Asteroid(X, Y, AsteroidMaterial.SHOOTING_STAR, 2));
                }else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                    MenuComponentEnemy.strength++;
                    notify("Cheat: Enemy Strength: ", MenuComponentEnemy.strength+"");
                }else{
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                        notify("Cheat: Add Enemy");
                        addEnemy();
                    }else{
                        notify("Cheat: Spawn Asteroid");
                        int X = Mouse.getX()-25;
                        int Y = Display.getHeight()-Mouse.getY()-25;
                        switch(rand.nextInt(3)){
                            case 0:
                                addAsteroid(new Asteroid(X, Y, AsteroidMaterial.COAL, 1));
                                break;
                            case 1:
                                addAsteroid(new Asteroid(X, Y, AsteroidMaterial.STONE, 1));
                                break;
                            case 2:
                                addAsteroid(new Asteroid(X, Y, AsteroidMaterial.IRON, 1));
                                break;
                        }
                    }
                }
            }
            if(key==Controls.CHEAT_PEACE){
                notify("Cheat: Disable Meteor shower");
                if(mothership!=null){
                    notify("Cheat: Damage Mothership");
                    mothership.health-=mothership.maxHealth/4;
                }
                meteorShower = false;
                meteorShowerTimer += 20*60*60;
            }
        }else if(pressed){
            if(!actionButtons.isEmpty()&&selectedBuilding!=null&&selectedBuilding.task==null){
                if(key==Keyboard.KEY_1&&actionButtons.get(0).enabled&&!actionButtons.get(0).label.contains("Demolish")){
                    actionButtons.get(0).actionPerformed(null);
                }
                if(key==Keyboard.KEY_2&&actionButtons.size()>1&&actionButtons.get(1).enabled&&!actionButtons.get(1).label.contains("Demolish")){
                    actionButtons.get(1).actionPerformed(null);
                }
                if(key==Keyboard.KEY_3&&actionButtons.size()>2&&actionButtons.get(2).enabled&&!actionButtons.get(2).label.contains("Demolish")){
                    actionButtons.get(2).actionPerformed(null);
                }
                if(key==Keyboard.KEY_4&&actionButtons.size()>3&&actionButtons.get(3).enabled&&!actionButtons.get(3).label.contains("Demolish")){
                    actionButtons.get(3).actionPerformed(null);
                }
                if(key==Keyboard.KEY_5&&actionButtons.size()>4&&actionButtons.get(4).enabled&&!actionButtons.get(4).label.contains("Demolish")){
                    actionButtons.get(4).actionPerformed(null);
                }
                if(key==Keyboard.KEY_6&&actionButtons.size()>5&&actionButtons.get(5).enabled&&!actionButtons.get(5).label.contains("Demolish")){
                    actionButtons.get(5).actionPerformed(null);
                }
                if(key==Keyboard.KEY_7&&actionButtons.size()>6&&actionButtons.get(6).enabled&&!actionButtons.get(6).label.contains("Demolish")){
                    actionButtons.get(6).actionPerformed(null);
                }
                if(key==Keyboard.KEY_8&&actionButtons.size()>7&&actionButtons.get(7).enabled&&!actionButtons.get(7).label.contains("Demolish")){
                    actionButtons.get(7).actionPerformed(null);
                }
                if(key==Keyboard.KEY_9&&actionButtons.size()>8&&actionButtons.get(8).enabled&&!actionButtons.get(8).label.contains("Demolish")){
                    actionButtons.get(8).actionPerformed(null);
                }
                if(key==Keyboard.KEY_0&&actionButtons.size()>9&&actionButtons.get(9).enabled&&!actionButtons.get(9).label.contains("Demolish")){
                    actionButtons.get(9).actionPerformed(null);
                }
            }else{
                if(key==Keyboard.KEY_1&&!workers.isEmpty()){
                    selectedWorker = workers.get(0);
                }
                if(key==Keyboard.KEY_2&&workers.size()>1){
                    selectedWorker = workers.get(1);
                }
                if(key==Keyboard.KEY_3&&workers.size()>2){
                    selectedWorker = workers.get(2);
                }
                if(key==Keyboard.KEY_4&&workers.size()>3){
                    selectedWorker = workers.get(3);
                }
                if(key==Keyboard.KEY_5&&workers.size()>4){
                    selectedWorker = workers.get(4);
                }
                if(key==Keyboard.KEY_6&&workers.size()>5){
                    selectedWorker = workers.get(5);
                }
                if(key==Keyboard.KEY_7&&workers.size()>6){
                    selectedWorker = workers.get(6);
                }
                if(key==Keyboard.KEY_8&&workers.size()>7){
                    selectedWorker = workers.get(7);
                }
                if(key==Keyboard.KEY_9&&workers.size()>8){
                    selectedWorker = workers.get(8);
                }
                if(key==Keyboard.KEY_0&&workers.size()>9){
                    selectedWorker = workers.get(9);
                }
            }
        }
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentButton){
                if(Core.isPointWithinComponent(x, y, c))return;
            }
        }
        if(pressed&&button==0){
            Building building = getBuilding(x, y);
            if(building!=null){
                selectedBuilding = building;
                selectedWorker = null;
            }
        }
        if(pressed&&button==1){
            if(selectedWorker==null){
                selectedBuilding = null;
            }
        }
    }
    @Override
    public void tick(){
        if(fading){
            blackScreenOpacity+=0.01;
        }
        //<editor-fold defaultstate="collapsed" desc="Ticking Overlays when paused">
        if(overlay!=null){
            overlay.tick();
        }
//</editor-fold>
        if(paused){
            return;
        }
        //<editor-fold defaultstate="collapsed" desc="Debug Data">
        debugData.clear();
        debugData.add("Level: 0");
        debugData.add("Version: "+VersionManager.currentVersion);
        if(mothership!=null){
            debugData.add("Mothership Health: "+mothership.health+"/"+mothership.maxHealth+" ("+Math.round(mothership.health/(double)mothership.maxHealth*100)+"%)");
        }
        debugData.add("Furnace XP: "+furnace.total);
        debugData.add("Enemy Strength: "+MenuComponentEnemy.strength);
        debugData.add("Enemy Timer: "+enemyTimer);
        debugData.add("Dropped items: "+droppedItems.size());
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
            debugData.add(" - "+amount+" "+item.name+" ("+Math.round(amount/(double)droppedItems.size()*100)+"%)");
        }
        debugData.add("Asteroid materials: "+AsteroidMaterial.values().length);
        for(AsteroidMaterial mat : AsteroidMaterial.values()){
            debugData.add(" 0 "+mat.toString()+": "+mat.timer);
        }
        debugData.add("Meteor Shower: "+meteorShower);
        debugData.add("Meteor Shower Timer: "+meteorShowerTimer);
        debugData.add("Workers: "+workers.size());
        debugData.add("Buildings: "+buildings.size());
        HashMap<BuildingType, Integer> theBuildings = new HashMap<>();
        for(Building building : buildings){
            if(theBuildings.containsKey(building.type)){
                theBuildings.put(building.type, theBuildings.get(building.type)+1);
            }else{
                theBuildings.put(building.type, 1);
            }
        }
        for(BuildingType building : theBuildings.keySet()){
            int amount = theBuildings.get(building);
            debugData.add(" - "+amount+" "+building.name+" ("+Math.round(amount/(double)buildings.size()*100)+"%)");
        }
        debugData.add("Worker Cooldown: "+workerCooldown);
        debugData.add("Phase: "+phase);
        debugData.add("Target population: "+targetPopulation);
        debugData.add("Population per floor: "+popPerFloor);
        debugData.add("Meteor Shower Delay Multiplier: "+meteorShowerDelayMultiplier);
        debugData.add("Meteor Shower Intensity Multiplier: "+meteorShowerIntensityMultiplier);
        if(pendingExpedition!=null){
            debugData.add("Pending Expedition: "+pendingExpedition.toString());
        }
        debugData.add("Active Expeditions: "+activeExpeditions.size());
        for(Expedition e : activeExpeditions){
            debugData.add(" - "+e.toString());
        }
        debugData.add("Finished Expeditions: "+finishedExpeditions.size());
        for(Expedition e : finishedExpeditions){
            debugData.add(" - "+e.toString());
        }
        debugData.add("Fog Timer: "+fogTimer);
        debugData.add("Cloud Timer: "+cloudTimer);
        debugData.add("Fog time: "+fogTime);
        debugData.add("Fog time increase: "+fogTimeIncrease);
        debugData.add("Fog intensity: "+fogIntensity);
        debugData.add("Fog Height intensity: "+fogHeightIntensity);
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Post-lose epilogue loading">
        if(lost&&phase>3){
            lostTimer++;
            if(lostTimer>loseSongLength/10){
                if(Sounds.songTimer()<loseSongLength/20){
                    allowArmogeddon = false;
                    for(Particle particle : particles){
                        particle.strength-=.1;
                    }
                    if(lostTimer>loseSongLength/20+20*5){
                        if(Core.gui.menu==this)gui.open(new MenuLost(gui, this));
                    }
                }
            }
        }
//</editor-fold>
        tick++;
        time++;
        if(hideSkyscrapers){
            notifyOnce("Hiding skyscrapers", 1);
        }
        if(showPowerNetworks){
            notifyOnce("Showing power networks", 1);
        }
        if(cheats){
            notifyOnce("Cheats Enabled", 1);
        }
        for(Notification n : notifications)n.tick();
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
        if(selectedWorker!=null&&selectedWorker.dead){
            selectedWorker = null;
            selectedBuilding = null;
        }
        if(workers.isEmpty()&&!lost&&!losing){
            Core.speedMult = 12;
        }else{
            Core.speedMult = 1;
        }
        if(damageReportTimer>=0){
            damageReportTimer--;
        }
        while(!buildingsToReplace.isEmpty()){
            ArrayList<Building> list = new ArrayList<>(buildingsToReplace.keySet());
            Building start = list.get(0);
            Building end = buildingsToReplace.remove(start);
            if(selectedBuilding==start)selectedBuilding = end;
            buildings.remove(start);
            buildings.add(end);
            refreshNetworks();
        }
        synchronized(buildings){
            for(Building building : buildings){
                building.tick();
            }
        }
        synchronized(drones){
            for(Iterator<Drone> it = drones.iterator(); it.hasNext();){
                Drone drone = it.next();
                drone.tick();
                if(drone.dead)it.remove();
            }
        }
        synchronized(workers){
            for(Iterator<Worker> it = workers.iterator(); it.hasNext();){
                Worker worker = it.next();
                worker.tick();
                if(worker.dead){
                    notify("Death: Worker", 35);
                    components.remove(worker.button);
                    it.remove();
                }
            }
        }
        for(int i = 0; i<workers.size(); i++){//relocating worker buttons
            Worker worker = workers.get(i);
            worker.button.y = i*50;
            worker.button.label = (i+1)+"";
        }
        synchronized(droppedItems){
            for(Iterator<DroppedItem> it = droppedItems.iterator(); it.hasNext();){ 
                DroppedItem item = it.next();
                item.tick();
                if(item.dead)it.remove();
            }
        }
        synchronized(asteroids){
            for(Iterator<Asteroid> it = asteroids.iterator(); it.hasNext();){ 
                Asteroid asteroid = it.next();
                asteroid.tick();
                if(asteroid.dead)it.remove();
            }
        }
        synchronized(particles){
            for(Iterator<Particle> it = particles.iterator(); it.hasNext();){ 
                Particle particle = it.next();
                particle.tick();
                if(particle.dead)it.remove();
            }
        }
        if(blackScreenOpacity>=1){
            MenuEpilogue g = new MenuEpilogue(gui);
            g.blackScreenOpacity = 1;
            g.fading = false;
            gui.open(g);
        }
        if(winTimer>0){
            winTimer--;
            if(winTimer==0){
                fading = true;
                Sounds.stopSound("music");
                Sounds.playSoundOneChannel("music", "VictoryMusic1");
            }
        }
        if(phase<3){
            for(Building building : buildings){
                if(building instanceof Skyscraper){
                    ((Skyscraper) building).pop = 0;
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
        if(lost&&allowArmogeddon){
            selectedBuilding = null;
            notifyOnce("Game Over", 1);
            for(int i = 0; i<2; i++){
                switch(rand.nextInt(3)){
                    case 0:
                        addAsteroid(new Asteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.COAL, 1));
                        break;
                    case 1:
                        addAsteroid(new Asteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.STONE, 1));
                        break;
                    case 2:
                        addAsteroid(new Asteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.IRON, 1));
                        break;
                }
            }
        }
        if(lost){
            baseGUI = false;
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
            if(meteorShower){
                for(AsteroidMaterial m : AsteroidMaterial.values()){
                    if(m.timer>=Integer.MAX_VALUE)continue;
                    m.timer = 0;
                }
            }else{
                if(Sounds.nowPlaying()!=null&&Sounds.nowPlaying().equals("Music1")){
                    Sounds.fadeSound("music");
                }
            }
            meteorShowerTimer = (int)Math.round((meteorShower?-(rand.nextInt(250)+750):rand.nextInt(2500)+7500)*meteorShowerDelayMultiplier);
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
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            if(m.timer>=Integer.MAX_VALUE)continue;
            m.timer--;
            if(m.timer<=0){
                addAsteroid(new Asteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), m, 1));
                m.timer = (int)Math.round(((rand.nextInt(m.max-m.min)+m.min)/(meteorShower?50:1))/meteorShowerIntensityMultiplier);
            }
        }
        if(!itemsToDrop.isEmpty()){
            droppedItems.addAll(itemsToDrop);
            itemsToDrop.clear();
        }
        while(!componentsToRemove.isEmpty()){
            if(componentsToRemove.get(0) instanceof MenuComponentTaskAnimation){
                taskAnimations.remove(componentsToRemove.remove(0));
                continue;
            }
            if(componentsToRemove.get(0) instanceof EnemyAlien){
                aliens.remove(componentsToRemove.get(0));
            }
            if(componentsToRemove.get(0) instanceof MenuComponentEnemy){
                enemies.remove(componentsToRemove.get(0));
            }
            components.remove(componentsToRemove.remove(0));
        }
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
        addAll(componentsToAdd);
        componentsToAdd.clear();
        if(autoTask){
            synchronized(buildings){
                for(Building building : buildings){
                    if(building.task!=null&&building.task.getPendingWorkers()==0){
                        assignWorker(building.task);
                    }
                }
            }
        }
        for(EnemyAlien a : aliens){
            if(a.y+a.width>Display.getHeight()){
                a.y = Display.getHeight()-a.height;
            }
            if(a.x+a.height>Display.getWidth()){
                a.x = Display.getWidth()-a.width;
            }
            if(a.x<0){
                a.x=0;
            }
            if(a.y<0){
                a.y=0;
            }
        }
        for(Building b : buildings){
            if(b instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) b;
                sky.isSelectedWorkerBehind = selectedWorker!=null&&selectedWorker.x>=sky.x&&selectedWorker.x<=sky.x+sky.width&&selectedWorker.y<=sky.y+sky.height/2&&selectedWorker.y>=sky.y-(sky.floorCount*sky.floorHeight);
            }
        }
        //<editor-fold defaultstate="collapsed" desc="Phase 3">
        if(phase==3){
            enemyTimer--;
            if(enemyTimer<=0){
                addEnemy();
                enemyTimer+=rand.nextInt((int)Math.max(20*60*4-Math.round(MenuComponentEnemy.strength*60*4),1))+20*60;
            }
            for(int i = 0; i<6; i++){
                civilianCooldown--;
                if(civilianCooldown<=0){
                    civilianCooldown = MenuGame.rand.nextInt(20*60*5);
                    double newCivilians = Math.min(50, Math.max(-100, MenuGame.rand.nextGaussian()));
                    if(newCivilians<0){
                        newCivilians*=-2;
                    }
                    while(newCivilians>0){
                        newCivilians--;
                        if(MenuGame.rand.nextInt(1000)>0){
                            addCivilians(1);
                        }else{
                            addWorker();
                        }
                    }
                }
            }
        }
//</editor-fold>
        DO:do{
            for(Building building : buildings){
                Skyscraper sky = null;
                if(building.type==BuildingType.SKYSCRAPER){
                    sky = (Skyscraper) building;
                }
                if(selectedWorker!=null){
                    if(Keyboard.isKeyDown(Controls.up))continue;
                    if(Keyboard.isKeyDown(Controls.down))continue;
                    if(Keyboard.isKeyDown(Controls.right))continue;
                    if(Keyboard.isKeyDown(Controls.left))continue;
                    if(selectedWorker.selectedTarget!=null)continue;
                    if(Core.isClickWithinBounds(selectedWorker.x+selectedWorker.width/2, selectedWorker.y+selectedWorker.height/2, building.x, building.y-(sky==null?0:sky.fallen), building.x+building.width, building.y+building.height-(sky==null?0:sky.fallen))){
                        selectedBuilding = building;
                        break DO;
                    }
                }
            }
            if(selectedWorker!=null)selectedBuilding = null;
        }while(false);
        //Power transfer
        getPowerNetworks();
        synchronized(powerNetworks){
            for(PowerNetwork network : powerNetworks){
                network.tick();
            }
        }
        getStarlightNetworks();
        synchronized(starlightNetworks){
            for(StarlightNetwork network : starlightNetworks){
                network.tick();
            }
        }
        super.tick();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==coal){
            addFuel();
        }else if(button==ironChunks){
            addIron();
        }
        if(button==furnace){
            furnace.upgrade();
        }
        for(Worker worker : workers){
            if(button==worker.button){
                selectedWorker = worker;
            }
        }
        for(MenuComponentActionButton actionButton : actionButtons){
            if(button==actionButton){
                actionButton.actionPerformed(null);
                oldSelectedBuilding = null;
                oldSelectedTask = null;
            }
        }
    }
    private void addIron(){
        addIron((int)Math.pow(10, furnace.level));
    }
    private void addFuel(){
        addFuel((int)Math.pow(10, furnace.level));
    }
    public void addIron(int amount){
        if(hasResources(new ItemStack(Item.ironOre, amount))){
            removeResources(new ItemStack(Item.ironOre, amount));
            furnace.ironOre+=amount;
            componentsToAdd.add(new MenuComponentFalling(Display.getWidth()-60, Display.getHeight()-150, Item.ironOre));
        }
    }
    public void addFuel(int amount){
        if(hasResources(new ItemStack(Item.coal, amount))){
            removeResources(new ItemStack(Item.coal, amount));
            furnace.coal+=amount;
            componentsToAdd.add(new MenuComponentFalling(Display.getWidth()-60, Display.getHeight()-150, Item.coal));
        }
    }
    public void win(){
        if(lost)return;
        meteorShower = false;
        meteorShowerTimer = Integer.MAX_VALUE;
        baseGUI = false;
        if(mothership!=null){
            componentsToRemove.add(mothership);
        }
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            m.timer = Integer.MAX_VALUE;
        }
        won = true;
        Sounds.fadeSound("music", "WinMusic");
        winTimer = 20*30;
    }
    public void lose(){
        if(won)return;
        meteorShower = false;
        meteorShowerTimer = Integer.MAX_VALUE;
        baseGUI = false;
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            m.timer = Integer.MAX_VALUE;
        }
        new Thread(() -> {
            while(!isDestroyed()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MenuGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        Sounds.fadeSound("music", "SadMusic7");//cryptic sorrow
        if(mothership!=null){
            mothership.leaving = true;
        }
        lost = true;
    }
    public void addItem(DroppedItem item) {
        itemsToDrop.add(item);
    }
    public void addResources(Item item) {
        actionUpdateRequired = true;
        for(ItemStack stack : base.resources){
            if(stack.item==item){
                stack.count++;
                return;
            }
        }
        base.resources.add(new ItemStack(item, 1));
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
        return base.resources.stream().anyMatch((s) -> (s.item==stack.item&&s.count>=stack.count));
    }
    public void removeResources(ItemStack[] resources) {
        for(ItemStack s : resources){
            removeResources(s);
        }
    }
    public void removeResources(ItemStack stack) {
        for(ItemStack s : base.resources){
            if(s.item==stack.item){
                s.count-=stack.count;
                return;
            }
        }
    }
    public Particle addParticleEffect(Particle particle){
        synchronized(particles){
            particles.add(particle);
            return particle;
        }
    }
    public void addWorker(){
        addWorker(base.x+base.width/2, base.y+base.height-12);
    }
    public void replaceBuilding(Building start, Building end){
        synchronized(buildings){
            if(buildings.contains(start)){
                buildingsToReplace.put(start,end);
            }
        }
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str){
        textWithBackground(left, top, right, bottom, str, false);
    }
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
    public void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    private void action(String label, boolean enabled, ActionListener listener, ItemStack... tooltip){
        if(label.contains("Demolish")||label.contains("Cancel Task")){
            actionButtonOffset+=15;
        }
        actionButtons.add(add(new MenuComponentActionButton(50/*+(actionButtons.size()*200)*/, /*Display.getHeight()-50*/actionButtons.size()*50+actionButtonOffset, actionButtonWidth, 50, label, enabled, tooltip){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener==null)return;
                listener.actionPerformed(e);
                actionUpdateRequired = true;
            }
        }));
        if(label.contains("Demolish")||label.contains("Cancel Task")){
            actionButtonOffset+=15;
        }
    }
    private void taskButton(String label, Worker worker, Task task){
        action(label, task.canPerform(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!task.canPerform())return;
                task.create();
                if(worker!=null)worker.task(task);
            }
        }, task.getTooltip());
    }
    public void startAnim(Task task){
        taskAnimations.add(new MenuComponentTaskAnimation(task.building.x, task.building.y, task.building.width, task.building.height, task.type.images, this, task){
            @Override
            public double getProgress(){
                return task.progress();
            }
        });
    }
    public void cancelAnim(Task task){
        for(MenuComponent component : components){
            if(component instanceof MenuComponentTaskAnimation){
                MenuComponentTaskAnimation anim = (MenuComponentTaskAnimation) component;
                anim.paused = true;
            }
        }
    }
    public boolean safe(Worker worker) {
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
        for(Building building : buildings){
            if(building instanceof ShieldGenerator){
                ShieldGenerator gen = (ShieldGenerator) building;
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
        for(Building building : buildings){
            if(building instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) building;
                if(sky.falling){
                    safe = false;
                }
            }
        }
        return safe;
    }
    public void expedition(int workers){
        pendingExpedition = new Expedition(workers);
    }
    private int calculatePopulationCapacity() {
        int pop = 0;
        for(Building building : buildings){
            if(building instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) building;
                pop += sky.getMaxPop();
            }
        }
        return pop;
    }
    private int calculatePopulation(){
        int pop = 0;
        for(Building building : buildings){
            if(building instanceof Skyscraper){
                pop += ((Skyscraper)building).pop;
            }
        }
        return pop;
    }
    public void save(){
        if(lost){
            return;
        }
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+Core.save+".dat");
        Config config = Config.newConfig(file);
        config.set("level", 0);
        config.set("version", VersionManager.currentVersion);
        if(mothership!=null){
            config.set("mothership health", mothership.health);
        }
        config.set("furnace level", furnace.level);
        config.set("autotask", autoTask);
        config.set("furnace iron", furnace.ironOre);
        config.set("furnace coal", furnace.coal);
        config.set("furnace xp", furnace.total);
        config.set("enemy strength", MenuComponentEnemy.strength);
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
        cfg.set("count", buildings.size());
        for(int i = 0; i<buildings.size(); i++){
            Building building = buildings.get(i);
            cfg.set(i+"", building.saveBuilding(Config.newConfig()));
        }
        config.set("Buildings", cfg);
        config.set("worker cooldown", workerCooldown);
        config.set("phase", phase);
        config.set("target pop", targetPopulation);
        config.set("pop per floor", popPerFloor);
        config.set("meteor shower delay", meteorShowerDelayMultiplier);
        config.set("meteor shower intensity", meteorShowerIntensityMultiplier);
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
        config.save();
    }
    public static MenuGame load(GUI gui){
        File file = new File(Main.getAppdataRoot()+"\\saves\\"+Core.save+".dat");
        if(!file.exists()){
            return null;
        }
        MenuGame game = new MenuGame(gui);
        Config config = Config.newConfig(file);
        config.load();
        int hp = config.get("mothership health", -1);
        if(hp!=-1){
            EnemyMothership ship = new EnemyMothership();
            ship.health = hp;
            game.mothership = game.add(ship);
        }
        game.furnace.level = config.get("furnace level", 0);
        game.furnace.ironOre = config.get("furnace iron", 0);
        game.autoTask = config.get("autotask", true);
        game.furnace.coal = config.get("furnace coal", 0);
        game.furnace.total = config.get("furnace xp", 0);
        game.furnace.auto = game.furnace.level>=2;
        MenuComponentEnemy.strength = config.get("enemy strength", 1d);
        game.enemyTimer = config.get("enemy timer", 20*30);
        Config cfg = config.get("Dropped Items", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            DroppedItem item = new DroppedItem(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), Item.getItemByName(cfg.get(i+" item","iron ingot")), game);
            item.life = cfg.get(i+" life", item.life);
            game.droppedItems.add(item);
        }
        game.meteorShower = config.get("Meteor Shower", game.meteorShower);
        game.meteorShowerTimer = config.get("Meteor Shower Timer", game.meteorShowerTimer);
        cfg = config.get("Buildings", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            Building b = Building.load(cfg.get(i+"", Config.newConfig()));
            if(b instanceof Base){
                game.base = (Base) b;
            }
            game.buildings.add(b);
        }
        if(game.base==null){
            return null;
        }
        for(int i = 0; i<config.get("workers", 1); i++){
            game.addWorker();
        }
        game.workerCooldown = config.get("worker cooldown", game.workerCooldown);
        game.phase = config.get("phase", game.phase);
        game.targetPopulation = config.get("target pop", game.targetPopulation);
        game.popPerFloor = config.get("pop per floor", game.popPerFloor);
        game.meteorShowerDelayMultiplier = config.get("meteor shower delay", game.meteorShowerDelayMultiplier);
        game.meteorShowerIntensityMultiplier = config.get("meteor shower intensity", game.meteorShowerIntensityMultiplier);
        game.pendingExpedition = Expedition.load(config.get("Pending Expedition"));
        cfg = config.get("Active Expeditions", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            game.activeExpeditions.add(Expedition.load(cfg.get(i+"", Config.newConfig())));
        }
        cfg = config.get("Finished Expeditions", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            game.finishedExpeditions.add(Expedition.load(cfg.get(i+"", Config.newConfig())));
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
        config.save();
        return game;
    }
    public void addCivilians(int civilians){
        for(Building b : buildings){
            if(b instanceof Skyscraper){
                Skyscraper s = (Skyscraper) b;
                civilians = s.addPop(civilians);
                if(civilians<=0) break;
            }
        }
    }
    void addEnemy(){
        if(rand.nextDouble()<(MenuComponentEnemy.strength)/100D){
            for(int i = 0; i<Math.min(10,Math.abs(rand.nextGaussian()*3)); i++){
                enemies.add(add(MenuComponentEnemy.randomEnemy(this)));
            }
        }else{
            enemies.add(add(MenuComponentEnemy.randomEnemy(this)));
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
            MenuGame.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), size/2D,10,0);
        }
        GL11.glColor4d((innerR+outerR)/2, (innerG+outerG)/2, (innerB+outerB)/2, 1);
        for(int i = 0; i<dist; i++){
            double percent = i/dist;
            MenuGame.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), (size*(2/3D))/2D,10,0);
        }
        GL11.glColor4d(innerR, innerG, innerB, 1);
        for(int i = 0; i<dist; i++){
            double percent = i/dist;
            MenuGame.drawRegularPolygon(x1+(xDiff*percent), y1+(yDiff*percent), (size*(1/3D))/2D,10,0);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public static void drawConnector(double x1, double y1, double x2, double y2, double size, double innerR, double innerG, double innerB, double outerR, double outerG, double outerB){
        drawLaser(x1, y1, x2, y2, size, innerR, innerG, innerB, outerR, outerG, outerB);
        GL11.glColor4d(outerR,outerG,outerB,1);
        drawRegularPolygon(x1, y1, size*1.5, 10, 0);
        drawRegularPolygon(x2, y2, size*1.5, 10, 0);
        GL11.glColor4d((innerR+outerR)/2, (innerG+outerG)/2, (innerB+outerB)/2, 1);
        drawRegularPolygon(x1, y1, size, 10, 0);
        drawRegularPolygon(x2, y2, size, 10, 0);
    }
    private void damageReport(){
        damageReportTimer = damageReportTime;
    }
    public Drone addDrone(Drone drone){
        synchronized(drones){
            drones.add(drone);
        }
        return drone;
    }
    private void assignAllWorkers(){
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
            if(!worker.isWorking()&&selectedWorker!=worker){
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
    private void addAll(Iterable<MenuComponent> componentsToAdd){
        for(MenuComponent c : componentsToAdd){
            add(c);
        }
    }
    private void phase(int i){
        i = Math.min(4,i);
        phase = i;
        switch(i){
            case 2:
                meteorShowerDelayMultiplier *= 0.9;
                meteorShowerIntensityMultiplier *= 2.5;
                break;
            case 3:
                meteorShowerDelayMultiplier *= 0.6;
                meteorShowerIntensityMultiplier *= 4;
                break;
            case 4:
                meteorShower = false;
                meteorShowerTimer = Integer.MAX_VALUE;
                for(AsteroidMaterial material : AsteroidMaterial.values()){
                    material.timer = Integer.MAX_VALUE;
                }
                if(mothership==null&&!isDestroyed()&&!won&&!lost){
                    mothership = add(new EnemyMothership());
                    Sounds.fadeSound("music");
                }
                break;
            default:
        }
        gui.open(this);
    }
    public void addCloud(){
        if(!MenuOptionsGraphics.clouds)return;
        double y = rand.nextInt(Display.getHeight());
        addCloud(0,y);
    }
    public void addCloud(double x, double y){
        if(!MenuOptionsGraphics.clouds)return;
        double strength = rand.nextInt(42);
        double rateOfChange = (rand.nextDouble()-.4)/80;
        double speed = Core.game.rand.nextGaussian()/10+1;
        switch(Core.game.phase){
            case 2:
                strength = rand.nextInt(40)+2;
                rateOfChange*=1.05;
                break;
            case 3:
                strength = rand.nextInt(35)+7;
                rateOfChange*=1.1;
                break;
            case 4:
                if(Core.game.mothership!=null){
                    switch(Core.game.mothership.phase){
                        case 2:
                            strength = rand.nextInt(30)+12;
                            rateOfChange*=1.15;
                            break;
                        case 3:
                            strength = rand.nextInt(25)+17;
                            rateOfChange*=1.175;
                            break;
                        case 4:
                            strength = rand.nextInt(20)+22;
                            rateOfChange*=1.2;
                            break;
                        case 1:
                        default:
                            strength = rand.nextInt(32)+10;
                            rateOfChange*=1.125;
                            break;
                    }
                    break;
                }
        }
        if(lost){
            strength = rand.nextInt(42-Particle.rainThreshold)+Particle.rainThreshold;
            rateOfChange = Math.max(0, rateOfChange);
            speed*=.75;
        }
        if(won){
            strength = rand.nextInt(10);
        }
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
            double xx = -(X*25)-50;
            if(Math.round(X)==Math.round(X*10)/10d){
                double Y = y;
                for(int i = 0; i<height; i++){
                    addParticleEffect(new Particle(xx+x, y, strength, rateOfChange, speed));
                    y-=40;
                }
                y = Y;
            }else{
                double Y = y;
                for(int i = 0; i<height; i++){
                    addParticleEffect(new Particle(xx+x, y-20, strength, rateOfChange, speed));
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
        int count = (int)(Display.getHeight()/size+1);
        double xOffset = count/size;
        for(int i = 0; i<count; i++){
            addParticleEffect(new ParticleFog(-size*2-xOffset*i, i*size-50, false, o1));
            addParticleEffect(new ParticleFog(-size*2-xOffset*i, i*size-50, true, o2));
        }
    }
    private void stopFog(){
        fogTimeIncrease = fogTime = 0;
    }
    public void damage(double x, double y){
        damage(x,y,1);
    }
    public void damage(double x, double y, int damage){
        damage(x,y,damage,null);
    }
    public void damage(double x, double y, AsteroidMaterial material){
        damage(x,y,1,material);
    }
    public void damage(double x, double y, int damage, AsteroidMaterial material){
        DAMAGE:for(int i = 0; i<damage; i++){
            for(Building building : buildings){
                if(building instanceof ShieldGenerator){
                    ShieldGenerator shield = (ShieldGenerator) building;
                    if(Core.distance(building, x, y)<=shield.getShieldSize()/2){
                        shield.setShieldSize(shield.getShieldSize() - 100);
                        if(shield.getShieldSize()>=0){
                            continue DAMAGE;
                        }else{
                            shield.setShieldSize(0);
                            break;
                        }
                    }
                }
            }
            Building hit = getBuilding(x,y);
            if(hit==null||!hit.damage(x,y)||material!=null&&material.forceDrop){
                //<editor-fold defaultstate="collapsed" desc="Hit ground">
                double dmgRad = 25;
                synchronized(workers){
                    for(Worker worker : workers){
                        if(isClickWithinBounds(x, y, worker.x-dmgRad, worker.y-dmgRad, worker.x+worker.width+dmgRad, worker.y+worker.height+dmgRad)){
                            worker.damage(x,y);
                        }
                    }
                }
                synchronized(droppedItems){
                    for(DroppedItem item : droppedItems){
                        if(isClickWithinBounds(x, y, item.x-dmgRad, item.y-dmgRad, item.x+item.width+dmgRad,item.y+item.height+dmgRad)){
                            item.damage(x,y);
                        }
                    }
                }
                if(material!=null){
                    if(material.forceDrop){
                        itemsToDrop.add(new DroppedItem(x, y, getItem(material), this));
                    }else{
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            itemsToDrop.add(new DroppedItem(x+15-25, y+8-25, getItem(material), this));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            itemsToDrop.add(new DroppedItem(x+8-25, y+28-25, getItem(material), this));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            itemsToDrop.add(new DroppedItem(x+38-25, y+24-25, getItem(material),  this));
                        }
                        if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                            itemsToDrop.add(new DroppedItem(x+23-25, y+34-25, getItem(material), this));
                        }
                    }
                }
//</editor-fold>
            }
        }
    }
    public Building getBuilding(double x, double y){
        Building hit = null;
        synchronized(buildings){
            for(Building building : buildings){
                if(building instanceof Skyscraper){
                    Skyscraper sky = (Skyscraper)building;
                    if(isClickWithinBounds(x, y, building.x, building.y-(sky.floorCount*sky.floorHeight), building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
                }else if(building instanceof Base){
                    if(isClickWithinBounds(x, y, building.x, building.y-25, building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
                }else{
                    if(isClickWithinBounds(x, y, building.x, building.y, building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
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
            case SHOOTING_STAR:
                return Item.star;
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
    public void pushParticles(double x, double y, double radius, double distance){
        pushParticles(x, y, radius, distance, 1);
    }
    /**
     * Push particles away from a location.
     * @param x the X value
     * @param y the Y value
     * @param radius The radius of the push field
     * @param distance How far to push the particles
     * @param fadeFactor How much the particles should fade
     */
    public void pushParticles(double x, double y, double radius, double distance, double fadeFactor){
        synchronized(particles){
            for(Particle particle : particles){
                if(particle.type==ParticleEffectType.EXPLOSION)continue;
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
                    particle.x+=distX*distance*mult;
                    particle.y+=distY*distance*mult;
                    if(particle instanceof ParticleFog){
                        ((ParticleFog)particle).opacity-=.1*fadeFactor*mult;
                    }
                    if(particle.type==ParticleEffectType.CLOUD){
                        particle.strength-=fadeFactor*mult;
                    }
                }
            }
        }
    }
    public double debugYOffset = 0;
    public String debugText(double textHeight, String text){
        GL11.glColor4d(0, 0, 0, .5);
        drawRect(0, debugYOffset, FontManager.getLengthForStringWithHeight(text, textHeight-1)+1, debugYOffset+textHeight, 0);
        GL11.glColor4d(1, 1, 1, 1);
        text = drawTextWithWrap(1, debugYOffset+1, Display.getWidth()-1, debugYOffset+textHeight-1, text);
        debugYOffset+=textHeight;
        return text;
    }
    public void addWorker(double x, double y){
        Worker worker = new Worker(x, y, this);
        workers.add(worker);
        add(worker.button);
    }
    public void playSecret(){
        playSecret(secretWaiting);
        secretWaiting = -1;
    }
    private void playSecret(int secret){
        switch(secret){
            case 0://observatory
                Sounds.playSound("music", "MysteryMusic3");
                addShootingStar();
                break;
        }
    }
    private void addShootingStar(){
        addAsteroid(new Asteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.SHOOTING_STAR, 2));
    }
    public Asteroid addAsteroid(Asteroid asteroid){
        synchronized(asteroids){
            asteroids.add(asteroid);
        }
        return asteroid;
    }
    protected void drawDayNightCycle(){
        Color noon = new Color(255, 216, 0, 32);
        Color night = new Color(22, 36, 114, 72);
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
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    private Worker getAvailableWorker(double x, double y){
        Worker closest = null;
        double distance = Double.MAX_VALUE;
        for(Worker worker : workers){
            if(!worker.isWorking()&&selectedWorker!=worker){
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
            ArrayList<Building> possibilities = new ArrayList<>();
            possibilities.addAll(buildings);
            boolean added = true;
            while(added){
                PowerNetwork network = null;
                added = false;
                for(Building b : possibilities){
                    network = PowerNetwork.detect(buildings, b);
                    if(network!=null){
                        synchronized(powerNetworks){
                            powerNetworks.add(network);
                        }
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
            ArrayList<Building> possibilities = new ArrayList<>();
            possibilities.addAll(buildings);
            boolean added = true;
            while(added){
                StarlightNetwork network = null;
                added = false;
                for(Building b : possibilities){
                    network = StarlightNetwork.detect(buildings, b);
                    if(network!=null){
                        synchronized(starlightNetworks){
                            starlightNetworks.add(network);
                        }
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
        notifications.add(new Notification(notification, time));
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
    private static class Notification{
        private String name;
        private int time;
        private final int maxTime;
        private int num = 1;
        private int height = 20;//time counts down, decrease to zero
        private double width = 0;//increase to 1
        public Notification(String name, int time){
            this.name = name;
            this.time = time;
            maxTime = time;
        }
        public boolean isDying(){
            return time<0;
        }
        public boolean isDead(){
            return isDying()&&height<=0;
        }
        public void add(){
            num++;
            time = maxTime;
        }
        public void tick(){
            if(isDying()){
                height--;
            }else if(width<1){
                width+=.1;
            }else{
                time--;
            }
        }
        @Override
        public String toString(){
            return "-- "+name+(num==1?"":" x"+num)+" --";
        }
    }
}