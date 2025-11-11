package org.travel.service;

import org.travel.dto.RegisterData;
import org.travel.dto.RegisterRequest;


public interface RegisterService {
    public RegisterData register(RegisterRequest registerRequest);
}
