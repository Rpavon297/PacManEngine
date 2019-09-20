package Testing.Evo.Genetica;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Gen {
    List<Object> alelos;
    float prec;
    boolean neg;
    int tam_cod;
    double min;
    double max;


    public Gen(float prec) {
        this.alelos = new ArrayList<>();
        this.prec = prec;
    }

    public Gen(Gen nGen){
        this.min = nGen.getMin();
        this.max = nGen.getMax();
        this.prec = nGen.getPrec();
        this.neg = nGen.getNeg();
        this.alelos = new ArrayList<>();
        this.alelos.addAll(nGen.getAlelos());
    }

    public List<Object> getAlelos(){return alelos;}

    public double getMin(){return this.min;}

    public double getMax(){return this.max;}

    boolean getNeg(){return this.neg;}

    public int getTam() {return this.alelos.size();}

    float getPrec() {return this.prec;}

    public void setAlelos(List<Object> alelos) {
        this.alelos = alelos;
        if(this.getFenotipo() < this.min || this.getFenotipo() > this.max) this.neg = !this.neg;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setGenotipo(double valor) {
        // TODO Auto-generated method stub
        if(valor < 0) {
            this.neg = true;
            valor = -valor;
        }else this.neg = false;

        int l = (int) (valor/this.prec);

        String arr = Integer.toBinaryString(l);
        this.alelos = new ArrayList<>(arr.length());

        for(int i = 0; i < this.tam_cod - arr.length(); i++)
            alelos.add(0);

        for(int i = 0; i < arr.length(); i++)
            alelos.add(Character.getNumericValue(arr.charAt(i)));
    }

    public  double getFenotipo(){

        double result = 0;
        int p = 0;

        for(int i = this.alelos.size() - 1; i >= 0; i--){
            result += (int)alelos.get(i) * Math.pow(2,p);
            p++;
        }
        if(this.neg)
            return -(result * this.prec);
        else
            return result * this.prec;
    }
    public void randomize(double min, double max){
        this.max = max;
        this.min = min;

        double aux = ThreadLocalRandom.current().nextDouble(min, max);

        int max_int = (int) (max/this.prec);
        int min_int = (int) (min/this.prec);

        String arrmax = Integer.toBinaryString(max_int);
        String arrmin = Integer.toBinaryString(min_int);

        this.tam_cod = Math.max(arrmax.length(), arrmin.length());

        this.setGenotipo(aux);
    }

    public boolean equals(Object o){
        if(!(o instanceof  Gen)) return false;

        Gen g = (Gen)o;
        return g.getFenotipo() == this.getFenotipo();
    }

    public String toString(){return Double.toString(this.getFenotipo());}
}
