package exceptions;

public class AssignmentLimitExceededException extends InventoryException {

    public AssignmentLimitExceededException(String message) {
        super(message);
    }
}
