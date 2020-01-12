package season11.kino_arena.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg){
        super(msg);
    }
    public  NotFoundException(){
        super("Not found!");
    }
}
