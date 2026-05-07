package training;

public record Kor(
        Pont kozeppont, double sugar
) implements Alakzat {
    @Override
    public Pont getKozeppont() {
        return null;
    }
}
