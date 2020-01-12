package season11.kino_arena.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String msg){
        super(msg);
    }
    public BadRequestException(){
        super("Bad request.");
    }
}
