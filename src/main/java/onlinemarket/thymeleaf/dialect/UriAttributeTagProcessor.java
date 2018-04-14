package onlinemarket.thymeleaf.dialect;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class UriAttributeTagProcessor extends AbstractAttributeTagProcessor{
	
	private static final String ATTR_NAME = "uri";
    private static final int PRECEDENCE = 900;
    
	protected UriAttributeTagProcessor(final String dialectPrefix) {
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                null,              // No tag name: match any tag name
                false,             // No prefix to be applied to tag name
                ATTR_NAME,         // Name of the attribute that will be matched
                true,              // Apply dialect prefix to attribute name
                PRECEDENCE,        // Precedence (inside dialect's precedence)
                true);  
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {

        final IEngineConfiguration configuration = context.getConfiguration();

        /*
         * Obtain the Thymeleaf Standard Expression parser
         */
        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);
        
        /*
         * Parse the attribute value as a Thymeleaf Standard Expression
         */
        final IStandardExpression expression =
                parser.parseExpression(context, attributeValue);
		
        
        /*
         * Execute the expression just parsed
         */
        final String uri = (String) expression.execute(context);
        
        /*
         * If no remark is to be applied, just set an empty body to this tag
         */
        if (uri == null) {
        	structureHandler.setAttribute(ATTR_NAME, "");
            return;
        }
        
        /*
         * Set the computed message as the body of the tag, HTML-escaped and
         * non-processable (hence the 'false' argument)
         */
        structureHandler.setAttribute(ATTR_NAME, uri.trim().toString());
		
	}

}
