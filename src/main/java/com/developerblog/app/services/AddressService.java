package com.developerblog.app.services;

import java.util.List;

import com.developerblog.app.shared.dto.AddressDTO;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
