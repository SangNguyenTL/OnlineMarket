package OnlineMarket.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.processor.SpringObjectTagProcessor;
import org.thymeleaf.standard.StandardDialect;

public class FilterFormDialect extends AbstractProcessorDialect{

	private static final String DIALECT_NAME = "Filter Form Dialect";
	
	public FilterFormDialect() {
		super(DIALECT_NAME, "filter", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new PaginationElementTagProcessor(dialectPrefix));
		processors.add(new UriAttributeTagProcessor(dialectPrefix));
		processors.add(new OrderHeadElementProcessor(dialectPrefix));
		processors.add(new SpringObjectTagProcessor(dialectPrefix));
		return processors;
	}
}