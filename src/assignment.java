import java.util.ArrayList;

enum insides {
    Harry,Filch,Book,Cloak,Norris,Exit,inspectorZone
}
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
}
public class assignment {
    public static void main(String[] args) {
        Game igra= new Game();
        igra.randomGenerateGame();
        igra.printGame();
   }
}

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
    * This method prepares the map for the algorithm, setting the environment in 2d array of ints
    *
    * */
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
    void printGame(){
        for (int i=8;i>=0;i--){
            for (int j=0; j<9;j++){
                space[i][j].print();
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

class cell{
    ArrayList<String> elements= new ArrayList<>();
    boolean inspected;
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
    void prepare(){
        elements.clear();
        inspected=false;
    }
}
enum memInsides{
    unknown,safe,inspected,
}
enum windRose{
    N,NE,E,SE,S,SW,W,NW
}
class memoryCell{
    int steps;

}
class BackTracking{
    int perception;
}