package org.daxprotocol.core.registries;

import java.util.List;

public record ClassMetadata(List<AnnotatedField> fields, List<AnnotatedMethod> methods) {}