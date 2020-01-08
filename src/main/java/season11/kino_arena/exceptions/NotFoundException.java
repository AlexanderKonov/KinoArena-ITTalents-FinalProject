package season11.kino_arena.exceptions;

public class NotFoundException extends Exception {
    public  NotFoundException(){
        super("Not found!");
    }
    public NotFoundException(String msg){
        super(msg);
    }
}
