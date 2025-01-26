package planetaryprotector.research.horizontal;
public abstract class HorizontalTense{
    public final void draw(float x, float y, float size){
        render(x, y, size);
    }
    protected abstract void render(float x, float y, float size);
}
