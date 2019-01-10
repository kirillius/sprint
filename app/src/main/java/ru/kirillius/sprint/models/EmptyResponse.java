package ru.kirillius.sprint.models;

/**
 * Created by Lavrentev on 25.12.2017.
 */

public class EmptyResponse {
    public String errorCode, errorMessage;

    public EmptyResponse() {

    }

    public EmptyResponse(String errorCode, String errorMessage) {
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }
}
