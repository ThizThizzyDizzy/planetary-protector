package planetaryprotector.friendly;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import planetaryprotector.GameObject;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.game.Game;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.research.ResearchEvent;
import planetaryprotector.structure.Base;
import planetaryprotector.structure.Skyscraper;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.task.Task;
import planetaryprotector.structure.task.TaskDemolish;
public class Worker extends GameObject{
    public DroppedItem targetItem;
    public DroppedItem grabbedItem;
    public static final int workerSpeed = 8;
    public int speed = workerSpeed;
    public int[] target;
    public int[] runningFrom;
    public int[] selectedTarget;
    public Task task;
    public Task targetTask;
    private final Game game;
    public Worker(Game game, int x, int y){
        super(game, x,y,10,10);
        this.game = game;
    }
    @Override
    public void draw(){
        Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture("/textures/worker.png"));
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
//        if(base==null)runningFrom = game.getCityBoundingBox().getCenter();
        for(Asteroid asteroid : game.asteroids){
            if(asteroid.getCenter().distance(getCenter())<50){
                if(!game.superSafe(this)){
                    runningFrom = new int[]{asteroid.x+asteroid.width/2,asteroid.y+asteroid.height/2};
                }
            }
        }
        for(Structure structure : game.structures){
            if(structure instanceof Skyscraper){
                if(((Skyscraper)structure).falling){
//                    if(structure.getCenter().distance(getCenter())<structure.width*3/4){
//                        runningFrom = new int[]{structure.x+structure.width/2, structure.y+structure.height/2};
//                    }
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
//            int[] loc = new int[]{targetTask.structure.x+targetTask.structure.width/2,targetTask.structure.y+targetTask.structure.height/2};
//            //<editor-fold defaultstate="collapsed" desc="Movement">
//            if(loc!=null){
//                move(loc);
//                if(getCenter().distance(loc[0], loc[1])<=25){
//                    task = targetTask;
//                    targetTask = null;
//                }
//            }
////</editor-fold>
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
            ArrayList<DroppedItem> items = new ArrayList<>(game.droppedItems);
            Collections.sort(items, (o1, o2) -> {
                if(o1.item.priority!=o2.item.priority){
                    return o2.item.priority-o1.item.priority;
                }
                float x1 = o1.getPosition().x-x, y1 = o1.getPosition().y-y;
                float x2 = o2.getPosition().x-x, y2 = o2.getPosition().y-y;
                return (int)Math.sqrt(x1*x1+y1*y1)-(int)Math.sqrt(x2*x2+y2*y2);
            });
            ONE:for(DroppedItem item : items){
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
            target = new int[]{(int)targetItem.getPosition().x,(int)targetItem.getPosition().y};
            //<editor-fold defaultstate="collapsed" desc="Grab Item">
            if(getCenter().distance(targetItem.getPosition().x, targetItem.getPosition().y)<=10){
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
//            for(Structure structure : game.structures){
//                if(structure instanceof ShieldGenerator){
//                    ShieldGenerator gen = (ShieldGenerator) structure;
//                    target = new int[]{structure.x+gen.shield.x,structure.y+gen.shield.y};
//                }
//            }
//</editor-fold>
        }
        if(target==null&&base!=null){
            target = new int[]{base.getWorkerX(),base.getWorkerY()};
        }
        //<editor-fold defaultstate="collapsed" desc="Movement">
        if(target!=null&&runningFrom==null){
            move(target);
            if(grabbedItem!=null){
                grabbedItem.setPosition(new org.joml.Vector3f(x, y, 10));
            }
            if(getCenter().distance(target[0],target[1])<=workerSpeed){
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
        task.structure.setTask(null);
        task = null;
    }
    public boolean isAvailable(){
        return !(dead||isWorking()||grabbedItem!=null);
    }
    public void damage(double x, double y){
        dead = true;
    }
    private void move(int[] location){
        int[] loc = new int[]{location[0]-width/2, location[1]-height/2};
        double xDiff = loc[0]-x;
        double yDiff = loc[1]-y;
        double dist = Math.sqrt(xDiff*xDiff+yDiff*yDiff);
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
    private void moveAway(int[] loc){
        double xDiff = loc[0]-x;
        double yDiff = loc[1]-y;
        double dist = Math.sqrt(xDiff*xDiff+yDiff*yDiff);
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
        for(Structure s : game.structures){
            if(s instanceof Base){
                if(((Base) s).deathTick>=0)continue;
                double d = getCenter().distance(((Base) s).getWorkerX(),((Base) s).getWorkerY());
                if(closest==null||d<dist){
                    closest = (Base) s;
                    dist = d;
                }
            }
        }
        return closest;
    }
    private double getClosestBaseDistance(){
        Base base = getClosestBase();
        if(base==null)return -1;
        return getCenter().distance(base.getWorkerX(),base.getWorkerY());
    }
}
