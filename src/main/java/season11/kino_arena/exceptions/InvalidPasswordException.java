package season11.kino_arena.exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){
        super("Passwords are invalid or not matching.");
    }
}
