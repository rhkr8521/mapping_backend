package com.mapping.mapping.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D> {
    private boolean result;

    private String message;
    private D data;

    //성공했을 때 인스턴스
    public static <D> ResponseDto<D> setSuccess(String message, D data){
        return ResponseDto.set(true,message,data);
    }
    //실패했을 때 인스턴스
    public static <D> ResponseDto<D> setFailed(String message){
        return ResponseDto.set(false,message,null);
    }
}