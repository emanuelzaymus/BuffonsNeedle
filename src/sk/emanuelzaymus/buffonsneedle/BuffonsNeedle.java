package sk.emanuelzaymus.buffonsneedle;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
public class BuffonsNeedle {

    private final Random seedRandom = new Random();

    private final Random positionRandom = new Random(seedRandom.nextInt());
    private final Random angleRandom = new Random(seedRandom.nextInt());

    private final double gapWidth;
    private final double needleLength;
    private final int throwsCount;
    private final int saveFromNumber;
    private final int saveEvery;

    private final List<Pair<Number, Number>> piEstimations;
    private double piResult;

    public BuffonsNeedle(final double gapWidth, final double needleLength, final int throwsCount,
                         final double skipPercent, final int estimationsCount) throws IllegalArgumentException {
        if (gapWidth <= needleLength) {
            throw new IllegalArgumentException("Needle is longer than gap.");
        }
        if (skipPercent < 0 || skipPercent > 1) {
            throw new IllegalArgumentException("Parameter skipPrecent needs to be a number between 0 and 1.");
        }

        this.gapWidth = gapWidth;
        this.needleLength = needleLength;
        this.throwsCount = throwsCount;
        this.saveFromNumber = (int) Math.ceil(throwsCount * skipPercent);
        this.saveEvery = (throwsCount - saveFromNumber) / estimationsCount;

        piEstimations = new LinkedList<>();
    }

    public double getPiResult() {
        return piResult;
    }

    public List<Pair<Number, Number>> getPiEstimations() {
        return piEstimations;
    }

    public void estimatePi() {
        int countLineCrossed = 0;

        for (int throwNum = 0; throwNum < throwsCount; throwNum++) {

            if (lineCrossedExperiment())
                countLineCrossed++;

            if (saveEstimation(throwNum)) {
                piEstimations.add(new Pair<>(throwNum, countPi(countLineCrossed, throwNum)));
            }
        }

        piResult = countPi(countLineCrossed);
    }

    private boolean lineCrossedExperiment() {
        final double leftNeedlePoint = positionRandom.nextDouble() * gapWidth;
        final double needleAngle = Math.sin(angleRandom.nextDouble() * Math.PI);
        final double triangleLeg = needleAngle * needleLength;

        return leftNeedlePoint + triangleLeg >= gapWidth;
    }

    private boolean saveEstimation(int throwNum) {
        if (saveFromNumber > throwNum) {
            return false;
        }
        return throwNum % saveEvery == 0;
    }

    private boolean saveEstimation2(int throwNum) {
        return saveFromNumber > throwNum && throwNum % saveEvery == 0;
    }

    private double countPi(final int countLineCrossed) {
        return countPi(countLineCrossed, throwsCount);
    }

    private double countPi(final int countLineCrossed, final int throwsCount) {
        return (2 * needleLength) / ((countLineCrossed / (double) throwsCount) * gapWidth);
    }

}
