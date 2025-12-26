package planetaryprotector.structure.task;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Wreck;
import java.util.ArrayList;
import planetaryprotector.anim.Animation;
import planetaryprotector.item.ItemStack;
public class TaskWreckClean extends TaskAnimated{
    private final Wreck wreck;
    public TaskWreckClean(Wreck wreck){
        super(wreck, TaskType.WRECK_CLEAN, Math.max(100,wreck.ingots));
        this.wreck = wreck;
    }
    @Override
    public String[] getDetails(){
        ArrayList<String> strs = new ArrayList<>();
        if(getWorkers()==0){
            strs.add("Cleaning "+structure.type.getDisplayName());
            strs.add("- "+Math.round(progress()*100)+"% completed");
        }else{
            int ticks = time-progress;
            ticks/=getWorkers();
            int seconds = 0;
            int minutes = 0;
            while(ticks>=20){
                seconds++;
                ticks-=20;
            }
            while(seconds>=60){
                minutes++;
                seconds-=60;
            }
            strs.add("Cleaning "+structure.type.getDisplayName());
            if(minutes>0){
                strs.add("- "+minutes+" minute"+(minutes==1?"":"s"));
            }
            if(seconds>0){
                strs.add("- "+seconds+" second"+(seconds==1?"":"s"));
            }
            strs.add("- "+Math.round(progress()*100)+"% completed");
        }
        return strs.toArray(new String[strs.size()]);
    }
    @Override
    public void work(){
        progress++;
//        if(wreck.ingots>0){
//            int itemX = structure.x+game.rand.nextInt(79)+11;
//            int itemY = structure.y+game.rand.nextInt(79)+11;
//            itemX-=5;
//            itemY-=5;
//            game.addItem(new DroppedItem(game, itemX, itemY, Item.ironIngot));
//            wreck.ingots--;
//        }
    }
    @Override
    public void finish(){
//        game.replaceStructure(structure, new Plot(game, structure.x, structure.y));
//        while(wreck.ingots>0){
//            int itemX = structure.x+game.rand.nextInt(79)+11;
//            int itemY = structure.y+game.rand.nextInt(79)+11;
//            itemX-=5;
//            itemY-=5;
//            game.addItem(new DroppedItem(game, itemX, itemY, Item.ironIngot));
//            wreck.ingots--;
//        }
    }
    @Override
    public boolean canPerform(){
        return structure.task==null;
    }
    @Override
    public void begin(){}
    @Override
    public ItemStack[] getTooltip(){
        return new ItemStack[0];
    }
    @Override
    protected void onCancel(){}
    @Override
    public Animation getAnimation(){
        return getAnimation("/textures/tasks/"+TaskType.WRECK_CLEAN.textureRoot+"/"+Game.theme.tex());
    }
    @Override
    public int getHeight(){
        return 100;
    }
    @Override
    public boolean isInBackground(){
        return true;
    }
}