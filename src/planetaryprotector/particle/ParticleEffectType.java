package planetaryprotector.particle;
public enum ParticleEffectType{
    SMOKE("smoke", 1),
    EXPLOSION("smoke", 1),
    CLOUD("smoke", 1),
    FOG("fog", 1),
    FIRE("smoke", 1);
    public final String[] images;
    public final String texture;
    public final int frames;
    private ParticleEffectType(String texture, int frames) {
        this.texture = texture;
        this.frames = frames;
        images = new String[frames];
    }
}