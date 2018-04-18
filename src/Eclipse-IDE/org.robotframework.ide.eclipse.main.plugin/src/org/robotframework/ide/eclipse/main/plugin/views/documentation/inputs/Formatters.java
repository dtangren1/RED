/*
 * Copyright 2018 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.views.documentation.inputs;

import static java.util.stream.Collectors.joining;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Formatters {

    static String title(final String title, final int level) {
        return "<h" + level + ">" + title + "</h" + level + ">";
    }

    static String paragraph(final String content) {
        return "<p>" + content + "</p>";
    }

    static String hyperlink(final URI href, final String label) {
        return hyperlink(href.toString(), label);
    }

    static String hyperlink(final String href, final String label) {
        return "<a href=\"" + href + "\">" + label + "</a>";
    }

    @SafeVarargs
    static String simpleHeader(final Optional<URI> imgUri, final String title,
            final List<String>... simpleTable) {
        return simpleHeader(imgUri, title, Arrays.asList(simpleTable));
    }

    static String simpleHeader(final Optional<URI> imgUri, final String title,
            final List<List<String>> simpleTable) {
        
        final StringBuilder builder = new StringBuilder();
        builder.append("<h3>");
        imgUri.ifPresent(
                uri -> builder.append("<img style=\"vertical-align: top;\" src=\"" + uri.toString() + "\"/> "));
        builder.append(title);
        builder.append("</h3>");

        if (!simpleTable.isEmpty()) {
            builder.append("<p>");
        }
        for (final List<String> row : simpleTable) {
            if (row.isEmpty()) {
                continue;
            }
            final String rowTitle = row.get(0);
            final String restOfRow = row.subList(1, row.size()).stream().collect(joining(" "));

            builder.append("<b>" + rowTitle + ": </b>");
            builder.append("<span style=\"font-family: monospace;\">" + restOfRow + "</span>");
            builder.append("<br/>");
        }
        if (!simpleTable.isEmpty()) {
            builder.append("</p>");
        }
        return builder.toString();

    }

}
