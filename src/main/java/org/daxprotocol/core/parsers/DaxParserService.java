package org.daxprotocol.core.parsers;

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.List;

public interface DaxParserService {

    DaxTag parseDaxTag(String tagStr, int msgContextId);

    List<DaxPair<?>> parsePairs(String pairsStr, int msgContextId);

    DaxPair<?> parsePair(String pairStr, int msgContextId);

    List<DaxTag> parseDaxTagList(String tagListStr, int msgContextId);

}