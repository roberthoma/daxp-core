package org.daxprotocol.core.mapper;

import org.daxprotocol.core.config.DaxpConfig;

public class DaxMessageMapper extends DaxStringReferenceMapper {

    public DaxMessageMapper() {
        super(DaxpConfig.START_IDX_MSG_MAPPER);
    }

}
