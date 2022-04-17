package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码
 * @author Junho
 * @date 2022/4/17 10:10
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    METHOD_NOT_FOUND(500, "未找到指定方法"),
    CLASS_NOT_FOUND(500, "未找到指定类");

    private final int code;
    private final String message;

}
