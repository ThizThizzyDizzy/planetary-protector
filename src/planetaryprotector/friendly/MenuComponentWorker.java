package planetaryprotector.friendly;
import planetaryprotector.item.MenuComponentDroppedItem;
import planetaryprotector.Controls;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.task.TaskRepair;
import planetaryprotector.building.task.Task;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.BuildingType;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.enemy.MenuComponentAsteroid;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentWorker extends MenuComponent{
    private MenuGame game;
    public MenuComponentDroppedItem targetItem;
    public MenuComponentDroppedItem grabbedItem;
    public MenuComponentButton button;
    public static final int workerSpeed = 5;
    public int speed = workerSpeed;
    public boolean dead = false;
    public int opacitizing;
    public double opacity;
    public double[] target;
    public double[] runningFrom;
    public double[] selectedTarget;
    public Task task;
    public Task targetTask;
    public MenuComponentWorker(double x, double y, MenuGame myparent){
        super(x,y,10,10);
        this.game = myparent;
        button = new MenuComponentButton(0, myparent.workers.size()*50, 50, 50, myparent.workers.size()+1+"", true);
    }
    @Override
    public void renderBackground(){
        if(game.selectedWorker==this){
            GL11.glColor4d(1, 1, 1, opacity);
            drawRect(x-opacity*10, y-opacity*10, x+width+opacity*10, y+height+opacity*10, ImageStash.instance.getTexture("/textures/him.png"));
            GL11.glColor4d(1, 1, 1, 1);
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/him.png"));
    }
    public void render(){}
    @Override
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
                grabbedItem = null;
            }
            cancelTask();
            return;
        }
        if(game==null){
            game = (MenuGame)parent;
        }
        for(MenuComponent component : game.components){
            if(component instanceof MenuComponentAsteroid){
                if(Core.distance(component, this)<50){
                    for(MenuComponentBuilding building : game.buildings){
                        if(building.type==BuildingType.BUNKER&&Core.isPointWithinComponent(x+width/2, y+height/2, building)||game.superSafe(this)){
                            //whew, in a bunker or under a shield, safe.
                        }else{
                            runningFrom = new double[]{component.x+component.width/2,component.y+component.height/2};
                        }
                    }
                }
            }
        }
        for(MenuComponentBuilding building : game.buildings){
            if(building.type==BuildingType.SKYSCRAPER){
                if(((MenuComponentSkyscraper)building).falling){
                    if(Core.distance(building.x+building.width/2, building.y-((MenuComponentSkyscraper)building).fallen+building.height/2, x+width/2, y+height/2)<building.width*2){
                        runningFrom = new double[]{building.x+building.width/2, building.y+building.height/2};
                    }
                }
            }
        }
        //<editor-fold defaultstate="collapsed" desc="Running from something">
        if(runningFrom!=null){
            if(grabbedItem!=null){
                game.droppedItems.add(grabbedItem);
            }
            grabbedItem = null;
            targetItem = null;
            if(task!=null){
                task = null;
            }
            for(int i = 0; i<speed; i++){
                if(x+width/2>runningFrom[0]){
                    x++;
                }
                if(x+width/2<runningFrom[0]){
                    x--;
                }
                if(y+height/2>runningFrom[1]){
                    y++;
                }
                if(y+height/2<runningFrom[1]){
                    y--;
                }
            }
            runningFrom = null;
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="return - Working">
        if(task!=null){
            if(grabbedItem!=null){
                grabbedItem = null;
            }
            if(task.progress==0){
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
                grabbedItem = null;
            }
            double[] loc = new double[]{targetTask.building.x+targetTask.building.width/2,targetTask.building.y+targetTask.building.height/2};
            //<editor-fold defaultstate="collapsed" desc="Movement">
            if(loc!=null){
                for(int i = 0; i<speed; i++){
                    if(x+width/2>loc[0]){
                        x--;
                    }
                    if(x+width/2<loc[0]){
                        x++;
                    }
                    if(y+height/2>loc[1]){
                        y--;
                    }
                    if(y+height/2<loc[1]){
                        y++;
                    }
                }
                if(game.distanceTo(this,loc[0],loc[1])<=25){
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
                for(MenuComponentDroppedItem item : game.droppedItems){
                    if(game.distance(this, item)<=10){
                        game.droppedItems.remove(item);
                        grabbedItem = item;
                        break;
                    }
                }
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Put away Item">
            if(grabbedItem!=null&&game.distanceTo(this, game.base.x+game.base.width/2,game.base.y+game.base.width-12.5)<15){
                game.addResources(grabbedItem.item);
                game.componentsToRemove.add(grabbedItem);
                grabbedItem = null;
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Right Click Movement">
            if(selectedTarget!=null){
                for(int i = 0; i<speed; i++){
                    if(x+width/2>selectedTarget[0]){
                        x--;
                    }
                    if(x+width/2<selectedTarget[0]){
                        x++;
                    }
                    if(y+height/2>selectedTarget[1]){
                        y--;
                    }
                    if(y+height/2<selectedTarget[1]){
                        y++;
                    }
                }
                if(game.distanceTo(this,selectedTarget[0],selectedTarget[1])<=5){
                    selectedTarget = null;
                }
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="WASD Movement">
            if((targetItem!=null||grabbedItem!=null)&&target!=null){
                targetItem = null;
                target = null;
            }
            if(Keyboard.isKeyDown(Controls.up)){
                y -= speed;
            }
            if(Keyboard.isKeyDown(Controls.left)){
                x -= speed;
            }
            if(Keyboard.isKeyDown(Controls.down)){
                y += speed;
            }
            if(Keyboard.isKeyDown(Controls.right)){
                x += speed;
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
                for(MenuComponentDroppedItem item : game.droppedItems){
                    if(game.distance(this, item)<=10){
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
            if(grabbedItem!=null&&game.distanceTo(this, game.base.x+game.base.width/2,game.base.y+game.base.width-12.5)<15){
                game.addResources(grabbedItem.item);
                game.componentsToRemove.add(grabbedItem);
                grabbedItem = null;
            }
    //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Go to Item">
            if(targetItem==null&&grabbedItem==null){
                ONE:for(MenuComponentDroppedItem item : game.droppedItems){
                    for (Iterator<MenuComponentWorker> it = game.workers.iterator(); it.hasNext();) {
                        MenuComponentWorker c = it.next();
                        if(c.targetItem==item){
                            continue ONE;
                        }
                    }
                    targetItem = item;
                    target = new double[]{targetItem.x+targetItem.width/2,targetItem.y+targetItem.height/2};
                    break;
                }
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
                grabbedItem = null;
                targetItem = null;
                //<editor-fold defaultstate="collapsed" desc="Run">
                target = new double[]{game.base.x+game.base.width/2,game.base.y+game.base.height-12.5};
                for(MenuComponentBuilding building : game.buildings){
                    if(building.type==BuildingType.SHIELD_GENERATOR){
                        target = new double[]{building.x+building.width/2,building.y+building.height/2};
                    }
                }
                for(MenuComponentBuilding building : game.buildings){
                    if(building.type==BuildingType.BUNKER){
                        target = new double[]{building.x+building.width/2,building.y+building.height/2};
                    }
                }
//</editor-fold>
            }
            //<editor-fold defaultstate="collapsed" desc="Movement">
            if(target!=null){
                for(int i = 0; i<speed; i++){
                    if(x+width/2>target[0]){
                        x--;
                    }
                    if(x+width/2<target[0]){
                        x++;
                    }
                    if(y+height/2>target[1]){
                        y--;
                    }
                    if(y+height/2<target[1]){
                        y++;
                    }
                }
                if(grabbedItem!=null){
                    grabbedItem.x = x;
                    grabbedItem.y = y-10;
                }
                if(game.distanceTo(this,target[0],target[1])<=5){
                    target = null;
                }
            }
//</editor-fold>
        }
        for (Iterator<MenuComponentWorker> it = game.workers.iterator(); it.hasNext();) {
            MenuComponentWorker c = it.next();
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
        if(task instanceof TaskRepair){
            TaskRepair t = (TaskRepair) task;
            if(t.damage==null){
                return;
            }
        }
        this.task = task;
        task.building.worker = this;
        task.building.task=task;
    }
    public boolean isWorking(){
        return task!=null||targetTask!=null;
    }
    public void cancelTask(){
        if(task==null){
            if(game.selectedBuilding!=null&&game.selectedBuilding.task!=null){
                game.selectedBuilding.task.cancel();
                game.selectedBuilding.task = null;
            }
            return;
        }
        task.cancel();
        task.building.worker = null;
        task.building.task = null;
        task = null;
    }
    private void finishTask(){
        task.finish();
        task.building.worker = null;
        task.building.task = null;
        task = null;
    }
    public boolean isAvailable(){
        return !(dead||isWorking()||grabbedItem!=null||game.selectedWorker==this);
    }
    public void damage(double x, double y){
        for(MenuComponentBuilding building : game.buildings){
            if(building.type==BuildingType.BUNKER){
                if(game.distance(this, building)<=50){
                    return;
                }
            }
        }
        dead = true;
    }
}