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
        Talker talker= new Talker();
        Game game = new Game();
        ArrayList<Coordinates> cords=new ArrayList<>();
        int response= talker.getType();
        if (response==0){
            game.randomGenerateGame();
        } else {
            while (true){
                cords=talker.getData();
                if (game.setGame(cords.get(0),cords.get(1),cords.get(2),cords.get(3),cords.get(4),cords.get(5))){
                    break;
                }
            }
        }
        ArrayList<Coordinates> cordsBuffer = new ArrayList<>();
        for (int i=0;i<cords.size();i++){
            cordsBuffer.add(new Coordinates(cords.get(i).getX(),cords.get(i).getY()));
        }
        int perception = talker.getPerception();
        long t2,t1=System.currentTimeMillis();
        BackTracking backTracking= new BackTracking(game,perception);
        cords=backTracking.getResult();
        t2=System.currentTimeMillis();
        t2=t2-t1;
        System.out.println("Backtracking Algorithm");
        if (cords.size()>1){
            System.out.println("Win");
            System.out.println(cords.size());
            while (!cords.isEmpty()){
                System.out.print("["+cords.get(0).getX()+","+cords.get(0).getY()+"]");
                cords.remove(0);
            }
            System.out.println();
            System.out.println("Time taken in ms: "+t2);
        } else {
            System.out.println("Lose");
        }

        System.out.println();
        Game game1 = new Game();
        game1.setGame(cordsBuffer.get(0),cordsBuffer.get(1),cordsBuffer.get(2),cordsBuffer.get(3),cordsBuffer.get(4),cordsBuffer.get(5));

        AStar aStar = new AStar(perception,game1);
        t1=System.currentTimeMillis();
        cords=aStar.algorithm();
        t2=System.currentTimeMillis();
        t2=t2-t1;
        System.out.println("A star Algorithm");
        if (cords.size()>1){
            System.out.println("Win");
            aStar.printPath(cords);
            System.out.println("Time taken in ms: "+t2);
        } else {
            System.out.println("Lose");
        }
   }
}

/**
 * This class works with console input. used for getting data from user
 */
class Talker{
    Talker(){

    }
    int getType(){
        Scanner sc = new Scanner(System.in);
        int info;
        while (true){
            info=sc.nextInt();
            if (info==0||info==1){
                break;
            }
        }
        return info;
    }
    int getPerception(){
        Scanner sc = new Scanner(System.in);
        int info;
        while (true){
            info=sc.nextInt();
            if (info==1||info==2){
                break;
            }
        }
        return info;
    }
    ArrayList<Coordinates> getData(){
        Scanner sc = new Scanner(System.in);
        String info;
        String[] pos;
        ArrayList<Coordinates> data= new ArrayList<>();
        while (true){
            data.clear();
            info=sc.nextLine();
            pos=info.split(" ");
            if (pos.length!=6){
                System.out.println("invalid input");
                continue;
            }
            String[] cords;
            for (int i=0;i<6;i++){
                if (pos[i].length()>2){
                    pos[i]=pos[i].substring(1);
                    pos[i]=pos[i].substring(0,pos[i].length()-1);
                    cords=pos[i].split(",");
                    if (cords.length!=2){
                        System.out.println("invalid input");
                        continue;
                    }
                    data.add(new Coordinates(Integer.parseInt(cords[0]),Integer.parseInt(cords[1])));
                }
            }
            if (data.size()==6){
                for (int i=0;i<6;i++){
                    if (data.get(i).getX()<0||data.get(i).getX()>8||data.get(i).getY()<0||data.get(i).getY()>8){
                        System.out.println("invalid boundaries");
                        continue;
                    }
                }
                break;
            }
        }
        return data;
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

    /**
     * The algorithm
     * first checks if Harry is caught, then moves to the new cell, marks it as a visited, take all object on it. Proceed next call,
     * if win conditions are not met.
     * @param cellMoveInto coordinates of the cell where Harry moves into
     * @return true if found the path to exit with book and didn't get caught, false otherwise
     */
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
        if (trace.size()>9){
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

    /**
     * Function that allows Harry to take object from the cell where he is at the moment
     * @return true if found the exit and the book was taken, false otherwise
     */
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
                takeBook();
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

    /**
     * This function prints mind map of the algorithm for debugging
     * "H" means position of Harry
     * "-" means safe
     * "~" means cloakParsable
     * "?" means unknown
     * "*" means inspected
     */
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
                        if (mind[j][i].typeOfCell==memInsides.unknown){
                            System.out.print("?");
                        } else {
                            System.out.print("*");
                        }
                    }
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * This function is called when Harry found the book. It marks all cells as unvisited.
     * copy the trace and clears it, then proceeds the algorithm
     */
    void takeBook(){
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

    /**
     * the same function as takeBook,but is called when Harry found the cloak
     */
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

    /**
     * Function that calls the algorithm and prints the result
     */
    ArrayList<Coordinates> getResult(){
        ArrayList<Coordinates> path= new ArrayList<>();
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
                        path.add(new Coordinates(answer.peek().getX(),answer.pop().getY()));
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
                        path.add(new Coordinates(answer.peek().getX(),answer.pop().getY()));
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
                    path.add(new Coordinates(answer.peek().getX(),answer.pop().getY()));
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
                        path.add(new Coordinates(answer.peek().getX(),answer.pop().getY()));
                    }
                } else {
                    path.add(new Coordinates(-1,-1));
                }
            } else {
                path.add(new Coordinates(-1,-1));
            }
        }
        return path;
    }
}

/**
 * This class works with mind maps of the algorithms. It checks if the cell is safe, cloakParsable or inspected
 */
class Vision{
    int perception;
    Vision(int n){
        perception=n;
    }

    /**
     * This function checks cells' type from the current position
     * @param game the game, from which it takes data about safety of cells
     * @param mind the mind map of the algorithm that needs to be updated
     * @param position current position of Harry, from which he perceives information about cells
     */
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

    /**
     * This function is used for the first check of cells type
     * for perception 1 it just calls seeCells function, but for perception 2 it also decides which cells are theoretically unsafe
     * @param game the game, from which it takes data about safety of cells
     * @param mind the mind map of the algorithm that needs to be updated
     * @param position current position of Harry, from which he perceives information about cells
     */
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

    /**
     * this function updates information about all neighbours of the chosen cell
     * @param mind the mind map of the algorithm that needs to be updated
     * @param position position, neighbours of which are need to be updated
     */
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

    /**
     * this function calls checkCloakSafe function for every cell of the mind map
     * @param   mind map of the algorithm that needs to be updated
     */
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

/**
 * This class extends from the MemoryCell class. It has additional information required for correct work
 * of the A star algorithm
 */
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
    // Each time we change G, we have to recalculate h
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

    /**
     * This function compares two StarCells
     * first it checks that both are safe,if not then the safest is better
     * After that it compares f value to choose the smallest one, in case of the same f value it compares h(heuristics)
     * @param o StarCell to compare with
     * @return -1 is better(has less values, safe); 0 if they are equal; 1 if worse than the comparable one
     */
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

/**
 * The A star algorithm with several modifications:
 * before the search it uses bfs to search through all cell, so it'll know the location of objects
 * after that it calculates several paths and choses the best one
 */
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
    }

    /**
     * This function adds to the priority queue all acceptable neighbours of this cell.
     * Also calculates g(and as consequence f) when uses A* algorithm
     */
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

    /**
     * This function called in BFSCloak algorithm. It allows for harry to go through cloakParsable cell during the BFS algorithm
     */
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

    /**
     * Algorithm of BFS that can go through cloakParsable cells. Called if failed normal BFS, but cloak is found
     * @return False if Harry gets caught, true otherwise
     */
    boolean BFSCloak(){
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
    /**
     * Algorithm of BFS that fills mind map with information about cells and stores coordinates of found objects
     */
    void BFS(){
        closed.clear();
        open.clear();
        vision.firstSee(game,mind,position);
        addCells();
        while(!open.isEmpty()){
            position.x=open.peek().x;
            position.y=open.poll().y;
            closed.add(new Coordinates(position.getX(), position.getY()));
            take();
            vision.seeCells(game,mind,position);
            addCells();
        }
    }

    /**
     * This function allows Harry to check what can be taken in this cell
     * Used durnig BFS or BFSCloak
     */
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

    /**
     * The "A star" algorithm for finding path between 2 cells
     * @param start position from which Harry starts
     * @param end position when Harry should end
     * @return ArrayList of Coordinates that contains the path in reversed order including both ends. If there is no such path -
     * it contains only [-1,-1] coordinate
     */
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
    /**
     * The "A star" algorithm for finding path between 2 cells with possibility to go through cloakParsable cells
     * @param start position from which Harry starts
     * @param end position wher Harry should end
     * @return ArrayList of Coordinates that contains the path in reversed order including both ends. If there is no such path -
     * it contains only [-1,-1] coordinate
     */
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

    /**
     * This function return the result of the algorithm
     * It calculates all possible paths and chooses the best(shortest) one
     * @return The path, if there is no such path returns ArrayList with one element -[-1,-1]
     */
    ArrayList<Coordinates> algorithm(){
        BFS();
        vision.checkAllCloakSafe(mind);
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
                position= new Coordinates(game.Harry.getX(),game.Harry.getY());
                 if (!BFSCloak()){
                     System.out.println("wrong");
                     game.printGame();
                 }
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

    /**
     * This function sets diagonal heuristics: cost is one for straight and diagonal move
     * @param goal the goal around which the heuristics will be calculated
     */
    void setHeuristic(Coordinates goal){
        for (int i=0;i<9;i++){
            for (int j=0; j<9;j++){
                mind[i][j].h=Math.max(Math.abs(i- goal.getX()),Math.abs(j- goal.getY()));
            }
        }
    }

    /**
     * Reset parents and costs of cells in the mind map
     */
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

    /**
     * prints heuristics of the mind map, used for debugging
     */
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
    /**
     * prints g values of the mind map, used for debugging
     */
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