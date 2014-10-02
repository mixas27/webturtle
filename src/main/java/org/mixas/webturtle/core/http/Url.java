package org.mixas.webturtle.core.http;

/**
 * @author Mikhail Stryzhonok
 */
public class Url {
    private static final String MULTIPLE_SLASH_REGEXP = "/{2,}";
    private String value;

    public Url(String value) {
        this.value = getFormattedForm(value);
    }

    private String getFormattedForm(String notFormatted) {
        notFormatted = notFormatted.replaceAll(MULTIPLE_SLASH_REGEXP,"/");
        if (notFormatted.endsWith("/")) {
            return notFormatted;
        }
        return notFormatted + "/";
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url = (Url) o;

        if (value != null ? !value.equals(url.value) : url.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
