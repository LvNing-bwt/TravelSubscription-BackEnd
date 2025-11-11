package me.hearta.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Data
public class Response<T> {
    private Code code;
    private String message;
    private T data;

    @Getter
    public enum Code{
        SUCCESS(200, "成功"),
        BAD_REQUEST(400, "请求参数错误"),
        UNAUTHORIZED(401, "未授权"),
        FORBIDDEN(403, "禁止访问"),
        NOT_FOUND(404,"资源不存在"),
        SERVER_ERROR(500, "服务器错误");

        private final int value;
        private final String desc;

        Code(int value,String desc){
            this.value = value;
            this.desc = desc;
        }
    }

    // 成功相应
    public static <T> Response<T> success(T data){
        Response<T> response = new Response<>();
        response.setCode(Code.SUCCESS);
        response.setMessage(Code.SUCCESS.getDesc());
        response.setData(data);
        return response;
    }

    // 错误相应
    public static <T> Response<T> error(Code code,@Nullable String message){
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message != null ? message : code.getDesc());
        return response;
    }

    public static <T> Response<T> error(Code code){
        return error(code,null);
    }
}
