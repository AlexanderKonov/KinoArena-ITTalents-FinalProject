package season11.kino_arena.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String msg){
        super(msg);
    }
    public BadRequestException(){
        super("Bad request.");
    }
}
