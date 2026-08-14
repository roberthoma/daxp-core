# DAXP Error Types

DAXP errors are grouped by the layer in which the error occurs.

## 1. FRAME_ERROR

Errors related to the complete DAXP frame.

Examples:

* `INVALID_FRAME`
* `EMPTY_FRAME`
* `INCOMPLETE_FRAME`
* `FRAME_TOO_LARGE`
* `INVALID_FRAME_STRUCTURE`
* `UNEXPECTED_END_OF_FRAME`

Example code range:

`1000–1099`

---

## 2. PREAMBLE_ERROR

Errors related to the DAXP preamble.

Examples:

* `PREAMBLE_MISSING`
* `INVALID_PREAMBLE`
* `PROTOCOL_VERSION_MISSING`
* `UNSUPPORTED_PROTOCOL_VERSION`
* `ENCODING_MISSING`
* `UNSUPPORTED_ENCODING`
* `NAMESPACE_MISSING`
* `UNKNOWN_NAMESPACE`
* `INVALID_NAMESPACE_ALIAS`
* `DUPLICATE_PREAMBLE_PARAMETER`

Example code range:

`1100–1199`

---

## 3. SYNTAX_ERROR

Errors detected while parsing DAXP tokens.

Examples:

* `INVALID_TOKEN`
* `INVALID_TAG`
* `INVALID_OPERATOR`
* `INVALID_OPERAND`
* `INVALID_SEPARATOR`
* `MISSING_OPERATOR`
* `MISSING_OPERAND`
* `UNEXPECTED_SEPARATOR`
* `MALFORMED_TRIPLET`

Example code range:

`1200–1299`

---

## 4. STRUCTURE_ERROR

Errors where individual tokens are syntactically valid, but the DAXP message structure is incorrect.

Examples:

* `MESSAGE_HEADER_MISSING`
* `MESSAGE_BODY_MISSING`
* `MESSAGE_TRAILER_MISSING`
* `INVALID_MESSAGE_ORDER`
* `INVALID_BLOCK_STRUCTURE`
* `BLOCK_COUNT_MISMATCH`
* `MESSAGE_COUNT_MISMATCH`
* `UNEXPECTED_BLOCK`
* `DUPLICATE_BLOCK`
* `UNEXPECTED_MESSAGE`

Example code range:

`1300–1399`

---

## 5. DICTIONARY_ERROR

Errors related to the DAXP dictionary and tag definitions.

Examples:

* `DICTIONARY_NOT_FOUND`
* `DICTIONARY_VERSION_NOT_FOUND`
* `UNKNOWN_TAG`
* `UNKNOWN_MESSAGE_TYPE`
* `UNKNOWN_ENTITY`
* `UNKNOWN_DATA_TYPE`
* `UNKNOWN_ACTION`
* `TAG_NOT_ALLOWED`
* `TAG_DEPRECATED`
* `INVALID_DICTIONARY_REFERENCE`
* `DICTIONARY_VERSION_MISMATCH`

Example code range:

`1400–1499`

---

## 6. VALIDATION_ERROR

The message is syntactically and structurally valid, but its data violates the model definition.

Examples:

* `REQUIRED_TAG_MISSING`
* `NULL_NOT_ALLOWED`
* `EMPTY_VALUE_NOT_ALLOWED`
* `INVALID_DATA_TYPE`
* `INVALID_VALUE`
* `VALUE_TOO_SHORT`
* `VALUE_TOO_LONG`
* `VALUE_BELOW_MINIMUM`
* `VALUE_ABOVE_MAXIMUM`
* `INVALID_PRECISION`
* `VALUE_NOT_ALLOWED`
* `INVALID_FORMAT`
* `DUPLICATE_TAG`

Example code range:

`1500–1599`

---

## 7. REFERENCE_ERROR

Errors related to DAXP references.

Examples:

* `INVALID_REFERENCE`
* `REFERENCE_NOT_FOUND`
* `INVALID_REFERENCE_TYPE`
* `CIRCULAR_REFERENCE`
* `REFERENCE_TARGET_MISSING`
* `REFERENCE_OUT_OF_SCOPE`

Example code range:

`1600–1699`

---

## 8. ACTION_ERROR

Errors related to DAXP operators/actions such as `^`.

Examples:

* `UNKNOWN_ACTION`
* `ACTION_NOT_ALLOWED`
* `INVALID_ACTION_ARGUMENT`
* `ACTION_TARGET_NOT_FOUND`
* `ACTION_NOT_SUPPORTED`
* `NULL_ACTION_NOT_ALLOWED`

Example code range:

`1700–1799`

---

## 9. INTEGRITY_ERROR

Errors indicating corrupted or inconsistent message content.

Examples:

* `CHECKSUM_MISSING`
* `INVALID_CHECKSUM`
* `CHECKSUM_MISMATCH`
* `MESSAGE_LENGTH_MISMATCH`
* `BLOCK_LENGTH_MISMATCH`
* `CORRUPTED_MESSAGE`

Example code range:

`1800–1899`

---

## 10. PROCESSING_ERROR

The DAXP message is valid, but the receiving application cannot execute the requested operation.

Examples:

* `PROCESSING_FAILED`
* `OPERATION_NOT_SUPPORTED`
* `RESOURCE_NOT_FOUND`
* `RESOURCE_ALREADY_EXISTS`
* `OPERATION_CONFLICT`
* `BUSINESS_RULE_VIOLATION`
* `REQUEST_REJECTED`
* `PROCESSING_TIMEOUT`

Example code range:

`2000–2099`

---

## 11. SECURITY_ERROR

Errors related to authentication, authorization, or message security.

Examples:

* `AUTHENTICATION_REQUIRED`
* `AUTHENTICATION_FAILED`
* `ACCESS_DENIED`
* `INVALID_TOKEN`
* `TOKEN_EXPIRED`
* `SIGNATURE_MISSING`
* `INVALID_SIGNATURE`
* `MESSAGE_NOT_TRUSTED`

Example code range:

`2100–2199`

---

## 12. SYSTEM_ERROR

Errors caused by the implementation or infrastructure rather than by the DAXP message itself.

Examples:

* `INTERNAL_ERROR`
* `SERVICE_UNAVAILABLE`
* `DEPENDENCY_UNAVAILABLE`
* `DATABASE_ERROR`
* `RESOURCE_EXHAUSTED`
* `INTERNAL_TIMEOUT`
* `IMPLEMENTATION_ERROR`

Example code range:

`9000–9999`
-------------------------
I would make one important architectural distinction in DAXP:

Protocol errors should not be mixed with business errors.

For example:

UNKNOWN_TAG → DAXP protocol/model error
INVALID_DATA_TYPE → DAXP validation error
CHECKSUM_MISMATCH → DAXP integrity error

but:

ACCOUNT_BLOCKED
CUSTOMER_NOT_FOUND
INSUFFICIENT_FUNDS

are application/domain errors, not DAXP errors.

I would therefore define something like:

DAXP Error → category + code + severity + location + description

For example:

category = VALIDATION
code     = 1503
type     = INVALID_DATA_TYPE
tag      = 2006
message  = Expected INTEGER, received STRING

This separation is particularly important for DAXP because it supports your broader concept of keeping the data/exchange model independent from its implementation. A Java parser, PL/SQL parser, or another implementation should ideally produce the same DAXP-level error classification.