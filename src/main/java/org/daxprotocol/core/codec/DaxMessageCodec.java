/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */
package org.daxprotocol.core.codec;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.parsers.DaxParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//public class DaxMessageCodec implements DaxCodec<DaxMessage>{
public class DaxMessageCodec {
    DaxConfig config;
    DaxPairCodec     pairCodec;
    DaxPreambleCodec preambleCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;
    DaxParser parserService;
    public DaxMessageCodec(
            DaxConfig config,
            DaxPairCodec pairCodec,
            DaxPreambleCodec preambleCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec,
            DaxParser parserService) {
      this.pairCodec = pairCodec;
      this.preambleCodec = preambleCodec;
      this.headCodec = headCodec;
      this.bodyCodec = bodyCodec;
      this.trailerCodec = trailerCodec;
      this.config = config;
      this.parserService = parserService;

    }

//TODO move to any service

    public String encodeAll(List<DaxMessage> messageList) {
    return " no messss !!!";
    }
        //@Override
    public String encode(DaxMessage message) {
        StringBuilder sb = new StringBuilder();
        DaxPreamble preamble = new DaxPreamble();
        preamble.setEncoding(config.getDefaultEncoding());
        preamble.setMsgContextId(config.getAppContextId());

        StringBuilder msgSb = new StringBuilder();

        msgSb.append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
             .append(bodyCodec.encode(message.getBody()));

        DaxTrailer trailer = new DaxTrailer();

        trailer.setChecksum(DaxCodecService.calculateChecksum(msgSb.toString()));

        sb.append(preambleCodec.encode(preamble))
                .append(msgSb)
                .append(trailerCodec.encode(trailer));

        //TODO create statistics counter
        //TODO  System.out.println("TODO Counter statistics message length = "+sb.length());

        return sb.toString();
    }

   private DaxMessage createMsg(List<DaxPair<?>> listOfPair){
       DaxHead head;
       DaxBody body;
       DaxTrailer trailer;

       head = headCodec.createHead(listOfPair);
       body = bodyCodec.createBody(head.getBlockCount(), listOfPair) ;
       trailer = trailerCodec.createTrailer(listOfPair);
       //todo trailer with check

       return new DaxMessage(head,body,trailer);
   }


//TODO Add validation after creation of DaxMessage. for example message with blocks, without BLOCK_TYPE !!!





    /// ////
    public List<DaxMessage> decodeAll(String msgStr) {
        List<DaxMessage> messageList = new ArrayList<>();
        DaxPreamble preamble = preambleCodec.decode(msgStr);

//        String fisrtTagType = "|9=";
        String strTagType = ""+
                DaxConfig.PAIR_SEPARATOR+ DaxTagConst.MSG_TYPE.getTagId()+ DaxConfig.EQUAL;

        String strTagBlock = ""+
                DaxConfig.PAIR_SEPARATOR+ DaxTagConst.BLOCK_INDEX.getTagId()+ DaxConfig.EQUAL;


        int fistMsgIdx = msgStr.indexOf(strTagType);

        String msgPairsStr = msgStr.substring(fistMsgIdx);


    //tmp    List<String> messagesList = parserService.splitByTag(msgPairsStr,strTagType);

        List<List<String>> listOfMsgBlock = new ArrayList<>();
//tmp
//        messagesList.forEach(s -> {
//            int fistBBIdx = s.indexOf(strTagBlock);
//
//            if (fistBBIdx==-1){
//                List<String> sss = new ArrayList<>();
//                sss.add(s);
//                listOfMsgBlock.add(sss);
//            }
//           else {
//                //  sss.add(s.substring(0,fistBBIdx));
//                List<String> sss = new ArrayList<>(parserService.splitByTag(s, strTagBlock));
//
//                listOfMsgBlock.add(sss);
//                //listOfMsgBlock.add(parserService.splitByTag(s, strTagType));
//           }
//
//
//
//        });

         //MSG List> Block List> Map of tag  and string value
        // rebuild "Primitive Obsession"


        List<List<Map< DaxTag, String>>> listMsgValue = new ArrayList<>();
//tmp
//        listOfMsgBlock.forEach(blockList ->  {
//                    List<Map< DaxTag, String>> mapTagValueList = new ArrayList<>();
//                      blockList.forEach(s ->
//                              mapTagValueList.add( parserService.parserBlock(s))
//                       );
//                    listMsgValue.add(mapTagValueList);
//        }
//        );


     //   Map<DaxTag, String> blockPairMap = parserService.blockParse();

        //tmp
//        List<DaxPair<?>> listOfPair = parserService.
//                          parsePairs(msgPairsStr,
//                                     DaxPatternFactory.compileMessagePairPattern(DaxpConfig.PAIR_SEPARATOR),
//                                     preamble.getMsgContextId());

        //tmp
        //List<List<DaxPair<?>>> msgPairList =  parserService.splitMessages(listOfPair);




//tmp
//        msgPairList.forEach(pairs ->
//            messageList.add(createMsg(pairs))
//        );

        return messageList;
    }

   // @Override
    public DaxMessage decode(String msg) {
        return decodeAll(msg).get(0);
    }


    public  int getMessageCount(String msgStr){
        DaxPreamble preamble = preambleCodec.decode(msgStr);
      return preamble.getMsgCnt() ;
    }

}
