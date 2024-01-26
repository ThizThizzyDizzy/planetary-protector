package planetaryprotector;
import planetaryprotector.game.Game;
import java.util.ArrayList;
import java.util.HashMap;
import planetaryprotector.game.GameState;
public class Expedition{
    public final int requiredWorkers;
    private final Game game;
    public int workers;
    public int time = 0;
    public int totalTime = 0;
    public int civilians = 0;
    public int civilianCooldown;
    public int dieCooldown;
    public boolean returning = false;
    public boolean done = false;
    public boolean returned = false;
    public HashMap<Integer, Integer> civilianGraph = new HashMap<>();
    public HashMap<Integer, Integer> workerGraph = new HashMap<>();
    public HashMap<Integer, Integer> civilianPromotionGraph = new HashMap<>();
    public HashMap<Integer, Boolean> returningGraph = new HashMap<>();
    public boolean recalled = false;
    public Expedition(Game game, int workers){
        requiredWorkers = workers;
        civilianGraph.put(0, 0);
        workerGraph.put(0, workers);
        lastWorkers = workers;
        this.game = game;
        civilianCooldown = game.rand.nextInt(20*60*4);
        dieCooldown = game.rand.nextInt(20*60*20);
    }
    public ArrayList<String> getText(){
        ArrayList<String> text = new ArrayList<>();
        if(!done){
            text.add("Current Expedition: ");
            if(workers!=requiredWorkers){
                text.add("Workers: "+workers+" ("+(workers-requiredWorkers<0?"-":"+")+(workers-requiredWorkers)+")");
            }
            text.add("Civilians: "+civilians);
            int ticks = totalTime;
            int seconds = 0;
            int minutes = 0;
            int hours = 0;
            while(ticks>=20){
                seconds++;
                ticks-=20;
            }
            while(seconds>=60){
                minutes++;
                seconds-=60;
            }
            while(minutes>=60){
                hours++;
                minutes-=60;
            }
            text.add("Total time:");
            if(hours>0){
                text.add("- "+hours+" hour"+(hours==1?"":"s"));
            }
            if(minutes>0){
                text.add("- "+minutes+" minute"+(minutes==1?"":"s"));
            }
            return text;
        }
        text.add("Expedition report: ");
        if(returned==false){
            text.add("Expedition Lost.");
            text.add("");
            return text;
        }
        if(workers!=requiredWorkers){
            text.add("Workers: "+workers+" ("+(workers-requiredWorkers<0?"-":"+")+(workers-requiredWorkers)+")");
        }
        text.add("Civilians: "+civilians);
        int ticks = totalTime;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        while(ticks>=20){
            seconds++;
            ticks-=20;
        }
        while(seconds>=60){
            minutes++;
            seconds-=60;
        }
        while(minutes>=60){
            hours++;
            minutes-=60;
        }
        text.add("Total time:");
        if(hours>0){
            text.add("- "+hours+" hour"+(hours==1?"":"s"));
        }
        if(minutes>0){
            text.add("- "+minutes+" minute"+(minutes==1?"":"s"));
        }
        return text;
    }
    int lastCivilians = 0;
    int lastWorkers = 0;
    public void tick(){
        for(int i = 0; i<(game.cheats?80*60:4*60); i++){
            if(done){
                return;
            }
            if(civilians!=lastCivilians){
                civilianGraph.put(totalTime, civilians);
                lastCivilians = civilians;
            }
            if(workers!=lastWorkers){
                workerGraph.put(totalTime, workers);
                lastWorkers = workers;
            }
            totalTime++;
            if(returning){
                time--;
                if(time<=0){
                    returned = true;
                    done = true;
                    civilianGraph.put(totalTime, civilians);
                    workerGraph.put(totalTime, workers);
                }
            }else{
                time++;
            }
            civilianCooldown--;
            if(civilianCooldown<=0){
                civilianCooldown = game.rand.nextInt(20*60*4);
                double newCivilians = Math.min(50, Math.max(-100, game.rand.nextGaussian()));
                if(newCivilians<0){
                    newCivilians*=-2;
                }
                while(newCivilians>0){
                    newCivilians--;
                    if(game.rand.nextInt(50)>0){
                        civilians++;
                    }else{
                        workers++;
                    }
                }
            }
            dieCooldown--;
            if(dieCooldown<=0){
                dieCooldown = game.rand.nextInt(20*60*15);
                double dead = game.rand.nextGaussian()*(civilians+workers)/25;
                while(dead>0){
                    dead--;
                    if(game.rand.nextInt(civilians+workers)>workers){
                        civilians--;
                    }else{
                        workers--;
                    }
                }
            }
            if(workers*20<civilians&&civilians>20&&workers<requiredWorkers){
                workers++;
                civilians--;
                civilianPromotionGraph.put(totalTime, 1);
            }else if(workers*50<civilians&&civilians>50){
                workers++;
                civilians--;
                civilianPromotionGraph.put(totalTime, 1);
            }
            if(workers<=0){
                done = true;
                civilianGraph.put(totalTime, civilians);
                workerGraph.put(totalTime, workers);
            }
            civilians = Math.max(0, civilians);
            if(!recalled){
                if(civilians>=100*requiredWorkers&&!returning){
                    returning = true;
                    returningGraph.put(totalTime, true);
                }else if(civilians<=80*requiredWorkers&&returning&&time<=20*60*2.5){
                    returning = false;
                    returningGraph.put(totalTime, false);
                }
            }
        }
    }
    public GameState.Expedition save(){
        GameState.Expedition state = new GameState.Expedition();
        state.requiredWorkers = requiredWorkers;
        state.workers = workers;
        state.time = time;
        state.totalTime = totalTime;
        state.civilians = civilians;
        state.civilianCooldown = civilianCooldown;
        state.dieCooldown = dieCooldown;
        state.returning = returning;
        state.recalled = recalled;
        state.done = done;
        state.returned = returned;
        return state;
    }
    public static Expedition load(GameState.Expedition state, Game game){
        Expedition e = new Expedition(game, state.requiredWorkers);
        e.workers = state.workers;
        e.time = state.time;
        e.totalTime = state.totalTime;
        e.civilians = state.civilians;
        e.civilianCooldown = state.civilianCooldown;
        e.dieCooldown = state.dieCooldown;
        e.returning = state.returning;
        e.recalled = state.recalled;
        e.done = state.done;
        e.returned = state.returned;
        return e;
    }
    @Override
    public String toString(){
        String str = "";
        for(String string : getText()){
            str+=" | "+string;
        }
        if(str.isEmpty())return str;
        return str.substring(3);
    }
}