package planetaryprotector.menu;
import planetaryprotector.item.ItemStack;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.building.task.TaskDemolish;
import planetaryprotector.building.task.TaskWreckClean;
import planetaryprotector.building.task.TaskUpgrade;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.building.MenuComponentMine;
import planetaryprotector.building.MenuComponentShieldGenerator;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentGenerator;
import planetaryprotector.building.MenuComponentBunker;
import planetaryprotector.building.MenuComponentWreck;
import planetaryprotector.building.MenuComponentPlot;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentSilo;
import planetaryprotector.building.MenuComponentBase;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
public class MenuExampleGame extends MenuGame{
    int timer = 0;
    public MenuExampleGame(GUI gui){
        super(gui, null, new MenuComponentBase(Display.getWidth()/2-50, Display.getHeight()/2-50), new ArrayList<>(), 3);
        doNotDisturb = true;
    }
    @Override
    public void tick(){
        timer--;
        for(ItemStack stack : base.resources){
            stack.count = 1000000;
        }
        if(workers.size()<10){
            addWorker();
        }
        if(timer<=0){
            timer+=5;
            MenuComponentBuilding b = MenuComponentBuilding.generateRandomBuilding(base, buildings);
            if(b!=null) buildings.add(add(b));
        }
        for(MenuComponentWorker w : workers){
            w.speed = 25;
            if(w.task!=null){
                w.task.progress = w.task.time;
            }
        }
        for(MenuComponentBuilding building : buildings){
            if(building.task!=null) continue;
            THAT:for(MenuComponentWorker worker : workers){
                if(!worker.isWorking()){
                    for(MenuComponentWorker w : workers){
                        if(w.targetTask!=null&&w.targetTask.building==building){
                            continue THAT;
                        }
                    }
                    if(building instanceof MenuComponentWreck&&droppedItems.size()<1000){
                        building.task = new TaskWreckClean((MenuComponentWreck) building);
                        worker.targetTask = building.task;
                    }
                    if(building instanceof MenuComponentPlot){
                        ArrayList<MenuComponentBuilding> targets = new ArrayList<>();
                        targets.add(new MenuComponentBunker(building.x, building.y));
                        targets.add(new MenuComponentGenerator(building.x, building.y));
                        targets.add(new MenuComponentMine(building.x, building.y));
                        targets.add(new MenuComponentShieldGenerator(building.x, building.y));
                        if(phase>=3) targets.add(new MenuComponentSilo(building.x, building.y));
                        targets.add(new MenuComponentSkyscraper(building.x, building.y));
                        MenuComponentBuilding target = targets.get(MenuGame.rand.nextInt(targets.size()));
                        building.task = new TaskConstruct(building, target);
                        worker.targetTask = building.task;
                    }
                    if(!(building instanceof MenuComponentPlot||building instanceof MenuComponentWreck)&&rand.nextDouble()<0.00001){
                        building.task = new TaskDemolish(building);
                        worker.targetTask = building.task;
                    }
                    if(building.canUpgrade()&&rand.nextDouble()<0.01){
                        building.task = new TaskUpgrade(building);
                        worker.targetTask = building.task;
                    }
                }
            }
        }
        super.tick(); //To change body of generated methods, choose Tools | Templates.
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
}