package model.api;

public class Result<T> {

    private T data;
    private Error error;
    private boolean success;


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

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }
}
