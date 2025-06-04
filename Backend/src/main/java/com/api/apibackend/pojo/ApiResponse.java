package com.api.apibackend.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // 忽略为 null 的字段
public class ApiResponse<T> {

    // HTTP 状态码
    private int status;
    // 业务错误对象，失败时非 null
    private ErrorDetail error;
    // 成功数据对象，成功时非 null
    private T data;

    private ApiResponse(int status, ErrorDetail error, T data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(int status, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int status, ErrorDetail error) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, error, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> uploadFailedError() {
        return ApiResponse.error(400, ErrorDetail.uploadFailed);
    }

    public static <T> ResponseEntity<ApiResponse<T>> userNotExistError() {
        return ApiResponse.error(404, ErrorDetail.userNotExist);
    }

    public static <T> ResponseEntity<ApiResponse<T>> videoNotExistError() {
        return ApiResponse.error(404, ErrorDetail.videoNotExist);
    }

    public static <T> ResponseEntity<ApiResponse<T>> noMoreUnseenError() {
        return ApiResponse.error(400, ErrorDetail.noMoreUnseen);
    }

    public static <T> ResponseEntity<ApiResponse<T>> likeLikedError() {
        return ApiResponse.error(400, ErrorDetail.likeLiked);
    }

    public static <T> ResponseEntity<ApiResponse<T>> usernameUsedError() {
        return ApiResponse.error(422, ErrorDetail.usernameUsed);
    }

    public static <T> ResponseEntity<ApiResponse<T>> noDeletePermissionError() {
        return ApiResponse.error(401, ErrorDetail.noDeletePermission);
    }

    public static <T> ResponseEntity<ApiResponse<T>> offsetTooBigError() {
        return ApiResponse.error(422, ErrorDetail.offsetTooBig);
    }

    public static <T> ResponseEntity<ApiResponse<T>> wrongPasswordError() {
        return ApiResponse.error(422, ErrorDetail.wrongPassword);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unlikeUnlikedError() {
        return ApiResponse.error(400, ErrorDetail.unlikeUnliked);
    }

}
