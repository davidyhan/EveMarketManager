package exceptions;

public class ItemNotFoundException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = 6240446572878248184L;

    public ItemNotFoundException(String message) {
        super(message);
    }
}
