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
import planetaryprotector.friendly.MenuComponentDrone;
import planetaryprotector.item.MenuComponentDroppedItem;
import planetaryprotector.menu.component.MenuComponentFalling;
import planetaryprotector.menu.component.MenuComponentFurnace;
import planetaryprotector.menu.component.MenuComponentRising;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.MenuComponentMeteorShower;
import planetaryprotector.enemy.MenuComponentAsteroid;
import planetaryprotector.particle.MenuComponentParticle;
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
import planetaryprotector.building.MenuComponentMine;
import planetaryprotector.building.MenuComponentShieldGenerator;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentGenerator;
import planetaryprotector.building.MenuComponentBunker;
import planetaryprotector.building.IllegalBuildingException;
import planetaryprotector.building.MenuComponentWreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentSilo;
import planetaryprotector.building.MenuComponentBase;
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
import planetaryprotector.menu.component.ZComponent;
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
    public ArrayList<MenuComponentDroppedItem> droppedItems = new ArrayList<>();
    public MenuComponentBase base;
    public static Random rand = new Random();
    public ArrayList<MenuComponentDroppedItem> itemsToDrop = new ArrayList<>();
    public ArrayList<MenuComponent> componentsToRemove = new ArrayList<>();
    public boolean baseGUI = false;
    private MenuComponentFurnace furnace;
    public ArrayList<MenuComponent> componentsToAdd = new ArrayList<>();
    public boolean lost = false;
    public MenuComponentMeteorShower shower;
    public boolean meteorShower;
    public MenuComponentClickable stone;
    public MenuComponentClickable ironIngots;
    public MenuComponentClickable ironChunks;
    public MenuComponentClickable coal;
    private int meteorShowerTimer = 80;
    public ArrayList<MenuComponentWorker> workers = new ArrayList<>();
    public ArrayList<EnemyAlien> aliens = new ArrayList<>();
    public ArrayList<MenuComponentBuilding> buildings = new ArrayList<>();
    public ArrayList<MenuComponentActionButton> actionButtons = new ArrayList<>();
    public MenuComponentWorker selectedWorker;
    public MenuComponentBuilding selectedBuilding;
    HashMap<MenuComponentBuilding, MenuComponentBuilding> buildingsToReplace = new HashMap<>();
    private MenuComponentBuilding oldSelectedBuilding;
    private Task oldSelectedTask;
    private Task selectedTask;
    public boolean paused;
    public boolean firstShower = true;
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
    ArrayList<MenuComponentDrone> drones = new ArrayList<>();
    private int enemyTimer = 20*30;
    private double damageReportTimer = 0;
    private double damageReportTime = 20*10;
    public EnemyMothership mothership;
    public int civilianCooldown = MenuGame.rand.nextInt(20*60*5);
    public double civilianIncreasePerSec = 0;
    public int civilianTimer = 0;
    private boolean autoTask;
    public boolean doNotDisturb = false;
    private int winTimer = -1;
    boolean fading;
    double blackScreenOpacity = 0;
    public static boolean cheats = false;
    public static int tick;
    public boolean won;
    private int cloudTimer = rand.nextInt(500)+350;
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
//</editor-fold>
    public MenuGame(GUI gui){
        super(gui, null);
        stone = add(new MenuComponentClickable(Display.getWidth()-100, 0, 20, 20, "/textures/items/Stone.png"));
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        ironIngots = add(new MenuComponentClickable(Display.getWidth()-100, 60, 20, 20, "/textures/items/Iron Ingot.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        shower = add(new MenuComponentMeteorShower());
        phase = 1;
        cheats = false;
    }
    public MenuGame(GUI gui, Menu parent){
        this(gui,parent,new MenuComponentBase(rand.nextInt(Display.getWidth()-100), rand.nextInt(Display.getHeight()-100)));
    }
    public MenuGame(GUI gui, Menu parent, MenuComponentBase base){
        super(gui,parent);
        int buildingCount = (Display.getWidth()/100)*(Display.getHeight()/100);
        this.base = base;
        buildings.add(add(base));
        for(int i = 0; i<buildingCount; i++){
            MenuComponentBuilding building = MenuComponentBuilding.generateRandomBuilding(base, buildings);
            if(building==null){
                continue;
            }
            buildings.add(add(building));
        }
        stone = add(new MenuComponentClickable(Display.getWidth()-100, 0, 20, 20, "/textures/items/Stone.png"));
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        ironIngots = add(new MenuComponentClickable(Display.getWidth()-100, 60, 20, 20, "/textures/items/Iron Ingot.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        shower = add(new MenuComponentMeteorShower());
        addWorker();
        phase = 1;
        cheats = false;
    }
    public MenuGame(GUI gui, MenuGame game, int phase){
        this(gui, game, game.base, game.buildings, phase);
    }
    public MenuGame(GUI gui, Menu parent, MenuComponentBase base, ArrayList<MenuComponentBuilding> buildings) {
        this(gui, parent, base, buildings, 1);
    }
    public MenuGame(GUI gui, Menu parent, MenuComponentBase base, ArrayList<MenuComponentBuilding> buildings, int phase) {
        super(gui,parent);
        this.base = add(new MenuComponentBase(base.x, base.y));
        buildings.add(base);
        for(MenuComponentBuilding building : buildings){
            this.buildings.add(add(building));
        }
        stone = add(new MenuComponentClickable(Display.getWidth()-100, 0, 20, 20, "/textures/items/Stone.png"));
        coal = add(new MenuComponentClickable(Display.getWidth()-100, 20, 20, 20, "/textures/items/Coal.png"));
        ironChunks = add(new MenuComponentClickable(Display.getWidth()-100, 40, 20, 20, "/textures/items/Iron Chunk.png"));
        ironIngots = add(new MenuComponentClickable(Display.getWidth()-100, 60, 20, 20, "/textures/items/Iron Ingot.png"));
        furnace = add(new MenuComponentFurnace(0, Display.getHeight()-100, 100, 100, this));
        shower = add(new MenuComponentMeteorShower());
        addWorker();
        this.phase = phase;
        cheats = false;
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
            for(MenuComponentBuilding building : buildings){
                if(building instanceof MenuComponentSkyscraper){
                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)building;
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
        super.renderBackground();
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
                        for(MenuComponentWorker worker : workers){
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
                        if(o1 instanceof MenuComponentDroppedItem){
                            y1 -= Display.getHeight();
                        }
                        if(o1 instanceof MenuComponentParticle&&((MenuComponentParticle)o1).air){
                            y1 += Display.getHeight()*8;
                        }
                        if(o1 instanceof MenuComponentAsteroid){
                            y1 += Display.getHeight()*5;
                        }
                        if(o1 instanceof MenuComponentEnemy){
                            y1 += Display.getHeight()*4;
                        }
                        if(o1 instanceof MenuComponentDrone){
                            y1 += Display.getHeight()*3;
                        }
                        if(o1==shower){
                            y1 += Display.getHeight()*2;
                        }
                        if(o1==stone||o1==coal||o1==ironChunks||o1==ironIngots||o1==furnace){
                            y1 += Display.getHeight()*2;
                        }
                        if(o1 instanceof MenuComponentTaskAnimation){
                            MenuComponentTaskAnimation t = (MenuComponentTaskAnimation)o1;
                            switch(t.task.building.type){
                                case MINE:
                                case EMPTY:
                                case WRECK:
                                    y1 -= Display.getHeight()*2;
                                    break;
                                case SKYSCRAPER:
                                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)t.task.building;
                                    y1 -= sky.fallen;
                            }
                        }
                        if(o1 instanceof MenuComponentSkyscraper){
                            MenuComponentSkyscraper sky = (MenuComponentSkyscraper)o1;
                            y1 -= sky.fallen;
                        }
//                    }
//                    if(o2 instanceof ZComponent){
//                        y2 += Display.getHeight()*((ZComponent)o2).getZ();
//                    }else{
                        if(o2==overlay){
                            y2 += Display.getHeight()*50;
                        }
                        for(MenuComponentWorker worker : workers){
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
                        if(o2 instanceof MenuComponentDroppedItem){
                            y2 -= Display.getHeight();
                        }
                        if(o2 instanceof MenuComponentParticle&&((MenuComponentParticle)o2).air){
                            y2 += Display.getHeight()*8;
                        }
                        if(o2 instanceof MenuComponentAsteroid){
                            y2 += Display.getHeight()*5;
                        }
                        if(o2 instanceof MenuComponentEnemy){
                            y2 += Display.getHeight()*4;
                        }
                        if(o2 instanceof MenuComponentDrone){
                            y2 += Display.getHeight()*3;
                        }
                        if(o2==shower){
                            y2 += Display.getHeight()*2;
                        }
                        if(o2==stone||o2==coal||o2==ironChunks||o2==ironIngots||o2==furnace){
                            y2 += Display.getHeight()*2;
                        }
                        if(o2 instanceof MenuComponentTaskAnimation){
                            MenuComponentTaskAnimation t = (MenuComponentTaskAnimation)o2;
                            switch(t.task.building.type){
                                case MINE:
                                case EMPTY:
                                case WRECK:
                                    y2 -= Display.getHeight()*2;
                                    break;
                                case SKYSCRAPER:
                                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)t.task.building;
                                    y2 -= sky.fallen;
                            }
                        }
                        if(o2 instanceof MenuComponentSkyscraper){
                            MenuComponentSkyscraper sky = (MenuComponentSkyscraper)o2;
                            y2 -= sky.fallen;
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
            Collections.sort(buildings, new Comparator<MenuComponentBuilding>(){
                @Override
                public int compare(MenuComponentBuilding o1, MenuComponentBuilding o2){
                    double y1 = o1.y;
                    double y2 = o2.y;
                    double height1 = o1.height;
                    double height2 = o2.height;
                    if(o1 instanceof MenuComponentSkyscraper){
                        MenuComponentSkyscraper sky = (MenuComponentSkyscraper)o1;
                        y1 -= sky.fallen;
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
        }
        //<editor-fold defaultstate="collapsed" desc="BaseGUI location">
        if(baseGUI){
            stone.x=Display.getWidth()-100;
            coal.x=Display.getWidth()-100;
            ironChunks.x=Display.getWidth()-100;
            ironIngots.x=Display.getWidth()-100;
            furnace.x=Display.getWidth()-100;
            for(MenuComponentWorker worker : workers){
                worker.button.x = 0;
                worker.button.color = (!(worker.dead||worker.isWorking()))?Color.WHITE:Color.RED;
                worker.button.enabled = !(worker.dead||worker.isWorking());
            }
            for(MenuComponentActionButton button : actionButtons){
                button.x = 50;
            }
        }else{
            stone.x=-100;
            coal.x=-100;
            ironChunks.x=-100;
            ironIngots.x=-100;
            furnace.x=-100;
            for(MenuComponentWorker worker : workers){
                worker.button.x = -50;
            }
            for(MenuComponentActionButton button : actionButtons){
                button.x = 0;
            }
        }
//</editor-fold>
        for(MenuComponentBuilding building : buildings){
            building.renderBackground();
        }
        for(MenuComponent component : components){
            if(component instanceof MenuComponentTaskAnimation){
                component.render();
            }
        }
        for(MenuComponentDroppedItem item : droppedItems){
            item.renderBackground();
        }
        for(MenuComponentWorker worker : workers){
            worker.renderBackground();
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        if(baseGUI){
            drawRect(Display.getWidth()-100, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/gui/sidebar.png"));
        }
        //<editor-fold defaultstate="collapsed" desc="BAD - MenuComponentFalling and MenuComponentRising re-rendering">
        for(MenuComponent c : components){
            if(c instanceof MenuComponentFalling||c instanceof MenuComponentRising){
                c.render();
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BAD - Furnace re-rendering">
furnace.render();
//</editor-fold>
        if(Mouse.isButtonDown(1)&&selectedWorker!=null){
            selectedWorker.selectedTarget = new double[]{Mouse.getX(),Display.getHeight()-Mouse.getY()};
        }
        //<editor-fold defaultstate="collapsed" desc="Damage Report">
        if(damageReportTimer>=0){
            for(MenuComponentBuilding b : buildings){
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
        //<editor-fold defaultstate="collapsed" desc="Shields">
for(MenuComponentBuilding building : buildings){
    if(building instanceof MenuComponentShieldGenerator){
        MenuComponentShieldGenerator gen = (MenuComponentShieldGenerator) building;
        gen.shield.renderOnWorld();
    }
}
//</editor-fold>
        if(!won&&!lost){
            //<editor-fold defaultstate="collapsed" desc="Phase 1 advancing">
            if(phase==1){
                int shieldArea = 0;
                double maxSize = 0;
                for(MenuComponentBuilding building : buildings){
                    if(building instanceof MenuComponentShieldGenerator){
                        MenuComponentShieldGenerator shield = (MenuComponentShieldGenerator) building;
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
    centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population Capacity: "+pop+"/"+targetPopulation+" ("+Math.round(pop/(double)targetPopulation*10000D)/100D+"%)");
    if(pop>=targetPopulation){
        phase(3);
    }
}
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Phase 3 rendering and advancing">
            if(phase==3){
                int pop = calculatePopulation();
                int maxPop = calculatePopulationCapacity();
                if(maxPop<targetPopulation){
                    centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population Capacity: "+maxPop+"/"+targetPopulation+" ("+Math.round(maxPop/(double)targetPopulation*10000D)/100D+"%)");
                    centeredTextWithBackground(0, 50, Display.getWidth(), 100, "Population: "+pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
                }else{
                    centeredTextWithBackground(0, 0, Display.getWidth(), 50, "Population: "+pop+"/"+maxPop+" ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
                }
                if(pop>=targetPopulation&&maxPop>=targetPopulation){
                    phase(4);
                    paused = false;
                }
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="BAD - Action Button Re-rendering">
            for(MenuComponentActionButton actionButton : actionButtons){
                actionButton.render();
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="BAD - resource amount re-rendering">
stone.render();
ironChunks.render();
ironIngots.render();
coal.render();
//</editor-fold>
            drawText(furnace.x+10, Display.getHeight()-60, Display.getWidth()-10, Display.getHeight()-40, furnace.ironOre+" Iron");
            drawText(furnace.x+10, Display.getHeight()-40, Display.getWidth()-10, Display.getHeight()-20, furnace.coal+" Coal");
            drawText(furnace.x+10, Display.getHeight()-20, Display.getWidth()-10, Display.getHeight(), furnace.level>=MenuComponentFurnace.maxLevel?"Maxed":"Level "+(furnace.level+1));
            if(baseGUI){
                for(int i = 0; i<base.resources.size(); i++){
                    drawText(Display.getWidth()-80, i*20, Display.getWidth(), (i+1)*20, base.resources.get(i).count+"");
                }
            }
        }else if(won){
            if(phase>0){
                centeredTextWithBackground(0, 0, Display.getWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                if(winTimer<20&&Sounds.nowPlaying.equals("VictoryMusic1")){
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
        if(oldSelectedBuilding!=selectedBuilding||oldSelectedTask!=selectedTask){
            componentsToRemove.addAll(actionButtons);
            actionButtons.clear();
            if(selectedBuilding!=null&&selectedWorker!=null){
                switch(selectedBuilding.type){
                    case SKYSCRAPER:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        taskButton("Add Floor", selectedWorker, new TaskSkyscraperAddFloor((MenuComponentSkyscraper)selectedBuilding));
                        taskButton("Add 10 Floors", selectedWorker, new TaskSkyscraperAddFloor((MenuComponentSkyscraper)selectedBuilding,10));
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case BUNKER:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case SILO:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        action("Toggle Power Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((MenuComponentSilo)selectedBuilding).outline = !((MenuComponentSilo)selectedBuilding).outline;
                            }
                        });
                        if(selectedBuilding.canUpgrade()){
                            taskButton("Upgrade", selectedWorker, new TaskUpgrade(selectedBuilding));
                        }else{
                            action("Maxed", false, new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){}
                            });
                        }
                        action("Build Missile", ((MenuComponentSilo)selectedBuilding).canBuildMissile(), new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent ae){
                                ((MenuComponentSilo)selectedBuilding).buildMissile();
                            }
                        }, ((MenuComponentSilo)selectedBuilding).missileCost());
                        if(selectedBuilding.level>1){
                            action("Build Drone", ((MenuComponentSilo)selectedBuilding).canBuildDrone(), new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){
                                    ((MenuComponentSilo)selectedBuilding).buildDrone();
                                }
                            }, ((MenuComponentSilo)selectedBuilding).droneCost());
                        }
                        action("Fire Missiles", ((MenuComponentSilo)selectedBuilding).canLaunchMissile(), new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent ae){
                                ((MenuComponentSilo)selectedBuilding).launchMissile();
                            }
                        });
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case GENERATOR:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        if(selectedBuilding.canUpgrade()){
                            taskButton("Upgrade", selectedWorker, new TaskUpgrade(selectedBuilding));
                        }else{
                            action("Maxed", false, new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){}
                            });
                        }
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case MINE:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        if(selectedBuilding.canUpgrade()){
                            taskButton("Upgrade", selectedWorker, new TaskUpgrade(selectedBuilding));
                        }else{
                            action("Maxed", false, new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){}
                            });
                        }
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case SHIELD_GENERATOR:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        action("Toggle Shield Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((MenuComponentShieldGenerator)selectedBuilding).shieldOutline = !((MenuComponentShieldGenerator)selectedBuilding).shieldOutline;
                            }
                        });
                        action("Toggle Power Outline", true, new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                ((MenuComponentShieldGenerator)selectedBuilding).powerOutline = !((MenuComponentShieldGenerator)selectedBuilding).powerOutline;
                            }
                        });
                        if(selectedBuilding.canUpgrade()){
                            taskButton("Upgrade", selectedWorker, new TaskUpgrade(selectedBuilding));
                        }else{
                            action("Maxed", false, new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){}
                            });
                        }
                        if(phase>=3&&((MenuComponentShieldGenerator)selectedBuilding).canBlast){
                            action("Blast", ((MenuComponentShieldGenerator)selectedBuilding).blastRecharge==0, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ((MenuComponentShieldGenerator)selectedBuilding).blast();
                                }
                            });
                        }
                        taskButton("Demolish", selectedWorker, new TaskDemolish(selectedBuilding));
                        break;
                    case WRECK:
                        taskButton("Clean up", selectedWorker, new TaskWreckClean((MenuComponentWreck) selectedBuilding));
                        break;
                    case BASE:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        action("Dispense All Workers", true, (ActionEvent e) -> {
                            dispenseWorkers();
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
                    case EMPTY:
                        taskButton("Repair", selectedWorker, new TaskRepair(selectedBuilding));
                        taskButton("Repair All", selectedWorker, new TaskRepairAll(selectedBuilding));
                        taskButton("Build Bunker", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentBunker(selectedBuilding.x, selectedBuilding.y)));
                        if(phase>=3){
                            taskButton("Build Silo", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentSilo(selectedBuilding.x, selectedBuilding.y)));
                        }
                        taskButton("Build Skyscraper", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentSkyscraper(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Mine", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentMine(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Generator", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentGenerator(selectedBuilding.x, selectedBuilding.y)));
                        taskButton("Build Shield Generator", selectedWorker, new TaskConstruct(selectedBuilding, new MenuComponentShieldGenerator(selectedBuilding.x, selectedBuilding.y)));
                        break;
                    default:
                        throw new IllegalBuildingException(selectedBuilding.type);
                }
                if(selectedBuilding.task!=null&&selectedWorker.task==null){
                    action("Continue Task", true, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker.task = selectedBuilding.task;
                        }
                    });
                }
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="BAD - Worker button re-rendering">
for(MenuComponentWorker w : workers){
    if(w==null)continue;
    if(w.button==null)continue;
    w.button.render();
}
//</editor-fold>
        oldSelectedBuilding = selectedBuilding;
        oldSelectedTask = selectedTask;
        if(selectedBuilding!=null&&selectedBuilding.task!=null){
            for(int i = 0; i<selectedBuilding.task.getDetails().length; i++){
                textWithBackground(baseGUI?300:250, 30*i, Display.getWidth(), 30*(i+1), selectedBuilding.task.getDetails()[i]);
            }
        }
        if(Core.debugMode&&cheats){
            debugYOffset = 0;
            double textHeight = Display.getHeight()/debugData.size();
            for(String str : debugData){
                debugText(textHeight, str);
            }
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
    public void renderForeground(){
        if(fading){
            blackScreenOpacity+=0.01;
        }
        GL11.glColor4d(0, 0, 0, blackScreenOpacity);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Controls.deselect&&pressed&&!repeat&&selectedWorker!=null){
            selectedWorker = null;
        }
        if(key==Controls.hide&&pressed&&!repeat){
            shower.hide = !shower.hide;
        }
        if(key==Controls.cancel&&pressed&&!repeat){
            if(selectedWorker!=null&&selectedWorker.task!=null){
                selectedWorker.cancelTask();
            }
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
            Sounds.mute = !Sounds.mute;
        }
        if(key==Controls.cheat&&pressed&&!repeat){
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                cheats = !cheats;
            }
        }
        if(cheats&&pressed&&!repeat){
            if(key==Keyboard.KEY_6&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                phase(phase+1);
                paused = false;
            }
            if(key==Keyboard.KEY_R){
                for(ItemStack resource : base.resources){
                    resource.count+=100;
                }
            }
            if(key==Keyboard.KEY_C){
                addCloud(Mouse.getX(), Display.getHeight()-Mouse.getY());
            }
            if(key==Keyboard.KEY_F){
                startFog();
            }
            if(!actionButtons.isEmpty()&&selectedBuilding!=null&&selectedBuilding.task==null){
                if(key==Keyboard.KEY_1){
                    actionButtons.get(0).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_2&&actionButtons.size()>1){
                    actionButtons.get(1).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_3&&actionButtons.size()>2){
                    actionButtons.get(2).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_4&&actionButtons.size()>3){
                    actionButtons.get(3).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_5&&actionButtons.size()>4){
                    actionButtons.get(4).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_6&&actionButtons.size()>5){
                    actionButtons.get(5).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_7&&actionButtons.size()>6){
                    actionButtons.get(6).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_8&&actionButtons.size()>7){
                    actionButtons.get(7).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_9&&actionButtons.size()>8){
                    actionButtons.get(8).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
                if(key==Keyboard.KEY_0&&actionButtons.size()>9){
                    actionButtons.get(9).actionPerformed(null);
                    if(selectedBuilding.task!=null){
                        selectedBuilding.task.progress = selectedBuilding.task.time-1;
                    }
                }
            }
            if(key==Keyboard.KEY_ADD){
                addWorker();
            }
            if(key==Keyboard.KEY_GRAVE){
                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                    MenuComponentEnemy.strength++;
                }else{
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                        addEnemy();
                    }else{
                        int X = Mouse.getX()-25;
                        int Y = Display.getHeight()-Mouse.getY()-25;
                        switch(rand.nextInt(3)){
                            case 0:
                                add(new MenuComponentAsteroid(X, Y, AsteroidMaterial.COAL, true));
                                break;
                            case 1:
                                add(new MenuComponentAsteroid(X, Y, AsteroidMaterial.STONE, true));
                                break;
                            case 2:
                                add(new MenuComponentAsteroid(X, Y, AsteroidMaterial.IRON, true));
                                break;
                        }
                    }
                }
            }
            if(key==Keyboard.KEY_P){
                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                    meteorShowerDelayMultiplier *= 10;
                    meteorShowerIntensityMultiplier *= 0.1;
                    if(mothership!=null){
                        mothership.health-=mothership.maxHealth/4;
                    }
                }
                meteorShower = false;
                shower.opacitizing = -1;
            }
        }else if(pressed&&!repeat){
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
    public void tick(){
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
        debugData.add("First shower: "+firstShower);
        debugData.add("Furnace XP: "+furnace.total);
        debugData.add("Enemy Strength: "+MenuComponentEnemy.strength);
        debugData.add("Enemy Timer: "+enemyTimer);
        debugData.add("Dropped items: "+droppedItems.size());
        HashMap<Item, Integer> items = new HashMap<>();
        for(MenuComponentDroppedItem item : droppedItems){
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
        debugData.add("Meteor Shower: "+meteorShower);
        debugData.add("Meteor Shower Timer: "+meteorShowerTimer);
        debugData.add("Workers: "+workers.size());
        debugData.add("Buildings: "+buildings.size());
        HashMap<BuildingType, Integer> theBuildings = new HashMap<>();
        for(MenuComponentBuilding building : buildings){
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
        tick++;
        fogTime+=fogTimeIncrease;
        if(fogTime>1){
            stopFog();
        }
        if(tick%20==0){
            drawFog();
        }
        if(selectedWorker!=null&&selectedWorker.dead){
            selectedWorker = null;
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
            ArrayList<MenuComponentBuilding> list = new ArrayList<>(buildingsToReplace.keySet());
            MenuComponentBuilding start = list.get(0);
            MenuComponentBuilding end = buildingsToReplace.remove(start);
            components.remove(start);
            buildings.remove(start);
            buildings.add(add(end));
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
            for(MenuComponentBuilding building : buildings){
                if(building instanceof MenuComponentSkyscraper){
                    ((MenuComponentSkyscraper) building).pop = 0;
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
            addWorker();
            workerCooldown += Math.max(1200,6000-workers.size()*20);
        }
        Collections.sort(droppedItems, new Comparator<MenuComponentDroppedItem>() {
            @Override
            public int compare(MenuComponentDroppedItem item1, MenuComponentDroppedItem item2){
                return (int)Math.round(distanceTo(item1, base.x+base.width/2,base.y+base.height-12.5)-distanceTo(item2, base.x+base.width/2,base.y+base.height-12.5));
            }
        });
        //<editor-fold defaultstate="collapsed" desc="Armogeddon">
        if(lost){
            for(int i = 0; i<2; i++){
                switch(rand.nextInt(3)){
                    case 0:
                        add(new MenuComponentAsteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.COAL, true));
                        break;
                    case 1:
                        add(new MenuComponentAsteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.STONE, true));
                        break;
                    case 2:
                        add(new MenuComponentAsteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), AsteroidMaterial.IRON, true));
                        break;
                }
            }
        }
        if(lost){
            baseGUI = false;
        }
//</editor-fold>
        if(meteorShower){
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
                    m.timer = 0;
                }
                shower.opacitizing = 1;
            }else{
                if(Sounds.nowPlaying.equals("Music1")){
                    Sounds.fadeSound("music");
                }
                firstShower = false;
                shower.opacitizing = -1;
            }
            meteorShowerTimer = (int)Math.round((meteorShower?-(rand.nextInt(250)+750):rand.nextInt(2500)+7500)*meteorShowerDelayMultiplier);
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
                cloudTimer = rand.nextInt(500)+350;
                if(lost){
                    cloudTimer*=1.5;
                }
            }
            if(rand.nextDouble()<.002){
                addCloud();
            }
        }
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            m.timer--;
            if(m.timer<=0){
                add(new MenuComponentAsteroid(rand.nextInt(Display.getWidth()-50), rand.nextInt(Display.getHeight()-50), m, true));
                m.timer = (int)Math.round(((rand.nextInt(m.max-m.min)+m.min)/(meteorShower?50:1))/meteorShowerIntensityMultiplier);
            }
        }
        while(!itemsToDrop.isEmpty()){
            droppedItems.add(add(itemsToDrop.remove(0)));
        }
        while(!componentsToRemove.isEmpty()){
            if(componentsToRemove.get(0) instanceof MenuComponentBuilding){
                buildings.remove(componentsToRemove.get(0));
            }
            if(componentsToRemove.get(0) instanceof EnemyAlien){
                aliens.remove(componentsToRemove.get(0));
            }
            if(componentsToRemove.get(0) instanceof MenuComponentEnemy){
                enemies.remove(componentsToRemove.get(0));
            }
            if(componentsToRemove.get(0) instanceof MenuComponentDroppedItem){
                droppedItems.remove(componentsToRemove.get(0));
            }
            if(componentsToRemove.get(0) instanceof MenuComponentDrone){
                drones.remove(componentsToRemove.get(0));
            }
            components.remove(componentsToRemove.remove(0));
        }
        //<editor-fold defaultstate="collapsed" desc="Expeditions">
        if(pendingExpedition!=null&&workers.size()>1){
            for(MenuComponentWorker worker : workers){
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
        MenuComponentWorker workerForRemoval = null;
        for (MenuComponentWorker worker : workers) {
            if(worker.dead){
                workerForRemoval = worker;
                break;
            }
        }
        if(workerForRemoval!=null){
            removeWorker(workerForRemoval);
        }
        addAll(componentsToAdd);
        componentsToAdd.clear();
        ONE:for(MenuComponent c : components){
            if(c instanceof MenuComponentDroppedItem){
                MenuComponentDroppedItem item = (MenuComponentDroppedItem)c;
                if(droppedItems.contains(item)) continue;
                for(MenuComponentWorker w : workers){
                    if(w.grabbedItem==item||w.targetItem==item)continue ONE;
                }
                droppedItems.add(item);
            }
        }
        if(autoTask&&selectedWorker!=null&&selectedWorker.task!=null){
            dispenseWorkers();
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
        for(MenuComponentBuilding b : buildings){
            if(b instanceof MenuComponentSkyscraper){
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper) b;
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
            for(MenuComponentBuilding building : buildings){
                MenuComponentSkyscraper sky = null;
                if(building.type==BuildingType.SKYSCRAPER){
                    sky = (MenuComponentSkyscraper) building;
                }
                if(selectedWorker!=null&&((!selectedWorker.isWorking()&&!(Keyboard.isKeyDown(Controls.up)||selectedWorker.selectedTarget!=null||Keyboard.isKeyDown(Controls.left)||Keyboard.isKeyDown(Controls.down)||Keyboard.isKeyDown(Controls.right)))||selectedWorker.isWorking())&&Core.game.distanceTo(selectedWorker, building.x+(building.width/2),building.y+(building.width/2)-(sky==null?0:sky.fallen))<=building.width/2){
                    selectedBuilding = building;
                    break DO;
                }
            }
            selectedBuilding = null;
        }while(false);
        super.tick();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==base){
            baseGUI = !baseGUI;
        }else if(button==coal){
            addFuel();
        }else if(button==ironChunks){
            addIron();
        }
        if(button==furnace){
            furnace.upgrade();
        }
        for(MenuComponentWorker worker : workers){
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
        shower.opacitizing = -1;
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
        shower.opacitizing = -1;
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
        Sounds.fadeSound("music", "SadMusic28");
        if(mothership!=null){
            mothership.leaving = true;
        }
        lost = true;
    }
    public void addItem(MenuComponentDroppedItem item) {
        itemsToDrop.add(item);
    }
    public void addResources(Item item) {
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
    public MenuComponentParticle addParticleEffect(MenuComponentParticle particle){
        componentsToAdd.add(particle);
        return particle;
    }
    public void addWorker(){
        MenuComponentWorker worker = new MenuComponentWorker(base.x+base.width/2, base.y+base.height-12, this);
        workers.add(add(worker));
        add(worker.button);
    }
    public void replaceBuilding(MenuComponentBuilding start, MenuComponentBuilding end){
        if(buildings.contains(start)){
            buildingsToReplace.put(start,end);
        }
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawText(left,top,right,bottom, str);
    }
    public void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    private void action(String label, boolean enabled, ActionListener listener, ItemStack... tooltip){
        actionButtons.add(add(new MenuComponentActionButton(50/*+(actionButtons.size()*200)*/, /*Display.getHeight()-50*/actionButtons.size()*50, 250, 50, label, enabled, tooltip){
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        }));
    }
    private void taskButton(String label, MenuComponentWorker worker, Task task){
        action(label, task.canPerform(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                worker.task(task);
            }
        }, task.getTooltip());
    }
    private void removeWorker(MenuComponentWorker work){
        components.remove(work.button);
        components.remove(work);
        workers.remove(work);
        for(int i = 0; i<workers.size(); i++){
            MenuComponentWorker worker = workers.get(i);
            worker.button.y = i*50;
            worker.button.label = (i+1)+"";
        }
    }
    public void startAnim(Task task){
        add(new MenuComponentTaskAnimation(task.building.x, task.building.y, task.building.width, task.building.height, task.type.images, this, task){
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
    public boolean safe(MenuComponentWorker worker) {
        if(lost){
            return false;
        }
        boolean safe = !meteorShower;
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentShieldGenerator){
                MenuComponentShieldGenerator gen = (MenuComponentShieldGenerator) building;
                if(gen.shieldSize/2-50>=distance(gen, worker)){
                    safe = true;
                }
            }
        }
        return safe;
    }
    public boolean superSafe(MenuComponentWorker worker) {
        if(lost){
            return false;
        }
        boolean safe = false;
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentShieldGenerator){
                MenuComponentShieldGenerator gen = (MenuComponentShieldGenerator) building;
                if(gen.shieldSize/2-50>=distance(gen, worker)){
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
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentSkyscraper){
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper) building;
                if(sky.falling){
                    safe = false;
                }
            }
        }
        return safe;
    }
    public double distance(MenuComponent o1, MenuComponent o2){
        if(o1==null||o2==null){
            return -1;
        }
        return Math.sqrt(Math.pow((o1.x+o1.width/2)-(o2.x+o2.width/2), 2)+Math.pow((o1.y+o1.height/2)-(o2.y+o2.height/2), 2));
    }
    public double distanceTo(MenuComponent component, double x, double y) {
        return distance(component, new MenuComponentButton(x, y, 0, 0, "", false));
    }
    public void expedition(int workers){
        pendingExpedition = new Expedition(workers);
    }
    private int calculatePopulationCapacity() {
        int pop = 0;
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentSkyscraper){
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper) building;
                if(sky.game==null){
                    sky.game = this;
                }
                pop += sky.getMaxPop();
            }
        }
        return pop;
    }
    private int calculatePopulation(){
        int pop = 0;
        for(MenuComponentBuilding building : buildings){
            if(building instanceof MenuComponentSkyscraper){
                pop += ((MenuComponentSkyscraper)building).pop;
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
        config.set("first shower", firstShower);
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
            MenuComponentDroppedItem item = droppedItems.get(i);
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
            MenuComponentBuilding building = buildings.get(i);
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
        game.firstShower = config.get("first shower", false);
        if(hp!=-1){
            EnemyMothership ship = new EnemyMothership();
            ship.health = hp;
            game.mothership = game.add(ship);
        }
        game.furnace.level = config.get("furnace level", 0);
        game.furnace.ironOre = config.get("furnace iron", 0);
        game.autoTask = config.get("autotask", false);
        game.furnace.coal = config.get("furnace coal", 0);
        game.furnace.total = config.get("furnace xp", 0);
        game.furnace.auto = game.furnace.level>=2;
        MenuComponentEnemy.strength = config.get("enemy strength", 1d);
        game.enemyTimer = config.get("enemy timer", 20*30);
        Config cfg = config.get("Dropped Items", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            MenuComponentDroppedItem item = new MenuComponentDroppedItem(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), Item.getItemByName(cfg.get(i+" item","iron ingot")), game);
            item.life = cfg.get(i+" life", item.life);
            game.droppedItems.add(game.add(item));
        }
        game.meteorShower = config.get("Meteor Shower", game.meteorShower);
        game.meteorShowerTimer = config.get("Meteor Shower Timer", game.meteorShowerTimer);
        cfg = config.get("Buildings", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            MenuComponentBuilding b = MenuComponentBuilding.load(cfg.get(i+"", Config.newConfig()));
            if(b instanceof MenuComponentBase){
                game.base = (MenuComponentBase) b;
            }
            game.buildings.add(game.add(b));
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
        config.save();
        return game;
    }
    public void addCivilians(int civilians){
        for(MenuComponentBuilding b : buildings){
            if(b instanceof MenuComponentSkyscraper){
                MenuComponentSkyscraper s = (MenuComponentSkyscraper) b;
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
    private void damageReport(){
        damageReportTimer = damageReportTime;
    }
    public MenuComponentDrone addDrone(MenuComponentDrone drone){
        drones.add(drone);
        componentsToAdd.add(drone);
        return drone;
    }
    private void dispenseWorkers(){
        ArrayList<Task> tasks = new ArrayList<>();
        for(MenuComponentWorker worker : workers){
            if(worker.task!=null){
                if(worker.task instanceof TaskDemolish||worker.task.type==TaskType.REPAIR) continue;
                tasks.add(worker.task);
            }
        }
        if(tasks.isEmpty()){
            return;
        }
        ArrayList<MenuComponentWorker> available = new ArrayList<>();
        for(MenuComponentWorker worker : workers){
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
            strength = rand.nextInt(42-MenuComponentParticle.rainThreshold)+MenuComponentParticle.rainThreshold;
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
                    addParticleEffect(new MenuComponentParticle(xx+x, y, strength, rateOfChange, speed));
                    y-=40;
                }
                y = Y;
            }else{
                double Y = y;
                for(int i = 0; i<height; i++){
                    addParticleEffect(new MenuComponentParticle(xx+x, y-20, strength, rateOfChange, speed));
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
            for(MenuComponentBuilding building : buildings){
                if(building instanceof MenuComponentShieldGenerator){
                    MenuComponentShieldGenerator shield = (MenuComponentShieldGenerator) building;
                    if(distanceTo(building, x, y)<=shield.getShieldSize()/2){
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
            MenuComponentBuilding hit = null;
            for(MenuComponentBuilding building : buildings){
                if(building instanceof MenuComponentSkyscraper){
                    MenuComponentSkyscraper sky = (MenuComponentSkyscraper)building;
                    if(isClickWithinBounds(x, y, building.x, building.y-(sky.floorCount*sky.floorHeight), building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
                }else if(building instanceof MenuComponentBase){
                    if(isClickWithinBounds(x, y, building.x, building.y-25, building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
                }else{
                    if(isClickWithinBounds(x, y, building.x, building.y, building.x+building.width, building.y+building.height)){
                        hit = building;
                    }
                }
            }
            if(hit==null||!hit.damage(x,y)){
                //<editor-fold defaultstate="collapsed" desc="Hit ground">
                double dmgRad = 25;
                for(MenuComponentWorker worker : workers){
                    if(isClickWithinBounds(x, y, worker.x-dmgRad, worker.y-dmgRad, worker.x+worker.width+dmgRad, worker.y+worker.height+dmgRad)){
                        worker.damage(x,y);
                    }
                }
                for(MenuComponentDroppedItem item : droppedItems){
                    if(isClickWithinBounds(x, y, item.x-dmgRad, item.y-dmgRad, item.x+item.width+dmgRad,item.y+item.height+dmgRad)){
                        item.damage(x,y);
                    }
                }
                if(material!=null){
                    if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                        itemsToDrop.add(new MenuComponentDroppedItem(x+15, y+8, getItem(material), this));
                    }
                    if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                        itemsToDrop.add(new MenuComponentDroppedItem(x+8, y+28, getItem(material), this));
                    }
                    if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                        itemsToDrop.add(new MenuComponentDroppedItem(x+38, y+24, getItem(material),  this));
                    }
                    if(rand.nextBoolean()&&rand.nextBoolean()&&rand.nextBoolean()){
                        itemsToDrop.add(new MenuComponentDroppedItem(x+23, y+34, getItem(material), this));
                    }
                }
//</editor-fold>
            }
        }
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
     * @param radius The radius of the push field
     * @param distance How far to push the particles
     */
    public void pushParticles(double x, double y, double radius, double distance){
        for(MenuComponent component : components){
            if(component instanceof MenuComponentParticle){
                if(((MenuComponentParticle)component).type==ParticleEffectType.EXPLOSION)continue;
                if(Core.distance(component.x, component.y, x, y)<=radius){
                    double mult = 1-(Core.distance(component.x, component.y, x, y)/radius);
                    double distX = component.x-x;
                    double distY = component.y-y;
                    double totalDist = Math.sqrt(distX*distX+distY*distY);
                    distX/=totalDist;
                    distY/=totalDist;
                    if(Double.isNaN(distX)){
                        continue;
                    }
                    component.x+=distX*distance*mult;
                    component.y+=distY*distance*mult;
                    if(component instanceof ParticleFog){
                        ((ParticleFog)component).opacity-=.05;
                    }
                    if(((MenuComponentParticle)component).type==ParticleEffectType.CLOUD){
                        ((MenuComponentParticle)component).strength-=.5;
                    }
                }
            }
        }
    }
    public double debugYOffset = 0;
    public String debugText(double textHeight, String text){
        GL11.glColor4d(0, 0, 0, .25);
        drawRect(0, debugYOffset, FontManager.getLengthForStringWithHeight(text, textHeight)+1, debugYOffset+textHeight, 0);
        GL11.glColor4d(1, 1, 1, 1);
        text = drawTextWithWrap(1, debugYOffset+1, Display.getWidth()-1, debugYOffset+textHeight-1, text);
        debugYOffset+=textHeight;
        return text;
    }
}