import java.io.IOException;

public interface ITask<T> {
	public T execute() throws IOException;
}
