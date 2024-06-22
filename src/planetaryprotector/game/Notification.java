package planetaryprotector.game;
public class Notification{
    public String name;
    public int time;
    private final int maxTime;
    private int num = 1;
    public int height = 20;//time counts down, decrease to zero
    public float width = 0;//increase to 1
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