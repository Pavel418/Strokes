import java.util.Stack;

/**
 * This class represents a stack with a maximum size.
 * @param <T> The type of the stack.
 */
public class SizedStack<T> extends Stack<T> {

	private final int maxSize;

	/**
	 * This constructor is used to create a new sized stack.
	 * @param size The maximum size of the stack.
	 */
	public SizedStack(int size) {
		super();
		this.maxSize = size;
	}

	/**
	 * This method is used to push an object to the stack.
	 * @param object The object to push.
	 * @return The object that was pushed.
	 */
	@Override
	public Object push(Object object) {
		while (this.size() > maxSize) {
			this.remove(0);
		}
		return super.push((T) object);
	}
}