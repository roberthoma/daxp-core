package org.daxprotocol.core.mapper;

import org.daxprotocol.core.config.DaxConfig;

public class DaxMessageMapper extends DaxStringReferenceMapper {

    public DaxMessageMapper() {
        super(DaxConfig.START_IDX_MSG_MAPPER);
    }

}
