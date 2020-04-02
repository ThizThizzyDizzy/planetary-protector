package planetaryprotector.friendly;
import java.util.Collections;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.game.Game;
import planetaryprotector.building.task.Task;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import planetaryprotector.Core;
import planetaryprotector.GameObject;
import planetaryprotector.building.Base;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.task.TaskDemolish;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.research.ResearchEvent;
import simplelibrary.opengl.ImageStash;
public class Worker extends GameObject{
    public DroppedItem targetItem;
    public DroppedItem grabbedItem;
    public static final int workerSpeed = 5;
    public int speed = workerSpeed;
    public double[] target;
    public double[] runningFrom;
    public double[] selectedTarget;
    public Task task;
    public Task targetTask;
    private final Game game;
    public Worker(Game game, double x, double y){
        super(game, x,y,10,10);
        this.game = game;
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/worker.png"));
        if(grabbedItem!=null){
            grabbedItem.render();
        }
    }
    public void tick(){
        if(dead){
            if(grabbedItem!=null){
                dropItem();
            }
            return;
        }
        Base base = getClosestBase();
        double baseDist = getClosestBaseDistance();
        runningFrom = null;
        if(base==null)runningFrom = new double[]{Display.getWidth()/2, Display.getHeight()/2};
        for(Asteroid asteroid : game.asteroids){
            if(Core.distance(asteroid, this)<50){
                if(!game.superSafe(this)){
                    runningFrom = new double[]{asteroid.x+asteroid.width/2,asteroid.y+asteroid.height/2};
                }
            }
        }
        for(Building building : game.buildings){
            if(building.type==BuildingType.SKYSCRAPER){
                if(((Skyscraper)building).falling){
                    if(Core.distance(building.x+building.width/2, building.y-((Skyscraper)building).fallen+building.height/2, x+width/2, y+height/2)<building.width*3/4){
                        runningFrom = new double[]{building.x+building.width/2, building.y+building.height/2};
                    }
                }
            }
        }
        if(targetItem!=null&&targetItem.dead){
            targetItem = null;
            target = null;
        }
//</editor-fold>
        if(grabbedItem!=null&&grabbedItem.dead)grabbedItem = null;
        //<editor-fold defaultstate="collapsed" desc="Running from something">
        if(runningFrom!=null){
            dropItem();
            targetItem = null;
            if(task!=null){
                task = null;
            }
            moveAway(runningFrom);
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="return - Working">
        if(task!=null){
            if(grabbedItem!=null){
                dropItem();
            }
            if(!task.started){
                task.start();
            }
            task.work();
            if(task.progress()>=1){
                finishTask();
            }
            return;
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="return - Going to work">
        if(targetTask!=null&&targetTask.finished){
            targetTask = null;
        }
        if(targetTask!=null){
            if(grabbedItem!=null){
                dropItem();
            }
            double[] loc = new double[]{targetTask.building.x+targetTask.building.width/2,targetTask.building.y+targetTask.building.height/2};
            //<editor-fold defaultstate="collapsed" desc="Movement">
            if(loc!=null){
                move(loc);
                if(Core.distance(this,loc[0],loc[1])<=25){
                    task = targetTask;
                    targetTask = null;
                }
            }
//</editor-fold>
            return;
        }
//</editor-fold>
        selectedTarget = null;
        //<editor-fold defaultstate="collapsed" desc="Put away Item">
        if(grabbedItem!=null&&baseDist<15&&baseDist>-1){
            game.addResources(grabbedItem.item);
            game.researchEvent(new ResearchEvent(ResearchEvent.Type.GAIN_RESOURCE, grabbedItem.item, 1));
            grabbedItem.dead = true;
            dropItem();
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Go to Item">
        if(targetItem==null&&grabbedItem==null){
            Collections.sort(game.droppedItems, (o1, o2) -> {
                if(o1.item.priority!=o2.item.priority){
                    return o2.item.priority-o1.item.priority;
                }
                float x1 = (int)o1.x-(int)x, y1 = (int)o1.y-(int)y;
                float x2 = (int)o2.x-(int)x, y2 = (int)o2.y-(int)y;
                return (int)Math.sqrt(x1*x1+y1*y1)-(int)Math.sqrt(x2*x2+y2*y2);
            });
            ONE:for(DroppedItem item : game.droppedItems){
                for(Worker c : game.workers){
                    if(c.targetItem==item){
                        continue ONE;
                    }
                }
                targetItem = item;
                break;
            }
        }
//</editor-fold>
        if(targetItem!=null&&grabbedItem==null){
            target = new double[]{targetItem.x+targetItem.width/2,targetItem.y+targetItem.height/2};
            //<editor-fold defaultstate="collapsed" desc="Grab Item">
            if(Core.distance(this, targetItem.x+targetItem.width/2, targetItem.y+targetItem.height/2)<=10){
                game.droppedItems.remove(targetItem);
                grabbedItem = targetItem;
                targetItem = null;
                target = null;
            }
        }
//</editor-fold>
        if(!game.safe(this)){
            if(grabbedItem!=null){
                game.droppedItems.add(grabbedItem);
            }
            dropItem();
            targetItem = null;
            //<editor-fold defaultstate="collapsed" desc="Run">
            target = null;
            for(Building building : game.buildings){
                if(building.type==BuildingType.SHIELD_GENERATOR){
                    target = new double[]{building.x+building.width/2,building.y+building.height/2};
                }
            }
//</editor-fold>
        }
        if(target==null&&base!=null){
            target = new double[]{base.getWorkerX(),base.getWorkerY()};
        }
        //<editor-fold defaultstate="collapsed" desc="Movement">
        if(target!=null&&runningFrom==null){
            move(target);
            if(grabbedItem!=null){
                grabbedItem.x = x;
                grabbedItem.y = y-10;
            }
            if(Core.distance(this,target[0],target[1])<=workerSpeed){
                target = null;
            }
        }
//</editor-fold>
        for(Iterator<Worker> it = game.workers.iterator(); it.hasNext();){
            Worker c = it.next();
            if(targetItem==c.grabbedItem){
                targetItem = null;
                target = null;
            }
        }
        if(y+width>Display.getHeight()){
            if(base==null)dead = true;
            y = Display.getHeight()-height;
        }
        if(x+height>Display.getWidth()){
            if(base==null)dead = true;
            x = Display.getWidth()-width;
        }
        if(x<0){
            if(base==null)dead = true;
            x=0;
        }
        if(y<0){
            if(base==null)dead = true;
            y=0;
        }
    }
    public void task(Task task){
        task.start();
        targetTask = task;
    }
    public boolean isWorking(){
        return task!=null||targetTask!=null;
    }
    private void finishTask(){
        Base base = getClosestBase();
        if(task instanceof TaskDemolish){
            x = base.getWorkerX();
            y = base.getWorkerY();
        }
        task.finishTask();
        task.building.setTask(null);
        task = null;
    }
    public boolean isAvailable(){
        return !(dead||isWorking()||grabbedItem!=null);
    }
    public void damage(double x, double y){
        dead = true;
    }
    private void move(double[] location){
        double[] loc = new double[]{location[0]-width/2, location[1]-height/2};
        double xDiff = loc[0]-x;
        double yDiff = loc[1]-y;
        double dist = Core.distance(0, 0, xDiff, yDiff);
        if(dist<=speed){
            x = loc[0];
            y = loc[1];
            return;
        }
        xDiff/=dist;
        yDiff/=dist;
        x+=xDiff*speed;
        y+=yDiff*speed;
    }
    private void moveAway(double[] loc){
        double xDiff = loc[0]-x;
        double yDiff = loc[1]-y;
        double dist = Core.distance(0, 0, xDiff, yDiff);
        xDiff/=dist;
        yDiff/=dist;
        x-=xDiff*speed;
        y-=yDiff*speed;
    }
    private void dropItem(){
        if(grabbedItem==null)return;
        if(!grabbedItem.dead){
            game.droppedItems.add(grabbedItem);
        }
        grabbedItem = null;
    }
    private Base getClosestBase(){
        Base closest = null;
        double dist = 0;
        for(Building b : game.buildings){
            if(b instanceof Base){
                if(((Base) b).deathTick>=0)continue;
                double d = Core.distance(this, ((Base) b).getWorkerX(),((Base) b).getWorkerY());
                if(closest==null||d<dist){
                    closest = (Base) b;
                    dist = d;
                }
            }
        }
        return closest;
    }
    private double getClosestBaseDistance(){
        Base base = getClosestBase();
        if(base==null)return -1;
        return Core.distance(this, base.getWorkerX(),base.getWorkerY());
    }
}
