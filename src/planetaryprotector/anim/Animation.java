package planetaryprotector.anim;
import java.util.ArrayList;
public class Animation{
    public ArrayList<Frame> frames = new ArrayList<>();
    public void addFrame(String s){
        addFrame(new Frame(s));
    }
    public void addFrame(int i){
        addFrame(new Frame(i));
    }
    public void addFrame(Frame frame){
        frames.add(frame);
    }
    public boolean isEmpty(){
        return frames.isEmpty();
    }
    public int length(){
        return frames.size();
    }
    public int getTexture(int frame){
        return frames.get(frame).getTexture();
    }
}