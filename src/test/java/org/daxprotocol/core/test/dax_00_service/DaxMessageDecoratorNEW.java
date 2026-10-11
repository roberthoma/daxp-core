package org.daxprotocol.core.test.dax_00_service;

import org.daxprotocol.core.application.DaxCoreConstants;

public final class DaxMessageDecoratorNEW {

    public enum Style {
        TAGS,        // <FS>, <GS>, <RS>, <US> (Twój obecny wariant)
        UNICODE_SYM, // ␜, ␝, ␞, ␟ (Oficjalne symbole ISO ASCII)
        GRAPHICAL,   // 📁, ⟦ ⟧, ↵, │ (Bardzo czytelny układ wizualny)
        ANSI_COLOR   // Kolorowe tagi w konsoli terminala/IDE
    }

    private DaxMessageDecoratorNEW() {}

    public static String decorate(String msgStr) {
        return decorate(msgStr, Style.TAGS);
    }

    public static String decorate(String msgStr, Style style) {
        if (msgStr == null || msgStr.isEmpty()) {
            return "";
        }

        int len = msgStr.length();
        StringBuilder sb = new StringBuilder(len + (len >> 3));

        for (int i = 0; i < len; i++) {
            char c = msgStr.charAt(i);

            // Szybkie łamanie linii dla tagów proto ($:1=, $:4=, $:9=)
            if (c == '$' && i + 3 < len && msgStr.charAt(i + 1) == ':' && msgStr.charAt(i + 3) == '=') {
                char tagNum = msgStr.charAt(i + 2);
                if (tagNum == '1' || tagNum == '4' || tagNum == '9') {
                    sb.append('\n');
                }
                sb.append(c);
                continue;
            }

            // Mapowanie znaków wg wybranego stylu
            if (c == DaxCoreConstants.SEPARATOR_FILE) {
                appendFileSeparator(sb, style);
            } else if (c == DaxCoreConstants.SEPARATOR_GROUP) {
                appendGroupSeparator(sb, style);
            } else if (c == DaxCoreConstants.SEPARATOR_RECORD) {
                appendRecordSeparator(sb, style);
            } else if (c == DaxCoreConstants.SEPARATOR_UNIT) {
                appendUnitSeparator(sb, style);
            } else if (c == DaxCoreConstants.END_OF_MEDIUM) {
                appendEndOfMedium(sb, style);
            } else if (c == DaxCoreConstants.DEFAULT_PAIR_SEPARATOR) {
                appendPairSeparator(sb, style);
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private static void appendFileSeparator(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("␜\n");
            case GRAPHICAL   -> sb.append("📁\n");
            case ANSI_COLOR  -> sb.append("\u001B[36m<FS>\u001B[0m\n");
            default          -> sb.append("<FS>\n");
        }
    }

    private static void appendGroupSeparator(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("␝");
            case GRAPHICAL   -> sb.append("❖"); // Lub klamry/ramki
            case ANSI_COLOR  -> sb.append("\u001B[33m<GS>\u001B[0m");
            default          -> sb.append("<GS>");
        }
    }

    private static void appendRecordSeparator(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("␞\n");
            case GRAPHICAL   -> sb.append(" ↵\n");
            case ANSI_COLOR  -> sb.append("\u001B[32m<RS>\u001B[0m\n");
            default          -> sb.append("<RS>\n");
        }
    }

    private static void appendUnitSeparator(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("␟");
            case GRAPHICAL   -> sb.append(" │ ");
            case ANSI_COLOR  -> sb.append("\u001B[35m|\u001B[0m");
            default          -> sb.append("<US>");
        }
    }

    private static void appendEndOfMedium(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("\n␙");
            case GRAPHICAL   -> sb.append("\n⏹");
            case ANSI_COLOR  -> sb.append("\n\u001B[31m<EM>\u001B[0m");
            default          -> sb.append("\n<EM>");
        }
    }

    private static void appendPairSeparator(StringBuilder sb, Style style) {
        switch (style) {
            case UNICODE_SYM -> sb.append("␁");
            case GRAPHICAL   -> sb.append("•");
            case ANSI_COLOR  -> sb.append("\u001B[90m<SOH>\u001B[0m");
            default          -> sb.append("<SOH>");
        }
    }
}