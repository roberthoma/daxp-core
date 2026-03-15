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
package org.daxprotocol.core.model.preamble;

public class DaxPreambleTag {
    public static final String  DAXP = "DAXP";   // protocol identifier
//    public static final String  VERSION = "V";   // protocol  version
    public static final String  ENCODING = "EN";       // encoding: ASCII | UTF-8 | UTF-16
    public static final String  MSG_COUNT = "MC";     //  Number of messages following preamble. Useful for validation.
    public static final String  MSG_CONTEXT = "CX";     // context: optional
    public static final String  MSG_SENDER = "SN";     // context: optional
}


/*
TF 	Tag Format
EN 	Encoding
MC 	Message
CX 	Context
TS 	Timestamp
SN 	Sender
TG 	Target
TK 	Token
 */