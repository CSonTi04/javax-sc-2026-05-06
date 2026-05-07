package training;

//két átellenes pont
public record Teglalap(
        Pont a, Pont b
) implements Alakzat {
    @Override
    public Pont getKozeppont() {
        return null;
    }
}
