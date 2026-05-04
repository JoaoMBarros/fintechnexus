package com.fintechnexus.api.domain.mappers;

import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.application.dto.AccountCreationResponseDTO;
import com.fintechnexus.api.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class)
public interface AccountMapper {

    Account toModel(AccountCreationDTO dto);

    @Mapping(target = "accountStatus", source = "status")
    @Mapping(target = "balance", source = "balanceAsDecimal")
    AccountCreationResponseDTO toDto(Account entity);
}
