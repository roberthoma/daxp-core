package org.daxprotocol.core.parsers;

import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.List;

public interface DaxParser {

    DaxTag           parseDaxTag(String tagStr, int msgContextId);

    List<DaxPair<?>> parsePairs(String pairsStr, int msgContextId);

    DaxPair<?>       parsePair(String pairStr, int msgContextId);

    List<DaxTag>     parseDaxTagList(String tagListStr, int msgContextId);

    DaxPreamble      parsePreamble(String msg);

    List<DaxMessage> parseMessageList(String msg);


}