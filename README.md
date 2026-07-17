# DAXP — Data & Attribute eXchange Protocol

DAXP is a lightweight, tag-based protocol for exchanging data, metadata, actions, and references between applications.

It is designed for systems that require compact messages, deterministic processing, and dynamic data definitions.

## Main Features

* Compact `tag=value` syntax
* Dynamic dictionaries
* Support for values, references, and actions
* Multiple messages in one frame
* Domain, context, and schema separation
* Suitable for Java, PL/SQL, and distributed systems
* Human-readable debug format
* Extensible message model

## Example

```text
DAXP|V=0.7|EN=UTF-8|CX=CRM|
$:1=CRM.DATA|$:5=I|$:6=2000|
2001=5|2002=Adam|2003=Kowalski|2004=adam@example.com|
$:9=70|
```

The message contains a CRM customer entity described by numeric tags defined in the DAXP Dynamic Dictionary.

## Operators

| Operator | Meaning                   |
| -------- | ------------------------- |
| `=`      | Value assignment          |
| `@`      | Reference                 |
| `^`      | Action or state operation |

Example:

```text
2002=Adam|
2003^N|
5001@10|
```

## Use Cases

DAXP can be used for:

* Communication between microservices
* Java and Oracle PL/SQL integration
* Financial and banking applications
* Dynamic user interfaces
* Event streaming
* Communication with AI agents
* Internal application protocols

## Project Status

DAXP is currently under active development.

The project includes:

* Protocol specification
* Java implementation
* Examples and demo applications
* Dynamic Dictionary support

## Documentation

Documentation and examples are available at:

https://daxprotocol.org

## License

The protocol specification and source code may use separate licenses.

Check the license files included in each repository.
