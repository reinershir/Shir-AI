package io.github.reinershir.ai.entity;
import lombok.Data;

import java.io.Serializable;

@Data
public class Rs<T> implements Serializable {
    private static final long serialVersionUID = 5393448305311433341L;
    private int code;
    private String msg;
    private T data;
    private Boolean state;
    private String challenge;


    public Rs(int code, String msg, T data,String challenge) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.challenge = challenge;
    }

    public Rs(int code, String msg, T data,Boolean state) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.state=state;
    }

}
