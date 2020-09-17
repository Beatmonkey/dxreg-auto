package model.api;

public class Result<T> {
    public T data;
    public Error error;
    boolean success;


    public static <T> Result<T> successful(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.success = true;
        return result;
    }

    public static <T> Result<T> failed(Error error) {
        Result<T> result = new Result<>();
        result.error = error;
        result.success = false;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }
}
