package com.api.apibackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetail {
    // 业务错误码
    private int code;
    // 错误信息
    private String msg;

    public static final ErrorDetail videoNotExist = new ErrorDetail(1, "This video isn't exist.");
    public static final ErrorDetail uploadFailed = new ErrorDetail(2, "Failed to upload video.");
    public static final ErrorDetail userNotExist = new ErrorDetail(3, "This user isn't exist.");
    public static final ErrorDetail noMoreUnseen = new ErrorDetail(4, "There's no more unseen video.");
    public static final ErrorDetail likeLiked = new ErrorDetail(5, "The video is already liked.");
    public static final ErrorDetail usernameUsed = new ErrorDetail(6, "The username is already used.");
    public static final ErrorDetail noDeletePermission = new ErrorDetail(7, "You have no permission to delete this video.");
    public static final ErrorDetail offsetTooBig = new ErrorDetail(8, "The offset is too big.");
    public static final ErrorDetail wrongPassword = new ErrorDetail(9, "The password isn't correct.");
    public static final ErrorDetail unlikeUnliked = new ErrorDetail(10, "The video is not liked.");

}
