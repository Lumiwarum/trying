import com.sun.xml.internal.bind.v2.TODO;
import sun.misc.Queue;

import java.sql.SQLOutput;
import java.util.*;

/**
 * This enum is used for describing elements in the cells of the field
 */
enum insides {
    Harry,Filch,Book,Cloak,Norris,Exit,inspectorZone
}

/**
 * This class is used for describing positions of object on the field
 * Integer values x and y are the coordinates
 */
class Coordinates{
    int x,y;
    Coordinates(){
        x=0;y=0;
    }
    Coordinates(int x,int y){
        this.x=x;
        this.y=y;
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }

    /**
     *
     * @param o - object to compare with
     * @return true if it's the same object, or they have the same coordinates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

/**
 * Class that has main function
 */
public class assignment {
    public static void main(String[] args) {
        Game igra= new Game();
        igra.setGame(new Coordinates(0,0),new Coordinates(1,5),new Coordinates(3,1),new Coordinates(4,6),new Coordinates(0,1),new Coordinates(4,3));
        int i=1000;
        //igra.printGame();
        ArrayList<Coordinates> answer;
        while (i>0){
            igra.randomGenerateGame();
            if (igra.space[igra.Harry.getX()][igra.Harry.getY()].isInspected()){
                continue;
            }
            AStar aStar= new AStar(1,igra);
            answer=aStar.algorithm();
            i--;
        }
        igra.prepareMap();
   }
}

/**
 * Class that is responsible for the map. It checks if the input is correct, can generate random map, can print it,
 * contains information about the objects.
 */
class Game {
    cell[][] space = new cell[9][9];
    Coordinates Filch = new Coordinates();
    Coordinates Norris = new Coordinates();
    Coordinates Book = new Coordinates();
    Coordinates Exit = new Coordinates();
    Coordinates Cloak = new Coordinates();
    Coordinates Harry = new Coordinates();

    Game(){
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                space[i][j]=new cell();
            }
        }
    }

    /**
     * This method checks the input
     * @param H Coordinates of Harry Potter
     * @param F Coordinates of Argus Filch
     * @param N Coordinates of cat Norris
     * @param B Coordinates of the book
     * @param C Coordinates of the invisibility cloak
     * @param E Coordinates of the exit
     * @return true if the input is valid, false otherwise
     */
    boolean setGame(Coordinates H,Coordinates F,Coordinates N,Coordinates B,Coordinates C,Coordinates E){
        if (((Math.abs(B.getX()-F.getX())<3)&&(Math.abs(B.getY()-F.getY())<3))||((Math.abs(B.getX()-N.getX())<2)&&(Math.abs(B.getY()-N.getY())<2))){
            return false;
        }
        if (((Math.abs(C.getX()-F.getX())<3)&&(Math.abs(C.getY()-F.getY())<3))||((Math.abs(C.getX()-N.getX())<2)&&(Math.abs(C.getY()-N.getY())<2))){
            return false;
        }
        if (((Math.abs(E.getX()-F.getX())<3)&&(Math.abs(E.getY()-F.getY())<3))||((Math.abs(E.getX()-N.getX())<2)&&(Math.abs(E.getY()-N.getY())<2))){
            return false;
        }
        if (E.getX()==B.getX()&&E.getY()==B.getY()){
            return false;
        }
        Filch=F;
        Norris=N;
        Harry=H;
        Book=B;
        Cloak=C;
        Exit=E;
        prepareMap();
        return true;
    }

    /**
     * This method prepares the map for the algorithm, setting the environment in 2d array of cells
     */
    void prepareMap(){
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                space[i][j].prepare();
            }
        }
        space[Harry.getX()][Harry.getY()].addInCell(insides.Harry);
        space[Filch.getX()][Filch.getY()].addInCell(insides.Filch);
        for (int i= Math.max(Filch.getX()-2,0);i< Math.min(Filch.getX()+3,9);i++){
            for (int j= Math.max(Filch.getY()-2,0);j< Math.min(Filch.getY()+3,9);j++){
                space[i][j].inspected=true;
            }
        }
        space[Norris.getX()][Norris.getY()].addInCell(insides.Norris);
        for (int i= Math.max(Norris.getX()-1,0);i<Math.min(Norris.getX()+2,9) ;i++){
            for (int j=Math.max(Norris.getY()-1,0) ;j<Math.min( Norris.getY()+2,9);j++){
                space[i][j].inspected=true;
            }
        }
        space[Book.getX()][Book.getY()].addInCell(insides.Book);
        space[Cloak.getX()][Cloak.getY()].addInCell(insides.Cloak);
        space[Exit.getX()][Exit.getY()].addInCell(insides.Exit);
    }

    /**
     * Simple method, that randomly generates Coordinates of the objects (except Harry, that always at 0,0)
     * and passes them to the setGAme function, until it returns true
     */
    void randomGenerateGame(){
        boolean generate=true;
        while(generate) {
            Coordinates F = new Coordinates((int) (Math.random() * 9), (int) (Math.random() * 9));
            Coordinates N = new Coordinates((int) (Math.random() * 9), (int) (Math.random() * 9));
            Coordinates B = new Coordinates((int) (Math.random() * 9), (int) (Math.random() * 9));
            Coordinates C = new Coordinates((int) (Math.random() * 9), (int) (Math.random() * 9));
            Coordinates E = new Coordinates((int) (Math.random() * 9), (int) (Math.random() * 9));
            Coordinates H = new Coordinates(0, 0);
            generate=!setGame(H,F,N,B,C,E);
        }
    }

    /**
     * This method prints the map in the console
     */
    void printGame(){
        for (int i=8;i>=0;i--){
            for (int j=0; j<9;j++){
                space[j][i].print();
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

/**
 * This class represents cell in the map grid
 * Each cell contains ArrayList of elements stored in it
 */
class cell{
    ArrayList<String> elements= new ArrayList<>();
    boolean inspected;

    /**
     * Function that adds an element into a cell
     * @param actorToAdd - element that added
     */
    void addInCell(insides actorToAdd){
        switch (actorToAdd){
            case Book:
                elements.add("B");
                break;
            case Exit:
                elements.add("E");
                break;
            case Cloak:
                elements.add("C");
                break;
            case Filch:
                elements.add("F");
                inspected=true;
                break;
            case Harry:
                elements.add("H");
                break;
            case Norris:
                elements.add("N");
                inspected=true;
                break;
            case inspectorZone:
                inspected=true;
        }
    }
    boolean isInspected(){
        return inspected;
    }

    /**
     * Function that print stored elements into console
     */
    void print(){
        if (elements.size()==0){
            if (inspected){
                System.out.print("*");
            } else {
                System.out.print("-");
            }
        }
        for (String elem: elements){
            System.out.print(elem);
        }
    }

    /**
     * This function clears cell's inside
     */
    void prepare(){
        elements.clear();
        inspected=false;
    }
}

/**
 * enum for MemoryCell,helps to describe what the algorithm knows about a specific cell
 */
enum memInsides{
    unknown,safe,inspected,cloakParseable
}

/**
 * Special class for cells of the grids in the algorithms
 */
class MemoryCell {
    memInsides typeOfCell;
    boolean unvisited;

    /**
     * By default, we don't know the type of cell and didn't visit it.
     */
    MemoryCell(){
        typeOfCell=memInsides.unknown;
        unvisited=true;
    }
}

/**
 * This class represents the first algorithm - Backtracking
 * The main Idea of this algorithm look through all safe unvisited cells, go into one of them, repeat till all cells are visited or win
 */
class BackTracking{
    int perception;
    Game game=new Game();
    boolean hasBook,hasCloak,caught,beforeBook;
    Vision vision;
    Coordinates position= new Coordinates();
    Stack<Coordinates> trace= new Stack<>();
    Stack<Coordinates> traceBook= new Stack<>();
    Stack<Coordinates> traceCloak= new Stack<>();
    MemoryCell[][] mind=new MemoryCell[9][9];

    /**
     * Sets the environment
     * @param game the map to play on
     * @param per perception of Harry (1 or 2)
     */
    BackTracking(Game game,int per){
        for (int i=0;i<9;i++){
            for (int j=0; j<9;j++){
                mind[i][j]=new MemoryCell();
            }
        }
        vision= new Vision(per);
        beforeBook=true;
        perception=per;
        hasBook=false;
        hasCloak=false;
        caught=false;
        this.game=game;
        position=new Coordinates(game.Harry.getX(),game.Harry.getY());
        mind[position.getX()][position.getY()].unvisited=false;
        take();
        vision.firstSee(game,mind,position);
        printResult();
    }

    /**
     * Looking for the first available cell with the next priority:
     * 1) safe cells
     * 2) cloakParsable cell, if Harry has the cloak
     * 3) unknown type of cells (possible only on the first few turns with perception 2
     * Starts with cell on the right from the current, then counterclockwise
     * @return first found cell or [-1,-1] otherwise
     */
    Coordinates getNewCell(){
        if (position.getX()<8){
            if (mind[position.getX()+1][position.getY()].typeOfCell==memInsides.safe&&mind[position.getX()+1][position.getY()].unvisited){
                return new Coordinates(position.getX()+1,position.getY());
            }
        }
        if (position.getX()<8&&position.getY()<8){
            if (mind[position.getX()+1][position.getY()+1].typeOfCell==memInsides.safe&&mind[position.getX()+1][position.getY()+1].unvisited){
                return new Coordinates(position.getX()+1,position.getY()+1);
            }
        }
        if (position.getY()<8){
            if (mind[position.getX()][position.getY()+1].typeOfCell==memInsides.safe&&mind[position.getX()][position.getY()+1].unvisited){
                return new Coordinates(position.getX(),position.getY()+1);
            }
        }
        if (position.getX()>0&&position.getY()<8){
            if (mind[position.getX()-1][position.getY()+1].typeOfCell==memInsides.safe&&mind[position.getX()-1][position.getY()+1].unvisited){
                return new Coordinates(position.getX()-1,position.getY()+1);
            }
        }
        if (position.getX()>0){
            if (mind[position.getX()-1][position.getY()].typeOfCell==memInsides.safe&&mind[position.getX()-1][position.getY()].unvisited){
                return new Coordinates(position.getX()-1,position.getY());
            }
        }
        if (position.getX()>0&&position.getY()>0){
            if (mind[position.getX()-1][position.getY()-1].typeOfCell==memInsides.safe&&mind[position.getX()-1][position.getY()-1].unvisited){
                return new Coordinates(position.getX()-1,position.getY()-1);
            }
        }
        if (position.getY()>0){
            if (mind[position.getX()][position.getY()-1].typeOfCell==memInsides.safe&&mind[position.getX()][position.getY()-1].unvisited){
                return new Coordinates(position.getX(),position.getY()-1);
            }
        }
        if (position.getX()<8&&position.getY()>0){
            if (mind[position.getX()+1][position.getY()-1].typeOfCell==memInsides.safe&&mind[position.getX()+1][position.getY()-1].unvisited){
                return new Coordinates(position.getX()+1,position.getY()-1);
            }
        }
        if (hasCloak){
            if (position.getX()<8){
                if (mind[position.getX()+1][position.getY()].typeOfCell==memInsides.cloakParseable&&mind[position.getX()+1][position.getY()].unvisited){
                    return new Coordinates(position.getX()+1,position.getY());
                }
            }
            if (position.getX()<8&&position.getY()<8){
                if (mind[position.getX()+1][position.getY()+1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()+1][position.getY()+1].unvisited){
                    return new Coordinates(position.getX()+1,position.getY()+1);
                }
            }
            if (position.getY()<8){
                if (mind[position.getX()][position.getY()+1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()][position.getY()+1].unvisited){
                    return new Coordinates(position.getX(),position.getY()+1);
                }
            }
            if (position.getX()>0&&position.getY()<8){
                if (mind[position.getX()-1][position.getY()+1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()-1][position.getY()+1].unvisited){
                    return new Coordinates(position.getX()-1,position.getY()+1);
                }
            }
            if (position.getX()>0){
                if (mind[position.getX()-1][position.getY()].typeOfCell==memInsides.cloakParseable&&mind[position.getX()-1][position.getY()].unvisited){
                    return new Coordinates(position.getX()-1,position.getY());
                }
            }
            if (position.getX()>0&&position.getY()>0){
                if (mind[position.getX()-1][position.getY()-1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()-1][position.getY()-1].unvisited){
                    return new Coordinates(position.getX()-1,position.getY()-1);
                }
            }
            if (position.getY()>0){
                if (mind[position.getX()][position.getY()-1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()][position.getY()-1].unvisited){
                    return new Coordinates(position.getX(),position.getY()-1);
                }
            }
            if (position.getX()<8&&position.getY()>0){
                if (mind[position.getX()+1][position.getY()-1].typeOfCell==memInsides.cloakParseable&&mind[position.getX()+1][position.getY()-1].unvisited){
                    return new Coordinates(position.getX()+1,position.getY()-1);
                }
            }
        }
        // when we don't know
        if (position.getX()<8){
            if (mind[position.getX()+1][position.getY()].typeOfCell==memInsides.unknown&&mind[position.getX()+1][position.getY()].unvisited){
                return new Coordinates(position.getX()+1,position.getY());
            }
        }
        if (position.getX()<8&&position.getY()<8){
            if (mind[position.getX()+1][position.getY()+1].typeOfCell==memInsides.unknown&&mind[position.getX()+1][position.getY()+1].unvisited){
                return new Coordinates(position.getX()+1,position.getY()+1);
            }
        }
        if (position.getY()<8){
            if (mind[position.getX()][position.getY()+1].typeOfCell==memInsides.unknown&&mind[position.getX()][position.getY()+1].unvisited){
                return new Coordinates(position.getX(),position.getY()+1);
            }
        }
        if (position.getX()>0&&position.getY()<8){
            if (mind[position.getX()-1][position.getY()+1].typeOfCell==memInsides.unknown&&mind[position.getX()-1][position.getY()+1].unvisited){
                return new Coordinates(position.getX()-1,position.getY()+1);
            }
        }
        if (position.getX()>0){
            if (mind[position.getX()-1][position.getY()].typeOfCell==memInsides.unknown&&mind[position.getX()-1][position.getY()].unvisited){
                return new Coordinates(position.getX()-1,position.getY());
            }
        }
        if (position.getX()>0&&position.getY()>0){
            if (mind[position.getX()-1][position.getY()-1].typeOfCell==memInsides.unknown&&mind[position.getX()-1][position.getY()-1].unvisited){
                return new Coordinates(position.getX()-1,position.getY()-1);
            }
        }
        if (position.getY()>0){
            if (mind[position.getX()][position.getY()-1].typeOfCell==memInsides.unknown&&mind[position.getX()][position.getY()-1].unvisited){
                return new Coordinates(position.getX(),position.getY()-1);
            }
        }
        if (position.getX()<8&&position.getY()>0){
            if (mind[position.getX()+1][position.getY()-1].typeOfCell==memInsides.unknown&&mind[position.getX()+1][position.getY()-1].unvisited){
                return new Coordinates(position.getX()+1,position.getY()-1);
            }
        }

        return new Coordinates(-1,-1);
    }
    boolean move(Coordinates cellMoveInto){
        if (cellMoveInto.getX()==-1&&cellMoveInto.getY()==-1){
            return false;
        }
        if (game.space[cellMoveInto.getX()][cellMoveInto.getY()].inspected&&(!hasCloak)){
            caught=true;
        }
        if (hasCloak&&((cellMoveInto.getX()==game.Filch.getX()&&cellMoveInto.getY()==game.Filch.getY())||(cellMoveInto.getX()==game.Norris.getX()&&cellMoveInto.getY()==game.Norris.getY()))){
            caught=true;
        }
        if (caught){
            return false;
        }
        if (trace.size()>17){
            return false;
        }
        Coordinates previous= new Coordinates(position.getX(), position.getY());
        trace.add(previous);
        position.x=cellMoveInto.getX();
        position.y=cellMoveInto.getY();
        mind[position.getX()][position.getY()].unvisited=false;
        vision.seeCells(game,mind,position);
        if (take()){
            return true;
        }
        ArrayList<Coordinates> visited=new ArrayList<>();
        while (true){
            Coordinates moveNext = getNewCell();
            visited.add(new Coordinates(moveNext.getX(), moveNext.getY()));
            if (moveNext.getX()==-1&&moveNext.getY()==-1){
                if (trace.size()==0){
                    break;
                }
                position.x=trace.peek().getX();
                position.y=trace.pop().getY();
                for (int i=0;i<visited.size()-1;i++){
                    mind[visited.get(i).getX()][visited.get(i).getY()].unvisited=true;
                }
                break;
            } else {
                if (move(moveNext)) {
                    return true;
                }
                mind[moveNext.getX()][moveNext.getY()].unvisited=false;
            }
        }

        return false;
    }
    boolean take(){
        ArrayList<String> insides = game.space[position.getX()][position.getY()].elements;
        boolean closed=false;
        while (!insides.isEmpty()){
            if(insides.get(0).equals("C")){
                takeCloak();
            }
            if(insides.get(0).equals("B")){
                hasBook=true;
                if (!hasCloak){
                    beforeBook=false;
                }
                reBuild();
            }
            if ((insides.get(0).equals("E"))&&(hasBook)){
                return true;
            }
            if (insides.get(0).equals("E")){
                closed=true;
            }
            insides.remove(0);
        }
        if (closed){
            insides.add("E");
        }
        return false;
    }
    void printCells(){
        for (int i=8;i>=0;i--){
            for (int j=0;j<9;j++){
                if (position.getX()==j&&position.getY()==i){
                    System.out.print("H ");
                    continue;
                }
                if (mind[j][i].typeOfCell==memInsides.safe){
                    System.out.print("-");
                } else {
                    if (mind[j][i].typeOfCell==memInsides.cloakParseable){
                        System.out.print("~");
                    } else {
                        System.out.print("*");
                    }
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    void reBuild(){
        for (int i=0;i<9;i++){
            for (int j=0; j<9;j++){
                mind[i][j].unvisited=true;
            }
        }
        traceBook=(Stack<Coordinates>) trace.clone();
        trace.clear();
        mind[position.getX()][position.getY()].unvisited=false;
        vision.seeCells(game,mind,position);
    }
    void takeCloak(){
        for (int i=0;i<9;i++){
            for (int j=0; j<9;j++){
                mind[i][j].unvisited=true;
            }
        }
        hasCloak=true;
        traceCloak=(Stack<Coordinates>) trace.clone();
        trace.clear();
        mind[position.getX()][position.getY()].unvisited=false;
        vision.seeCells(game,mind,position);
    }
    void printResult(){
        if (move(getNewCell())){
            if (hasCloak){
                if (beforeBook) {
                    Stack<Coordinates> answer=new Stack<>();
                    trace.add(position);
                    while (!trace.isEmpty()){
                        answer.add(trace.pop());
                    }
                    while (!traceBook.isEmpty()){
                        answer.add(traceBook.pop());
                    }
                    while (!traceCloak.isEmpty()){
                        answer.add(traceCloak.pop());
                    }
                    while (!answer.isEmpty()){
                        System.out.print("["+answer.peek().getX()+","+answer.pop().getY()+"]");
                    }
                } else {
                    Stack<Coordinates> answer=new Stack<>();
                    trace.add(position);
                    while (!trace.isEmpty()){
                        answer.add(trace.pop());
                    }
                    while (!traceCloak.isEmpty()){
                        answer.add(traceCloak.pop());
                    }
                    while (!traceBook.isEmpty()){
                        answer.add(traceBook.pop());
                    }
                    while (!answer.isEmpty()){
                        System.out.print("["+answer.peek().getX()+","+answer.pop().getY()+"]");
                    }
                }
            } else {
                Stack<Coordinates> answer=new Stack<>();
                trace.add(position);
                while (!trace.isEmpty()){
                    answer.add(trace.pop());
                }
                while (!traceBook.isEmpty()){
                    answer.add(traceBook.pop());
                }
                while (!answer.isEmpty()){
                    System.out.print("["+answer.peek().getX()+","+answer.pop().getY()+"]");
                }
            }
        } else {
            if (hasCloak){
                vision.checkAllCloakSafe(mind);
                position.x=game.Cloak.getX();
                position.y=game.Cloak.getY();
                for (int i=0;i<9;i++){
                    for (int j=0; j<9;j++){
                        mind[i][j].unvisited=true;
                    }
                }
                mind[position.getX()][position.getY()].unvisited=false;
                if (move(getNewCell())){
                    Stack<Coordinates> answer=new Stack<>();
                    trace.add(position);
                    while (!trace.isEmpty()){
                        answer.add(trace.pop());
                    }
                    while (!traceBook.isEmpty()){
                        answer.add(traceBook.pop());
                    }
                    while (!traceCloak.isEmpty()){
                        answer.add(traceCloak.pop());
                    }
                    while (!answer.isEmpty()){
                        System.out.print("["+answer.peek().getX()+","+answer.pop().getY()+"]");
                    }
                } else {
                    System.out.println("no way");
                }
            } else {
                System.out.println("no way");
            }
        }
    }
}
class Vision{
    int perception;
    Vision(int n){
        perception=n;
    }
    void seeCells(Game game,MemoryCell[][] mind,Coordinates position){

        if (perception==1) {
            if (position.getX() < 8) {
                if (game.space[position.getX()+1][position.getY()].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() + 1][position.getY()].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() + 1][position.getY()].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() + 1][position.getY()].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +1,position.getY()));
                }
            }

            if ((position.getX() < 8) && (position.getY() < 8)) {
                if (game.space[position.getX() + 1][position.getY()+1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() + 1][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() + 1][position.getY() + 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() + 1][position.getY() + 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +1,position.getY()+1));
                }
            }

            if (position.getY() < 8) {
                if (game.space[position.getX()][position.getY() + 1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX()][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX()][position.getY() + 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX()][position.getY() + 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX(),position.getY()));
                }
            }

            if ((position.getX() > 0) && (position.getY() < 8)) {
                if (game.space[position.getX() - 1][position.getY()+1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() - 1][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() - 1][position.getY() + 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() - 1][position.getY() + 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -1,position.getY()+1));
                }
            }

            if (position.getX() > 0) {
                if (game.space[position.getX() - 1][position.getY()].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() - 1][position.getY()].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() - 1][position.getY()].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() - 1][position.getY()].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -1,position.getY()));
                }
            }

            if ((position.getX() > 0) && (position.getY() > 0)) {
                if (game.space[position.getX() - 1][position.getY() - 1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() - 1][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() - 1][position.getY() - 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() - 1][position.getY() - 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -1,position.getY()-1));
                }
            }

            if (position.getY() > 0) {
                if (game.space[position.getX()][position.getY() - 1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX()][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX()][position.getY() - 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX()][position.getY() - 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX(),position.getY()-1));
                }
            }

            if ((position.getX() < 8) && (position.getY() > 0)) {
                if (game.space[position.getX() + 1][position.getY() - 1].inspected) {
                    if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                        mind[position.getX() + 1][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                    } else {
                        mind[position.getX() + 1][position.getY() - 1].typeOfCell = memInsides.inspected;
                    }
                } else {
                    mind[position.getX() + 1][position.getY() - 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +1,position.getY()-1));
                }
            }
        }
        if (perception==2) {
            if (position.getX() < 7) {
                if (game.space[position.getX() + 2][position.getY()].inspected) {
                    mind[position.getX() + 2][position.getY()].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() + 2][position.getY()].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +2,position.getY()));
                }
            }
            if ((position.getX() < 7) && (position.getY() < 8)) {
                if (game.space[position.getX() + 2][position.getY()+1].inspected) {
                    mind[position.getX() + 2][position.getY() + 1].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() + 2][position.getY() + 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +2,position.getY()+1));
                }
            }
            if ((position.getX() < 8) && (position.getY() < 7)) {
                if (game.space[position.getX() + 1][position.getY()+2].inspected) {
                    mind[position.getX() + 1][position.getY() + 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() + 1][position.getY() + 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +1,position.getY()+2));
                }
            }
            if (position.getY() < 7) {
                if (game.space[position.getX()][position.getY() + 2].inspected) {
                    mind[position.getX()][position.getY() + 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX()][position.getY() + 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() ,position.getY()+2));
                }
            }
            if ((position.getX() > 0) && (position.getY() < 7)) {
                if (game.space[position.getX() - 1][position.getY() + 2].inspected) {
                    mind[position.getX() - 1][position.getY() + 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() - 1][position.getY() + 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -1,position.getY()+2));
                }
            }
            if ((position.getX() > 1) && (position.getY() < 8)) {
                if (game.space[position.getX() - 2][position.getY() + 1].inspected) {
                    mind[position.getX() - 2][position.getY() + 1].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() - 2][position.getY() + 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -2,position.getY()+1));
                }
            }
            if (position.getX() > 1) {
                if (game.space[position.getX() - 2][position.getY()].inspected) {
                    mind[position.getX() - 2][position.getY()].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() - 2][position.getY()].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -2,position.getY()));
                }
            }
            if ((position.getX() > 1) && (position.getY() > 0)) {
                if (game.space[position.getX() - 2][position.getY() - 1].inspected) {
                    mind[position.getX() - 2][position.getY() - 1].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() - 2][position.getY() - 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -2,position.getY()-1));
                }
            }
            if ((position.getX() > 0) && (position.getY() > 1)) {
                if (game.space[position.getX() - 1][position.getY() - 2].inspected) {
                    mind[position.getX() - 1][position.getY() - 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() - 1][position.getY() - 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() -1,position.getY()-2));
                }
            }
            if (position.getY() > 1) {
                if (game.space[position.getX()][position.getY() - 2].inspected) {
                    mind[position.getX()][position.getY() - 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX()][position.getY() - 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() ,position.getY()-2));
                }
            }
            if ((position.getX() < 8) && (position.getY() > 1)) {
                if (game.space[position.getX() + 1][position.getY() - 2].inspected) {
                    mind[position.getX() + 1][position.getY() - 2].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() + 1][position.getY() - 2].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +1,position.getY()-2));
                }
            }
            if ((position.getX() < 7) && (position.getY() > 0)) {
                if (game.space[position.getX() + 2][position.getY() - 1].inspected) {
                    mind[position.getX() + 2][position.getY() - 1].typeOfCell = memInsides.inspected;
                } else {
                    mind[position.getX() + 2][position.getY() - 1].typeOfCell = memInsides.safe;
                    checkCloakSafe(mind,new Coordinates(position.getX() +2,position.getY()-1));
                }
            }
        }
    }
    void firstSee(Game game,MemoryCell[][] mind,Coordinates position){
        if (perception==1) {
            seeCells(game,mind,position);
        }
        if (perception==2) {
            seeCells(game,mind,position);
            if (position.getX() < 7) {
                if (!game.space[position.getX() + 2][position.getY()].inspected) {
                    mind[position.getX()+1][position.getX()].typeOfCell= memInsides.safe;
                }
            }
            if ((position.getX() < 7) &&(position.getY() < 7)) {
                if ((!game.space[position.getX() + 2][position.getY()+1].inspected)&&(!game.space[position.getX() + 1][position.getY()+2].inspected)) {
                    mind[position.getX() + 1][position.getY() + 1].typeOfCell = memInsides.safe;
                }
            }
            if (position.getY() < 7) {
                if (!game.space[position.getX()][position.getY() + 2].inspected) {
                    mind[position.getX()][position.getY() + 1].typeOfCell = memInsides.safe;
                }
            }
            if ((position.getX() > 1) && (position.getY() < 7)) {
                if ((!game.space[position.getX() - 1][position.getY() + 2].inspected)&&(!game.space[position.getX() - 2][position.getY() + 1].inspected)) {
                    mind[position.getX() - 1][position.getY() + 1].typeOfCell = memInsides.safe;
                }
            }
            if (position.getX() > 1) {
                if (!game.space[position.getX() - 2][position.getY()].inspected) {
                    mind[position.getX() - 1][position.getY()].typeOfCell = memInsides.safe;
                }
            }
            if ((position.getX() > 1) && (position.getY() > 1)) {
                if ((!game.space[position.getX() - 2][position.getY() - 1].inspected)&&(!game.space[position.getX() - 1][position.getY() - 2].inspected)) {
                    mind[position.getX() - 1][position.getY() - 1].typeOfCell = memInsides.safe;
                }
            }
            if (position.getY() > 1) {
                if (!game.space[position.getX()][position.getY() - 2].inspected) {
                    mind[position.getX()][position.getY() - 1].typeOfCell = memInsides.safe;
                }
            }
            if ((position.getX() < 7) && (position.getY() > 1)) {
                if ((!game.space[position.getX() + 1][position.getY() - 2].inspected)&&(!game.space[position.getX() + 2][position.getY() - 1].inspected)) {
                    mind[position.getX() + 1][position.getY() - 1].typeOfCell = memInsides.safe;
                }
            }
            checkCloakSafe(mind,position);
        }
    }
    void checkCloakSafe(MemoryCell[][] mind,Coordinates position){
        if (position.getX() < 8) {
            if (mind[position.getX()+1][position.getY()].typeOfCell==memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell == memInsides.safe) {
                    mind[position.getX() + 1][position.getY()].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if ((position.getX() < 8) && (position.getY() < 8)) {
            if (mind[position.getX() + 1][position.getY() + 1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell == memInsides.safe) {
                    mind[position.getX() + 1][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if (position.getY() < 8) {
            if (mind[position.getX()][position.getY() + 1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                    mind[position.getX()][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if ((position.getX() > 0) && (position.getY() < 8)) {
            if (mind[position.getX() - 1][position.getY()+1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                    mind[position.getX() - 1][position.getY() + 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if (position.getX() > 0) {
            if (mind[position.getX() - 1][position.getY()].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                    mind[position.getX() - 1][position.getY()].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if ((position.getX() > 0) && (position.getY() > 0)) {
            if (mind[position.getX() - 1][position.getY() - 1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell == memInsides.safe) {
                    mind[position.getX() - 1][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if (position.getY() > 0) {
            if (mind[position.getX()][position.getY() - 1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                    mind[position.getX()][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }

        if ((position.getX() < 8) && (position.getY() > 0)) {
            if (mind[position.getX() + 1][position.getY() - 1].typeOfCell == memInsides.inspected) {
                if (mind[position.getX()][position.getY()].typeOfCell==memInsides.safe) {
                    mind[position.getX() + 1][position.getY() - 1].typeOfCell = memInsides.cloakParseable;
                }
            }
        }
    }
    void checkAllCloakSafe(MemoryCell[][] mind){
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (mind[i][j].typeOfCell!=memInsides.safe){
                    continue;
                }
                checkCloakSafe(mind,new Coordinates(i,j));
            }
        }
    }
}
class StarCell extends MemoryCell implements Comparable<StarCell>{
    double h,f;
    int x,y,g;
    int parentX,parentY;
    StarCell(double g,double h){
        this.g=(int)g;
        this.h=h;
        typeOfCell=memInsides.unknown;
    }
    StarCell(){
        parentX=-1;
        parentY=-1;
        g=99;
        h=0;
        typeOfCell=memInsides.unknown;
    }
    void setG(int g){
        this.g=g;
        f=g+h;
    }
    void printH(){
        System.out.print(h);
    }
    void printG(){
        System.out.print(g);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, h, f, x, y, parentX, parentY);
    }

    @Override
    public int compareTo(StarCell o) {
        if (this.typeOfCell==memInsides.safe&&o.typeOfCell==memInsides.unknown){
            return -1;
        }
        if (this.typeOfCell==memInsides.unknown&&o.typeOfCell==memInsides.safe){
            return 1;
        }
        if (this.f>o.f){
            return 1;
        } else {
            if (this.f<o.f){
                return -1;
            } else {
                if (this.h>o.h){
                    return 1;
                } else {
                    if (this.h<o.f){
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
class AStar{
    Vision vision;
    StarCell[][] mind= new StarCell[9][9];
    HashSet<Coordinates> closed=new HashSet<>();
    Game game;
    Coordinates position=new Coordinates();
    boolean reachable;
    Coordinates cloak,book;
    PriorityQueue<StarCell> open= new PriorityQueue<>();
    AStar(int per,Game game){
        reachable=false;
        this.game=game;
        vision=new Vision(per);
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                mind[i][j]=new StarCell();
                mind[i][j].x=i;
                mind[i][j].y=j;
            }
        }
        BFS();
        vision.checkAllCloakSafe(mind);

    }
    void addCells(){
        if (position.getX() < 8) {
            if (open.contains(mind[position.getX()+1][position.getY()])){
                if ((mind[position.x][position.y].g+1<mind[position.getX()+1][position.getY()].g)) {
                    open.remove(mind[position.getX()+1][position.getY()]);
                    mind[position.getX() + 1][position.getY()].setG(mind[position.x][position.y].g + 1);
                    mind[position.getX() + 1][position.getY()].parentX = position.getX();
                    mind[position.getX() + 1][position.getY()].parentY = position.getY();
                    open.add(mind[position.getX()+1][position.getY()]);
                }
            } else {
                if ((mind[position.getX() + 1][position.getY()].typeOfCell != memInsides.inspected)&&(mind[position.getX() + 1][position.getY()].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY())))) {
                    open.add(mind[position.getX() + 1][position.getY()]);
                    mind[position.getX() + 1][position.getY()].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() + 1][position.getY()].parentX = position.getX();
                    mind[position.getX() + 1][position.getY()].parentY = position.getY();
                }
            }
        }

        if ((position.getX() < 8) && (position.getY() < 8)) {
            if (open.contains(mind[position.getX()+1][position.getY()+1])){
                if ((mind[position.x][position.y].g+1<mind[position.getX()+1][position.getY()+1].g)) {
                    open.remove(mind[position.getX()+1][position.getY()+1]);
                    mind[position.getX() + 1][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() + 1][position.getY() + 1].parentX = position.getX();
                    mind[position.getX() + 1][position.getY() + 1].parentY = position.getY();
                    open.add(mind[position.getX()+1][position.getY()+1]);
                }

            } else {
                if ((mind[position.getX() + 1][position.getY() + 1].typeOfCell != memInsides.inspected) && (mind[position.getX() + 1][position.getY() + 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY() + 1)))) {
                    open.add(mind[position.getX() + 1][position.getY() + 1]);
                    mind[position.getX() + 1][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() + 1][position.getY() + 1].parentX = position.getX();
                    mind[position.getX() + 1][position.getY() + 1].parentY = position.getY();
                }
            }
        }

        if (position.getY() < 8) {
            if (open.contains(mind[position.getX()][position.getY()+1])){
                if (mind[position.x][position.y].g+1<mind[position.getX()][position.getY()+1].g) {
                    open.remove(mind[position.getX()][position.getY()+1]);
                    mind[position.getX()][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX()][position.getY() + 1].parentX = position.getX();
                    mind[position.getX()][position.getY() + 1].parentY = position.getY();
                    open.add(mind[position.getX()][position.getY()+1]);
                }
            } else {
                if ((mind[position.getX()][position.getY() + 1].typeOfCell != memInsides.inspected) && (mind[position.getX()][position.getY() + 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX(), position.getY() + 1)))) {
                    open.add(mind[position.getX()][position.getY() + 1]);
                    mind[position.getX()][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX()][position.getY() + 1].parentX = position.getX();
                    mind[position.getX()][position.getY() + 1].parentY = position.getY();
                }
            }
        }

        if ((position.getX() > 0) && (position.getY() < 8)) {
            if (open.contains(mind[position.getX()-1][position.getY()+1])){
                if (mind[position.x][position.y].g+1<mind[position.getX()-1][position.getY()+1].g) {
                    open.remove(mind[position.getX()-1][position.getY()+1]);
                    mind[position.getX() - 1][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY() + 1].parentX = position.getX();
                    mind[position.getX() - 1][position.getY() + 1].parentY = position.getY();
                    open.add(mind[position.getX()-1][position.getY()+1]);
                }
            } else {
                if ((mind[position.getX() - 1][position.getY() + 1].typeOfCell != memInsides.inspected) && (mind[position.getX() - 1][position.getY() + 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY() + 1)))) {
                    open.add(mind[position.getX() - 1][position.getY() + 1]);
                    mind[position.getX() - 1][position.getY() + 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY() + 1].parentX = position.getX();
                    mind[position.getX() - 1][position.getY() + 1].parentY = position.getY();
                }
            }
        }

        if (position.getX() > 0) {
            if (open.contains(mind[position.getX()-1][position.getY()])){
                if (mind[position.x][position.y].g+1<mind[position.getX()-1][position.getY()].g) {
                    open.remove(mind[position.getX()-1][position.getY()]);
                    mind[position.getX() - 1][position.getY()].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY()].parentX = position.getX();
                    mind[position.getX() - 1][position.getY()].parentY = position.getY();
                    open.add(mind[position.getX()-1][position.getY()]);
                }
            } else {
                if ((mind[position.getX() - 1][position.getY()].typeOfCell != memInsides.inspected) && (mind[position.getX() - 1][position.getY()].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY())))) {
                    open.add(mind[position.getX() - 1][position.getY()]);
                    mind[position.getX() - 1][position.getY()].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY()].parentX = position.getX();
                    mind[position.getX() - 1][position.getY()].parentY = position.getY();
                }
            }
        }

        if ((position.getX() > 0) && (position.getY() > 0)) {
            if (open.contains(mind[position.getX()-1][position.getY()-1])){
                if (mind[position.x][position.y].g+1<mind[position.getX()-1][position.getY()-1].g) {
                    open.remove(mind[position.getX()-1][position.getY()-1]);
                    mind[position.getX() - 1][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY() - 1].parentX = position.getX();
                    mind[position.getX() - 1][position.getY() - 1].parentY = position.getY();
                    open.add(mind[position.getX()-1][position.getY()-1]);
                }
            } else {
                if ((mind[position.getX() - 1][position.getY() - 1].typeOfCell != memInsides.inspected) && (mind[position.getX() - 1][position.getY() - 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY() - 1)))) {
                    open.add(mind[position.getX() - 1][position.getY() - 1]);
                    mind[position.getX() - 1][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() - 1][position.getY() - 1].parentX = position.getX();
                    mind[position.getX() - 1][position.getY() - 1].parentY = position.getY();
                }
            }
        }

        if (position.getY() > 0) {
            if (open.contains(mind[position.getX()][position.getY()-1])){
                if (mind[position.x][position.y].g+1<mind[position.getX()][position.getY()-1].g) {
                    open.remove(mind[position.getX()][position.getY()-1]);
                    mind[position.getX()][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX()][position.getY() - 1].parentX = position.getX();
                    mind[position.getX()][position.getY() - 1].parentY = position.getY();
                    open.add(mind[position.getX()][position.getY()-1]);
                }
            } else {
                if ((mind[position.getX()][position.getY() - 1].typeOfCell != memInsides.inspected) && (mind[position.getX()][position.getY() - 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX(), position.getY() - 1)))) {
                    open.add(mind[position.getX()][position.getY() - 1]);
                    mind[position.getX()][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX()][position.getY() - 1].parentX = position.getX();
                    mind[position.getX()][position.getY() - 1].parentY = position.getY();
                }
            }
        }

        if ((position.getX() < 8) && (position.getY() > 0)) {
            if (open.contains(mind[position.getX()+1][position.getY()-1])){
                if (mind[position.x][position.y].g+1<mind[position.getX()+1][position.getY()-1].g) {
                    open.remove(mind[position.getX()+1][position.getY()-1]);
                    mind[position.getX() + 1][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() + 1][position.getY() - 1].parentX = position.getX();
                    mind[position.getX() + 1][position.getY() - 1].parentY = position.getY();
                    open.add(mind[position.getX()+1][position.getY()-1]);
                }
            } else {
                if ((mind[position.getX() + 1][position.getY() - 1].typeOfCell != memInsides.inspected) && (mind[position.getX() + 1][position.getY() - 1].typeOfCell != memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY() - 1)))) {
                    open.add(mind[position.getX() + 1][position.getY() - 1]);
                    mind[position.getX() + 1][position.getY() - 1].setG(mind[position.getX()][position.getY()].g + 1);
                    mind[position.getX() + 1][position.getY() - 1].parentX = position.getX();
                    mind[position.getX() + 1][position.getY() - 1].parentY = position.getY();
                }
            }
        }
    }
    void addCellsBFS(){
        addCells();
        if (cloak!=null) {
            if (position.getX() < 8) {
                if (!open.contains(mind[position.getX() + 1][position.getY()])) {
                    if ((mind[position.getX() + 1][position.getY()].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY())))) {
                        open.add(mind[position.getX() + 1][position.getY()]);
                    }
                }
            }

            if ((position.getX() < 8) && (position.getY() < 8)) {
                if (!open.contains(mind[position.getX() + 1][position.getY() + 1])) {
                    if ((mind[position.getX() + 1][position.getY() + 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY() + 1)))) {
                        open.add(mind[position.getX() + 1][position.getY() + 1]);
                    }
                }
            }

            if (position.getY() < 8) {
                if (!open.contains(mind[position.getX()][position.getY() + 1])) {
                    if ((mind[position.getX()][position.getY() + 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX(), position.getY() + 1)))) {
                        open.add(mind[position.getX()][position.getY() + 1]);
                    }
                }
            }

            if ((position.getX() > 0) && (position.getY() < 8)) {
                if (!open.contains(mind[position.getX() - 1][position.getY() + 1])) {
                    if ((mind[position.getX() - 1][position.getY() + 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY() + 1)))) {
                        open.add(mind[position.getX() - 1][position.getY() + 1]);
                    }
                }
            }

            if (position.getX() > 0) {
                if (!open.contains(mind[position.getX() - 1][position.getY()])) {
                    if ((mind[position.getX() - 1][position.getY()].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY())))) {
                        open.add(mind[position.getX() - 1][position.getY()]);
                    }
                }
            }

            if ((position.getX() > 0) && (position.getY() > 0)) {
                if (!open.contains(mind[position.getX() - 1][position.getY() - 1])) {
                    if ((mind[position.getX() - 1][position.getY() - 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() - 1, position.getY() - 1)))) {
                        open.add(mind[position.getX() - 1][position.getY() - 1]);
                    }
                }
            }

            if (position.getY() > 0) {
                if (!open.contains(mind[position.getX()][position.getY() - 1])) {
                    if ((mind[position.getX()][position.getY() - 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX(), position.getY() - 1)))) {
                        open.add(mind[position.getX()][position.getY() - 1]);
                    }
                }
            }

            if ((position.getX() < 8) && (position.getY() > 0)) {
                if (!open.contains(mind[position.getX() + 1][position.getY() - 1])) {
                    if ((mind[position.getX() + 1][position.getY() - 1].typeOfCell == memInsides.cloakParseable) && (!closed.contains(new Coordinates(position.getX() + 1, position.getY() - 1)))) {
                        open.add(mind[position.getX() + 1][position.getY() - 1]);
                    }
                }
            }
        }
    }
    boolean BFSCloack(){
        closed.clear();
        open.clear();
        vision.firstSee(game,mind,position);
        addCellsBFS();
        while(!open.isEmpty()){
            position.x=open.peek().x;
            position.y=open.poll().y;
            if (cloak==null&&(mind[position.x][position.y].typeOfCell==memInsides.inspected||mind[position.x][position.y].typeOfCell==memInsides.cloakParseable))
            {
                return false;
            }
            if ((game.Filch.getX()== position.getX()&&game.Filch.getY()== position.getY())||(game.Norris.getX()== position.getX()&&game.Norris.getY()== position.getY())){
                return false;
            }
            closed.add(new Coordinates(position.getX(), position.getY()));
            take();
            vision.seeCells(game,mind,position);
            addCellsBFS();
        }
        return true;
    }
    boolean BFS(){
        closed.clear();
        open.clear();
        vision.firstSee(game,mind,position);
        addCells();
        while(!open.isEmpty()){
            position.x=open.peek().x;
            position.y=open.poll().y;
            if (cloak==null&&(mind[position.x][position.y].typeOfCell==memInsides.inspected||mind[position.x][position.y].typeOfCell==memInsides.cloakParseable))
            {
                return false;
            }
            if ((game.Filch.getX()== position.getX()&&game.Filch.getY()== position.getY())||(game.Norris.getX()== position.getX()&&game.Norris.getY()== position.getY())){
                return false;
            }
            closed.add(new Coordinates(position.getX(), position.getY()));
            take();
            vision.seeCells(game,mind,position);
            addCells();
        }
        return true;
    }
    void take(){
        ArrayList<String> insides= game.space[position.getX()][position.getY()].elements;
        ArrayList<String> buffer= new ArrayList<>();
        while(!insides.isEmpty()){
            if (insides.get(0)=="B"){
                book=new Coordinates(position.getX(), position.getY());
            }
            if (insides.get(0)=="C"){

                cloak= new Coordinates(position.getX(), position.getY());
            }
            if (insides.get(0)=="E"){
                reachable=true;
            }
            buffer.add(insides.get(0));
            insides.remove(0);
        }
        game.space[position.getX()][position.getY()].elements=buffer;
    }
    ArrayList<Coordinates> getPath(Coordinates start,Coordinates end){
        refreshMind();
        setHeuristic(end);
        open.clear();
        closed.clear();
        boolean impossible=false;
        position.x=start.getX();
        position.y=start.getY();
        Coordinates previous=new Coordinates();
        mind[position.getX()][position.getY()].g=0;
        open.add(mind[position.getX()][position.getY()]);
        while (true){
            if (open.size()==0){
                impossible=true;
                break;
            }
            position.x=open.peek().x;
            position.y=open.poll().y;
            closed.add(new Coordinates(position.getX(), position.getY()));
            if (position.getX()== end.x&&position.getY()== end.y){
                break;
            }
            addCells();
        }
        ArrayList<Coordinates> path= new ArrayList<>();
        if (impossible){
            path.add(new Coordinates(-1,-1));
        }
        while (true){
            path.add(new Coordinates(position.x, position.y));
            previous.x= position.x;
            previous.y= position.y;
            if (position.x==start.x&&position.y==start.y){
                break;
            }
            position.x=mind[previous.x][previous.y].parentX;
            position.y=mind[previous.x][previous.y].parentY;
        }
        return path;
    }
    ArrayList<Coordinates> getCloakPath(Coordinates start,Coordinates end){
        ArrayList<Coordinates> cloakParsable = new ArrayList<>();
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (mind[i][j].typeOfCell==memInsides.cloakParseable){
                    cloakParsable.add(new Coordinates(i,j));
                    mind[i][j].typeOfCell=memInsides.safe;
                }
            }
        }
        ArrayList<Coordinates> ans=getPath(start, end);
        while (!cloakParsable.isEmpty()){
            mind[cloakParsable.get(0).getX()][cloakParsable.get(0).getY()].typeOfCell=memInsides.cloakParseable;
            cloakParsable.remove(0);
        }
        return ans;
    }
    ArrayList<Coordinates> algorithm(){
        open.clear();
        closed.clear();
        ArrayList<Coordinates> path,path2,path3,buffer;
        if (book!=null&&reachable){
            path = getPath(game.Harry,book);
            path.remove(0);
            buffer= getPath(book,game.Exit);
            buffer.addAll(path);
            path.clear();
            path.addAll(buffer);
            if (cloak!=null){
                path2 = getPath(game.Harry,cloak);
                path2.remove(0);
                buffer = getCloakPath(cloak,book);
                buffer.addAll(path2);
                buffer.remove(0);
                path2 = getCloakPath(book,game.Exit);
                path2.addAll(buffer);

                path3 = getPath(game.Harry,book);
                path3.remove(0);
                buffer = getPath(book,cloak);
                buffer.addAll(path3);
                buffer.remove(0);
                path3 = getCloakPath(cloak,game.Exit);
                path3.addAll(buffer);
                if (path2.size()<=path.size()){
                    path = path2;
                }
                if (path3.size()<=path.size())
                {
                    path=path3;
                }
            }
        } else {
            if (cloak!= null){
                ArrayList<Coordinates> cloakParsable = new ArrayList<>();
                vision.checkAllCloakSafe(mind);
                for (int i=0;i<9;i++){
                    for (int j=0;j<9;j++){
                        if (mind[i][j].typeOfCell==memInsides.cloakParseable){
                            cloakParsable.add(new Coordinates(i,j));
                            mind[i][j].typeOfCell=memInsides.safe;
                        }
                    }
                }
                BFSCloack();
                while (!cloakParsable.isEmpty()){
                    mind[cloakParsable.get(0).getX()][cloakParsable.get(0).getY()].typeOfCell=memInsides.cloakParseable;
                    cloakParsable.remove(0);
                }
                vision.checkAllCloakSafe(mind);
            }
            if (book!=null&&reachable){
                path2 = getPath(game.Harry,cloak);
                path2.remove(0);
                buffer = getCloakPath(cloak,book);
                buffer.addAll(path2);
                buffer.remove(0);
                path2 = getCloakPath(book,game.Exit);
                path2.addAll(buffer);
                path=path2;
            } else {
                path = new ArrayList<>();
                path.add(new Coordinates(-1,-1));
            }
        }
        return path;
    }
    void printPath(ArrayList<Coordinates> path){
        Stack<String> ans=new Stack<>();
        for (int i=0;i<path.size();i++){
            ans.add("["+path.get(i).getX()+","+path.get(i).getY()+"]");
        }
        System.out.println(ans.size());
        while (!ans.isEmpty()){
            System.out.print(ans.pop());
        }
        System.out.println();
    }
    void setHeuristic(Coordinates goal){
        for (int i=0;i<9;i++){
            for (int j=0; j<9;j++){
                mind[i][j].h=Math.max(Math.abs(i- goal.getX()),Math.abs(j- goal.getY()));
                //mind[i][j].h=Math.sqrt(Math.pow((i-goal.getX()),2)+Math.pow((j-goal.getY()),2));
            }
        }
    }
    void refreshMind(){
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                mind[i][j].parentY=-1;
                mind[i][j].parentX=-1;
                mind[i][j].g=99;
            }
        }
    }
    void govno (){
        System.out.println();
        for (int i=8;i>=0;i--){
            for (int j=0; j<9;j++){
                switch (mind[j][i].typeOfCell){
                    case inspected:
                        System.out.print("*");
                        break;
                    case safe:
                        System.out.print("-");
                        break;
                    case unknown:
                        System.out.print("?");
                        break;
                    case cloakParseable:
                        System.out.print("~");
                        break;
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    void printHeuristic(){
        System.out.println();
        for (int i=8;i>=0;i--){
            for (int j=0; j<9;j++){
                mind[j][i].printH();
                System.out.print("\t");
            }
            System.out.println();
        }
    }
    void printMovements(){
        for (int i=8;i>=0;i--){
            for (int j=0; j<9;j++){
                mind[j][i].printG();
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}