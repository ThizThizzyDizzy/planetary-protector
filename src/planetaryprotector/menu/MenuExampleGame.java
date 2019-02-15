package planetaryprotector.menu;
import planetaryprotector.item.ItemStack;
import planetaryprotector.friendly.Worker;
import planetaryprotector.building.task.TaskDemolish;
import planetaryprotector.building.task.TaskWreckClean;
import planetaryprotector.building.task.TaskUpgrade;
import planetaryprotector.building.task.TaskConstruct;
import planetaryprotector.building.Mine;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.Building;
import planetaryprotector.building.CoalGenerator;
import planetaryprotector.building.Bunker;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.Plot;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.Silo;
import planetaryprotector.building.Base;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.GUI;
public class MenuExampleGame extends MenuGame{
    int timer = 0;
    public MenuExampleGame(GUI gui){
        super(gui, null, new Base(Display.getWidth()/2-50, Display.getHeight()/2-50), new ArrayList<>(), 3);
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
            Building b = Building.generateRandomBuilding(base, buildings);
            if(b!=null) buildings.add(b);
        }
        for(Worker w : workers){
            w.speed = 25;
            if(w.task!=null){
                w.task.progress = w.task.time;
            }
        }
        for(Building building : buildings){
            if(building.task!=null) continue;
            THAT:for(Worker worker : workers){
                if(!worker.isWorking()){
                    for(Worker w : workers){
                        if(w.targetTask!=null&&w.targetTask.building==building){
                            continue THAT;
                        }
                    }
                    if(building instanceof Wreck&&droppedItems.size()<1000){
                        building.task = new TaskWreckClean((Wreck) building);
                        worker.targetTask = building.task;
                    }
                    if(building instanceof Plot){
                        ArrayList<Building> targets = new ArrayList<>();
                        targets.add(new Bunker(building.x, building.y));
                        targets.add(new CoalGenerator(building.x, building.y));
                        targets.add(new Mine(building.x, building.y));
                        targets.add(new ShieldGenerator(building.x, building.y));
                        if(phase>=3) targets.add(new Silo(building.x, building.y));
                        targets.add(new Skyscraper(building.x, building.y));
                        Building target = targets.get(MenuGame.rand.nextInt(targets.size()));
                        building.task = new TaskConstruct(building, target);
                        worker.targetTask = building.task;
                    }
                    if(!(building instanceof Plot||building instanceof Wreck)&&rand.nextDouble()<0.00001){
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