package com.project.container.network.provider;



public interface UserCaseProvider extends Provider {
    String token();
    String locationCode();
    void storageToken(String token);
}
