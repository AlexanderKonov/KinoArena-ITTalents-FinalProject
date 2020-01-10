package season11.kino_arena.exceptions;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String s){
        super(s);
    }

    public AuthorizationException(){
        super("You must log in to use this service!");
    }
}
