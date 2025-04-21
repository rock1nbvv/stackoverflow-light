package rockinbvv.stackoverflowlight.app.exception;

public class EntityNotFoundException extends ApplicationException {
    /**
     * Constructs a standardized not-found exception for a given entity and field.
     *
     * @param entityType the logical type of the entity (e.g. "Post", "User")
     * @param field      the name of the field used in the lookup (e.g. "id", "email")
     * @param value      the value of the field that was not found
     */
    public EntityNotFoundException(EntityType entityType, String field, Object value) {
        super(String.format("Entity %s not found with %s: %s", entityType, field, value));
    }

    @Override
    public String getErrorCode() {
        return "ENTITY_NOT_FOUND";
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }

}
