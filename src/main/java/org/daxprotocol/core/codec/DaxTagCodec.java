package org.daxprotocol.core.codec;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxTagCodec {
    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    public DaxTagCodec(DaxpConfig config, DaxStringReferenceMapper contextMapper){
      this.config = config;
      this.contextMapper = contextMapper;
    }

    public String encode( DaxTag tag){
        if(tag.getContextId() != DaxpConfig.DAXP_CONTEXT_ID &&
                tag.getContextId() != config.getAppContextId() )
        {
            return  contextMapper.getReference(tag.getContextId()) +
                    DaxpConfig.CONTEXT_TAG_SEPARATOR + tag.getTagId();
        }
        return String.valueOf(tag.getTagId());

    }
//    public void encode( DaxTag tag, StringBuilder sb){
//
//        if(tag.getContextId() != DaxpConfig.DAXP_CONTEXT_ID &&
//                tag.getContextId() != config.getAppContextId() )
//        {
//            sb.append(contextMapper.getReference(tag.getContextId() ))
//                    .append(DaxpConfig.CONTEXT_TAG_SEPARATOR);
//        }
//
//
//        sb.append(tag.getTagId())
//        ??????
//
//    }

    public DaxTag decode(String tagStr){
        DaxTag tag = new DaxTag(0,0);

        return tag;
    }



}
