package testing.evo.algoritmo;

import java.util.List;

/**
 * condition-statement ::= if( condition ) { statement } else { statement }
 *
 * statement ::= condition-statement | terminal
 *
 * condition ::= isGhostClose | isPowerPillClose | isEdibleGhostClose | isPacManStuck
 *
 * terminal ::= fleeClosestGhost | eatCloserPowerPill | chaseCloserGhost
 */

public class DecodGramatica {
    //  CONSTANTES GENETICAS
    private final int NCSTATEMENT = Gramatica.NCSTATEMENT;
    private final int NSTATEMENT = Gramatica.NSTATEMENT;
    private final int NCONDITION = Gramatica.NCONDITION;
    private final int NTERMINAL = Gramatica.NTERMINAL;

    private int wraps;
    private int maxWraps;
    private List<Double> codones;

    public DecodGramatica(List<Double> lista) {
        wraps = 0;
        maxWraps = 2;

        this.codones = lista;
    }

    private String sangria(int i){
        String sangria = " ";
        String espaciado = " ";
        for(int j = 0; j < i; j++)
            sangria += espaciado;
        return sangria;
    }

    public void showTree() {
        wraps = 0;

        conditionStatement(0, -1, true);

    }

    private int conditionStatement(int i, int sangria, boolean operative){
        int instruction = codones.get(i).intValue() % NCSTATEMENT;

        sangria++;
        i = checkWraps(i);

        if(wraps < maxWraps){
            switch (instruction){
                case 0:
                    System.out.print(sangria(sangria) + "IF ");
                    if(condition(i)){
                        //AUMENTAR i TRAS CONDITION
                        i = checkWraps(i);
                        i = statement(i, sangria, operative);
                        i = checkWraps(i);
                        System.out.println(sangria(sangria) + "ELSE ");
                        i = statement(i, sangria, operative);
                    }
                    System.out.println(sangria(sangria) + "END IF-ELSE ");

                    break;
                case 1:
                    System.out.print(sangria(sangria) + "IF ");

                    if(condition(i)){
                        //AUMENTAR i TRAS CONDITION
                        i = checkWraps(i);
                        i = statement(i, sangria,operative);
                    }

                    System.out.println(sangria(sangria) + "END IF");
                    break;
            }
        }

        return i;
    }

    private int statement(int i, int sangria, boolean operative){
        int instruction = codones.get(i).intValue() % NSTATEMENT;

        i = checkWraps(i);

        if(wraps < maxWraps){

            switch (instruction){
                case 0:
                    i = conditionStatement(i, sangria, operative);
                    break;
                case 1:
                    i = terminal(i,sangria, operative);
                    break;
            }
        }

        return i;
    }

    private int terminal(int i, int sangria, boolean operative){
        int terminal = codones.get(i).intValue() % NTERMINAL;

        sangria++;
        i = checkWraps(i);

        if(wraps < maxWraps) {

            switch (terminal) {
                case 0:
                    if (operative)
                        System.out.println(sangria(sangria) + "CHASE CLOSE GHOST");
                    break;
                case 1:
                    if (operative)
                        System.out.println(sangria(sangria) + "FLEE FROM CLOSEST GHOST");
                    break;
                case 2:
                    if (operative)
                        System.out.println(sangria(sangria) + "EAT CLOSEST POWER PILL");
                    break;
                case 3:
                    if (operative)
                        System.out.println(sangria(sangria) + "EAT CLOSEST PILL");
                    break;
            }
        }

        return i;
    }

    private boolean condition(int i){
        int condition = codones.get(i).intValue() % NCONDITION;

        switch (condition){
            case 0:
                System.out.println("IS GHOST CLOSE");
                return true;
            case 1:
                System.out.println("IS POWER PILL CLOSE");
                return true;
            case 2:
                System.out.println("IS EDIBLE GHOST CLOSE");
                return true;
            default:
                return false;
        }
    }

    private int checkWraps(int i){
        i++;

        if(i == codones.size()){
            i = 0;
            wraps++;
        }
        return i;
    }

}
