package free.lightokens.api.builder;

/**
 * Instantiates instances of entity classes representing
 * a database entity
 * @author Mounir Regragui
 *
 * @param <E> the type of entity
 */
public interface EntityBuilder<E> {

	/**
	 * Instantiates a new instance of the entity
	 * @return the new instance
	 */
	E instantiate();
}
