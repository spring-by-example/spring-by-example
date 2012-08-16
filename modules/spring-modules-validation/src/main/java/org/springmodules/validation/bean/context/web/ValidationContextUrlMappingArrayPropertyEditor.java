package org.springmodules.validation.bean.context.web;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

/**
 * A property editor for the {@link ValidationContextUrlMapping} class.
 * <p/>
 * This editor expects text in the following format:
 * <p/>
 *              <center><i>url_pattern=context1,context2,...,contextN</i></center>
 * <p/>
 * Where <i>url_pattern</i> is the URL pattern to be mapped to the context tokens and
 * <i>context1,context2,...,contextN</i> is the comma-separated list of validation context tokens.
 *
 * @author Uri Boness
 */
public class ValidationContextUrlMappingArrayPropertyEditor extends PropertyEditorSupport {

    private final static String MAPPINGS_SEPARATORS = "\n\r";
    private final static char MAPPING_OPERATOR = '=';

    public String getAsText() {
        throw new UnsupportedOperationException("This property edito only supports one way conversion (text to value)");
    }

    public void setAsText(String text) throws IllegalArgumentException {
        text = text.trim();
        String[] mappingLines = StringUtils.tokenizeToStringArray(text, MAPPINGS_SEPARATORS);
        ValidationContextUrlMapping[] mappings = new ValidationContextUrlMapping[mappingLines.length];
        for (int i=0; i<mappings.length; i++) {
            String mappingLine = mappingLines[i].trim();
            int index = mappingLine.lastIndexOf(MAPPING_OPERATOR);
            String pattern = mappingLine.substring(0, index);
            String tokensString = mappingLine.substring(index+1, mappingLine.length());
            String[] tokens = StringUtils.commaDelimitedListToStringArray(tokensString);
            mappings[i] = new ValidationContextUrlMapping(pattern, tokens);
        }
        setValue(mappings);
    }

}
