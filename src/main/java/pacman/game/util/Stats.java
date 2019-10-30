package pacman.game.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds iteratively built statistical observations without storing the individual observations
 * <p>
 * Created by pwillic on 26/02/2016.
 */
public class Stats {

    private double average;
    private double sum;
    private double sumsq;
    private double sd;
    private int n;
    private List<Double> results;

    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    private boolean computed;

    private String description;
    private long msTaken;

    public Stats(String description) {
        this.description = description;
        this.results = new ArrayList<>();
    }

    public Stats(long msTaken, String description, double max, double min, int n, double sumsq, double sum) {
        this.msTaken = msTaken;
        this.description = description;
        this.max = max;
        this.min = min;
        this.n = n;
        this.sumsq = sumsq;
        this.sum = sum;
        this.results = new ArrayList<>();
    }

    public static void main(String[] args) {
        Stats stats = new Stats("");
        stats.add(1.0f);
        stats.add(2.0f);
        stats.add(2.0f);
        stats.add(2.0f);
        stats.add(2.0f);
        stats.add(2.0f);

        System.out.println(stats);

    }

    public void add(double observation) {
        n++;
        sum += observation;
        sumsq += (observation * observation);

        results.add(observation);

        if (observation < min) {
            min = observation;
        }
        if (observation > max) {
            max = observation;
        }
        computed = false;
    }

    private void compute() {
        if (!computed) {
            average = sum / n;
            double num = sumsq - (n * average * average);
            if (num < 0) {
                num = 0;
            }
            sd = Math.sqrt(num / (n - 1));
            computed = true;
        }
    }

    public void add(Stats other) {
        n += other.n;
        sum += other.sum;
        sumsq += other.sumsq;
        if (other.min < min) {
            min = other.min;
        }
        if (other.max > max) {
            max = other.max;
        }
        computed = false;
        this.msTaken += other.msTaken;
    }

    public double getAverage() {
        if (!computed) {
            compute();
        }
        return average;
    }

    public int getN() {
        return n;
    }

    public double getSum() {
        return sum;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStandardDeviation() {
        if (!computed) {
            compute();
        }
        return sd;
    }

    public double getStandardError() {
        if (!computed) {
            compute();
        }
        return sd / Math.sqrt(n);
    }

    public long getMsTaken() {
        return msTaken;
    }

    public void setMsTaken(long msTaken) {
        this.msTaken = msTaken;
    }

    public double getSumsq() {
        return sumsq;
    }

    public double getSd() {
        if (!computed) {
            compute();
        }
        return sd;
    }

    public boolean isComputed() {
        return computed;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (!computed) {
            compute();
        }
        return "Stats{" +
                "Desc=" + description +
                ", average=" + average +
                ", sum=" + sum +
                ", sumsq=" + sumsq +
                ", sd=" + sd +
                ", n=" + n +
                ", min=" + min +
                ", max=" + max +
                ", stdErr=" + getStandardError() +
                ", ms=" + msTaken +
                '}';
    }

    public String fileRepresentation(String separator){
        return msTaken + separator
                + description + separator
                + max + separator
                + min + separator
                 + n + separator
                + sumsq + separator
                + sum;
    }

    public Stats(String line, String separator){
        String[] parts = line.split(separator);
        this.msTaken = Long.parseLong(parts[0]);
        this.description = parts[1];
        this.max = Double.parseDouble(parts[2]);
        this.min = Double.parseDouble(parts[3]);
        this.n = Integer.parseInt(parts[4]);
        this.sumsq = Double.parseDouble(parts[5]);
        this.sum = Double.parseDouble(parts[6]);
    }

    public List<Double> getResults(){
        return results;
    }

    public double getNormalizedDataMean(int ncv){
        if(min == max)
            return 0.5;

        List<Double> normalizedData = new ArrayList<>();
        double total = 0;

        for(double result : results){
            total += (result - min)/(max - min);
            normalizedData.add((result - min)/(max - min));
        }

        double errorSum = 0, mean = total/normalizedData.size();

        for(double data :  normalizedData)
            errorSum += (data - mean) * (data - mean);

        double standardDeviation = Math.sqrt(errorSum/normalizedData.size());
        double lower_limit = mean - (ncv*standardDeviation);
        double upper_limit = mean + (ncv*standardDeviation);

        List<Double> filteredData = new ArrayList<>();
        for(double data : normalizedData) {
            if (data > lower_limit && data < upper_limit)
                filteredData.add(data);
        }

        double sumat = 0;
        for(double data : filteredData)
            sumat += data;

        return sumat/filteredData.size();

    }

    public double getFilteredDataMean(int ncv){
        return 0.0;
    }

    public List<Double> getNormalizedData() {
        List<Double> normalizedData = new ArrayList<>();
        double total = 0;

        for(double result : results){
            total += (result - min)/(max - min);
            normalizedData.add((result - min)/(max - min));
        }

        return normalizedData;
    }
}
