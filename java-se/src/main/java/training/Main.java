package training;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    void main() {
        //téglalap, x1,x2,y1,y2
        //kör x,y + sugár
        //ki kell számolni a középpontot
        this.run();
    }

    public void run() {
        var kor = new Kor(new Pont(1, 2), 3);
        var kozeppont = szamitKozeppont(kor);
        IO.println(kozeppont);
    }

    private Pont szamitKozeppont(Alakzat alakzat) {
        return switch (alakzat) {
            case Kor(Pont kozeppont, _) -> kozeppont;
            case Teglalap(var a, var b) -> new Pont((a.x() + b.x()) / 2, (a.y() + b.y()) / 2);
        };
    }
}
