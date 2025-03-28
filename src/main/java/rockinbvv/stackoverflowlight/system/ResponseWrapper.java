package rockinbvv.stackoverflowlight.system;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {

    private Boolean success;
    private T data;
    private String errorMessage;

    public static ResponseWrapper ok() {
        return ResponseWrapper.builder()
                .data(null)
                .success(true)
                .build();
    }

    public static <T> ResponseWrapper<T> ok(T data) {
        return ResponseWrapper.<T>builder()
                .data(data)
                .success(true)
                .build();
    }

    public static <T> ResponseWrapper<T> error(String errorMessage, T data) {
        return ResponseWrapper.<T>builder()
                .success(false)
                .errorMessage(errorMessage)
                .data(data)
                .build();
    }

    public static ResponseWrapper<Void> error(String errorMessage) {
        return error(errorMessage, null);
    }
}
