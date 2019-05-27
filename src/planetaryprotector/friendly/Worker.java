package planetaryprotector.friendly;
import java.util.Collections;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.Controls;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.task.TaskRepair;
import planetaryprotector.building.task.Task;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.GameObject;
import planetaryprotector.building.Skyscraper;
import planetaryprotector.building.task.TaskDemolish;
import planetaryprotector.enemy.Asteroid;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class Worker extends GameObject{
    public DroppedItem targetItem;
    public DroppedItem grabbedItem;
    public MenuComponentButton button;
    public static final int workerSpeed = 5;
    public int speed = workerSpeed;
    public int opacitizing;
    public double opacity;
    public double[] target;
    public double[] runningFrom;
    public double[] selectedTarget;
    public Task task;
    public Task targetTask;
    private final MenuGame game;
    public Worker(double x, double y, MenuGame game){
        super(x,y,10,10);
        button = new MenuComponentButton(0, game.workers.size()*50, 50, 50, game.workers.size()+1+"", true);
        this.game = game;
    }
    @Override
    public void render(){
        if(game.selectedWorker==this){
            GL11.glColor4d(1, 1, 1, opacity);
            drawRect(x-opacity*10, y-opacity*10, x+width+opacity*10, y+height+opacity*10, ImageStash.instance.getTexture("/textures/him.png"));
            GL11.glColor4d(1, 1, 1, 1);
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/him.png"));
        if(grabbedItem!=null){
            grabbedItem.render();
        }
    }
    public void tick(){
        if(game.selectedWorker==this){
            if(!game.paused){
                if(opacity>=0.5){
                    opacitizing = -1;
                }
                if(opacity<=0){
                    opacitizing = 1;
                }
                opacity += (opacitizing*0.025);
            }
        }
        if(dead){
            if(grabbedItem!=null){
                dropItem();
            }
            cancelTask();
            return;
        }
        for(Asteroid asteroid : game.asteroids){
            if(Core.distance(asteroid, this)<50){
                for(Building building : game.buildings){
                    if(building.type==BuildingType.BUNKER&&Core.isPointWithinComponent(x+width/2, y+height/2, building)||game.superSafe(this)){
                        //whew, in a bunker or under a shield, safe.
                    }else{
                        runningFrom = new double[]{asteroid.x+asteroid.width/2,asteroid.y+asteroid.height/2};
                    }
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
        //<editor-fold defaultstate="collapsed" desc="Running from something">
        if(runningFrom!=null){
            dropItem();
            targetItem = null;
            if(task!=null){
                task = null;
            }
            moveAway(runningFrom);
            runningFrom = null;
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
        if(game.selectedWorker==this){
            //<editor-fold defaultstate="collapsed" desc="Grab Item">
            if((!game.droppedItems.isEmpty())&&grabbedItem==null){
                for(DroppedItem item : game.droppedItems){
                    if(Core.distance(this, item.x+item.width/2, item.y+item.height/2)<=10){
                        game.droppedItems.remove(item);
                        grabbedItem = item;
                        break;
                    }
                }
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Put away Item">
            if(grabbedItem!=null&&Core.distance(this, game.base.x+game.base.width/2,game.base.y+game.base.width-12.5)<15){
                game.addResources(grabbedItem.item);
                grabbedItem.dead = true;
                dropItem();
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Right Click Movement">
            if(selectedTarget!=null){
                move(selectedTarget);
                if(Core.distance(this,selectedTarget[0],selectedTarget[1])<=speed){
                    selectedTarget = null;
                }
            }
//</editor-fold>
            if((targetItem!=null||grabbedItem!=null)&&target!=null){
                targetItem = null;
                target = null;
            }
            //<editor-fold defaultstate="collapsed" desc="WASD Movement">
            boolean move = false;
            if(Keyboard.isKeyDown(Controls.up)){
                if(selectedTarget==null)selectedTarget = new double[]{x+width/2,y+height/2};
                selectedTarget[1]-=speed;
                move = true;
            }
            if(Keyboard.isKeyDown(Controls.left)){
                if(selectedTarget==null)selectedTarget = new double[]{x+width/2,y+height/2};
                selectedTarget[0]-=speed;
                move = true;
            }
            if(Keyboard.isKeyDown(Controls.down)){
                if(selectedTarget==null)selectedTarget = new double[]{x+width/2,y+height/2};
                selectedTarget[1]+=speed;
                move = true;
            }
            if(Keyboard.isKeyDown(Controls.right)){
                if(selectedTarget==null)selectedTarget = new double[]{x+width/2,y+height/2};
                selectedTarget[0]+=speed;
                move = true;
            }
            if(selectedTarget!=null&&move){
                move(selectedTarget);
                selectedTarget = null;
            }
            if(grabbedItem!=null){
                grabbedItem.x = x;
                grabbedItem.y = y-10;
            }
//</editor-fold>
            target = null;
            targetItem = null;
        }else{
            selectedTarget = null;
            //<editor-fold defaultstate="collapsed" desc="Grab Item">
            if((!game.droppedItems.isEmpty())&&grabbedItem==null){
                for(DroppedItem item : game.droppedItems){
                    if(Core.distance(this, item.x+item.width/2, item.y+item.height/2)<=10){
                        game.droppedItems.remove(item);
                        grabbedItem = item;
                        targetItem = null;
                        target = null;
                        break;
                    }
                }
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Put away Item">
            if(grabbedItem!=null&&Core.distance(this, game.base.x+game.base.width/2,game.base.y+game.base.width-12.5)<15){
                game.addResources(grabbedItem.item);
                grabbedItem.dead = true;
                dropItem();
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Go to Item">
            if(targetItem==null&&grabbedItem==null){
                Collections.sort(game.droppedItems, (o1, o2) -> {
                    float x1 = (int)o1.x-(int)x, y1 = (int)o1.y-(int)y;
                    float x2 = (int)o2.x-(int)x, y2 = (int)o2.y-(int)y;
                    return (int)Math.sqrt(x1*x1+y1*y1)-(int)Math.sqrt(x2*x2+y2*y2);
                });
                ONE:for(DroppedItem item : game.droppedItems){
                    for (Iterator<Worker> it = game.workers.iterator(); it.hasNext();) {
                        Worker c = it.next();
                        if(c.targetItem==item){
                            continue ONE;
                        }
                    }
                    targetItem = item;
                    break;
                }
            }
            if(targetItem!=null&&grabbedItem==null){
                target = new double[]{targetItem.x+targetItem.width/2,targetItem.y+targetItem.height/2};
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Head to base">
            if(((game.droppedItems.isEmpty())&&grabbedItem==null)||grabbedItem!=null){
                target = new double[]{game.base.x+game.base.width/2,game.base.y+game.base.height-12.5};
            }
    //</editor-fold>
            if(!game.safe(this)){
                if(grabbedItem!=null){
                    game.droppedItems.add(grabbedItem);
                }
                dropItem();
                targetItem = null;
                //<editor-fold defaultstate="collapsed" desc="Run">
                target = new double[]{game.base.x+game.base.width/2,game.base.y+game.base.height-12.5};
                for(Building building : game.buildings){
                    if(building.type==BuildingType.SHIELD_GENERATOR){
                        target = new double[]{building.x+building.width/2,building.y+building.height/2};
                    }
                }
                for(Building building : game.buildings){
                    if(building.type==BuildingType.BUNKER){
                        target = new double[]{building.x+building.width/2,building.y+building.height/2};
                    }
                }
//</editor-fold>
            }
            //<editor-fold defaultstate="collapsed" desc="Movement">
            if(target!=null){
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
        }
        for (Iterator<Worker> it = game.workers.iterator(); it.hasNext();) {
            Worker c = it.next();
            if(targetItem==c.grabbedItem){
                targetItem = null;
            }
        }
        if(y+width>Display.getHeight()){
            y = Display.getHeight()-height;
        }
        if(x+height>Display.getWidth()){
            x = Display.getWidth()-width;
        }
        if(x<0){
            x=0;
        }
        if(y<0){
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
    @Deprecated
    public void cancelTask(){
        if(task==null){
            if(game.selectedBuilding!=null&&game.selectedBuilding.task!=null){
                game.selectedBuilding.task.cancel();
                game.selectedBuilding.task = null;
            }
            return;
        }
        task.cancel();
        task.building.task = null;
        task = null;
    }
    private void finishTask(){
        if(task instanceof TaskDemolish){
            x = Core.game.base.x+Core.game.base.width/2;
            y = Core.game.base.y+Core.game.base.height/2;
        }
        task.finishTask();
        task.building.task = null;
        task = null;
    }
    public boolean isAvailable(){
        return !(dead||isWorking()||grabbedItem!=null||game.selectedWorker==this);
    }
    public void damage(double x, double y){
        for(Building building : game.buildings){
            if(building.type==BuildingType.BUNKER){
                if(Core.distance(this, building)<=50){
                    return;
                }
            }
        }
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
            synchronized(game.droppedItems){
                game.droppedItems.add(grabbedItem);
            }
        }
        grabbedItem = null;
    }
}
